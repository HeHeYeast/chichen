// @exec: {"yield_time_ms": 1000, "max_output_tokens": 500}
const results=await Promise.allSettled(Object.entries(load("directionPrompts")).map(async([id,prompt])=>{
 const r=await tools.image_gen__imagegen({prompt,referenced_image_paths:load("directionRefs"),transparent_background:false});
 store("direction-"+id,r);notify("Direction "+id+" generated.");generatedImage(r);
 return id;
}));text(results.map((r,i)=>({id:"ABCD"[i],status:r.status,error:r.status==="rejected"?String(r.reason):undefined})));
