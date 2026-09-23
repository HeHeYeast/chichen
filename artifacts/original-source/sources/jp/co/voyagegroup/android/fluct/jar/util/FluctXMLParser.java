package jp.co.voyagegroup.android.fluct.jar.util;

import android.content.Context;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.db.FluctInterstitialTable;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctAd;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctConversionEntity;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.web.FluctHttpAccess;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctXMLParser {
    private static final String TAG = "FluctXMLParser";

    public static Document executeUrl(Context context, String url) {
        Log.d(TAG, "executeUrl : url is " + url);
        if (!FluctUtils.checkPermission(context)) {
            Log.e(TAG, "executeUrl : not set permission");
            return null;
        }
        if (!FluctUtils.isNetWorkAvailable(context)) {
            Log.e(TAG, "executeUrl : network not available");
            return null;
        }
        return FluctHttpAccess.getDocument(url);
    }

    public static FluctSetting parserConfig(Context context, Document configDocument, String mediaId) {
        Log.d(TAG, "parserConfig : ");
        if (configDocument == null) {
            Log.e(TAG, "parserConfig : configDocument is null");
            return null;
        }
        FluctSetting fluctSetting = new FluctSetting();
        fluctSetting.setFluctInterstitial(null);
        FluctAd fluctAd = new FluctAd();
        fluctSetting.setFluctAd(fluctAd);
        Element root = configDocument.getDocumentElement();
        NodeList properties = root.getChildNodes();
        for (int nodeLen = 0; nodeLen < properties.getLength(); nodeLen++) {
            Node node = properties.item(nodeLen);
            if (node != null && node.getFirstChild() != null) {
                setNode(context, node, fluctSetting, mediaId);
            }
        }
        return fluctSetting;
    }

    private static void setNode(Context context, Node node, FluctSetting fluctSetting, String mediaId) throws DOMException {
        Log.d(TAG, "setNode : ");
        String name = node.getNodeName();
        String nodeValue = node.getFirstChild().getNodeValue();
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_MODE)) {
            Log.v(TAG, "setNode : mode is " + nodeValue);
            fluctSetting.setMode(nodeValue);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_BROWSER)) {
            Log.v(TAG, "setNode : browser is " + nodeValue);
            try {
                fluctSetting.setBrowser(Integer.parseInt(nodeValue));
                return;
            } catch (NumberFormatException e) {
                Log.e(TAG, "setNode : NumberFormatException is " + e.getLocalizedMessage());
                return;
            }
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_REFRESHTIME)) {
            Log.v(TAG, "setNode : refresh time is " + nodeValue);
            try {
                fluctSetting.setRefreshTime(Long.parseLong(nodeValue));
                return;
            } catch (NumberFormatException e2) {
                Log.e(TAG, "setNode : NumberFormatException is " + e2.getLocalizedMessage());
                return;
            }
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_LOADTIME)) {
            Log.v(TAG, "setNode : load time is " + nodeValue);
            try {
                fluctSetting.setLoadTime(Long.parseLong(nodeValue));
                return;
            } catch (NumberFormatException e3) {
                Log.e(TAG, "setNode : NumberFormatException is " + e3.getLocalizedMessage());
                return;
            }
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_BACKCOLOR)) {
            Log.v(TAG, "setNode : backColor is " + nodeValue);
            fluctSetting.getFluctAd().setBackColor(nodeValue);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_ADHTML)) {
            Log.v(TAG, "setNode : adhtml is " + nodeValue);
            fluctSetting.getFluctAd().setAdHtml(nodeValue);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_CONVERSION_URL)) {
            setConversionNode(node, fluctSetting);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_UA)) {
            fluctSetting.setUserAgent(nodeValue);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_ANIMATIONS)) {
            setAnimationNode(node, fluctSetting);
            return;
        }
        if (name.equalsIgnoreCase(FluctConstants.XML_NODE_INTERSTITIAL)) {
            setInterStital(context, node, fluctSetting, mediaId);
        } else if (name.equalsIgnoreCase("error")) {
            setErrorNode(node, fluctSetting);
            Log.d(TAG, "setNode : Error" + nodeValue);
        }
    }

    private static void setAnimationNode(Node node, FluctSetting fluctSetting) {
        Log.d(TAG, "setAnimationNode : ");
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getNodeType() == 1 && item.getNodeName().equals("android")) {
                ArrayList<FluctSetting.Animation> animationList = new ArrayList<>();
                if (item.getFirstChild() != null) {
                    String[] animations = item.getFirstChild().getNodeValue().split(",");
                    for (String str : animations) {
                        String[] animation = str.split(":");
                        if (animation.length == 2 && Integer.parseInt(animation[1]) != 0) {
                            switch (Integer.parseInt(animation[0])) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    animationList.add(new FluctSetting.Animation(Integer.parseInt(animation[0]), Integer.parseInt(animation[1])));
                                    break;
                            }
                        }
                    }
                }
                fluctSetting.setAnimations(animationList);
            }
        }
    }

    private static void setInterStital(Context context, Node node, FluctSetting setting, String mediaId) throws DOMException {
        Log.d(TAG, "setInterStital : ");
        NodeList nodeList = node.getChildNodes();
        FluctInterstitialTable table = new FluctInterstitialTable();
        table.setMediaId(mediaId);
        table.setUpdateTime(FluctUtils.getCurrentTimeSec());
        for (int loop = 0; loop < nodeList.getLength(); loop++) {
            Node item = nodeList.item(loop);
            if (item.getNodeName().equals(FluctConstants.XML_NODE_DISPLAY_RATE)) {
                if (item.getFirstChild() != null) {
                    String rate = item.getFirstChild().getNodeValue();
                    table.setRate(Integer.valueOf(rate).intValue());
                }
            } else if (item.getNodeName().equals(FluctConstants.XML_NODE_WIDTH)) {
                if (item.getFirstChild() != null) {
                    String width = item.getFirstChild().getNodeValue();
                    table.setWidth(Integer.valueOf(width).intValue());
                }
            } else if (item.getNodeName().equals(FluctConstants.XML_NODE_HEIGHT)) {
                if (item.getFirstChild() != null) {
                    String height = item.getFirstChild().getNodeValue();
                    table.setHeight(Integer.valueOf(height).intValue());
                }
            } else if (item.getNodeName().equals(FluctConstants.XML_NODE_ADHTML) && item.getFirstChild() != null) {
                table.setAdHtml(item.getFirstChild().getNodeValue());
            }
        }
        setting.setFluctInterstitial(table);
    }

    private static void setConversionNode(Node node, FluctSetting fluctSetting) {
        String nodeValue;
        String nodeValue2;
        Log.d(TAG, "setConversionNode : ");
        FluctConversionEntity conversion = new FluctConversionEntity();
        ArrayList<String> urlsList = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int nodeLen = 0; nodeLen < nodeList.getLength(); nodeLen++) {
            if (nodeList.item(nodeLen).getNodeName().equals("url")) {
                if (nodeList.item(nodeLen).getFirstChild() != null && (nodeValue2 = nodeList.item(nodeLen).getFirstChild().getNodeValue()) != null) {
                    Log.v(TAG, "setConversionNode : convurl is " + nodeValue2);
                    urlsList.add(nodeValue2);
                }
            } else if (nodeList.item(nodeLen).getNodeName().equals(FluctConstants.XML_NODE_CONV_BROWSER) && nodeList.item(nodeLen).getFirstChild() != null && (nodeValue = nodeList.item(nodeLen).getFirstChild().getNodeValue()) != null) {
                Log.v(TAG, "setConversionNode : browserOpenUrl is " + nodeValue);
                conversion.setBrowserOpenUrl(nodeValue);
            }
        }
        conversion.setConvUrl(urlsList);
        fluctSetting.setFluctConversion(conversion);
    }

    private static void setErrorNode(Node node, FluctSetting fluctSetting) throws DOMException {
        Log.d(TAG, "setErrorNode : ");
        NodeList nodeList = node.getChildNodes();
        for (int nodeLen = 0; nodeLen < nodeList.getLength(); nodeLen++) {
            if (nodeList.item(nodeLen).getNodeName().equals("message")) {
                String nodeValue = nodeList.item(nodeLen).getFirstChild().getNodeValue();
                Log.v(TAG, "setErrorNode : error is " + nodeValue);
                fluctSetting.setErrorMessages(nodeValue);
            }
        }
    }
}
