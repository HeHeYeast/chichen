package net.nend.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendConstants {
    static final boolean IS_DEBUG_CODE = false;
    static final int MAX_ICON_COUNT = 8;
    static final String NEND_UID_KEY = "NENDUUID";
    static final String VERSION = "2.3.3";

    static class AdDefaultParams {
        static final int HEIGHT = 50;
        static final int MAX_AD_RELOAD_INTERVAL_IN_SECONDS = 99999;
        static final int MIN_AD_RELOAD_INTERVAL_IN_SECONDS = 30;
        static final int RELOAD_INTERVAL_IN_SECONDS = 60;
        static final int WIDTH = 320;

        private AdDefaultParams() {
        }
    }

    static class NendHttpParams {
        static final int CONNECTION_TIMEOUT_IN_SECOND = 10;
        static final int SOCKET_TIMEOUT_IN_SECOND = 10;

        private NendHttpParams() {
        }
    }

    static final class RequestParams {
        static final String BANNER_DOMAIN = "ad1.nend.net";
        static final String BANNER_PATH = "na.php";
        static final String ICON_DOMAIN = "ad3.nend.net";
        static final String ICON_PATH = "nia.php";
        static final String PROTOCOL = "http";

        private RequestParams() {
        }
    }

    static final class OptOutParams {
        static final String PAGE_URL = "http://nend.net/privacy/optsdkgate";

        private OptOutParams() {
        }
    }

    enum MetaData {
        ADSCHEME("NendAdScheme"),
        ADAUTHORITY("NendAdAuthority"),
        ADPATH("NendAdPath"),
        OPT_OUT_URL("NendOptOutUrl"),
        OPT_OUT_IMAGE_URL("NendOptOutImageUrl");

        private String name;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static MetaData[] valuesCustom() {
            MetaData[] metaDataArrValuesCustom = values();
            int length = metaDataArrValuesCustom.length;
            MetaData[] metaDataArr = new MetaData[length];
            System.arraycopy(metaDataArrValuesCustom, 0, metaDataArr, 0, length);
            return metaDataArr;
        }

        MetaData(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }
    }

    enum Attribute {
        SPOT_ID("NendSpotId"),
        API_KEY("NendApiKey"),
        RELOADABLE("NendReloadable");

        private String name;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static Attribute[] valuesCustom() {
            Attribute[] attributeArrValuesCustom = values();
            int length = attributeArrValuesCustom.length;
            Attribute[] attributeArr = new Attribute[length];
            System.arraycopy(attributeArrValuesCustom, 0, attributeArr, 0, length);
            return attributeArr;
        }

        Attribute(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }
    }

    enum IconAttribute {
        TITLE_COLOR("NendTitleColor"),
        TITLE_VISIBLE("NendTitleVisible"),
        ICON_COUNT("NendIconCount"),
        ICON_ORIENTATION("NendOrientation"),
        ICON_SPACE("NendIconSpaceEnabled");

        private String name;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static IconAttribute[] valuesCustom() {
            IconAttribute[] iconAttributeArrValuesCustom = values();
            int length = iconAttributeArrValuesCustom.length;
            IconAttribute[] iconAttributeArr = new IconAttribute[length];
            System.arraycopy(iconAttributeArrValuesCustom, 0, iconAttributeArr, 0, length);
            return iconAttributeArr;
        }

        IconAttribute(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }
    }

    private NendConstants() {
    }
}
