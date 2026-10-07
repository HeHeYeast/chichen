import {farmIcon} from './farm-ui-icons.js';

// Shared item markup; the caller owns navigation, selected state and result badges.
export function compactNavItem({id,title}){
  const icons={0:'chef',1:'farm',5:'shop',6:'explore',4:'book'};
  return `${farmIcon(icons[id])}<span>${title}</span>`;
}
