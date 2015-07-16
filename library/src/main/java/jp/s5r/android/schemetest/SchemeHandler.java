package jp.s5r.android.schemetest;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;

public class SchemeHandler {

    private static final String ANY = "jtwaelkjasfgkgasfttrasa";

    private final Uri    mUri;
    private final Bundle mParams;

    private Uri mPatternUri;
    private HashMap<String, String> mPathData = new HashMap<>();

    public SchemeHandler(String pattern, Uri uri, Bundle params) {
        mUri = uri;
        mParams = params;
        parsePattern(pattern);
    }

    private void parsePattern(String pattern) {
        if (pattern.startsWith("//")) {
            mPatternUri = Uri.parse(ANY + ":" + pattern);
        } else if (pattern.startsWith("/")) {
            mPatternUri = Uri.parse(ANY + "://" + ANY + pattern);
        } else if (pattern.contains("://")) {
            mPatternUri = Uri.parse(pattern);
        }
    }

    public boolean isMatch() {
        if (mPatternUri == null) {
            return false;
        }

        if (!ANY.equals(mPatternUri.getScheme()) && !mPatternUri.getScheme().equals(mUri.getScheme())) {
            return false;
        }

        if (!ANY.equals(mPatternUri.getHost()) && !mPatternUri.getHost().equals(mUri.getHost())) {
            return false;
        }

        for (int i = 0; i < mUri.getPathSegments().size() && i < mPatternUri.getPathSegments().size(); i++) {
            String pattern = mPatternUri.getPathSegments().get(i);
            String path    = mUri.getPathSegments().get(i);
            if (pattern.startsWith("{") && pattern.endsWith("}")) {
                mPathData.put(pattern.substring(1, pattern.length() - 1), path);
            } else {
                if (!"*".equals(pattern) && !pattern.equals(path)) {
                    return false;
                }
            }
        }

        if ("*".equals(mPatternUri.getLastPathSegment())) {
            return true;
        }
        return mPatternUri.getPathSegments().size() == mUri.getPathSegments().size();
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
        return mPathData.get(key);
    }

    public Integer getPathInteger(String key) {
        String value = mPathData.get(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
        }
        return -1;
    }
}
