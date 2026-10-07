// Follow the shipped map's destination control, including offscreen buildings.
export async function openFarmPlace(page,id){
  await page.locator('[data-camera="places"]').click();
  await page.locator(`[data-locate="${id}"]`).click();
  await page.locator(`[data-place="${id}"]`).click();
}
// Selling an exact amount: 仓库 → tap one kind → its amount sheet (quick picks, slider, ±) → 卖出.
export async function openFarmSale(page,key){
  await openFarmPlace(page,'house');
  await page.locator(`.warehouse-screen [data-wh-bird="${key}"]`).click();
  await page.locator('.warehouse-screen .wh-sheet').waitFor();
  // Every kind is locked at one by default; these scripts sell exact amounts, so release the lock first.
  const free=page.locator('.warehouse-screen [data-wh-lock="0"]');if(await free.count())await free.click();
}
// which: a quick-pick amount, or 'max' for the 全部 coin.
export async function pickSaleAmount(page,which){
  if(which==='max')await page.locator('.wh-sheet [data-wh-set]').last().click();
  else await page.locator(`.wh-sheet [data-wh-set="${which}"]`).click();
}
// Leave the warehouse: close its sheet (tap the scrim), then the page.
export async function closeWarehouse(page){
  if(await page.locator('.warehouse-screen .wh-scrim').count())await page.locator('.warehouse-screen .wh-scrim').click({position:{x:12,y:96}});
  await page.locator('.warehouse-screen .close').click();
}
