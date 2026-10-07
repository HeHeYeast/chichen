// @exec: {"yield_time_ms": 120000, "max_output_tokens": 1800}
const variants=load("kitchenVariants"), refs=load("kitchenReferences");
const jobs=variants.map(async v=>{
 const result=await tools.image_gen__imagegen({prompt:v.prompt,referenced_image_paths:refs});
 store("kitchenOutput_"+v.key,result);
 text({variant:v.name,result});
 generatedImage(result);
});
await Promise.allSettled(jobs);
