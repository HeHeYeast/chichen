package com.facebook.model;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface GraphPlace extends GraphObject {
    String getCategory();

    String getId();

    GraphLocation getLocation();

    String getName();

    void setCategory(String str);

    void setId(String str);

    void setLocation(GraphLocation graphLocation);

    void setName(String str);
}
