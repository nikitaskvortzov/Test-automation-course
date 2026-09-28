package Api_tests;

import Common.api.ApiAssert;
import Common.api.basicApi.GoodsApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag("smoke")
public class ApiTestAfterEachDelete {

    private final GoodsApi goodsApi = new GoodsApi();

    private final List<Integer> createdIds = new ArrayList<>();

    @Test
    @Step("Создать товар и проверить получение его идентификатора")
    void createGoodsReturnsSuccessAndId() {
        String uniqueName = "TestProduct-" + UUID.randomUUID();
        double price = 12.34;

        Response response = goodsApi.createGoods(
                uniqueName,
                price
        );

        ApiAssert.goodsCreated(response);

        int goodsId = ApiAssert.createdGoodsId(response);
        createdIds.add(goodsId);
    }

    @AfterEach
    @Step("Удалить товары, созданные во время теста")
    void cleanUp() {
        for (Integer id : createdIds) {
            Response response = goodsApi.deleteGoods(id);
            ApiAssert.goodsDeleted(response);
        }

        createdIds.clear();
    }
}
