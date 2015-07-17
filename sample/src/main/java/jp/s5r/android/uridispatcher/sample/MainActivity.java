package jp.s5r.android.uridispatcher.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import jp.s5r.android.uridispatcher.UriDispatcher;
import jp.s5r.android.uridispatcher.annotation.QueryParam;
import jp.s5r.android.uridispatcher.annotation.PathParam;
import jp.s5r.android.uridispatcher.annotation.MatchUri;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        UriDispatcher.dispatch(this, intent);
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @MatchUri("scheme-test://host")
    public void handleUri() {
        showToast("Handle scheme-test://host");
    }

    @MatchUri("//host")
    public void handleHostWithoutSlash() {
        showToast("Handle //host");
    }

    @MatchUri("//host/")
    public void handleHostWithSlash() {
        showToast("Handle //host/");
    }

    @MatchUri("//host/user-list")
    public void handleUserList() {
        showToast("Handle //host/user-list");
    }

    @MatchUri("//host/search")
    public void handleSearchWithQuery(@QueryParam("query") String query,
                                      @QueryParam("id") int id) {
        showToast("Handle //host/search?query=" + query + "&id=" + id);
    }

    @MatchUri("//host/users/{id}")
    public void handleUserWithId(@PathParam("id") int id) {
        showToast("Handle //host/users/" + id);
    }

    @MatchUri("//host/users/{id}/follower")
    public void handleFollowerWithUserId(@PathParam("id") int id) {
        showToast("Handle //host/users/" + id + "/follower");
    }

    @MatchUri("//host/users/*")
    public void handleUsersAll() {
        showToast("Handle //host/users/*");
    }

    @MatchUri("/users/1")
    public void handleUser1() {
        showToast("Handle /users/1");
    }
}
