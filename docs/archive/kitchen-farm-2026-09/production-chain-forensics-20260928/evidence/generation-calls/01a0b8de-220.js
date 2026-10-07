// @exec: {"yield_time_ms": 120000, "max_output_tokens": 1100}
const refs=[load("platformSetBasePath"),load("platformSetRefs")[0]];
const results=await Promise.allSettled(load("platformSetStages").filter(s=>s.key!=="02-shabby").map(async s=>{
 const prompt=load("platformSetDerivedCommon").replace("approved working base","working base")+"\n"+s.direction;
 store("platformSetPrompt_"+s.key,prompt);
 const result=await tools.image_gen__imagegen({prompt,referenced_image_paths:refs});
 store("platformSetOutput_"+s.key,result);
 text({stage:s.name,output_hint:result.output_hint});
 generatedImage(result);
 return s.key;
}));
text(results.map(r=>r.status==="fulfilled"?{status:r.status,key:r.value}:{status:r.status,error:String(r.reason)}));
