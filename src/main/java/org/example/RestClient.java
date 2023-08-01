package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    public static final String USER_URL = "/api/auth/";
    public static final String ORDER_URL = "/api/orders/";
    public static final String INGREDIENTS_URL = "/api/ingredients";
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    protected static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}