from pathlib import Path
b=Path('artifacts/original-source/sources/com/idtinc')
for f,a,z in [('maingame/sublayout0/MainGameUnit.java',1580,1684),('maingame/sublayout0/MainGameUnit.java',2377,2578),('maingame/sublayout0/CharacterUnit.java',180,510),('maingame/sublayout0/Tool_1_SelectScrollUnit.java',1,190),('maingame/sublayout0/EggSelectUnit.java',1,180)]:
 lines=b.joinpath(f).read_text().splitlines()[a-1:z];print('\nFILE',f,a);print('\n'.join(l for l in lines if 'Log.d' not in l and not l.strip().startswith(('private ','public float','public int','public short','public boolean','this.')) or (any(k in l for k in ['Offset','CENTER','Transform','Origin','anime','Cnt =','setPoint','setOpen','setBlack','setEnd']))))
