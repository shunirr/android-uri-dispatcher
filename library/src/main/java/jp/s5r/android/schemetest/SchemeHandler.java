package jp.s5r.android.schemetest;

import android.os.Bundle;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SchemeHandler {

    private static final String ANY = "any";

    private final URI    mUri;
    private final Bundle mParams;
    private final Map<String, String> mQueryStrings;

    private boolean mIsParsed = false;
    private URI mPatternUri;
    private HashMap<String, String> mPathData = new HashMap<>();

    public SchemeHandler(String pattern, URI uri) {
        this(pattern, uri, null);
    }

    public SchemeHandler(String pattern, URI uri, Bundle params) {
        mUri = uri;
        mParams = params;
        // URI の validator が強力なため対処
        parsePattern(pattern.replace('{', '(').replace('}', ')'));
        mQueryStrings = parseQueryString(uri);
    }

    private Map<String, String> parseQueryString(URI uri) {
        HashMap<String, String> map = new HashMap<>();
        String query = uri.getQuery();
        if (query == null || "".equals(query)) {
            return map;
        }

        String[] queries = query.split("&");
        if (queries.length == 0) {
            String[] kv = query.split("=");
            map.put(kv[0], kv[1]);
        } else {
            for (String q : queries) {
                String[] kv = q.split("=");
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }

    private void parsePattern(String pattern) {
        if (pattern.startsWith("//")) {
            mPatternUri = URI.create(ANY + ":" + pattern);
        } else if (pattern.startsWith("/")) {
            mPatternUri = URI.create(ANY + "://" + ANY + pattern);
        } else if (pattern.contains("://")) {
            mPatternUri = URI.create(pattern);
        }
    }

    public boolean isMatch() {
        mIsParsed = true;

        if (mPatternUri == null) {
            return false;
        }

        if (!ANY.equals(mPatternUri.getScheme()) && !mPatternUri.getScheme().equals(mUri.getScheme())) {
            return false;
        }

        if (!ANY.equals(mPatternUri.getHost()) && !mPatternUri.getHost().equals(mUri.getHost())) {
            return false;
        }

        String[] uriPathSegments = mUri.getPath().split("\\/");
        String[] patternUriPathSegments = mPatternUri.getPath().split("\\/");

        for (int i = 0; i < uriPathSegments.length && i < patternUriPathSegments.length; i++) {
            String pattern = patternUriPathSegments[i];
            String path    = uriPathSegments[i];
            // URI の validator が強力なため対処
            if (pattern.startsWith("(") && pattern.endsWith(")")) {
                mPathData.put(pattern.substring(1, pattern.length() - 1), path);
            } else {
                if (!"*".equals(pattern) && !pattern.equals(path)) {
                    return false;
                }
            }
        }

        if (patternUriPathSegments.length > 0
                && "*".equals(patternUriPathSegments[patternUriPathSegments.length - 1])) {
            return true;
        }
        return uriPathSegments.length == patternUriPathSegments.length;
    }

    public String getParamString(final String key) {
        if (!mIsParsed) {
            isMatch();
        }
        if (mParams != null && mParams.containsKey(key)) {
            Object o = mParams.get(key);
            if (o != null && o instanceof String) {
                return (String) o;
            }
        } else {
            String value = mQueryStrings.get(key);
            if (value != null && !"".equals(value)) {
                return value;
            }
        }
        return null;
    }

    public Integer getParamInteger(final String key) {
        if (!mIsParsed) {
            isMatch();
        }
        if (mParams != null && mParams.containsKey(key)) {
            Object o = mParams.get(key);
            if (o != null && o instanceof Integer) {
                return (Integer) o;
            }
        } else {
            String value = mQueryStrings.get(key);
            if (value != null && !"".equals(value)) {
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return -1;
    }

    public String getPathString(String key) {
        if (!mIsParsed) {
            isMatch();
        }
        return mPathData.get(key);
    }

    public Integer getPathInteger(String key) {
        if (!mIsParsed) {
            isMatch();
        }
        String value = mPathData.get(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
        }
        return -1;
    }
}
