package Ui_tests;

import Common.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

@Tag("smoke")

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
        open(Config.getBaseApi());
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


