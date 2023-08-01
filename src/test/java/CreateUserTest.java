import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserLogIn;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest {
    public static final String ERROR_MESSAGE_DOUBLE_USER = "User already exists";
    public static final String ERROR_MESSAGE_NOT_ENOUGH_DATA = "Email, password and name are required fields";
    public String accessToken;
    User user;

    @Before
    public void setup() {
        user = User.generateUserRandom();
    }

    @Test
    @DisplayName("Создать уникального пол-ля")
    public void userCreatTest() {
        ValidatableResponse regResponse = UserClient.createUser(user);
        ValidatableResponse loginResponse = UserClient.loginUser(UserLogIn.getUserLogin(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        regResponse
                .statusCode(200)
                .assertThat().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Создание пол-ля, который уже существует")
    public void doubleUserCreatTest() {
        UserClient.createUser(user);
        accessToken = UserClient.loginUser(UserLogIn.getUserLogin(user))
                .extract().path("accessToken").toString();
        ValidatableResponse response = UserClient.createUser(user);
        response
                .statusCode(403)
                .assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo(ERROR_MESSAGE_DOUBLE_USER));
    }

    @Test
    @DisplayName("Создание пол-ля без заполнения одного из обяз.полей: email")
    public void userCreatWithoutEmailTest() {
        user.setEmail(null);
        ValidatableResponse response = UserClient.createUser(user);
        response
                .statusCode(403)
                .assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo(ERROR_MESSAGE_NOT_ENOUGH_DATA));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}