package Common.api;

import io.restassured.response.Response;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAssert {

    private ApiAssert() {
    }

    public static void statusCode(
            Response response,
            int expectedStatus
    ) {
        assertThat(response.statusCode())
                .as("HTTP-статус ответа")
                .isEqualTo(expectedStatus);
    }

    public static int createdGoodsId(Response response) {
        Integer id = response.jsonPath().getInt("data.id");

        assertThat(id)
                .as("ID созданного товара")
                .isNotNull();

        return id;
    }

    public static void goodsCreated(Response response) {
        statusCode(response, 200);

        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение ответа")
                .isEqualTo("success");

        createdGoodsId(response);
    }

    public static void goodsDeleted(Response response) {
        assertThat(response.statusCode())
                .as("HTTP-статус удаления товара")
                .isIn(200, 204, 404);
    }

    public static void goodsListIsEmpty(Response response) {
        List<?> goods = response.jsonPath()
                .getList("goods");

        assertThat(goods)
                .as("Список товаров")
                .isEmpty();
    }

    public static void goodsListContains(
            Response response,
            String expectedName
    ) {
        List<String> names = response.jsonPath()
                .getList("goods.name", String.class);

        assertThat(names)
                .as("Имена товаров")
                .contains(expectedName);
    }
}
