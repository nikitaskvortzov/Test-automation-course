package Homework;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Homework4 {

    private Integer saveId;

    // Получить список товаров: GET /goods/list
    public Response getGoods() {
        Response resp = given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/goods/list")
                .then()
                .extract().response();

        try {
            if (resp.jsonPath().getList("goods").size() > 0) {
                saveId = resp.jsonPath().getInt("goods[0].id");
            } else {
                saveId = null;
            }
        } catch (Exception e) {
            saveId = null;
        }

        return resp;
    }

    // Геттер для использования id вне метода
    public Integer getSaveId() {
        return saveId;
    }

    // Cоздать товар через POST /goods/add
    public Response postGoods(String name, double price) {
        String payload = "{ \"name\": \"" + name + "\", \"price\": " + price + " }";

        return given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/goods/add")
                .then()
                .extract().response();
    }

    // Обновить товар через PUTCH /goods/{id}
    public Response patchGoods(Integer id, String newName, double newPrice) {

        String payload = "{ \"name\": \"" + newName + "\", \"price\": " + newPrice + " }";
        return given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .pathParam("id", id)
                .body(payload)
                .when()
                .patch("/goods/{id}")
                .then()
                .extract().response();
    }
}

