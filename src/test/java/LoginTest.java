import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserLogIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class LoginTest {
    public static final String ERROR_MESSAGE_NO_CORRECT_DATA = "email or password are incorrect";
    private User user;
    private String accessToken;

    @Before
    public void setup() {
        user = User.generateUserRandom();
    }

    @Test
    @DisplayName("Логин под существующим пол-ем")
    public void loginTest() {
        UserClient.createUser(user);
        ValidatableResponse response = UserClient.loginUser(UserLogIn.getUserLogin(user));
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(200)
                .assertThat()
                .body("accessToken", is(notNullValue()));
    }

    @Test
    @DisplayName("Логин пол-ля с неверным полем: логин")
    public void loginWithNoCorrectLoginTest() {
        user.setEmail("randomUnknownUser");
        ValidatableResponse response = UserClient.loginUser(UserLogIn.getUserLogin(user));
        response
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo(ERROR_MESSAGE_NO_CORRECT_DATA));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}