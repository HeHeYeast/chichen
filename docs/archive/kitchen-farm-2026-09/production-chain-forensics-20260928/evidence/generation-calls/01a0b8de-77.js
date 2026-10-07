// @exec: {"yield_time_ms": 120000, "max_output_tokens": 500}
const result=await tools.image_gen__imagegen({
prompt:load("kitchenExtra").prompt,
referenced_image_paths:[
"D:/gxy_code/game/chicken_duck_test/assets/png/Tool/Tool0/tool_0_0_0_0.jpg",
"D:/gxy_code/game/chicken_duck_test/assets/png/Tool/Tool0/tool_0_0_2_0.jpg"
]});
store("kitchenOutput_d",result);
text(result.output_hint);
generatedImage(result);
