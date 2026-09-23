import {resolveSprite,spriteSVG,uiIcon} from './art/manifest.js';

// Reuse the game's original painted icons; utility symbols use the same ink.
export function interfaceIcon(name){
  const atlas={kitchen:4,farm:5,book:6,shop:7};
  if(name in atlas)return spriteSVG(resolveSprite(uiIcon(atlas[name])));
  const paths={
    explore:'<path fill="#e2bf76" d="M5 28q9-10 22-2"/><path d="M16 4v24"/><path fill="#88b77c" d="M5 5h18l5 5-5 5H5z"/><path fill="#f7d679" d="M27 16H10l-5 4 5 4h17z"/>',
    inventory:'<path fill="#dbac67" d="M4 12h24v16H4z"/><path fill="#f6d58a" d="m3 12 4-7h18l4 7z"/><path d="M10 16v8m6-8v8m6-8v8M4 20h24"/>',
    workshop:'<path fill="#ffe19a" d="M6 18h20v11H6z"/><path fill="#fff9e5" d="M7 18C-1 14 3 5 10 7c1-7 12-7 13 0 8-2 11 8 3 11z"/><path d="M11 21v5m10-5v5"/>',
    settings:'<path fill="#ead4a1" d="m12 3 8 0 1 5 5 1 3 7-4 4 0 6-8 3-4-4-6 0-4-8 4-4 0-6z"/><circle cx="16" cy="16" r="5" fill="#fff9df"/>',
    help:'<path fill="#fff4ce" d="M6 3h20v26H6z"/><path d="M12 11c0-6 11-6 9 0-1 3-5 3-5 7"/><circle cx="16" cy="23" r="1"/>',
    clean:'<path d="m24 3-9 17"/><path fill="#efd27a" d="m10 15 11 6-6 9-12-6z"/><path d="m10 21-3 5m8-3-3 5"/>',
  };
  return `<svg class="interface-icon" viewBox="0 0 32 32" aria-hidden="true" fill="none" stroke="#68472e" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">${paths[name]??paths.book??paths.help}</svg>`;
}

export function routeIllustration(){
  return '<svg class="route-illustration" viewBox="0 0 360 74" preserveAspectRatio="xMidYMid slice" aria-hidden="true"><path fill="#f5e7bd" d="M0 0h360v74H0z"/><circle cx="285" cy="20" r="13" fill="#edc566"/><path fill="#b6c492" d="M0 60 58 22l47 26 44-33 78 48 65-25 68 20v16H0z"/><path fill="#8da777" d="m0 60 79-14 76 27 74-25 131 22v4H0z"/><path d="M175 74q-50-25 28-31t38-28" fill="none" stroke="#fff3cd" stroke-width="7"/><path d="M37 32v34m-15-30h29l7 7-7 7H22z" fill="#e3b978" stroke="#755b3b" stroke-width="2"/></svg>';
}
