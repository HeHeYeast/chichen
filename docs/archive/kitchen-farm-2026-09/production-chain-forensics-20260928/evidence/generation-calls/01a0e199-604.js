// @exec: {"yield_time_ms": 120000, "max_output_tokens": 1300}
await Promise.all(load("gateSpecs").slice(2).map(async([id,spec])=>{
const prompt=load("gateBase")+"\n"+spec;
const r=await tools.image_gen__imagegen({prompt,referenced_image_paths:load("gateRefs"),transparent_background:true});
store(id,{prompt,result:r});text({id,hint:r.output_hint});
}));
