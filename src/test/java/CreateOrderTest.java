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

public class CreateOrderTest {
    OrderClient orderStep = new OrderClient();
    private User user;
    private Order order;
    private String accessToken;
    public static final String ERROR_MESSAGE_NOT_INGREDIENTS = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        user = User.generateUserRandom();
    }

    @Test
    @DisplayName("Создание заказа: авторизация + ингредиенты")
    public void createOrderWithLogInTest() {
        ValidatableResponse userResponse = UserClient.createUser(user);
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[1]._id"));
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithLogin(accessToken, order);
        orderResponse.statusCode(200).assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutLoginTest() {
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[0]._id"));
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderStep.createNewOrder(order);
        orderResponse.statusCode(200).assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ValidatableResponse userResponse = UserClient.createUser(user);
        List<String> ingredients = new ArrayList<>();
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithLogin(accessToken, order);
        orderResponse.statusCode(400).assertThat().body("success", equalTo(false)).and().body("message", equalTo(ERROR_MESSAGE_NOT_INGREDIENTS));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithBrokenIngredientsTest() {
        ValidatableResponse userResponse = UserClient.createUser(user);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithInvalidHash(accessToken);
        orderResponse.statusCode(500);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}