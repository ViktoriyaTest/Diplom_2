package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.example.RestClient.*;

public class OrderClient {
    @Step("Данные об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_URL)
                .then()
                .log()
                .all();
    }

    @Step("Данные о заказах пол-ля с авторизацией")
    public ValidatableResponse getOrderUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_URL)
                .then()
                .log()
                .all();
    }

    @Step("Данные о заказах конкретного пользователя")
    public ValidatableResponse getOrderUserWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_URL)
                .then()
                .log()
                .all();
    }

    @Step("Новый заказ без авторизации")
    public ValidatableResponse createNewOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_URL)
                .then()
                .log()
                .all();
    }

    @Step("Новый заказ с авторизацией")
    public ValidatableResponse createOrderWithLogin(String accessToken, Order order) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_URL)
                .then()
                .log()
                .all();
    }

    @Step("Заказ с неверным хэшем ингредиентов с авторизацией")
    public ValidatableResponse createOrderWithInvalidHash(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body("{\"ingredients\": \"invalid ingredient hash\"}")
                .when()
                .post(ORDER_URL)
                .then()
                .log()
                .all();
    }
}