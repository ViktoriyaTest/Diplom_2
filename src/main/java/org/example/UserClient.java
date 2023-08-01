package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_PATH = "api/auth/";

    @Step("Создание пол-ля")
    public static ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then()
                .log()
                .all();
    }

    @Step("Логин пол-ля в системе")
    public static ValidatableResponse loginUser(UserLogIn logIn) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(logIn)
                .post(USER_PATH + "login")
                .then()
                .log()
                .all();
    }

    @Step("Изменение пол-ля с авторизацией")
    public static ValidatableResponse changeUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .body("{\"email\": \"NewUser100@yandex.ru\",  \"name\": \"NewUserforBurger\"}")
                .patch(USER_URL + "user")
                .then()
                .log()
                .all();
    }

    @Step("Изменение пол-ля без авторизации")
    public static ValidatableResponse changeDataWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .patch(USER_URL + "user")
                .then()
                .log()
                .all();
    }

    @Step("Удаление пол-ля")
    public static ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .delete(USER_PATH + "user")
                .then()
                .log()
                .all();
    }
}