package org.apache.http.entity.mime;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MinimalField {
    private final String name;
    private final String value;

    MinimalField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.value;
    }

    public String toString() {
        return this.name + ": " + this.value;
    }
}
