package Common.api.basicApi;

import Common.api.RestApiBuilder;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

public class GoodsApi {

    @Step("Получить список товаров: page={page}, size={size}")
    public Response getGoodsList(int page, int size) {
        return RestApiBuilder.request()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/goods/list")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Создать товар: {name}, цена: {price}")
    public Response createGoods(String name, double price) {
        Map<String, Object> body = Map.of(
                "name", name,
                "price", price
        );

        return RestApiBuilder.request()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/goods/add")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Получить товар по ID: {id}")
    public Response getGoodsById(int id) {
        return RestApiBuilder.request()
                .pathParam("id", id)
                .when()
                .get("/goods/{id}")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Удалить товар по ID: {id}")
    public Response deleteGoods(int id) {
        return RestApiBuilder.request()
                .pathParam("id", id)
                .when()
                .delete("/goods/{id}")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    @Step("Обновить товар по ID: {id}")
    public Response patchGoods(
            int id,
            String name,
            double price
    ) {
        Map<String, Object> body = Map.of(
                "name", name,
                "price", price
        );

        return RestApiBuilder.request()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch("/goods/{id}")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }
}
