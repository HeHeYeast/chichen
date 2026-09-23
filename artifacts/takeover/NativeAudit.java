package com.jibao.kitchen;
import org.json.*;
import java.nio.file.*;
import android.content.Context;
public class NativeAudit {
 public static void main(String[] args)throws Exception{
  JSONArray rows=new JSONArray(Files.readString(Path.of(args[0]))),out=new JSONArray();
  Path folder=Path.of(args[1]);Files.createDirectories(folder);
  for(int i=0;i<rows.length();i++){
   JSONObject r=rows.getJSONObject(i),result=new JSONObject().put("name",r.getString("name")).put("webAccepted",r.getBoolean("webAccepted")).put("webError",r.get("webError"));
   try{Context c=new Context(Files.createTempDirectory(folder,"audit-").toFile());SaveRepository.save(c,r.getJSONObject("state").toString(),true);result.put("nativeAccepted",true);}
   catch(Exception e){result.put("nativeAccepted",false).put("nativeError",e.toString());}
   out.put(result);
  }
  Files.writeString(Path.of(args[2]),out.toString(2));System.out.println(out.toString(2));
 }
}
