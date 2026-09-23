package com.jibao.kitchen;

import android.content.Context;
import org.json.JSONObject;

/** Durable save ACKs remain successful even when post-commit notifications fail. */
public final class SaveBridge {
    public interface NotificationSync { void sync(JSONObject state) throws Exception; }
    public static JSONObject write(Context context,String raw,boolean importing,Long expectedRevision,NotificationSync notification) {
        JSONObject committed;
        try {
            if(expectedRevision!=null) committed=SaveRepository.commit(context,raw,expectedRevision);
            else { SaveRepository.save(context,raw,importing);committed=new JSONObject().put("ok",true).put("committed",true); }
        } catch(Exception error) {
            JSONObject failure=new JSONObject();
            try { failure.put("ok",false).put("message","保存失败："+error.getMessage())
                    .put("code",error instanceof SaveRepository.RevisionConflictException?"REVISION_CONFLICT":"SAVE_FAILED"); }
            catch(Exception ignored) { }
            return failure;
        }
        try { notification.sync(new JSONObject(raw)); }
        catch(Exception error) {
            try { committed.put("notificationWarning","进度已保存，提醒安排失败："+error.getMessage()); }
            catch(Exception ignored) { }
        }
        return committed;
    }
    private SaveBridge() { }
}
