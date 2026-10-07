// @exec: {"yield_time_ms": 120000, "max_output_tokens": 500}
const result=await tools.image_gen__imagegen({prompt:load("appIconPrompt"),referenced_image_paths:[load("appIconReference")]});
store("appIconOutput",result);
text(result.output_hint);
generatedImage(result);
