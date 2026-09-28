package Api_tests;

import Common.api.ApiAssert;
import Common.api.basicApi.GoodsApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag("smoke")
public class Homework4Test {

    private final GoodsApi goodsApi = new GoodsApi();

    private final List<Integer> createdGoodsIds = new ArrayList<>();

    @Test
    @DisplayName("GET /goods/list — код 200")
    void getGoodsListReturns200() {
        Response response = goodsApi.getGoodsList(0, 10);

        ApiAssert.statusCode(response, 200);
    }

    @Test
    @DisplayName("POST /goods/add — создание товара")
    void createGoodsReturns200() {
        String name = "TestProduct-" + UUID.randomUUID();
        double price = 12.34;

        Response response = goodsApi.createGoods(name, price);

        ApiAssert.goodsCreated(response);
        rememberCreatedGoods(response);
    }

    @Test
    @DisplayName("POST /goods/add — некорректное имя")
    void createGoodsWithEmptyNameReturns400() {
        Response response = goodsApi.createGoods("", 1.0);

        ApiAssert.statusCode(response, 400);
    }

    @Test
    @DisplayName("GET /goods/{id} — код 200")
    void getGoodsByIdReturns200() {
        int goodsId = createTestGoods();

        Response response = goodsApi.getGoodsById(goodsId);

        ApiAssert.statusCode(response, 200);
    }

    @Test
    @DisplayName("GET /goods/{id} — код 404")
    void getGoodsByIdReturns404() {
        Response response = goodsApi.getGoodsById(999999);

        ApiAssert.statusCode(response, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} — код 200")
    void deleteGoodsReturns200() {
        int goodsId = createTestGoods();

        Response response = goodsApi.deleteGoods(goodsId);

        ApiAssert.statusCode(response, 200);
        createdGoodsIds.remove(Integer.valueOf(goodsId));
    }

    @Test
    @DisplayName("DELETE /goods/{id} — код 404")
    void deleteGoodsReturns404() {
        Response response = goodsApi.deleteGoods(999999);

        ApiAssert.statusCode(response, 404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} — код 200")
    void patchGoodsReturns200() {
        int goodsId = createTestGoods();

        Response response = goodsApi.patchGoods(
                goodsId,
                "Updated-" + UUID.randomUUID(),
                15.55
        );

        ApiAssert.statusCode(response, 200);
    }

    @Test
    @DisplayName("PATCH /goods/{id} — код 400")
    void patchGoodsWithEmptyNameReturns400() {
        int goodsId = createTestGoods();

        Response response = goodsApi.patchGoods(
                goodsId,
                "",
                15.55
        );

        ApiAssert.statusCode(response, 400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} — код 404")
    void patchGoodsReturns404() {
        Response response = goodsApi.patchGoods(
                999999,
                "Updated-" + UUID.randomUUID(),
                15.55
        );

        ApiAssert.statusCode(response, 404);
    }

    @Step("Создать тестовый товар")
    private int createTestGoods() {
        String name = "TestProduct-" + UUID.randomUUID();

        Response response = goodsApi.createGoods(name, 10.0);

        ApiAssert.goodsCreated(response);
        rememberCreatedGoods(response);

        return ApiAssert.createdGoodsId(response);
    }

    @Step("Сохранить ID созданного товара")
    private void rememberCreatedGoods(Response response) {
        int id = ApiAssert.createdGoodsId(response);
        createdGoodsIds.add(id);
    }

    @AfterEach
    @Step("Удалить товары, созданные во время теста")
    void cleanUp() {
        for (Integer id : new ArrayList<>(createdGoodsIds)) {
            Response response = goodsApi.deleteGoods(id);

            if (response.statusCode() != 200
                    && response.statusCode() != 204
                    && response.statusCode() != 404) {
                throw new AssertionError(
                        "Не удалось удалить товар с ID: " + id
                );
            }
        }

        createdGoodsIds.clear();
    }
}
