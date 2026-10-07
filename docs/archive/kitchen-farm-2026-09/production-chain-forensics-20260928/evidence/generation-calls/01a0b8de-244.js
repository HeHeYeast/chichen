// @exec: {"yield_time_ms": 120000, "max_output_tokens": 500}
const result=await tools.image_gen__imagegen({prompt:load("originalTouchupPrompt"),referenced_image_paths:[load("originalTouchupReference")]});
store("originalTouchupOutput",result);
text(result.output_hint);
generatedImage(result);
