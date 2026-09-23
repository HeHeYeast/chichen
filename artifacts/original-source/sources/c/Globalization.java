package c;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.format.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0097k;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Globalization extends C0103q {
    public static final String CURRENCY = "currency";
    public static final String CURRENCYCODE = "currencyCode";
    public static final String DATE = "date";
    public static final String DATESTRING = "dateString";
    public static final String DATETOSTRING = "dateToString";
    public static final String DAYS = "days";
    public static final String FORMATLENGTH = "formatLength";
    public static final String FULL = "full";
    public static final String GETCURRENCYPATTERN = "getCurrencyPattern";
    public static final String GETDATENAMES = "getDateNames";
    public static final String GETDATEPATTERN = "getDatePattern";
    public static final String GETFIRSTDAYOFWEEK = "getFirstDayOfWeek";
    public static final String GETLOCALENAME = "getLocaleName";
    public static final String GETNUMBERPATTERN = "getNumberPattern";
    public static final String GETPREFERREDLANGUAGE = "getPreferredLanguage";
    public static final String ISDAYLIGHTSAVINGSTIME = "isDayLightSavingsTime";
    public static final String ITEM = "item";
    public static final String LONG = "long";
    public static final String MEDIUM = "medium";
    public static final String MONTHS = "months";
    public static final String NARROW = "narrow";
    public static final String NUMBER = "number";
    public static final String NUMBERSTRING = "numberString";
    public static final String NUMBERTOSTRING = "numberToString";
    public static final String OPTIONS = "options";
    public static final String PERCENT = "percent";
    public static final String SELECTOR = "selector";
    public static final String STRINGTODATE = "stringToDate";
    public static final String STRINGTONUMBER = "stringToNumber";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String WIDE = "wide";

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws C0097k {
        JSONObject jSONObjectI;
        new JSONObject();
        try {
            if (str.equals(GETLOCALENAME)) {
                jSONObjectI = a();
            } else if (str.equals(GETPREFERREDLANGUAGE)) {
                jSONObjectI = b();
            } else if (str.equalsIgnoreCase(DATETOSTRING)) {
                jSONObjectI = a(jSONArray);
            } else if (str.equalsIgnoreCase(STRINGTODATE)) {
                jSONObjectI = b(jSONArray);
            } else if (str.equalsIgnoreCase(GETDATEPATTERN)) {
                jSONObjectI = c(jSONArray);
            } else if (str.equalsIgnoreCase(GETDATENAMES)) {
                if (Build.VERSION.SDK_INT < 9) {
                    throw new C0097k("UNKNOWN_ERROR");
                }
                jSONObjectI = d(jSONArray);
            } else if (str.equalsIgnoreCase(ISDAYLIGHTSAVINGSTIME)) {
                jSONObjectI = e(jSONArray);
            } else if (str.equalsIgnoreCase(GETFIRSTDAYOFWEEK)) {
                jSONObjectI = c();
            } else if (str.equalsIgnoreCase(NUMBERTOSTRING)) {
                jSONObjectI = f(jSONArray);
            } else if (str.equalsIgnoreCase(STRINGTONUMBER)) {
                jSONObjectI = g(jSONArray);
            } else if (str.equalsIgnoreCase(GETNUMBERPATTERN)) {
                jSONObjectI = h(jSONArray);
            } else if (str.equalsIgnoreCase(GETCURRENCYPATTERN)) {
                jSONObjectI = i(jSONArray);
            } else {
                return false;
            }
            c0101o.a(jSONObjectI);
        } catch (C0097k e) {
            c0101o.a(new C0108v(C0108v.a.ERROR, e.a()));
        } catch (Exception e2) {
            c0101o.a(new C0108v(C0108v.a.JSON_EXCEPTION));
        }
        return true;
    }

    private static JSONObject a() throws JSONException, C0097k {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("value", Locale.getDefault().toString());
            return jSONObject;
        } catch (Exception e) {
            throw new C0097k("UNKNOWN_ERROR");
        }
    }

    private static JSONObject b() throws JSONException, C0097k {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("value", Locale.getDefault().getDisplayLanguage().toString());
            return jSONObject;
        } catch (Exception e) {
            throw new C0097k("UNKNOWN_ERROR");
        }
    }

    private JSONObject a(JSONArray jSONArray) throws C0097k {
        try {
            return new JSONObject().put("value", new SimpleDateFormat(c(jSONArray).getString("pattern")).format(new Date(((Long) jSONArray.getJSONObject(0).get(DATE)).longValue())));
        } catch (Exception e) {
            throw new C0097k("FORMATTING_ERROR");
        }
    }

    private JSONObject b(JSONArray jSONArray) throws JSONException, C0097k, ParseException {
        JSONObject jSONObject = new JSONObject();
        try {
            Date date = new SimpleDateFormat(c(jSONArray).getString("pattern")).parse(jSONArray.getJSONObject(0).get(DATESTRING).toString());
            Time time = new Time();
            time.set(date.getTime());
            jSONObject.put("year", time.year);
            jSONObject.put("month", time.month);
            jSONObject.put("day", time.monthDay);
            jSONObject.put("hour", time.hour);
            jSONObject.put("minute", time.minute);
            jSONObject.put("second", time.second);
            jSONObject.put("millisecond", new Long(0L));
            return jSONObject;
        } catch (Exception e) {
            throw new C0097k("PARSING_ERROR");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x014b A[PHI: r2
  0x014b: PHI (r2v4 java.lang.String) = (r2v3 java.lang.String), (r2v21 java.lang.String), (r2v21 java.lang.String) binds: [B:4:0x0046, B:11:0x00b9, B:23:0x013b] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x014d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private org.json.JSONObject c(org.json.JSONArray r7) throws org.json.JSONException, vpadn.C0097k {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: c.Globalization.c(org.json.JSONArray):org.json.JSONObject");
    }

    @TargetApi(9)
    private JSONObject d(JSONArray jSONArray) throws C0097k {
        int i;
        int i2;
        final Map<String, Integer> displayNames;
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray2 = new JSONArray();
        ArrayList arrayList = new ArrayList();
        try {
            if (jSONArray.getJSONObject(0).length() > 0) {
                i2 = (((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).isNull("type") || !((String) ((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).get("type")).equalsIgnoreCase(NARROW)) ? 0 : 1;
                i = (((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).isNull(ITEM) || !((String) ((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).get(ITEM)).equalsIgnoreCase(DAYS)) ? 0 : 10;
            } else {
                i = 0;
                i2 = 0;
            }
            int i3 = i + i2;
            if (i3 == 1) {
                displayNames = Calendar.getInstance().getDisplayNames(2, 1, Locale.getDefault());
            } else if (i3 == 10) {
                displayNames = Calendar.getInstance().getDisplayNames(7, 2, Locale.getDefault());
            } else if (i3 == 11) {
                displayNames = Calendar.getInstance().getDisplayNames(7, 1, Locale.getDefault());
            } else {
                displayNames = Calendar.getInstance().getDisplayNames(2, 2, Locale.getDefault());
            }
            Iterator<String> it = displayNames.keySet().iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            Collections.sort(arrayList, new Comparator<String>(this) { // from class: c.Globalization.1
                @Override // java.util.Comparator
                public final /* synthetic */ int compare(String str, String str2) {
                    return ((Integer) displayNames.get(str)).compareTo((Integer) displayNames.get(str2));
                }
            });
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                jSONArray2.put(arrayList.get(i4));
            }
            return jSONObject.put("value", jSONArray2);
        } catch (Exception e) {
            throw new C0097k("UNKNOWN_ERROR");
        }
    }

    private static JSONObject e(JSONArray jSONArray) throws C0097k {
        try {
            return new JSONObject().put("dst", TimeZone.getTimeZone(Time.getCurrentTimezone()).inDaylightTime(new Date(((Long) jSONArray.getJSONObject(0).get(DATE)).longValue())));
        } catch (Exception e) {
            throw new C0097k("UNKNOWN_ERROR");
        }
    }

    private static JSONObject c() throws C0097k {
        try {
            return new JSONObject().put("value", Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());
        } catch (Exception e) {
            throw new C0097k("UNKNOWN_ERROR");
        }
    }

    private JSONObject f(JSONArray jSONArray) throws C0097k {
        try {
            return new JSONObject().put("value", j(jSONArray).format(jSONArray.getJSONObject(0).get(NUMBER)));
        } catch (Exception e) {
            throw new C0097k("FORMATTING_ERROR");
        }
    }

    private JSONObject g(JSONArray jSONArray) throws C0097k {
        try {
            return new JSONObject().put("value", j(jSONArray).parse((String) jSONArray.getJSONObject(0).get(NUMBERSTRING)));
        } catch (Exception e) {
            throw new C0097k("PARSING_ERROR");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static org.json.JSONObject h(org.json.JSONArray r6) throws org.json.JSONException, vpadn.C0097k {
        /*
            org.json.JSONObject r3 = new org.json.JSONObject
            r3.<init>()
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch: java.lang.Exception -> Le6
            java.text.NumberFormat r0 = java.text.DecimalFormat.getInstance(r0)     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormat r0 = (java.text.DecimalFormat) r0     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormatSymbols r1 = r0.getDecimalFormatSymbols()     // Catch: java.lang.Exception -> Le6
            char r1 = r1.getDecimalSeparator()     // Catch: java.lang.Exception -> Le6
            java.lang.String r2 = java.lang.String.valueOf(r1)     // Catch: java.lang.Exception -> Le6
            r1 = 0
            org.json.JSONObject r1 = r6.getJSONObject(r1)     // Catch: java.lang.Exception -> Le6
            int r1 = r1.length()     // Catch: java.lang.Exception -> Le6
            if (r1 <= 0) goto Lef
            r1 = 0
            org.json.JSONObject r1 = r6.getJSONObject(r1)     // Catch: java.lang.Exception -> Le6
            java.lang.String r4 = "options"
            java.lang.Object r1 = r1.get(r4)     // Catch: java.lang.Exception -> Le6
            org.json.JSONObject r1 = (org.json.JSONObject) r1     // Catch: java.lang.Exception -> Le6
            java.lang.String r4 = "type"
            boolean r1 = r1.isNull(r4)     // Catch: java.lang.Exception -> Le6
            if (r1 != 0) goto Lef
            r1 = 0
            org.json.JSONObject r1 = r6.getJSONObject(r1)     // Catch: java.lang.Exception -> Le6
            java.lang.String r4 = "options"
            java.lang.Object r1 = r1.get(r4)     // Catch: java.lang.Exception -> Le6
            org.json.JSONObject r1 = (org.json.JSONObject) r1     // Catch: java.lang.Exception -> Le6
            java.lang.String r4 = "type"
            java.lang.Object r1 = r1.get(r4)     // Catch: java.lang.Exception -> Le6
            java.lang.String r1 = (java.lang.String) r1     // Catch: java.lang.Exception -> Le6
            java.lang.String r4 = "currency"
            boolean r4 = r1.equalsIgnoreCase(r4)     // Catch: java.lang.Exception -> Le6
            if (r4 == 0) goto Lc4
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch: java.lang.Exception -> Le6
            java.text.NumberFormat r0 = java.text.DecimalFormat.getCurrencyInstance(r0)     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormat r0 = (java.text.DecimalFormat) r0     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormatSymbols r1 = r0.getDecimalFormatSymbols()     // Catch: java.lang.Exception -> Le6
            java.lang.String r1 = r1.getCurrencySymbol()     // Catch: java.lang.Exception -> Le6
            r5 = r1
            r1 = r0
            r0 = r5
        L6d:
            java.lang.String r2 = "pattern"
            java.lang.String r4 = r1.toPattern()     // Catch: java.lang.Exception -> Le6
            r3.put(r2, r4)     // Catch: java.lang.Exception -> Le6
            java.lang.String r2 = "symbol"
            r3.put(r2, r0)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "fraction"
            int r2 = r1.getMinimumFractionDigits()     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r2)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "rounding"
            java.lang.Integer r2 = new java.lang.Integer     // Catch: java.lang.Exception -> Le6
            r4 = 0
            r2.<init>(r4)     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r2)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "positive"
            java.lang.String r2 = r1.getPositivePrefix()     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r2)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "negative"
            java.lang.String r2 = r1.getNegativePrefix()     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r2)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "decimal"
            java.text.DecimalFormatSymbols r2 = r1.getDecimalFormatSymbols()     // Catch: java.lang.Exception -> Le6
            char r2 = r2.getDecimalSeparator()     // Catch: java.lang.Exception -> Le6
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r2)     // Catch: java.lang.Exception -> Le6
            java.lang.String r0 = "grouping"
            java.text.DecimalFormatSymbols r1 = r1.getDecimalFormatSymbols()     // Catch: java.lang.Exception -> Le6
            char r1 = r1.getGroupingSeparator()     // Catch: java.lang.Exception -> Le6
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Exception -> Le6
            r3.put(r0, r1)     // Catch: java.lang.Exception -> Le6
            return r3
        Lc4:
            java.lang.String r4 = "percent"
            boolean r1 = r1.equalsIgnoreCase(r4)     // Catch: java.lang.Exception -> Le6
            if (r1 == 0) goto Lef
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch: java.lang.Exception -> Le6
            java.text.NumberFormat r0 = java.text.DecimalFormat.getPercentInstance(r0)     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormat r0 = (java.text.DecimalFormat) r0     // Catch: java.lang.Exception -> Le6
            java.text.DecimalFormatSymbols r1 = r0.getDecimalFormatSymbols()     // Catch: java.lang.Exception -> Le6
            char r1 = r1.getPercent()     // Catch: java.lang.Exception -> Le6
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Exception -> Le6
            r5 = r1
            r1 = r0
            r0 = r5
            goto L6d
        Le6:
            r0 = move-exception
            vpadn.k r0 = new vpadn.k
            java.lang.String r1 = "PATTERN_ERROR"
            r0.<init>(r1)
            throw r0
        Lef:
            r1 = r0
            r0 = r2
            goto L6d
        */
        throw new UnsupportedOperationException("Method not decompiled: c.Globalization.h(org.json.JSONArray):org.json.JSONObject");
    }

    private static JSONObject i(JSONArray jSONArray) throws JSONException, C0097k {
        JSONObject jSONObject = new JSONObject();
        try {
            String string = jSONArray.getJSONObject(0).getString(CURRENCYCODE);
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
            Currency currency = Currency.getInstance(string);
            decimalFormat.setCurrency(currency);
            jSONObject.put("pattern", decimalFormat.toPattern());
            jSONObject.put("code", currency.getCurrencyCode());
            jSONObject.put("fraction", decimalFormat.getMinimumFractionDigits());
            jSONObject.put("rounding", new Integer(0));
            jSONObject.put("decimal", String.valueOf(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));
            jSONObject.put("grouping", String.valueOf(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator()));
            return jSONObject;
        } catch (Exception e) {
            throw new C0097k("FORMATTING_ERROR");
        }
    }

    private static DecimalFormat j(JSONArray jSONArray) throws JSONException {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        try {
            if (jSONArray.getJSONObject(0).length() > 1 && !((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).isNull("type")) {
                String str = (String) ((JSONObject) jSONArray.getJSONObject(0).get(OPTIONS)).get("type");
                if (str.equalsIgnoreCase(CURRENCY)) {
                    decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
                } else if (str.equalsIgnoreCase(PERCENT)) {
                    decimalFormat = (DecimalFormat) DecimalFormat.getPercentInstance(Locale.getDefault());
                }
            }
        } catch (JSONException e) {
        }
        return decimalFormat;
    }
}
