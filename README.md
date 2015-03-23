# android-scheme-test

作りかけ、以下のようなことが出来る予定。

```java
Override
protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    SchemeTest.handle(this, intent);
}
```

```java
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
```

## License

Released under the [Apache License, v2.0](http://www.apache.org/licenses/LICENSE-2.0).
