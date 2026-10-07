// @exec: {"yield_time_ms": 1000, "max_output_tokens": 500}
const r=await tools.image_gen__imagegen({prompt:load("finalDPrompt"),referenced_image_paths:load("directionRefs"),transparent_background:false});
store("direction-D-final",r);generatedImage(r);
