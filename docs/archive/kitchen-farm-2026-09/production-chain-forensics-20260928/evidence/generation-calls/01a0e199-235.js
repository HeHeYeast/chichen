// @exec: {"yield_time_ms": 120000, "max_output_tokens": 1000}
const r=await tools.image_gen__imagegen({prompt:load("styleCommon")+"\n\n"+load("styleVariants").B,referenced_image_paths:load("styleRefs"),transparent_background:false});store("styleB",r);generatedImage(r);
