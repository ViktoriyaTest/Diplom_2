import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserTest {
    public static final String ERROR_MESSAGE_NOT_AUTHORISED = "You should be authorised";
    User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.generateUserRandom();
    }

    @Test
    @DisplayName("Изменение данных пол-ля с авторизацией")
    public void changeUserInfoWithLoginTest() {
        ValidatableResponse regResponse = UserClient.createUser(user);
        accessToken = regResponse.extract().path("accessToken");
        ValidatableResponse changeResponse = UserClient.changeUser(user, accessToken);
        changeResponse
                .statusCode(200)
                .assertThat()
                .body("user.email", equalTo("newuser100@yandex.ru"))
                .and()
                .body("user.name", equalTo("NewUserforBurger"))
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пол-ля без авторизацией")
    public void changeUserInfoWithoutLoginTest() {
        String newEmail = user.getEmail() + "test";
        user.setName(newEmail);
        ValidatableResponse responseUpdate = UserClient.changeDataWithoutAuth(user);
        responseUpdate
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo(ERROR_MESSAGE_NOT_AUTHORISED));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}