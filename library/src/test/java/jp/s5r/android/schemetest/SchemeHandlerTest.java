package jp.s5r.android.schemetest;

import junit.framework.Assert;

import org.junit.Test;

import java.net.URI;

public class SchemeHandlerTest {
    static final URI HOST               = URI.create("scheme://host");
    static final URI HOST_SLASH         = URI.create("scheme://host/");
    static final URI HOST_PATH          = URI.create("scheme://host/path");
    static final URI HOST_PATH_QUERY    = URI.create("scheme://host/path?query=hoge&id=1000");
    static final URI HOST_PATH_1        = URI.create("scheme://host/path/1");
    static final URI HOST_PATH_1_ACTION = URI.create("scheme://host/path/1/action");

    @Test
    public void shouldMatchSchemeHost() {
        Assert.assertEquals(new SchemeHandler("scheme://host", HOST).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("//host", HOST).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("/", HOST).isMatch(), false);
    }

    @Test
    public void shouldMatchSchemeHostSlash() {
        Assert.assertEquals(new SchemeHandler("scheme://host/", HOST_SLASH).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("//host/", HOST_SLASH).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("/", HOST_SLASH).isMatch(), true);
    }

    @Test
    public void shouldMatchSchemeHostPath() {
        Assert.assertEquals(new SchemeHandler("scheme://host/path", HOST_PATH).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("//host/path", HOST_PATH).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("/path", HOST_PATH).isMatch(), true);
    }

    @Test
    public void shouldMatchSchemeHostPathQuery() {
        Assert.assertEquals(new SchemeHandler("scheme://host/path", HOST_PATH_QUERY).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("//host/path", HOST_PATH_QUERY).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("/path", HOST_PATH_QUERY).isMatch(), true);
    }

    @Test
    public void shouldMatchWildcard() {
        Assert.assertEquals(new SchemeHandler("scheme://host/*", HOST_PATH_1).isMatch(), true);
        Assert.assertEquals(new SchemeHandler("//host/*", HOST_PATH_1).isMatch(), true);
    }

    @Test
    public void shouldGetPathParam() {
        SchemeHandler handler = new SchemeHandler("scheme://host/path/{id}", HOST_PATH_1);
        Assert.assertEquals((int) handler.getPathInteger("id"), 1);
    }

    @Test
    public void shouldGetPathParamAction() {
        SchemeHandler handler = new SchemeHandler("scheme://host/path/{id}/action", HOST_PATH_1_ACTION);
        Assert.assertEquals((int) handler.getPathInteger("id"), 1);
    }

    @Test
    public void shouldGetQueryParam() {
        SchemeHandler handler = new SchemeHandler("scheme://host/path", HOST_PATH_QUERY);
        Assert.assertEquals(handler.getParamString("query"), "hoge");
        Assert.assertEquals((int) handler.getParamInteger("id"), 1000);
    }
}
