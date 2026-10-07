// Business stock in UI tests: the 全部出品 sheet shows one tile per kind; a tile opens that kind's
// basket sheet, where the wooden slider sets the amount and 「放好了」 returns to the tiles.
export async function setStock(page,key,n){
  if(!await page.locator('.bs-dialog[open] .bs-stock-grid').count())await page.locator('#panels [data-business-sheet="stock"]').first().click();
  await page.locator(`.bs-dialog [data-business-stock-key="${key}"]`).click();
  await page.locator('.bs-dialog [data-business-basket-range]').evaluate((el,value)=>{el.value=String(value);el.dispatchEvent(new Event('input',{bubbles:true}));el.dispatchEvent(new Event('change',{bubbles:true}));},n);
  await page.locator('.bs-dialog [data-business-basket-done]').click();
  await page.locator('.bs-dialog[open] .bs-stock-grid').waitFor();
}
// The amount a tile shows as stocked (「摆 N」), 0 when it only shows what is available.
export async function stockOf(page,key){
  const text=await page.locator(`.bs-dialog [data-business-stock-key="${key}"] .kp-count`).innerText();
  const match=text.match(/摆\s*(\d+)/);return match?Number(match[1]):0;
}
// Orders: a delivery/reserve tile opens its amount drawer (pre-filled with what is still needed);
// the slider sets the exact amount and 「好」 closes it.
export async function setSlot(page,slot,n){
  await page.locator(`[data-slot-pick="${slot}"]`).click();
  await page.locator('[data-slot-range]').evaluate((el,value)=>{el.value=String(value);el.dispatchEvent(new Event('input',{bubbles:true}));el.dispatchEvent(new Event('change',{bubbles:true}));},n);
  await page.locator('.od-drawer [data-slot-done].gd-btn').click();
}
// 厨房往事: the amount to hand over, set with the slider on the open chapter card.
export async function setStoryAmount(page,id,n){
  const range=page.locator(`[data-story-range="${id}"]`);
  if(await range.count())await range.evaluate((el,value)=>{el.value=String(value);el.dispatchEvent(new Event('input',{bubbles:true}));el.dispatchEvent(new Event('change',{bubbles:true}));},n);
}
