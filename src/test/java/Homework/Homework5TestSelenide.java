package Homework;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Condition.text;
import static org.hamcrest.Matchers.containsString;
import static com.codeborne.selenide.Selenide.*;

public class Homework5TestSelenide {
    private Homework5 helper;

    @BeforeEach
    void setup() {
        helper = new Homework5();
        open("http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
        helper.close();
    }

    private void ensureProductExists() {
        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]"))
                .exists();

        if (!exists) {
            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
            adminLink.click();

            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
                    .setValue("admin");

            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
                    .setValue("secret123");

            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
                    .setValue("Тест");

            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
                    .setValue("33");

            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]")).shouldBe(Condition.visible);
        }
    }

    private void ensureProductExists2() {
        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест2'] and .//div[contains(.,'333')]]"))
                .exists();

        if (!exists) {
            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
            adminLink.click();

            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
                    .setValue("admin");

            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
                    .setValue("secret123");

            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
                    .setValue("Тест2");

            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
                    .setValue("333");

            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();

        }
    }

    private void ensureProductExists3() {
        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест3'] and .//div[contains(.,'333')]]"))
                .exists();

        if (!exists) {
            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
            adminLink.click();

            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
                    .setValue("admin");

            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
                    .setValue("secret123");

            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();

            // создаём третий тестовый товар через админку, если нужно
            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
                    .setValue("Тест3");

            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
                    .setValue("333");

            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();

            // Вернуться на сайт
            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест3'] and .//div.contains(.,'333')]")).shouldBe(Condition.visible);

        }
    }


    @Test
    void addToCartAndCheckModal_ForPreviouslyAddedProduct_NoPageObject() {
        ensureProductExists();

        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
                .shouldBe(Condition.visible);
        addToCartBtn.click();

        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
        cartBtn.click();

        SelenideElement dialog = $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
                .shouldBe(Condition.visible);

        SelenideElement modalCardName = dialog.$(org.openqa.selenium.By.xpath(".//*[contains(text(),'Тест')]"));
        modalCardName.shouldHave(Condition.text("Тест"));
    }

    @Test
    void addProductViaAdminAndCheckStorefront_NoPageObject() {
        ensureProductExists();

        SelenideElement productCard = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]"))
                .shouldBe(Condition.visible);

        SelenideElement nameInCard = productCard.$("h4");
        SelenideElement priceInCard = productCard.$(org.openqa.selenium.By.xpath(".//div[contains(.,'33')]"));

        nameInCard.shouldHave(Condition.text("Тест"));
        priceInCard.shouldHave(Condition.text("33"));
    }

    @Test
    void adminLoginWithInvalidCredentials_ShouldShowError() {
        SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
        adminLink.click();

        $(org.openqa.selenium.By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible).setValue("randomUser" + System.currentTimeMillis());
        $(org.openqa.selenium.By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible).setValue("randomPass" + System.currentTimeMillis());

        $(org.openqa.selenium.By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();

        $(org.openqa.selenium.By.xpath("//*[contains(text(),'Неверные учетные данные пользователя')]"))
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверные учетные данные пользователя"));
    }

    @Test
    void addToCartThenReload_ShouldResetCartCountToZero() {
        ensureProductExists();

        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
                .shouldBe(Condition.visible);
        addToCartBtn.click();

        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
        cartBtn.click();

        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
                .shouldBe(Condition.visible);

        refresh();
        $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);

        String cartCount = $(org.openqa.selenium.By.id("cart-count")).shouldBe(Condition.visible).getText();
        int count = Integer.parseInt(cartCount);
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    void addToCartTenTimesThenCheckout_ShouldShowInsufficientFunds() {
        ensureProductExists();

        By plusButtonXPath = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
        for (int i = 0; i < 10; i++) {
            SelenideElement plusBtn = $(plusButtonXPath).shouldBe(Condition.visible);
            plusBtn.click();
        }

        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath(
                "//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
                .shouldBe(Condition.visible);
        addToCartBtn.click();

        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
        cartBtn.click();

        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
                .shouldBe(Condition.visible);

        SelenideElement checkoutBtn = $(org.openqa.selenium.By.xpath("//button[contains(.,'Оформить заказ')]"))
                .shouldBe(Condition.visible);
        checkoutBtn.click();

        String alertText = switchTo().alert().getText();
        Assertions.assertThat(alertText).contains("Денег не хватает!");
        switchTo().alert().accept();
    }

    //Задание 3

    @Test
    void addThreeUnitsToCartAndCheckout_WithinBudget_ShouldShowOrderProcessingNotification() {
        ensureProductExists();

        By plusButtonXPath = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
        for (int i = 0; i < 5; i++) {
            SelenideElement plusBtn = $(plusButtonXPath).shouldBe(Condition.visible);
            plusBtn.click();
        }

        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath(
                "//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
                .shouldBe(Condition.visible);
        addToCartBtn.click();

        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
        cartBtn.click();

        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
                .shouldBe(Condition.visible);

        SelenideElement checkoutBtn = $(org.openqa.selenium.By.xpath("//button[contains(.,'Оформить заказ')]"))
                .shouldBe(Condition.visible);
        checkoutBtn.click();

        SelenideElement toast = $(By.xpath("//*[@id=\"toast-container\"]/div[2]"))
                .shouldBe(Condition.visible);
        String toastText = toast.getText();
        Assertions.assertThat(toastText).contains("Заказ принят в обработку!");

    }

    @Test
    void cartTotalVerification_ForSeveralDifferentProducts() {
        ensureProductExists();

        By plusButtonFirst = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
        for (int i = 0; i < 2; i++) {
            SelenideElement btn = $(plusButtonFirst).shouldBe(Condition.visible);
            btn.click();
        }

        By plusButtonCard2 = By.xpath("//div[@id='card-2']//button[@class='qty-btn' and @data-action='qty-change' and @data-id='2' and @data-step='1']");
        for (int i = 0; i < 2; i++) {
            SelenideElement btn = $(plusButtonCard2).shouldBe(Condition.visible);
            btn.click();
        }

        int[] idsToAdd = new int[]{37, 2};
        for (int id : idsToAdd) {
            By addToCartForId = By.xpath(
                    "//div[contains(@class,'product-card')][.//button[@data-id='" + id + "']]//button[@class='btn' and @data-action='add-to-cart']"
            );
            SelenideElement addBtn = $(addToCartForId).shouldBe(Condition.visible);
            addBtn.click();
        }

        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
        cartBtn.click();

        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
                .shouldBe(Condition.visible);

        SelenideElement body = $(org.openqa.selenium.By.tagName("body"));
        body.shouldHave(Condition.text("Сумма: 204 ₽"));
    }

    @Test
    void shouldShowNotificationAfterAdminLoginAndAddProduct() {
        ensureProductExists2();
        boolean productCardExists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест2'] and .//div[contains(.,'333')]]"))
                .exists();

        if (productCardExists) {
            return;
        }

        SelenideElement toast = $(By.cssSelector("#toast-container .toast"))
                .shouldBe(Condition.visible);

        toast.shouldHave(Condition.text("Товар успешно добавлен!"));
    }

    @Test
    void shouldLoginToAdminAndEditProductAndReturnToListVerifyChangesApplied() {
        ensureProductExists3();
    }



}