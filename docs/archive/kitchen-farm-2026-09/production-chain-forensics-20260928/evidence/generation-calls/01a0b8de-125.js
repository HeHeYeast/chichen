// @exec: {"yield_time_ms": 120000, "max_output_tokens": 900}
const jobs=load("kitchenLayoutVariants").map(async v=>{
 const result=await tools.image_gen__imagegen({prompt:v.prompt,referenced_image_paths:load("kitchenLayoutRefs")});
 store("kitchenLayoutOutput_"+v.key,result);
 text({variant:v.name,output_hint:result.output_hint});
 generatedImage(result);
 return v.key;
});
const results=await Promise.allSettled(jobs);
text(results.map(r=>r.status==="fulfilled"?{status:r.status,key:r.value}:{status:r.status,error:String(r.reason)}));
