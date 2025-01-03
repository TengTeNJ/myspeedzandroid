package com.potent.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< 说明 >
 * @date: 2014/10/15 17:45
 */
public class StringTools {
    public static final String VERSIONNAMENEW = "VERSIONNAMENEW";
    public static final String VERSIONCODENEW = "VERSIONCODENEW";
    public static final String UPDATETIME = "UPDATETIME";
    public static final String UPDATECONTENT = "UPDATECONTENT";
    public static final String UPDATEURL = "UPDATEURL";
    public static final String FORCEDUPDATE = "FORCEDUPDATE";
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }
    public static Map<String, String> getVersionFromXML(String xml) {
        Map<String, String> res = new HashMap<String, String>();
        XmlPullParser xmlParse = Xml.newPullParser();
        try {
            xmlParse.setInput(new StringReader(xml));
            while (xmlParse.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xmlParse.getEventType() == XmlPullParser.START_TAG) {
                    String name = xmlParse.getName();
                    if (name.equals("data")) {
                        res.put(VERSIONCODENEW, xmlParse.getAttributeValue(null, "versionCode"));
                        res.put(VERSIONNAMENEW, xmlParse.getAttributeValue(null, "versionname"));
                        res.put(UPDATETIME, xmlParse.getAttributeValue(null, "updatetime"));
                        res.put(UPDATECONTENT, xmlParse.getAttributeValue(null, "updatecontent"));
                        res.put(UPDATEURL, xmlParse.getAttributeValue(null, "apppath"));
                        res.put(FORCEDUPDATE, xmlParse.getAttributeValue(null, "forcedupdate"));
                    }
                }
                xmlParse.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }
}
