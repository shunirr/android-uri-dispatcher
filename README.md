# android-scheme-test

作りかけ、以下のようなことが出来る予定。

```java
@Override
protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    SchemeTest.handle(this, intent);
}
```

```java
@SchemeUrl("schemetest://host")
void handleSchemeHost() {
    Log.d("Handle schemetest://host");
}

@SchemeUrl("//host")
void handleHost() {
    Log.d("Handle //host");
}

@SchemeUrl("//host/")
void handleRootPath() {
    Log.d("Handle //host/");
}

@SchemeUrl("//host/path")
void handlePath() {
    Log.d("Handle //host/path");
}

@SchemeUrl("//host/path?query={query}")
void handleStringQueryParam(@SchemeParam("query") String query) {
    Log.d("Handle //host/path?query=string");
}

@SchemeUrl("//host/path?id={id}")
void handleIntegerQueryParam(@SchemeParam("id") int id) {
    Log.d("Handle //host/path?id=integer");
}

@SchemeUrl("//host/path?query={query}&id={id}")
void handleMultiQueryParam(@SchemeParam("query") String query,
                           @SchemeParam("id") int id) {
    Log.d("Handle //host/path?query=query&id=integer");
}

@SchemeUrl("//host/path/{id}")
void handlePathParam(@SchemeParam("id") int id) {
    Log.d("Handle //host/path/{id}");
}

@SchemeUrl("//host/path/wild/*")
void handlePathWildCard() {
    Log.d("Handle //host/path/wild/*");
}
```

## License

Released under the [Apache License, v2.0](http://www.apache.org/licenses/LICENSE-2.0).
