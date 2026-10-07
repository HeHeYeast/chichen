import importlib.util,json,uuid,shutil,unittest
from pathlib import Path
from PIL import Image,ImageDraw

spec=importlib.util.spec_from_file_location('art_pipeline',Path(__file__).parents[1]/'art_pipeline.py')
p=importlib.util.module_from_spec(spec);spec.loader.exec_module(p)

class ArtPipelineTests(unittest.TestCase):
 def setUp(self):
  self.tempbase=Path(__file__).resolve().parents[2]/'artifacts/internal-art/tool-tests';self.tempbase.mkdir(parents=True,exist_ok=True)
  self.root=self.tempbase/str(uuid.uuid4());self.root.mkdir();self.previous=p.ROOT;p.ROOT=self.root
  self.out=self.root/'private';self.source=self.root/'source.png'
  im=Image.new('RGBA',(128,128));d=ImageDraw.Draw(im);d.rectangle((30,25,99,109),fill='#ae843fff');d.rectangle((54,45,74,65),fill=(0,0,0,0));im.save(self.source)
  self.job={'id':'ART-future-701','kind':'species','identityKey':'0:701','contentId':'future-701','region':'unbounded-region','prompt':'test reference','references':['source.png'],'concept':{},'variants':p.PROFILES['species']}
  p.write(self.out/'jobs.json',{'version':1,'jobs':[self.job]})
 def tearDown(self):
  p.ROOT=self.previous
  assert self.root.resolve().is_relative_to(self.tempbase.resolve())
  shutil.rmtree(self.root)
 def stage(self):p.ingest(self.out,self.job['id'],self.source);p.process(self.out)
 def test_opaque_and_empty_sources_rejected(self):
  for rgba,error in [((1,2,3,255),'OPAQUE_BACKGROUND'),((0,0,0,0),'EMPTY_ALPHA')]:
   Image.new('RGBA',(100,100),rgba).save(self.source)
   with self.assertRaisesRegex(ValueError,error):p.analyze(self.source,'species')
 def test_shape_bounds_and_negative_space(self):
  im,box,_,errors=p.analyze(self.source,'species');self.assertEqual(errors,[])
  normalized,geo=p.normalize(im,box,[512,512],'species','full')
  self.assertAlmostEqual(geo['groundAnchor'][1],.92,delta=.002)
  x=round(64*geo['scale']+geo['translation'][0]);y=round(55*geo['scale']+geo['translation'][1]);self.assertEqual(normalized.getpixel((x,y))[3],0)
  self.assertGreater(normalized.getchannel('A').getbbox()[0],0)
  im=Image.new('RGBA',(128,128));ImageDraw.Draw(im).rectangle((5,60,120,75),fill='red');im.save(self.source)
  self.assertIn('EXTREME_ASPECT',p.analyze(self.source,'species')[3])
 def test_review_and_publish_hash_gates(self):
  self.stage();id=self.job['id'];src=p.read(self.out/'current'/f'{id}.json')
  record={'sourceHash':'stale','checks':dict.fromkeys(p.REVIEW_AXES,True),'notes':'Actual internal visual review evidence.'};file=self.out/'record.json';p.write(file,record)
  with self.assertRaisesRegex(ValueError,'STALE_REVIEW'):p.approve(self.out,id,file)
  record['sourceHash']=src['sourceHash'];record['outputHash']=p.digest(self.out/'processed'/id/'metadata.json');p.write(file,record);p.approve(self.out,id,file)
  with self.assertRaisesRegex(ValueError,'BATCH_REVIEW_REQUIRED'):p.publish(self.out,self.root/'published')
  p.write(self.out/'batch-review.json',{'sourceHashes':{id:src['sourceHash']}})
  self.assertEqual(p.publish(self.out,self.root/'published')['published'],1)
  full=self.out/'processed'/id/'full.png';full.write_bytes(b'changed')
  with self.assertRaisesRegex(ValueError,'OUTPUT_HASH_MISMATCH'):p.publish(self.out,self.root/'published')
 def test_changed_concept_requires_new_review(self):
  self.stage();self.job['concept']={'changed':True};p.write(self.out/'jobs.json',{'version':1,'jobs':[self.job]})
  with self.assertRaisesRegex(ValueError,'STALE_CONCEPT'):p.process(self.out)
 def test_incremental_publish_preserves_other_batches_and_identity(self):
  self.stage();id=self.job['id'];src=p.read(self.out/'current'/f'{id}.json')
  record={'sourceHash':src['sourceHash'],'outputHash':p.digest(self.out/'processed'/id/'metadata.json'),'checks':dict.fromkeys(p.REVIEW_AXES,True),'notes':'Actual internal visual review evidence.'}
  file=self.out/'record.json';p.write(file,record);p.approve(self.out,id,file)
  p.write(self.out/'batch-review.json',{'sourceHashes':{id:src['sourceHash']}})
  destination=self.root/'published';other={'id':'prior','kind':'mementos','contentId':'prior','productionStatus':'FINAL'}
  p.write(destination/'manifest.json',{'version':1,'assets':{'prior':other}})
  result=p.publish(self.out,destination);self.assertEqual(result['totalPublished'],2)
  self.assertEqual(p.read(destination/'manifest.json')['assets']['prior'],other)
  bad=p.read(destination/'manifest.json');bad['assets'][id]['identityKey']='1:999';p.write(destination/'manifest.json',bad)
  with self.assertRaisesRegex(ValueError,'PUBLISHED_IDENTITY_DRIFT'):p.publish(self.out,destination)
 def test_finalize_rejects_navigation_only_and_stale_reports(self):
  destination=self.root/'published';p.write(destination/'manifest.json',{'version':1,'assets':{}});report=self.root/'qa.json'
  for value in [{'passed':True,'manifestHash':'stale'},{'passed':True,'manifestHash':p.digest(destination/'manifest.json'),'navigationOnly':True}]:
   p.write(report,value)
   with self.assertRaisesRegex(ValueError,'STALE_OR_FAILED_RUNTIME_QA'):p.finalize(destination,report)
 def test_paths_and_duplicate_ids(self):
  with self.assertRaisesRegex(ValueError,'PATH_ESCAPE'):p.within(self.out,'../escape')
  with self.assertRaisesRegex(ValueError,'UNSAFE_ID'):p.safe_id('../bad')
  with self.assertRaisesRegex(ValueError,'DUPLICATE_ID'):p.validate_jobs({'version':1,'jobs':[self.job,self.job]})
 def test_arbitrary_batch_sizes(self):
  jobs=[{**self.job,'id':f'future-{i}','contentId':str(i)} for i in range(5)]
  self.assertEqual(len(p.validate_jobs({'version':1,'jobs':jobs})['jobs']),5)
 def test_replacement_and_explicit_candidate_selection(self):
  self.stage();id=self.job['id'];first=p.read(self.out/'current'/f'{id}.json')
  im=Image.open(self.source).convert('RGBA');ImageDraw.Draw(im).rectangle((35,70,50,85),fill='blue');im.save(self.source)
  p.ingest(self.out,id,self.source);self.assertNotEqual(p.read(self.out/'current'/f'{id}.json')['sourceHash'],first['sourceHash'])
  p.select_candidate(self.out,id,1);self.assertEqual(p.read(self.out/'current'/f'{id}.json')['sourceHash'],first['sourceHash'])
  with self.assertRaisesRegex(ValueError,'INVALID_ATTEMPT'):p.select_candidate(self.out,id,0)

if __name__=='__main__':unittest.main()
