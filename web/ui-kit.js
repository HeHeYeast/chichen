// Game UI kit: markup helpers for popups built from painted art (styles in ui-kit.css).
// Text on art always goes in a [data-safe] box that sits on the art's measured text area,
// so titles and button labels are centred by construction.
const ART = '/web/art/';
const esc = value => String(value ?? '').replace(/[&<>"']/g, c => ({'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[c]));

export const kitIcon = {
  clock: `<img src="${ART}golden-journey/precision-clock.png" alt="">`,
  coin: `<img src="${ART}golden-business/coin.png" alt="">`,
  glass: `<img src="${ART}golden-journey/magnifier.png" alt="">`,
  pouch: `<img src="${ART}golden-journey/pouch.png" alt="">`,
  flag: `<img src="${ART}golden-journey/flag.png" alt="">`,
};
/** A painted piece from the GPT work set (web/art/golden-ui), e.g. kitArt('ic-broom'). */
export const kitArt = (name, cls = '') => `<img${cls ? ` class="${cls}"` : ''} src="${ART}golden-ui/${name}.png" alt="" draggable="false">`;
// Painted value icons for chips and rows (kitIcon.hourglass, kitIcon.broom, ...).
for (const [key, name] of Object.entries({hourglass: 'ic-hourglass', flame: 'ic-flame', pot: 'ic-pot', egg: 'ic-egg', duckEgg: 'ic-duck-egg', jar: 'ic-jar', broom: 'ic-broom', germ: 'ic-germ', fresh: 'ic-fresh', upgrade: 'ic-upgrade', hammer: 'ic-hammer', fence: 'ic-fence', house: 'ic-house', star: 'ic-star', heart: 'ic-heart', check: 'ic-check', chick: 'ic-chick', basket: 'ic-basket', bill: 'ic-bill', bell: 'ic-bell', gift: 'ic-gift', calendar: 'ic-calendar', book: 'ic-book', map: 'ic-map', leaf: 'ic-leaf', chest: 'ic-chest', tag: 'ic-tag', refresh: 'ic-refresh', alarm: 'ic-alarm', cross: 'ic-cross', point: 'skill-point', coins: 'coin-pile'})) kitIcon[key] = kitArt(name);
export const kitArrow = `<img class="gd-arrow" src="${ART}golden-ui/arrow.png" alt="" aria-hidden="true" draggable="false">`;
export const kitEgg = duck => `<img class="gd-egg${duck ? ' duck' : ''}" src="${ART}egg-v4.png" alt="">`;

/** Title board on the paper's top edge; the close button sits on the corner. */
export const kitTitle = (title, closeAttrs = 'data-close') => `<div class="gd-title" data-art="plank"><span data-safe><b>${esc(title)}</b></span></div><button type="button" class="gd-close" ${closeAttrs} aria-label="关闭">×</button>`;
/** A value pill: painted icon + short text. */
export const kitChip = (icon, text, cls = '') => `<span class="gd-chip ${cls}">${icon ?? ''}${esc(text)}</span>`;
/** Primary wooden button (label centred on the plank face); green variant for journeys. */
export const kitButton = (label, attrs = '', cls = '') => { const n = [...String(label)].length; return `<button type="button" class="gd-btn ${cls}${n >= 6 ? ' xlong' : n >= 5 ? ' long' : ''}" ${attrs}><span data-safe>${esc(label)}</span></button>`; };
/** Secondary cream pill button. */
export const kitButton2 = (label, attrs = '') => `<button type="button" class="gd-btn2" ${attrs}>${esc(label)}</button>`;
/** A small section label between two leaf dividers. */
export const kitLabel = text => `<div class="gd-label">${esc(text)}</div>`;
/** Progress bar; the fill is inset inside the groove. */
export const kitBar = (percent, label = '') => `<div class="gd-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="${Math.round(percent)}"${label ? ` aria-label="${esc(label)}"` : ''}><i data-fill${percent > 0 ? '' : ' data-zero'} style="width:calc(${Math.max(0, Math.min(100, percent))}% - 6px)"></i></div>`;
/** Big number with a small unit, in the heavy UI face used for numbers. */
export const kitBig = (value, unit = '') => `<span class="gd-big">${esc(value)}${unit ? `<small>${esc(unit)}</small>` : ''}</span>`;

/**
 * Place a popup in the free band between the scene's top bars and the bottom navigation.
 * The title board overhangs the paper by 50 px, so the band starts 50 px lower.
 */
export function placeKitDialog(dialog, {safeTop = 70, navTop = innerHeight} = {}) {
  dialog.style.removeProperty('--gd-max'); dialog.style.setProperty('--gd-top', '0px');
  const top = safeTop + 50, free = Math.max(160, navTop - 8 - top), height = dialog.getBoundingClientRect().height;
  if (height > free) dialog.style.setProperty('--gd-max', free + 'px');
  dialog.style.setProperty('--gd-top', Math.round(top + Math.max(0, (free - Math.min(height, free)) / 2)) + 'px');
  const body = dialog.querySelector('.gd-body');
  if (body) body.classList.toggle('gd-scrolls', body.scrollHeight > body.clientHeight + 1);
}
/** A value pill whose content is already markup (e.g. 「现在 [coin] 18」). */
export const kitChipHtml = (html, cls = '') => `<span class="gd-chip ${cls}">${html}</span>`;

// ---- kit pages (full-screen sub-pages; styles in ui-kit-page.css) ----
/** Page header: back on the left, the plank title (optional painted icon) in the middle, help on the right. */
export const kitPageHead = ({title, icon = '', help = ''}) => `<header class="kp-head"><button type="button" class="close kp-back" aria-label="返回">‹</button><div class="kp-title" data-art="plaque"><span data-safe>${icon ? `<i data-icon aria-hidden="true">${icon}</i>` : ''}<b>${esc(title)}</b></span></div>${help ? `<button type="button" class="kp-help" ${help} aria-label="这页怎么玩">?</button>` : ''}</header>`;
/** Paper bookmarks above the sheet: [{label, attrs, on, count}]. */
export const kitTabs = (items, label = '分页') => `<nav class="kp-tabs" role="tablist" aria-label="${esc(label)}">${items.map(t => `<button type="button" role="tab" ${t.attrs ?? ''} aria-selected="${!!t.on}"><span data-safe>${esc(t.label)}${t.count != null ? `<small>${esc(t.count)}</small>` : ''}</span></button>`).join('')}</nav>`;
/** The paper sheet: scrolling content, then the actions at its foot. scroll: {cls, attrs} for the scroller;
 *  mark variable-length lists with attrs 'data-list' (they may end early without counting as blank). */
export const kitSheet = (content, foot = '', cls = '', above = '', scroll = {}) => `<div class="kp-sheet ${cls}"><div class="kp-scroll scroll ${scroll.cls ?? ''}" ${scroll.attrs ?? ''}>${content}</div>${above ? `<div class="kp-above" data-row>${above}</div>` : ''}${foot ? `<footer class="kp-foot" data-row>${foot}</footer>` : ''}</div>`;
/** A square inventory tile: picture on a painted tile, count on its corner, name underneath. */
/** sub: optional markup under the name (a price line). */
export const kitCell = ({pic, name, count = null, attrs = '', tag = '', on = false, label = name, sub = ''}) => `<button type="button" class="kp-cell${on ? ' on' : ''}" ${attrs} aria-label="${esc(label)}"><span class="kp-cell-art" data-visual>${pic}${count != null ? `<b class="kp-count">${esc(count)}</b>` : ''}${tag ? `<small class="kp-tag">${esc(tag)}</small>` : ''}</span><span class="kp-cell-name">${esc(name)}</span>${sub ? `<span class="kp-cell-sub">${sub}</span>` : ''}</button>`;
/** A round filter coin with a picture or a short word. */
export const kitCoin = (content, attrs = '', on = false, label = '') => `<button type="button" class="kp-coin" ${attrs} aria-pressed="${on}"${label ? ` aria-label="${esc(label)}"` : ''}>${content}</button>`;

/** The 图鉴 header (same as the hand-built 收藏 page): chick + 图鉴, directory and help on the right;
 *  sub-pages also get the back button. */
export const kitBookHead = ({icon = '', help = '', search = '', back = false, title = '图鉴'}) => `<header class="kp-head kp-book-head"><button type="button" class="close kp-back"${back ? '' : ' hidden'} aria-label="返回">‹</button><span class="kp-book-chick" aria-hidden="true">${icon}</span><h1>${esc(title)}</h1>${search ? `<button type="button" class="kp-help kp-search" ${search} aria-label="图鉴目录"><img src="/web/art/golden-journey/magnifier.png" alt=""></button>` : ''}${help ? `<button type="button" class="kp-help" ${help} aria-label="这页怎么玩">?</button>` : ''}</header>`;
