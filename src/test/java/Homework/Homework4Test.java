package Homework;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static Homework.Tag.testTeg;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class Homework4Test {

    private final Homework4 service = new Homework4();

    private static final List<Integer> CREATED_GOODS_IDS = new ArrayList<>();

    @BeforeAll
    @Step("Настроить Allure для API и UI-тестов")
    static void setUpAllure() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.authentication =
                RestAssured.preemptive()
                        .basic("admin", "secret123");

        RestAssured.filters(
                new AllureRestAssured()
        );

        if (!SelenideLogger.hasListener("allure")) {
            SelenideLogger.addListener(
                    "allure",
                    new AllureSelenide()
                            .screenshots(true)
                            .savePageSource(true)
            );
        }
    }

    @Tag(testTeg)
    @Test
    @DisplayName("GET /goods/list - код 200, тело пустое")
    void testGetListWithGivenWhenThenEmptyBody() {
        Response response = getGoodsList(0, 10);

        checkStatusCode(response, 200);
        checkGoodsListIsEmpty(response);
    }

    @Test
    @DisplayName("GET /goods/list через RequestSpecification - код 200, тело пустое")
    void testGetListViaRequestSpecificationEmptyBody() {
        RequestSpecification request = createGoodsListRequest(0, 10);

        Response response = sendGoodsListRequest(request);

        checkStatusCode(response, 200);
        checkGoodsListIsEmpty(response);
    }

    @Test
    @DisplayName("POST /goods/add и проверка товара через GET /goods/list")
    void testPostAddAndVerifyInListUsingRestAssured() {
        String name = "TestProduct-" + System.currentTimeMillis();
        double price = 9.99;

        Integer goodsId = createGoods(name, price);

        Response response = getGoodsList(0, 100);

        checkStatusCode(response, 200);
        checkGoodsExistsInList(response, name);
        rememberGoodsId(goodsId);
    }

    @Test
    @DisplayName("POST /goods/add и проверка товара через AssertJ")
    void testPostAddAndVerifyInListUsingAssertJ() {
        String name = "AssertJProduct-" + System.currentTimeMillis();
        double price = 12.34;

        Integer goodsId = createGoods(name, price);

        Response response = getGoodsList(0, 100);

        List<String> names = getGoodsNames(response);

        checkGoodsNameUsingAssertJ(names, name);
        rememberGoodsId(goodsId);
    }

    @Test
    @DisplayName("POST /goods/add - код 200")
    void testPost200() {
        String name = "Товар " + System.currentTimeMillis();

        Response response = createGoodsRequest(name, 1);

        checkStatusCode(response, 200);
        saveCreatedGoodsId(response);
    }

    @Test
    @DisplayName("POST /goods/add - код 400")
    void testPost400() {
        Response response = createGoodsRequest("", 1);

        checkStatusCode(response, 400);
    }

    @Test
    @DisplayName("GET /goods/{id} - код 200")
    void testGetId200() {
        Integer goodsId = getExistingGoodsId();

        Response response = getGoodsById(goodsId);

        checkStatusCode(response, 200);
    }

    @Test
    @DisplayName("GET /goods/{id} - код 404")
    void testGetId404() {
        int notExistingId = 100;

        Response response = getGoodsById(notExistingId);

        checkStatusCode(response, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} - код 200")
    void testDeleteId200() {
        Integer goodsId = getExistingGoodsId();

        Response response = deleteGoodsById(goodsId);

        checkStatusCode(response, 200);
    }

    @Test
    @DisplayName("DELETE /goods/{id} - код 404")
    void testDeleteId404() {
        Integer existingId = getExistingGoodsId();
        int notExistingId = existingId + 100;

        Response response = deleteGoodsById(notExistingId);

        checkStatusCode(response, 404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 200")
    void testPatch200() {
        Integer goodsId = getExistingGoodsId();

        String newName = "Товар " + System.currentTimeMillis();
        double newPrice = 12.34;

        Response response = patchGoods(goodsId, newName, newPrice);

        checkStatusCode(response, 200);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 400")
    void testPatch400() {
        Integer goodsId = getExistingGoodsId();

        Response response = patchGoods(goodsId, "", 12.34);

        checkStatusCode(response, 400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 404")
    void testPatch404() {
        Integer existingId = getExistingGoodsId();
        int notExistingId = existingId + 100;

        String newName = "Товар " + System.currentTimeMillis();

        Response response = patchGoods(
                notExistingId,
                newName,
                12.34
        );

        checkStatusCode(response, 404);
    }

    @Test
    @DisplayName("GET /goods/list - код 200")
    void testGetList200() {
        Response response = getGoodsList(0, 10);

        checkStatusCode(response, 200);
    }


    @Step("Создать GET-запрос списка товаров: page={page}, size={size}")
    private RequestSpecification createGoodsListRequest(int page, int size) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("page", page)
                .queryParam("size", size);
    }

    @Step("Отправить GET-запрос списка товаров")
    private Response sendGoodsListRequest(RequestSpecification request) {
        return request
                .when()
                .get("/goods/list")
                .then()
                .extract()
                .response();
    }

    @Step("Получить список товаров: page={page}, size={size}")
    private Response getGoodsList(int page, int size) {
        return given()
                .filter(new AllureRestAssured())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/goods/list")
                .then()
                .extract()
                .response();
    }

    @Step("Создать товар: {name}, цена: {price}")
    private Integer createGoods(String name, double price) {
        Response response = createGoodsRequest(name, price);

        checkStatusCode(response, 200);

        Integer id = response.jsonPath().getInt("data.id");
        assertThat(id).as("ID созданного товара").isNotNull();

        return id;
    }

    @Step("Отправить POST-запрос на создание товара: {name}")
    private Response createGoodsRequest(String name, double price) {
        return service.postGoods(name, price);
    }

    @Step("Получить товар по ID: {goodsId}")
    private Response getGoodsById(int goodsId) {
        return given()
                .filter(new AllureRestAssured())
                .pathParam("id", goodsId)
                .when()
                .get("/goods/{id}")
                .then()
                .extract()
                .response();
    }

    @Step("Удалить товар по ID: {goodsId}")
    private Response deleteGoodsById(int goodsId) {
        return given()
                .filter(new AllureRestAssured())
                .pathParam("id", goodsId)
                .when()
                .delete("/goods/{id}")
                .then()
                .extract()
                .response();
    }

    @Step("Изменить товар по ID: {goodsId}")
    private Response patchGoods(
            int goodsId,
            String name,
            double price
    ) {
        return service.patchGoods(goodsId, name, price);
    }

    @Step("Получить существующий ID товара")
    private Integer getExistingGoodsId() {
        service.getGoods();

        Integer goodsId = service.getSaveId();

        assertThat(goodsId)
                .as("ID товара")
                .isNotNull();

        return goodsId;
    }

    @Step("Получить имена товаров из ответа")
    private List<String> getGoodsNames(Response response) {
        return response.jsonPath()
                .getList("goods.name", String.class);
    }


    @Step("Проверить HTTP-статус: ожидается {expectedStatus}")
    private void checkStatusCode(Response response, int expectedStatus) {
        assertThat(response.statusCode())
                .as("HTTP-статус ответа")
                .isEqualTo(expectedStatus);
    }

    @Step("Проверить, что список товаров пуст")
    private void checkGoodsListIsEmpty(Response response) {
        List<?> goods = response.jsonPath()
                .getList("goods");

        assertThat(goods)
                .as("Список товаров")
                .isEmpty();
    }

    @Step("Проверить наличие товара в списке: {name}")
    private void checkGoodsExistsInList(
            Response response,
            String name
    ) {
        List<String> names = getGoodsNames(response);

        assertThat(names)
                .as("Имена товаров")
                .contains(name);
    }

    @Step("Проверить наличие товара через AssertJ: {name}")
    private void checkGoodsNameUsingAssertJ(
            List<String> names,
            String name
    ) {
        assertThat(names)
                .as("Имена товаров")
                .contains(name);
    }

    @Step("Сохранить ID созданного товара")
    private void saveCreatedGoodsId(Response response) {
        Integer goodsId = response.jsonPath()
                .getInt("data.id");

        rememberGoodsId(goodsId);
    }

    @Step("Запомнить ID товара для последующего удаления: {goodsId}")
    private void rememberGoodsId(Integer goodsId) {
        if (goodsId != null) {
            CREATED_GOODS_IDS.add(goodsId);
        }
    }


    @AfterEach
    @Step("Удалить товары, созданные во время теста")
    void cleanUp() {
        for (Integer goodsId : CREATED_GOODS_IDS) {
            try {
                Response response = deleteGoodsById(goodsId);

                if (response.statusCode() != 200
                        && response.statusCode() != 404) {
                    System.out.println(
                            "Не удалось удалить товар с ID: " + goodsId
                    );
                }
            } catch (Exception exception) {
                System.out.println(
                        "Ошибка при удалении товара с ID: " + goodsId
                );
            }
        }

        CREATED_GOODS_IDS.clear();
    }
}


//package Homework;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import java.util.List;
//import static Homework.Tag.testTeg;
//import static io.restassured.RestAssured.*;
//import static io.restassured.RestAssured.preemptive;
//import static org.hamcrest.Matchers.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class Homework4Test {
//
//    private final Homework4 ser = new Homework4();
//
//    static {
//        RestAssured.baseURI = "http://localhost:8080";
//        RestAssured.authentication = preemptive().basic("admin", "secret123");
//    }
//
//    // Задание 1
//
//    @Tag(testTeg)
//    @Test
//    @DisplayName("GET /goods/list - код 200, тело пустое")
//    void testGetListWithGivenWhenThenEmptyBody() {
//        given()
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .when()
//                .get("/goods/list")
//                .then()
//                .statusCode(200)
//                .body("goods", hasSize(0));
//    }
//
//    @Test
//    @DisplayName("GET /goods/list через RequestSpecification - код 200, тело пустое")
//    void testGetListViaRequestSpecificationEmptyBody() {
//        RequestSpecification req = given()
//                .queryParam("page", 0)
//                .queryParam("size", 10);
//
//        req.when()
//                .get("/goods/list")
//                .then()
//                .statusCode(200)
//                .body("goods", hasSize(0));
//    }
//
//    @Test
//    @DisplayName("POST /goods/add и проверить через Rest Assured, что GET /goods/list содержит товар")
//    void testPostAddAndVerifyInListUsingRestAssured() {
//        String name = "TestProduct-" + System.currentTimeMillis();
//        double price = 9.99;
//
//        Response postResp = ser.postGoods(name, price);
//        int status = postResp.statusCode();
//        if (!(status == 200 || status == 201)) {
//            throw new AssertionError("POST вернул некорректный статус: " + status);
//        }
//
//        when().get("/goods/list")
//                .then()
//                .statusCode(200)
//                .body("goods.name", hasItem(name));
//    }
//
//    @Test
//    @DisplayName("POST /goods/add и проверить через AssertJ, что GET /goods/list содержит товар")
//    void testPostAddAndVerifyInListUsingAssertJ() {
//        String name = "AssertJProduct-" + System.currentTimeMillis();
//        double price = 12.34;
//
//        Response postResp = ser.postGoods(name, price);
//        int status = postResp.statusCode();
//        if (!(status == 200 || status == 201)) {
//            throw new AssertionError("POST вернул некорректный статус: " + status);
//        }
//
//        Response listResp = when().get("/goods/list")
//                .then().statusCode(200).extract().response();
//        List<String> names = listResp.jsonPath().getList("goods.name", String.class);
//
//        assertThat(names).contains(name);
//    }
//
//    // Задание 2
//
//    @Test
//    @DisplayName("POST /goods/add - код 200")
//    void testPost200() {
//        String randomName = "Товар " + System.currentTimeMillis();
//        Response resp = ser.postGoods(randomName, 1);
//        assertThat(resp.statusCode()).isEqualTo(200);
//
//    }
//
//    @Test
//    @DisplayName("POST /goods/add - код 400")
//    void testPost400() {
//        Response resp = ser.postGoods("", 1);
//        assertThat(resp.statusCode()).isEqualTo(400);
//
//    }
//
//    @Test
//    @DisplayName("GET /goods/id - код 200")
//    void testGetId200() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        assertThat(listId).isNotNull();
//
//        given()
//                .pathParam("id", listId)
//                .when()
//                .get("/goods/{id}")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    @DisplayName("GET /goods/id - код 404")
//    void testGetId404() {
//        int notExistingId = 100;
//
//        given()
//                .pathParam("id", notExistingId)
//                .when()
//                .get("/goods/{id}")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    @DisplayName("DELETE /goods/id - код 200")
//    void testDeleteId200() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        assertThat(listId).isNotNull();
//
//        given()
//                .pathParam("id", listId)
//                .when()
//                .delete("/goods/{id}")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    @DisplayName("DELETE /goods/id - код 404")
//    void testDeleteId404() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        int notExistingId = listId + 100;
//        assertThat(listId).isNotNull();
//
//        given()
//                .pathParam("id", notExistingId)
//                .when()
//                .delete("/goods/{id}")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    @DisplayName("PATCH /goods/{id} - код 200")
//    void testPatch200() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        assertThat(listId).isNotNull();
//
//        String newName = "Товар " + System.currentTimeMillis();
//        double newPrice = 12.34;
//
//        Response patchResp = ser.patchGoods(listId, newName, newPrice);
//        patchResp.then().statusCode(200);
//    }
//
//    @Test
//    @DisplayName("PATCH /goods/{id} - код 400")
//    void testPatch400() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        assertThat(listId).isNotNull();
//
//        String notExistingName = "";
//        double newPrice = 12.34;
//
//        Response patchResp = ser.patchGoods(listId, notExistingName, newPrice);
//        patchResp.then().statusCode(400);
//    }
//
//    @Test
//    @DisplayName("PATCH /goods/{id} - код 404")
//    void testPatch404() {
//        Response listResp = ser.getGoods();
//        Integer listId = ser.getSaveId();
//        assertThat(listId).isNotNull();
//
//        String newName = "Товар " + System.currentTimeMillis();
//        double newPrice = 12.34;
//        int notExistingId = listId + 100;
//
//        Response patchResp = ser.patchGoods(notExistingId, newName, newPrice);
//        patchResp.then().statusCode(404);
//    }
//
//    @Test
//    @DisplayName("GET /goods/list - код 200")
//    void testGetList200() {
//         Response resp = ser.getGoods();
//         resp.then().statusCode(200);
//    }
//
//}

