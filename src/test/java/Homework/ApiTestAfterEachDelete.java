package Homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTestAfterEachDelete {

    private static List<Integer> createdIds = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    public Response postGoods(String name, double price) {
        String payload = "{ \"name\": \"" + name + "\", \"price\": " + price + " }";

        Response response = given()
                .auth().preemptive().basic("admin", "secret123")
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract().response();

        if (response.statusCode() == 200) {
            Integer id = response.jsonPath().getInt("data.id");
            if (id != null) {
                createdIds.add(id);
            }
        }

        return response;
    }

    @AfterEach
    public void cleanUp() {
        for (Integer id : createdIds) {
            deleteGoodsById(id);
        }
        createdIds.clear();
    }

    private void deleteGoodsById(Integer id) {
        if (id == null) return;
        given()
                .auth().preemptive().basic("admin", "secret123")
                .when()
                .delete("/goods/" + id)
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(204), is(404)));
    }

    @Test
    public void testCreateGoodsReturnsSuccessAndId() {
        String uniqueName = "TestProduct-" + UUID.randomUUID();
        double price = 12.34;

        Response response = postGoods(uniqueName, price);

        System.out.println("Response body: " + response.asString());

        response.then().statusCode(200)
                .body("message", equalTo("success"))
                .body("data.id", notNullValue());

    }
}
