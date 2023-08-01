import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.Order;
import org.example.OrderClient;
import org.example.User;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class GetOrderFromUserTest {
    public static final String ERROR_MESSAGE_NOT_AUTHORISED = "You should be authorised";
    OrderClient orderClient = new OrderClient();
    private User user;
    private Order order;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.generateUserRandom();
    }

    @Test
    @DisplayName("Получить список заказов с авторизацией")
    public void getDataOrderWithLoginTest() {
        ValidatableResponse userResponse = UserClient.createUser(user);
        ValidatableResponse ingredientsResponse = orderClient.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[1]._id"));
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        orderClient.createOrderWithLogin(accessToken, order);
        ValidatableResponse orderResponse = orderClient.getOrderUser(accessToken);
        orderResponse.statusCode(200).assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получить список заказов без авторизацией")
    public void getDataOrderWithoutLoginTest() {
        ValidatableResponse ingredientsResponse = orderClient.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[3]._id"));
        order = new Order(ingredients);
        orderClient.createNewOrder(order);
        ValidatableResponse orderResponse = orderClient.getOrderUserWithoutLogin();
        orderResponse.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo(ERROR_MESSAGE_NOT_AUTHORISED));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}