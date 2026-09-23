// Frozen purchase behavior from the released 1.0.0 engine. Kept only as an
// independent regression oracle while the live engine moves to declarative rules.
export function releasedIngredientIds(s) {
  const ids=new Set([0]);
  const [lamp,pan,boil,fryer,oven,stew,kettle,box,steamer=-1]=s.toolLevels;
  const duck=(s.total['1:0']??0)>0;
  const add=(condition,...values)=>{if(condition)values.forEach(id=>ids.add(id));};
  const total=Object.values(s.total).reduce((a,b)=>a+b,0);
  add(pan>=0,1,2);add(boil>=0||(duck&&kettle>=0),3);add(boil>=0,4);
  add(fryer>=0,5,6,7);add(oven>=0,8,9,10);add(boil>=1,9);
  add(s.total['0:1']>0||s.total['1:1']>0,11);add(stew>=0,12,13);
  add(stew>=0||fryer>=1,14);add(total>=100,15);add(s.total['0:18']>0,16);
  add(total>=300,17);add(total>=500,18);add(pan>=1,19,30,31);
  add(pan>=1||boil>=1,20);add(boil>=1,21,22,24);
  add(boil>=1||oven>=1||stew>=1||(duck&&pan>=1),23);
  add(fryer>=1,25,32);add(s.total['0:35']>0,26);
  add(oven>=1||(duck&&(lamp>=0||boil>=0||stew>=0)),27);
  add(stew>=1,28);add(stew>=1||(duck&&(fryer>=2||oven>=1||stew>=2||kettle>=2)),29);
  add(oven>=1||(duck&&oven>=0),33);add(stew>=1||(duck&&pan>=2),34);
  add(total>=2000,35);add(s.kitchenLevel>=2,36);add(pan>=2,37,38);
  add(boil>=2,39,40);add(fryer>=2||oven>=2||(duck&&pan>=2),41);
  add(oven>=2,42);add(stew>=2,43);add(duck&&pan>=0,44);
  add(duck&&(fryer>=0||stew>=1),45);
  add(duck&&(pan>=1||boil>=2||fryer>=2||oven>=2),46);
  add(duck&&boil>=1,47,48);add(duck&&stew>=1,49);add(duck&&pan>=2,50);
  add(duck&&boil>=2,51,52);add(duck&&fryer>=2,53);add(duck&&oven>=2,54,55);
  add(kettle>=0,56);add(kettle>=1,57);add(kettle>=2,58,59,60,61,62);
  add(duck&&kettle>=0,63,64);add(duck&&kettle>=1,65,66);add(duck&&kettle>=2,67);
  add(box>=1,71,72);add(duck&&box>=1,73,74);
  add(steamer>=0,9,16);add(steamer>=1,24,25,37);add(steamer>=2,71);
  return [...ids].sort((a,b)=>a-b);
}
