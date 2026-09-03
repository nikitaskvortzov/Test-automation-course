package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class Homework8AdminLoginPage {

    private final SelenideElement loginInput =
            $("#username");

    private final SelenideElement passwordInput =
            $("#password");

    private final SelenideElement loginButton =
            $("button[type='submit']");

    @BeforeEach
    @Step("Открыть страницу авторизации администратора")
    void setUp() {
        open(Config.getBaseUrl() + "/admin");
    }

    @AfterEach
    @Step("Закрыть браузер")
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Авторизация администратора с данными из конфигурации")
    void adminLoginShouldLoginUsingConfig() {
        loginFromConfig();

        checkAdminPageIsOpened();
    }

    @Step("Ввести логин администратора")
    private void enterLogin() {
        loginInput
                .shouldBe(Condition.visible)
                .setValue(Config.getAdminUsername());
    }

    @Step("Ввести пароль администратора")
    private void enterPassword() {
        passwordInput
                .shouldBe(Condition.visible)
                .setValue(Config.getAdminPassword());
    }

    @Step("Нажать кнопку входа")
    private void clickLogin() {
        loginButton
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Авторизоваться с данными из конфигурации")
    private void loginFromConfig() {
        enterLogin();
        enterPassword();
        clickLogin();
    }

    @Step("Проверить, что открыта админ-панель")
    private void checkAdminPageIsOpened() {
        String currentUrl = WebDriverRunner.url();

        Assertions.assertThat(currentUrl)
                .as("URL после авторизации")
                .contains("/admin");
    }
}


//package Homework;
//
//import Homework.utils.Config;
//import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.SelenideElement;
//import org.junit.jupiter.api.Test;
//
//import static com.codeborne.selenide.Selenide.$;
//import static com.codeborne.selenide.Selenide.open;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class Homework8AdminLoginPage {
//
//    private final SelenideElement loginInput = $("#username");
//    private final SelenideElement passwordInput = $("#password");
//    private final SelenideElement loginButton = $("button[type='submit']");
//
//    public void enterLogin() {
//        loginInput.shouldBe(Condition.visible).setValue(Config.getAdminUsername());
//    }
//
//    public void enterPassword() {
//        passwordInput.shouldBe(Condition.visible).setValue(Config.getAdminPassword());
//    }
//
//    public void clickLogin() {
//        loginButton.shouldBe(Condition.visible).click();
//    }
//
//    public void loginFromConfig() {
//        enterLogin();
//        enterPassword();
//        clickLogin();
//    }
//    @Test
//    void adminLogin_shouldLoginUsingConfig_inlineTest() {
//        open(Config.getBaseUrl() + "/admin");
//        loginFromConfig();
//        String url = com.codeborne.selenide.WebDriverRunner.url();
//        assertTrue(url.contains("/admin"), "После входа должен быть доступ к админке");
//    }
//
//}
//
//
