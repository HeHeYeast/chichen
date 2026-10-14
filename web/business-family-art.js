// Composition only: original character pixels, individually cut shop materials.
import {characterPortraitMarkup} from './character-art.js';
import {productionAsset} from './production-art.js';
export const familyArt=(name,cls='')=>`<img class="family-art ${cls}" src="/web/art/golden-business/${name}.png" alt="" draggable="false">`;
export function familyCharacter(egg,id){
  return characterPortraitMarkup(egg,id,'family-character');
}
export function familyBasket(row){return `<span class="family-basket" aria-hidden="true">${familyArt(row.egg?'tray-base':'basket-base','family-vessel-base')}${familyCharacter(row.egg,row.id)}${familyArt(row.egg?'tray-front':'basket-front','family-vessel-front')}</span>`;}
const GUESTS={RG1:{egg:0,id:0,identity:'老街坊 · 家常味'},RG2:{egg:0,id:8,identity:'溪岸 · 采买与野餐'},RG3:{egg:0,id:17,identity:'茶坡 · 茶香与点心'},RG4:{egg:1,id:0,identity:'沿湾 · 装箱与分享'}};
export const guestIdentity=id=>GUESTS[id]?.identity??'小店常客';
export function guestAvatar(id,known=true){const c=GUESTS[id]??GUESTS.RG1,p=productionAsset('regulars',id,'portrait');return `<span class="family-avatar" aria-hidden="true">${familyArt('family-avatar-base')}${known?(p?`<img class="family-character human-guest human-${id}" src="${p}" alt="">`:familyCharacter(c.egg,c.id)):familyArt('family-unknown','family-unknown')}</span>`;}
export const familyStamp=(state,label)=>`<span class="family-stamp is-${state}">${familyArt('family-stamp-'+state)}<span>${label}</span></span>`;
