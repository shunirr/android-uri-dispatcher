# UriDispatcher

UriDispatcher is a library for Android.  Dispatching to matching method assigned by annotation.

## How to use

Calling to `UriDispatcher#dispatch` with uri string or intent instance.

```java
@Override
protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    UriDispatcher.dispatch(this, intent);
}
```

Called appropriate methods.

```java
@MatchUri("scheme-test://host")
public void handleUri() {
    showToast("Handle scheme-test://host");
}
```

Scheme part is optional.

```java
@MatchUri("//host")
public void handleHostWithoutSlash() {
    showToast("Handle //host");
}
```

Host part is optional.

```java

@MatchUri("/path")
public void handlePath() {
    showToast("Handle /path");
}
```

You can get QueryStrings using a `QueryParam` annotation.

```java

@MatchUri("//host/search")
public void handleSearchWithQuery(@QueryParam("query") String query,
                                  @QueryParam("id") int id) {
    showToast("Handle //host/search?query=" + query + "&id=" + id);
}
```

You can get any path using a `PathParam` annotation.

```java
@MatchUri("//host/users/{id}")
public void handleUserWithId(@PathParam("id") int id) {
    showToast("Handle //host/users/" + id);
}

@MatchUri("//host/users/{id}/follower")
public void handleFollowerWithUserId(@PathParam("id") int id) {
    showToast("Handle //host/users/" + id + "/follower");
}
```

You can use wildcard.

```java
@MatchUri("//host/users/*")
public void handleUsersAll() {
    showToast("Handle //host/users/*");
}
```

## License

Released under the [Apache License, v2.0](http://www.apache.org/licenses/LICENSE-2.0).
