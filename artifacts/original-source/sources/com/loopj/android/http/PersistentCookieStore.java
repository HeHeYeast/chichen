package com.loopj.android.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private final SharedPreferences cookiePrefs;
    private final ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap<>();

    public PersistentCookieStore(Context context) {
        Cookie cookieDecodeCookie;
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        String string = this.cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (string != null) {
            for (String str : TextUtils.split(string, ",")) {
                String string2 = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + str, null);
                if (string2 != null && (cookieDecodeCookie = decodeCookie(string2)) != null) {
                    this.cookies.put(str, cookieDecodeCookie);
                }
            }
            clearExpired(new Date());
        }
    }

    @Override // org.apache.http.client.CookieStore
    public void addCookie(Cookie cookie) {
        String name = cookie.getName();
        if (!cookie.isExpired(new Date())) {
            this.cookies.put(name, cookie);
        } else {
            this.cookies.remove(name);
        }
        SharedPreferences.Editor editorEdit = this.cookiePrefs.edit();
        editorEdit.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        editorEdit.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
        editorEdit.commit();
    }

    @Override // org.apache.http.client.CookieStore
    public void clear() {
        this.cookies.clear();
        SharedPreferences.Editor editorEdit = this.cookiePrefs.edit();
        Iterator<String> it = this.cookies.keySet().iterator();
        while (it.hasNext()) {
            editorEdit.remove(COOKIE_NAME_PREFIX + it.next());
        }
        editorEdit.remove(COOKIE_NAME_STORE);
        editorEdit.commit();
    }

    @Override // org.apache.http.client.CookieStore
    public boolean clearExpired(Date date) {
        boolean z;
        boolean z2 = false;
        SharedPreferences.Editor editorEdit = this.cookiePrefs.edit();
        Iterator<Map.Entry<String, Cookie>> it = this.cookies.entrySet().iterator();
        while (true) {
            z = z2;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, Cookie> next = it.next();
            String key = next.getKey();
            if (next.getValue().isExpired(date)) {
                this.cookies.remove(key);
                editorEdit.remove(COOKIE_NAME_PREFIX + key);
                z2 = true;
            } else {
                z2 = z;
            }
        }
        if (z) {
            editorEdit.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        }
        editorEdit.commit();
        return z;
    }

    @Override // org.apache.http.client.CookieStore
    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    protected String encodeCookie(SerializableCookie serializableCookie) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(serializableCookie);
            return byteArrayToHexString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    protected Cookie decodeCookie(String str) {
        try {
            return ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(str))).readObject()).getCookie();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String byteArrayToHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            int i = b & 255;
            if (i < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toHexString(i));
        }
        return stringBuffer.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
