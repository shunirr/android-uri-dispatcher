package jp.s5r.android.schemetest;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import jp.s5r.android.schemetest.annotation.SchemeParam;

public class SchemeHandler {

    private final String mPattern;
    private final Uri    mUri;
    private final Bundle mParams;
    private final List<SchemeParam> mSchemeParams;

    private HashMap<String, String> mPatternParams;
    private String                  mPatternPath;

    public SchemeHandler(String pattern, List<SchemeParam> schemeParams, Uri uri, Bundle params) {
        mPattern = pattern;
        mUri = uri;
        mParams = params;
        mSchemeParams = schemeParams;

        mPatternParams = getPatternParams(pattern);
    }

    private HashMap<String, String> getPatternParams(String pattern) {
        HashMap<String, String> result = new HashMap<>();

        int index = pattern.indexOf("?");
        if (index > 0) {
            mPatternPath = pattern.substring(0, index);
            String params = pattern.substring(index + 1);
            String[] paramPairArray = params.split("&");

            for (String paramPair : paramPairArray) {
                String[] kv = paramPair.split("=");
                result.put(kv[0], kv[1].substring(1, kv[1].length() - 1));
            }
        } else {
            mPatternPath = pattern;
        }

        return result;
    }

    private boolean isIncludeSchemeParams(String key) {
        for (SchemeParam param : mSchemeParams) {
            if (key.equals(param.value())) {
                return true;
            }
        }
        return false;
    }

    public boolean isMatch() {
        String uriStr;
        if (mPattern.startsWith("//")) {
            uriStr = "//" + mUri.getHost() + mUri.getPath();
        } else if (mPattern.startsWith("/")) {
            uriStr = mUri.getPath();
        } else {
            uriStr = mUri.getScheme() + "://" + mUri.getHost() + mUri.getPath();
        }

        if (mSchemeParams.size() == 0) {
            return uriStr.equals(mPatternPath);
        } else {
            int matchedParamCount = 0;
            for (String name : mUri.getQueryParameterNames()) {
                if (isIncludeSchemeParams(name)) {
                    matchedParamCount++;
                }
            }
            return matchedParamCount == mPatternParams.size();
        }
    }

    public String getParamString(final String key) {
        if (mParams != null && mParams.containsKey(key)) {
            Object o = mParams.get(key);
            if (o != null && o instanceof String) {
                return (String) o;
            }
        } else {
            String value = mUri.getQueryParameter(key);
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    public Integer getParamInteger(final String key) {
        if (mParams != null && mParams.containsKey(key)) {
            Object o = mParams.get(key);
            if (o != null && o instanceof Integer) {
                return (Integer) o;
            }
        } else {
            String value = mUri.getQueryParameter(key);
            if (!TextUtils.isEmpty(value)) {
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return -1;
    }
}
