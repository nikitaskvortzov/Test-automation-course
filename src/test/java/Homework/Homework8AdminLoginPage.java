package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework8AdminLoginPage {

    private final SelenideElement loginInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("button[type='submit']");

    public void enterLogin() {
        loginInput.shouldBe(Condition.visible).setValue(Config.getAdminUsername());
    }

    public void enterPassword() {
        passwordInput.shouldBe(Condition.visible).setValue(Config.getAdminPassword());
    }

    public void clickLogin() {
        loginButton.shouldBe(Condition.visible).click();
    }

    public void loginFromConfig() {
        enterLogin();
        enterPassword();
        clickLogin();
    }
    @Test
    void adminLogin_shouldLoginUsingConfig_inlineTest() {
        open(Config.getBaseUrl() + "/admin");
        loginFromConfig();
        String url = com.codeborne.selenide.WebDriverRunner.url();
        assertTrue(url.contains("/admin"), "После входа должен быть доступ к админке");
    }

}


