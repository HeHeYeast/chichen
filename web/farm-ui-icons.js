// Production SVGs extracted from the approved Farm UI V1, with the same palette.
// Literal paths also let the offline runtime packager discover every icon.
export const FARM_UI_ICONS=Object.freeze({
  chick:'/web/art/farm/ui/chick.svg',
  coin:'/web/art/farm/ui/coin.svg',
  gear:'/web/art/farm/ui/gear.svg',
  sprout:'/web/art/farm/ui/sprout.svg',
  home:'/web/art/farm/ui/home.svg',
  basket:'/web/art/farm/ui/basket.svg',
  quest:'/web/art/farm/ui/quest.svg',
  target:'/web/art/farm/ui/target.svg',
  chef:'/web/art/farm/ui/chef.svg',
  farm:'/web/art/farm/ui/farm.svg',
  shop:'/web/art/farm/ui/shop.svg',
  explore:'/web/art/farm/ui/explore.svg',
  book:'/web/art/farm/ui/book.svg',
  shrine:'/web/art/farm/ui/shrine.svg',
  fence:'/web/art/farm/ui/fence.svg',
  panorama:'/web/art/farm/ui/panorama.svg',
});
export function farmIcon(name){
  return `<img class="farm-icon" src="${FARM_UI_ICONS[name]}" alt="" draggable="false">`;
}
