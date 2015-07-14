package jp.s5r.android.schemetest;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import jp.s5r.android.schemetest.annotation.SchemeParam;
import jp.s5r.android.schemetest.annotation.SchemePath;

public class SchemeHandler {

    private final String mPattern;
    private final Uri    mUri;
    private final Bundle mParams;
    private final List<SchemeParam> mSchemeParams;
    private final List<SchemePath> mSchemePaths;

    private HashMap<String, String> mPatternParams;
    private String                  mPatternPath;

    public SchemeHandler(String pattern,
                         List<SchemeParam> schemeParams,
                         List<SchemePath> schemePaths,
                         Uri uri,
                         Bundle params) {
        mPattern = pattern;
        mUri = uri;
        mParams = params;
        mSchemeParams = schemeParams;
        mSchemePaths = schemePaths;
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

    private boolean isIncludeSchemePaths(String key) {
        for (SchemePath path : mSchemePaths) {
            if (key.equals(path.value())) {
                return true;
            }
        }
        return false;
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

        if (mSchemeParams.size() == 0 && mSchemePaths.size() == 0) {
            return uriStr.equals(mPatternPath);
        } else {
            return isMatchedParams() && isMatchedPaths(uriStr);
        }
    }

    private boolean isMatchedPaths(String inputUri) {
        String[] schemeAndPath = inputUri.split("\\/\\/");
        String[] inputUriPathSegments;
        String inputHost;
        int segmentPosition;
        if (schemeAndPath.length == 1) {
            inputUriPathSegments = inputUri.split("\\/");
            segmentPosition = 0;
            inputHost = null;
        } else {
            inputUriPathSegments = schemeAndPath[1].split("\\/");
            segmentPosition = 1;
            inputHost = inputUriPathSegments[0];
        }

        int matchedPathCount = 0;
        if (inputHost == null || mUri.getHost().equals(inputHost)) {
            for (String path : mUri.getPathSegments()) {
                // {id} みたいな SchemePath にマッチするか
                if (isIncludeSchemePaths(path)) {
                    matchedPathCount++;
                } else {
                    // SchemePath 以外のパスがマッチしているか
                    if (!path.equals(inputUriPathSegments[segmentPosition])) {
                        return false;
                    }
                }
                segmentPosition++;
            }
        }
        return segmentPosition == inputUriPathSegments.length
                && matchedPathCount == mPatternParams.size();
    }

    private boolean isMatchedParams() {
        int matchedParamCount = 0;
        for (String name : mUri.getQueryParameterNames()) {
            if (isIncludeSchemeParams(name)) {
                matchedParamCount++;
            }
        }
        return matchedParamCount == mPatternParams.size();
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

    public String getPathString(String key) {
        return "";
    }

    public Integer getPathInteger(String key) {
        return -1;
    }
}
