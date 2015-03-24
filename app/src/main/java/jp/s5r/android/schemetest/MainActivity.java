package jp.s5r.android.schemetest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import jp.s5r.android.schemetest.annotation.SchemeParam;
import jp.s5r.android.schemetest.annotation.SchemeUrl;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        SchemeTest.handle(this, intent);
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("schemetest://host")
    void handleSchemeHost() {
        showToast("Handle schemetest://host");
    }

    @SchemeUrl("//host")
    void handleHost() {
        showToast("Handle //host");
    }

    @SchemeUrl("//host/")
    void handleRootPath() {
        showToast("Handle //host/");
    }

    @SchemeUrl("//host/path")
    void handlePath() {
        showToast("Handle //host/path");
    }

    @SchemeUrl("//host/path?query={query}")
    void handleStringQueryParam(@SchemeParam("query") String query) {
        if (query == null) {
            return;
        }
        showToast("Handle //host/path?query=" + query);
    }

    @SchemeUrl("//host/path?id={id}")
    void handleIntegerQueryParam(@SchemeParam("id") int id) {
        if (id < 0) {
            return;
        }
        showToast("Handle //host/path?id=" + id);
    }

    @SchemeUrl("//host/path?query={query}&id={id}")
    void handleMultiQueryParam(@SchemeParam("query") String query,
                               @SchemeParam("id") int id) {
        if (query == null || id < 0) {
            return;
        }
        showToast("Handle //host/path?query=" + query + "&id=" + id);
    }

    @SchemeUrl("//host/path/{id}")
    void handlePathParam(@SchemeParam("id") int id) {
        if (id < 0) {
            return;
        }
        showToast("Handle //host/path/" + id);
    }

    @SchemeUrl("//host/path/wild/*")
    void handlePathWildCard() {
        showToast("Handle //host/path/wild/*");
    }

    @SchemeUrl("/path")
    void handleWithoutHost() {
        showToast("Handle /path");
    }
}
