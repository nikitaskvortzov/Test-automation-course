/*
package Homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.List;
import static Homework.Tag.testTeg;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.preemptive;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Homework4Test {

    private final Homework4 ser = new Homework4();

    static {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.authentication = preemptive().basic("admin", "secret123");
    }

    // Задание 1

    @Tag(testTeg)
    @Test
    @DisplayName("GET /goods/list - код 200, тело пустое")
    void testGetListWithGivenWhenThenEmptyBody() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", hasSize(0));
    }

    @Test
    @DisplayName("GET /goods/list через RequestSpecification - код 200, тело пустое")
    void testGetListViaRequestSpecificationEmptyBody() {
        RequestSpecification req = given()
                .queryParam("page", 0)
                .queryParam("size", 10);

        req.when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", hasSize(0));
    }

    @Test
    @DisplayName("POST /goods/add и проверить через Rest Assured, что GET /goods/list содержит товар")
    void testPostAddAndVerifyInListUsingRestAssured() {
        String name = "TestProduct-" + System.currentTimeMillis();
        double price = 9.99;

        Response postResp = ser.postGoods(name, price);
        int status = postResp.statusCode();
        if (!(status == 200 || status == 201)) {
            throw new AssertionError("POST вернул некорректный статус: " + status);
        }

        when().get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", hasItem(name));
    }

    @Test
    @DisplayName("POST /goods/add и проверить через AssertJ, что GET /goods/list содержит товар")
    void testPostAddAndVerifyInListUsingAssertJ() {
        String name = "AssertJProduct-" + System.currentTimeMillis();
        double price = 12.34;

        Response postResp = ser.postGoods(name, price);
        int status = postResp.statusCode();
        if (!(status == 200 || status == 201)) {
            throw new AssertionError("POST вернул некорректный статус: " + status);
        }

        Response listResp = when().get("/goods/list")
                .then().statusCode(200).extract().response();
        List<String> names = listResp.jsonPath().getList("goods.name", String.class);

        assertThat(names).contains(name);
    }

    // Задание 2

    @Test
    @DisplayName("POST /goods/add - код 200")
    void testPost200() {
        String randomName = "Товар " + System.currentTimeMillis();
        Response resp = ser.postGoods(randomName, 1);
        assertThat(resp.statusCode()).isEqualTo(200);

    }

    @Test
    @DisplayName("POST /goods/add - код 400")
    void testPost400() {
        Response resp = ser.postGoods("", 1);
        assertThat(resp.statusCode()).isEqualTo(400);

    }

    @Test
    @DisplayName("GET /goods/id - код 200")
    void testGetId200() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        assertThat(listId).isNotNull();

        given()
                .pathParam("id", listId)
                .when()
                .get("/goods/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /goods/id - код 404")
    void testGetId404() {
        int notExistingId = 100;

        given()
                .pathParam("id", notExistingId)
                .when()
                .get("/goods/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /goods/id - код 200")
    void testDeleteId200() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        assertThat(listId).isNotNull();

        given()
                .pathParam("id", listId)
                .when()
                .delete("/goods/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("DELETE /goods/id - код 404")
    void testDeleteId404() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        int notExistingId = listId + 100;
        assertThat(listId).isNotNull();

        given()
                .pathParam("id", notExistingId)
                .when()
                .delete("/goods/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 200")
    void testPatch200() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        assertThat(listId).isNotNull();

        String newName = "Товар " + System.currentTimeMillis();
        double newPrice = 12.34;

        Response patchResp = ser.patchGoods(listId, newName, newPrice);
        patchResp.then().statusCode(200);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 400")
    void testPatch400() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        assertThat(listId).isNotNull();

        String notExistingName = "";
        double newPrice = 12.34;

        Response patchResp = ser.patchGoods(listId, notExistingName, newPrice);
        patchResp.then().statusCode(400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} - код 404")
    void testPatch404() {
        Response listResp = ser.getGoods();
        Integer listId = ser.getSaveId();
        assertThat(listId).isNotNull();

        String newName = "Товар " + System.currentTimeMillis();
        double newPrice = 12.34;
        int notExistingId = listId + 100;

        Response patchResp = ser.patchGoods(notExistingId, newName, newPrice);
        patchResp.then().statusCode(404);
    }

    @Test
    @DisplayName("GET /goods/list - код 200")
    void testGetList200() {
         Response resp = ser.getGoods();
         resp.then().statusCode(200);
    }

}


*/
