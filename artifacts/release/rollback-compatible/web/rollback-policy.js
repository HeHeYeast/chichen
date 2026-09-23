// Build-time switches stop new work, never saved-ticket interpreters or IDs.
// A compatible hotfix replaces this object only; it keeps schema 6 and all data.
export const ROLLBACK_POLICY=Object.freeze({...{"region":false,"business":false,"orders":false,"collections":false,"regulars":false,"projects":false,"ui":false,"regions":{"V":false,"R":false,"T":false,"B":false}},regions:Object.freeze({"V":false,"R":false,"T":false,"B":false})});
const labels=Object.freeze({region:'地区探索与试做',business:'新营业',orders:'新采购',collections:'新收藏登记',regulars:'新常客故事',projects:'新项目阶段',ui:'新版页面布局'});
export function newOperationsEnabled(feature,regionId=null){
  if(!Object.hasOwn(labels,feature))throw Error('未知的发行开关');
  if(regionId!==null&&(feature!=='region'||!Object.hasOwn(ROLLBACK_POLICY.regions,regionId)))throw Error('未知的地区发行开关');
  return ROLLBACK_POLICY[feature]&&(regionId===null||ROLLBACK_POLICY.regions[regionId]);
}
export function assertNewOperation(feature,regionId=null){
  if(!newOperationsEnabled(feature,regionId))throw Object.assign(Error(`${labels[feature]}暂时暂停，已有进度和待领取内容仍会保留。`),{code:'FEATURE_PAUSED',feature,regionId});
}
