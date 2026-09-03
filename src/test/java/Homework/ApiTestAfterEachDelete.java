package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ApiTestAfterEachDelete {

    private static final List<Integer> createdIds = new ArrayList<>();

    private static final String USERNAME = Config.getAdminUsername();
    private static final String PASSWORD = Config.getAdminPassword();

    @BeforeAll
    public static void setup() {
        setupRestAssured();
        setupAllureSelenide();
    }

    @Step("Настройка REST Assured")
    private static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.filters(
                new AllureRestAssured()
        );
    }

    @Step("Настроика Allure Selenide Listener")
    private static void setupAllureSelenide() {
        if (!SelenideLogger.hasListener("allure")) {
            SelenideLogger.addListener(
                    "allure",
                    new AllureSelenide()
                            .screenshots(true)
                            .savePageSource(true)
            );
        }
    }

    @Test
    public void testCreateGoodsReturnsSuccessAndId() {
        String uniqueName = "TestProduct-" + UUID.randomUUID();
        double price = 12.34;

        Integer goodsId = createGoodsAndRememberId(
                uniqueName,
                price
        );

        checkCreatedGoodsId(goodsId);
    }

    @Step("Создать товар и сохранить его идентификатор для удаления")
    public Integer createGoodsAndRememberId(String name, double price) {
        Response response = postGoods(name, price);

        checkGoodsCreatedSuccessfully(response);

        Integer id = response.jsonPath().getInt("data.id");
        createdIds.add(id);

        return id;
    }

    @Step("Отправить POST-запрос на создание товара: {name}, цена: {price}")
    public Response postGoods(String name, double price) {
        Map<String, Object> payload = Map.of(
                "name", name,
                "price", price
        );

        return given()
                .filter(new AllureRestAssured())
                .auth()
                .preemptive()
                .basic(USERNAME, PASSWORD)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/goods/add")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Проверить успешное создание товара")
    public void checkGoodsCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(200)
                .body("message", equalTo("success"))
                .body("data.id", notNullValue());
    }

    @Step("Проверить, что идентификатор товара получен")
    public void checkCreatedGoodsId(Integer id) {
        if (id == null) {
            throw new AssertionError(
                    "Идентификатор созданного товара не должен быть null"
            );
        }
    }

    @AfterEach
    @Step("Удалить товары, созданные во время теста")
    public void cleanUp() {
        for (Integer id : createdIds) {
            if (id != null) {
                Response response = deleteGoodsById(id);
                checkGoodsDeletedSuccessfully(response);
            }
        }

        createdIds.clear();
    }

    @Step("Отправить DELETE-запрос для удаления товара с id: {id}")
    public Response deleteGoodsById(Integer id) {
        return given()
                .filter(new AllureRestAssured())
                .auth()
                .preemptive()
                .basic(USERNAME, PASSWORD)
                .when()
                .delete("/goods/{id}", id)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Проверить успешное удаление товара")
    public void checkGoodsDeletedSuccessfully(Response response) {
        response.then()
                .statusCode(anyOf(
                        is(200),
                        is(204),
                        is(404)
                ));
    }
}

