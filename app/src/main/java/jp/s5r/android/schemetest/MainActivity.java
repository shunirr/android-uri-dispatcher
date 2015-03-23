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

    @SchemeUrl("schemetest://host")
    void handleSchemeHost() {
        Toast.makeText(this, "Handle schemetest://host", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host")
    void handleHost() {
        Toast.makeText(this, "Handle //host", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/")
    void handleRootPath() {
        Toast.makeText(this, "Handle //host/", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path")
    void handlePath() {
        Toast.makeText(this, "Handle //host/path", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path?query={query}")
    void handleStringQueryParam(@SchemeParam("query") String query) {
        if (query == null) {
            return;
        }
        Toast.makeText(this, "Handle //host/path?query=string", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path?id={id}")
    void handleIntegerQueryParam(@SchemeParam("id") int id) {
        if (id < 0) {
            return;
        }
        Toast.makeText(this, "Handle //host/path?id=integer", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path?query={query}&id={id}")
    void handleMultiQueryParam(@SchemeParam("query") String query,
                               @SchemeParam("id") int id) {
        if (query == null || id < 0) {
            return;
        }
        Toast.makeText(this, "Handle //host/path?query=query&id=integer", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path/{id}")
    void handlePathParam(@SchemeParam("id") int id) {
        if (id < 0) {
            return;
        }
        Toast.makeText(this, "Handle //host/path/{id}", Toast.LENGTH_SHORT).show();
    }

    @SchemeUrl("//host/path/wild/*")
    void handlePathWildCard() {
        Toast.makeText(this, "Handle //host/path/wild/*", Toast.LENGTH_SHORT).show();
    }
}
