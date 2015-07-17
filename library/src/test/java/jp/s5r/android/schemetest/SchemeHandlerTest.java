package jp.s5r.android.schemetest;

import android.net.Uri;

import junit.framework.Assert;

import org.junit.Test;

public class SchemeHandlerTest {
    @Test
    public void hoge() {
        SchemeHandler handler = new SchemeHandler("//host", Uri.parse("scheme://host"));
        Assert.assertEquals(handler.isMatch(), false);
    }

//    new SchemeHandler("scheme-test://host"
//    new SchemeHandler("//host"
//    new SchemeHandler("//host/"
//    new SchemeHandler("//host/user-list"
//    new SchemeHandler("//host/search"
//    new SchemeHandler("//host/users/{id}"
//    new SchemeHandler("//host/users/{id}/follower"
//    new SchemeHandler("//host/users/*"
//    new SchemeHandler("/users/1"
}
