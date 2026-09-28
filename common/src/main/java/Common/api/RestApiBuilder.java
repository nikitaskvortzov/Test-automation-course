package Common.api;

import Common.config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestApiBuilder {

    public static RequestSpecification request() {
        return given()
                .baseUri(Config.getBaseUrl())
                .auth()
                .preemptive()
                .basic(
                        Config.getAdminUsername(),
                        Config.getAdminPassword()
                )
                .filter(new AllureRestAssured());
    }
}
