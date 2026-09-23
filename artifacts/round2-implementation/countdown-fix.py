from pathlib import Path
p=Path('web/workshop-ui.js');s=p.read_text(encoding='utf-8').replace('还需 ${Math.floor(minutes/60)}小时${minutes%60}分', '<span data-trip-countdown>还需 ${Math.floor(minutes/60)}小时${minutes%60}分</span>')
s=s.replace('return {open,observe,refresh:', "return {open,observe,updateTime:()=>{const el=find('[data-trip-countdown]'),t=getState().progress.trip;if(el&&t?.status==='running'){const m=Math.max(0,Math.ceil((t.endAt-getNow())/60000));el.textContent='还需 '+Math.floor(m/60)+'小时'+m%60+'分';}},refresh:")
p.write_text(s,encoding='utf-8')
p=Path('web/app.js');s=p.read_text(encoding='utf-8').replace('if(tick%10===0){updateCleaningStatus();reportStatus();}', 'if(tick%10===0){updateCleaningStatus();workshopUI.updateTime();reportStatus();}');p.write_text(s,encoding='utf-8')
