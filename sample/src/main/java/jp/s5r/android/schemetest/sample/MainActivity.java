package jp.s5r.android.schemetest.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import jp.s5r.android.schemetest.SchemeTest;
import jp.s5r.android.schemetest.annotation.SchemeParam;
import jp.s5r.android.schemetest.annotation.SchemePath;
import jp.s5r.android.schemetest.annotation.SchemeUrl;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.s5r.android.schemetest.R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        SchemeTest.handle(this, intent);
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("scheme-test://host")
    public void handleSchemeHost() {
        showToast("Handle scheme-test://host");
    }

    @SchemeUrl("//host")
    public void handleHost() {
        showToast("Handle //host");
    }

    @SchemeUrl("//host/")
    public void handleRootPath() {
        showToast("Handle //host/");
    }

    @SchemeUrl("//host/foo")
    public void handlePath() {
        showToast("Handle //host/foo");
    }

    @SchemeUrl("//host/bar")
    public void handleMultiQueryParam(@SchemeParam("query") String query,
                                      @SchemeParam("id") int id) {
        showToast("Handle //host/bar?query=" + query + "&id=" + id);
    }

    @SchemeUrl("//host/buz/{id}")
    public void handlePathParam(@SchemePath("id") int id) {
        showToast("Handle //host/buz/" + id);
    }
}
