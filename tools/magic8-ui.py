"""Small, explicit UI helper for the authorized Magic8 install checks."""
import os, subprocess, sys, re, time
import xml.etree.ElementTree as ET
from pathlib import Path
os.environ['ADB_LIBUSB']='1'
ADB=[r'D:\gxy_code\_toolchain\android-sdk\platform-tools\adb.exe','-s','AYXGVB6126000685']
def call(*args): return subprocess.check_output(ADB+list(args))
def nodes():
    result=call('shell','uiautomator','dump','/sdcard/Download/jibao-ui.xml').decode('utf-8',errors='replace')
    if 'UI hierchary dumped to:' not in result:
        raise RuntimeError('No fresh UI dump was produced; refusing to reuse stale screen controls: '+result.strip())
    return list(ET.fromstring(call('shell','cat','/sdcard/Download/jibao-ui.xml')).iter('node'))
mode=sys.argv[1]
if mode=='text':
    for n in nodes():
        if n.get('text') and (n.get('package')=='com.jibao.kitchen' or (len(sys.argv)>2 and sys.argv[2]=='system')):
            print(n.get('text'),n.get('class'),n.get('bounds'))
elif mode=='tap':
    candidates=[n for n in nodes() if n.get('text')==sys.argv[2] and n.get('clickable')=='true' and n.get('enabled')=='true']
    if len(candidates)!=1: raise RuntimeError('Expected exactly one visible enabled control, got '+str(len(candidates)))
    x1,y1,x2,y2=map(int,re.findall(r'\d+',candidates[0].get('bounds')))
    if x2<=x1 or y2<=y1: raise RuntimeError('Control is clipped; scroll it into view first')
    call('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));time.sleep(.7)
elif mode=='shot':
    time.sleep(.7);Path(sys.argv[2]).write_bytes(call('exec-out','screencap','-p'))
