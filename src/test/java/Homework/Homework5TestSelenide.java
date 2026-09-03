package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.switchTo;

public class Homework5TestSelenide {

    private static final String BASE_URL = Config.getBaseUrl();

    private static final String PRODUCT_NAME = "Тест";
    private static final String PRODUCT_PRICE = Config.getStartProductPrice2();

    private static final String PRODUCT_NAME_2 = "Тест2";
    private static final String PRODUCT_PRICE_2 = "333";

    private static final String PRODUCT_NAME_3 = "Тест3";
    private static final String PRODUCT_PRICE_3 = "333";

    private Homework5 helper;

    @BeforeAll
    @Step("Настройка Allure Selenide Listener")
    static void setUpAllureSelenide() {
        if (!SelenideLogger.hasListener("allure")) {
            SelenideLogger.addListener(
                    "allure",
                    new AllureSelenide()
                            .screenshots(true)
                            .savePageSource(true)
            );
        }
    }

    @BeforeEach
    @Step("Открыть главную страницу магазина")
    void setUp() {
        helper = new Homework5();
        open(BASE_URL);
    }

    @AfterEach
    @Step("Закрыть браузер и освободить ресурсы")
    void tearDown() {
        closeWebDriver();
        helper.close();
    }

    @Test
    void addToCartAndCheckModalForPreviouslyAddedProduct() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        clickAddToCart(PRODUCT_NAME, PRODUCT_PRICE);
        openCart();

        checkCartModalIsVisible();
        checkProductInCartModal(PRODUCT_NAME);
    }

    @Test
    void addProductViaAdminAndCheckStorefront() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        checkProductCardIsVisible(PRODUCT_NAME, PRODUCT_PRICE);
        checkProductName(PRODUCT_NAME, PRODUCT_PRICE);
        checkProductPrice(PRODUCT_NAME, PRODUCT_PRICE);
    }

    @Test
    void adminLoginWithInvalidCredentialsShouldShowError() {
        openAdminPage();

        enterLogin("randomUser" + System.currentTimeMillis());
        enterPassword("randomPass" + System.currentTimeMillis());
        clickSignInButton();

        checkInvalidCredentialsError();
    }

    @Test
    void addToCartThenReloadShouldResetCartCountToZero() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        clickAddToCart(PRODUCT_NAME, PRODUCT_PRICE);
        openCart();
        checkCartModalIsVisible();

        reloadPage();

        checkCartButtonIsVisible();
        checkCartCount(0);
    }

    @Test
    void addToCartTenTimesThenCheckoutShouldShowInsufficientFunds() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        increaseProductQuantity(37, 10);
        clickAddToCart(PRODUCT_NAME, PRODUCT_PRICE);
        openCart();

        checkCartModalIsVisible();
        clickCheckoutButton();

        checkAlertText("Денег не хватает!");
        acceptAlert();
    }

    @Test
    void addThreeUnitsToCartAndCheckoutWithinBudget() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        increaseProductQuantity(37, 5);
        clickAddToCart(PRODUCT_NAME, PRODUCT_PRICE);
        openCart();

        checkCartModalIsVisible();
        clickCheckoutButton();

        checkOrderProcessingNotification();
    }

    @Test
    void cartTotalVerificationForSeveralDifferentProducts() {
        ensureProductExists(PRODUCT_NAME, PRODUCT_PRICE);

        increaseProductQuantity(37, 2);
        increaseProductQuantity(2, 2);

        clickAddToCartById(37);
        clickAddToCartById(2);

        openCart();
        checkCartModalIsVisible();
        checkCartTotal("Сумма: 204 ₽");
    }

    @Test
    void shouldShowNotificationAfterAdminLoginAndAddProduct() {
        createProductThroughAdmin(
                PRODUCT_NAME_2,
                PRODUCT_PRICE_2
        );

        checkProductCreatedNotification();
    }

    @Test
    void shouldLoginToAdminAndEditProductAndReturnToList() {
        createProductThroughAdmin(
                PRODUCT_NAME_3,
                PRODUCT_PRICE_3
        );

        returnToStorefront();

        checkProductCardIsVisible(
                PRODUCT_NAME_3,
                PRODUCT_PRICE_3
        );
    }

    @Step("Проверить наличие товара {productName} с ценой {price}")
    private void ensureProductExists(
            String productName,
            String price
    ) {
        if (!isProductExists(productName, price)) {
            createProductThroughAdmin(productName, price);
            returnToStorefront();
        }

        checkProductCardIsVisible(productName, price);
    }

    @Step("Проверить наличие товара {productName} с ценой {price}")
    private boolean isProductExists(
            String productName,
            String price
    ) {
        return productCard(productName, price).exists();
    }

    @Step("Создать товар через панель администратора: {productName}, цена {price}")
    private void createProductThroughAdmin(
            String productName,
            String price
    ) {
        openAdminPage();

        enterLogin("admin");
        enterPassword("secret123");
        clickSignInButton();

        enterProductName(productName);
        enterProductPrice(price);
        clickCreateButton();

        checkProductCreatedNotification();
    }

    @Step("Открыть страницу администратора")
    private void openAdminPage() {
        $(By.cssSelector("a[href='/admin'].btn-outline"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Ввести логин: {username}")
    private void enterLogin(String username) {
        $(By.xpath("//input[@placeholder='Username']"))
                .shouldBe(Condition.visible)
                .setValue(username);
    }

    @Step("Ввести пароль")
    private void enterPassword(String password) {
        $(By.xpath("//input[@placeholder='Password']"))
                .shouldBe(Condition.visible)
                .setValue(password);
    }

    @Step("Нажать кнопку Sign in")
    private void clickSignInButton() {
        $(By.xpath("//button[contains(.,'Sign in')]"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Ввести название товара: {productName}")
    private void enterProductName(String productName) {
        $(By.xpath("//input[@placeholder='Название']"))
                .shouldBe(Condition.visible)
                .setValue(productName);
    }

    @Step("Ввести цену товара: {price}")
    private void enterProductPrice(String price) {
        $(By.xpath("//input[@placeholder='Цена']"))
                .shouldBe(Condition.visible)
                .setValue(price);
    }

    @Step("Нажать кнопку создания товара")
    private void clickCreateButton() {
        $(By.xpath("//button[contains(.,'Создать')]"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Вернуться на главную страницу магазина")
    private void returnToStorefront() {
        $(By.xpath("//*[contains(text(),'Вернуться на сайт')]"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Добавить товар {productName} в корзину")
    private void clickAddToCart(
            String productName,
            String price
    ) {
        productCard(productName, price)
                .$(By.xpath(".//button[contains(.,'В корзину')]"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Добавить товар с ID {productId} в корзину")
    private void clickAddToCartById(int productId) {
        $(By.xpath(
                "//div[contains(@class,'product-card')]" +
                        "[.//button[@data-id='" + productId + "']]" +
                        "//button[@data-action='add-to-cart']"
        ))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Увеличить количество товара с ID {productId} на {count} единиц")
    private void increaseProductQuantity(
            int productId,
            int count
    ) {
        By plusButton = By.xpath(
                "//button[@class='qty-btn']" +
                        "[@data-action='qty-change']" +
                        "[@data-id='" + productId + "']" +
                        "[@data-step='1']"
        );

        for (int i = 0; i < count; i++) {
            $(plusButton)
                    .shouldBe(Condition.visible)
                    .click();
        }
    }

    @Step("Открыть корзину")
    private void openCart() {
        $(By.id("open-cart-btn"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Перезагрузить страницу")
    private void reloadPage() {
        refresh();
    }

    @Step("Нажать кнопку оформления заказа")
    private void clickCheckoutButton() {
        $(By.xpath("//button[contains(.,'Оформить заказ')]"))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Принять браузерное уведомление")
    private void acceptAlert() {
        switchTo().alert().accept();
    }


    @Step("Проверить отображение карточки товара {productName} с ценой {price}")
    private void checkProductCardIsVisible(
            String productName,
            String price
    ) {
        productCard(productName, price)
                .shouldBe(Condition.visible);
    }

    @Step("Проверить название товара {productName}")
    private void checkProductName(
            String productName,
            String price
    ) {
        productCard(productName, price)
                .$("h4")
                .shouldHave(Condition.text(productName));
    }

    @Step("Проверить цену товара {price}")
    private void checkProductPrice(
            String productName,
            String price
    ) {
        productCard(productName, price)
                .$(By.xpath(".//div[contains(.,'" + price + "')]"))
                .shouldHave(Condition.text(price));
    }

    @Step("Проверить, что модальное окно корзины отображается")
    private void checkCartModalIsVisible() {
        cartModal()
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие товара {productName} в корзине")
    private void checkProductInCartModal(String productName) {
        cartModal()
                .$(By.xpath(".//*[contains(text(),'" + productName + "')]"))
                .shouldHave(Condition.text(productName));
    }

    @Step("Проверить кнопку корзины")
    private void checkCartButtonIsVisible() {
        $(By.id("open-cart-btn"))
                .shouldBe(Condition.visible);
    }

    @Step("Проверить количество товаров в корзине: {expectedCount}")
    private void checkCartCount(int expectedCount) {
        String actualCount = $(By.id("cart-count"))
                .shouldBe(Condition.visible)
                .getText();

        Assertions.assertThat(Integer.parseInt(actualCount))
                .isEqualTo(expectedCount);
    }

    @Step("Проверить сообщение о неверных учетных данных")
    private void checkInvalidCredentialsError() {
        $(By.xpath(
                "//*[contains(text(),'Неверные учетные данные пользователя')]"
        ))
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.text(
                                "Неверные учетные данные пользователя"
                        )
                );
    }

    @Step("Проверить текст браузерного уведомления: {expectedText}")
    private void checkAlertText(String expectedText) {
        String actualText = switchTo()
                .alert()
                .getText();

        Assertions.assertThat(actualText)
                .contains(expectedText);
    }

    @Step("Проверить уведомление о недостатке денежных средств")
    private void checkInsufficientFundsNotification() {
        checkAlertText("Денег не хватает!");
    }

    @Step("Проверить уведомление о принятии заказа")
    private void checkOrderProcessingNotification() {
        $(By.xpath("//*[@id='toast-container']/div[2]"))
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.text("Заказ принят в обработку!")
                );
    }

    @Step("Проверить уведомление об успешном создании товара")
    private void checkProductCreatedNotification() {
        $(By.cssSelector("#toast-container .toast"))
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.text("Товар успешно добавлен!")
                );
    }

    @Step("Проверить итоговую сумму корзины: {expectedTotal}")
    private void checkCartTotal(String expectedTotal) {
        $("body")
                .shouldHave(Condition.text(expectedTotal));
    }


    @Step("Найти карточку товара {productName} с ценой {price}")
    private SelenideElement productCard(
            String productName,
            String price
    ) {
        return $(By.xpath(
                "//div[contains(@class,'product-card')]" +
                        "[.//h4[normalize-space()='" + productName + "']]" +
                        "[.//div[contains(normalize-space(),'" + price + "')]]"
        ));
    }

    @Step("Найти модальное окно корзины")
    private SelenideElement cartModal() {
        return $(By.xpath(
                "//div[" +
                        "@role='dialog' " +
                        "or @id='cart-modal' " +
                        "or contains(@class,'cart-modal') " +
                        "or contains(@class,'modal')" +
                        "]"
        ));
    }
}

//package Homework;
//
//import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.Selenide;
//import com.codeborne.selenide.SelenideElement;
//import io.restassured.response.Response;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import static com.codeborne.selenide.Condition.text;
//import static org.hamcrest.Matchers.containsString;
//import static com.codeborne.selenide.Selenide.*;
//
//public class Homework5TestSelenide {
//    private Homework5 helper;
//
//    @BeforeEach
//    void setup() {
//        helper = new Homework5();
//        open("http://localhost:8080");
//    }
//
//    @AfterEach
//    void tearDown() {
//        closeWebDriver();
//        helper.close();
//    }
//
//    private void ensureProductExists() {
//        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]"))
//                .exists();
//
//        if (!exists) {
//            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
//            adminLink.click();
//
//            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
//                    .setValue("admin");
//
//            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
//                    .setValue("secret123");
//
//            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();
//
//            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
//                    .setValue("Тест");
//
//            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
//                    .setValue("33");
//
//            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();
//
//            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();
//
//            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]")).shouldBe(Condition.visible);
//        }
//    }
//
//    private void ensureProductExists2() {
//        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест2'] and .//div[contains(.,'333')]]"))
//                .exists();
//
//        if (!exists) {
//            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
//            adminLink.click();
//
//            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
//                    .setValue("admin");
//
//            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
//                    .setValue("secret123");
//
//            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();
//
//            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
//                    .setValue("Тест2");
//
//            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
//                    .setValue("333");
//
//            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();
//
//        }
//    }
//
//    private void ensureProductExists3() {
//        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест3'] and .//div[contains(.,'333')]]"))
//                .exists();
//
//        if (!exists) {
//            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
//            adminLink.click();
//
//            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
//                    .setValue("admin");
//
//            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
//                    .setValue("secret123");
//
//            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();
//
//            // создаём третий тестовый товар через админку, если нужно
//            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
//                    .setValue("Тест3");
//
//            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
//                    .setValue("333");
//
//            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();
//
//            // Вернуться на сайт
//            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();
//
//            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест3'] and .//div.contains(.,'333')]")).shouldBe(Condition.visible);
//
//        }
//    }
//
//
//    @Test
//    void addToCartAndCheckModal_ForPreviouslyAddedProduct_NoPageObject() {
//        ensureProductExists();
//
//        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
//                .shouldBe(Condition.visible);
//        addToCartBtn.click();
//
//        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//        cartBtn.click();
//
//        SelenideElement dialog = $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//                .shouldBe(Condition.visible);
//
//        SelenideElement modalCardName = dialog.$(org.openqa.selenium.By.xpath(".//*[contains(text(),'Тест')]"));
//        modalCardName.shouldHave(Condition.text("Тест"));
//    }
//
//    @Test
//    void addProductViaAdminAndCheckStorefront_NoPageObject() {
//        ensureProductExists();
//
//        SelenideElement productCard = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]"))
//                .shouldBe(Condition.visible);
//
//        SelenideElement nameInCard = productCard.$("h4");
//        SelenideElement priceInCard = productCard.$(org.openqa.selenium.By.xpath(".//div[contains(.,'33')]"));
//
//        nameInCard.shouldHave(Condition.text("Тест"));
//        priceInCard.shouldHave(Condition.text("33"));
//    }
//
//    @Test
//    void adminLoginWithInvalidCredentials_ShouldShowError() {
//        SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
//        adminLink.click();
//
//        $(org.openqa.selenium.By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible).setValue("randomUser" + System.currentTimeMillis());
//        $(org.openqa.selenium.By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible).setValue("randomPass" + System.currentTimeMillis());
//
//        $(org.openqa.selenium.By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();
//
//        $(org.openqa.selenium.By.xpath("//*[contains(text(),'Неверные учетные данные пользователя')]"))
//                .shouldBe(Condition.visible)
//                .shouldHave(Condition.text("Неверные учетные данные пользователя"));
//    }
//
//    @Test
//    void addToCartThenReload_ShouldResetCartCountToZero() {
//        ensureProductExists();
//
//        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
//                .shouldBe(Condition.visible);
//        addToCartBtn.click();
//
//        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//        cartBtn.click();
//
//        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//                .shouldBe(Condition.visible);
//
//        refresh();
//        $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//
//        String cartCount = $(org.openqa.selenium.By.id("cart-count")).shouldBe(Condition.visible).getText();
//        int count = Integer.parseInt(cartCount);
//        Assertions.assertThat(count).isEqualTo(0);
//    }
//
//    @Test
//    void addToCartTenTimesThenCheckout_ShouldShowInsufficientFunds() {
//        ensureProductExists();
//
//        By plusButtonXPath = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
//        for (int i = 0; i < 10; i++) {
//            SelenideElement plusBtn = $(plusButtonXPath).shouldBe(Condition.visible);
//            plusBtn.click();
//        }
//
//        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath(
//                "//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
//                .shouldBe(Condition.visible);
//        addToCartBtn.click();
//
//        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//        cartBtn.click();
//
//        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//                .shouldBe(Condition.visible);
//
//        SelenideElement checkoutBtn = $(org.openqa.selenium.By.xpath("//button[contains(.,'Оформить заказ')]"))
//                .shouldBe(Condition.visible);
//        checkoutBtn.click();
//
//        String alertText = switchTo().alert().getText();
//        Assertions.assertThat(alertText).contains("Денег не хватает!");
//        switchTo().alert().accept();
//    }
//
//    //Задание 3
//
//    @Test
//    void addThreeUnitsToCartAndCheckout_WithinBudget_ShouldShowOrderProcessingNotification() {
//        ensureProductExists();
//
//        By plusButtonXPath = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
//        for (int i = 0; i < 5; i++) {
//            SelenideElement plusBtn = $(plusButtonXPath).shouldBe(Condition.visible);
//            plusBtn.click();
//        }
//
//        SelenideElement addToCartBtn = $(org.openqa.selenium.By.xpath(
//                "//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]"))
//                .shouldBe(Condition.visible);
//        addToCartBtn.click();
//
//        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//        cartBtn.click();
//
//        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//                .shouldBe(Condition.visible);
//
//        SelenideElement checkoutBtn = $(org.openqa.selenium.By.xpath("//button[contains(.,'Оформить заказ')]"))
//                .shouldBe(Condition.visible);
//        checkoutBtn.click();
//
//        SelenideElement toast = $(By.xpath("//*[@id=\"toast-container\"]/div[2]"))
//                .shouldBe(Condition.visible);
//        String toastText = toast.getText();
//        Assertions.assertThat(toastText).contains("Заказ принят в обработку!");
//
//    }
//
//    @Test
//    void cartTotalVerification_ForSeveralDifferentProducts() {
//        ensureProductExists();
//
//        By plusButtonFirst = By.xpath("//button[@class='qty-btn' and @data-action='qty-change' and @data-id='37' and @data-step='1']");
//        for (int i = 0; i < 2; i++) {
//            SelenideElement btn = $(plusButtonFirst).shouldBe(Condition.visible);
//            btn.click();
//        }
//
//        By plusButtonCard2 = By.xpath("//div[@id='card-2']//button[@class='qty-btn' and @data-action='qty-change' and @data-id='2' and @data-step='1']");
//        for (int i = 0; i < 2; i++) {
//            SelenideElement btn = $(plusButtonCard2).shouldBe(Condition.visible);
//            btn.click();
//        }
//
//        int[] idsToAdd = new int[]{37, 2};
//        for (int id : idsToAdd) {
//            By addToCartForId = By.xpath(
//                    "//div[contains(@class,'product-card')][.//button[@data-id='" + id + "']]//button[@class='btn' and @data-action='add-to-cart']"
//            );
//            SelenideElement addBtn = $(addToCartForId).shouldBe(Condition.visible);
//            addBtn.click();
//        }
//
//        SelenideElement cartBtn = $(org.openqa.selenium.By.id("open-cart-btn")).shouldBe(Condition.visible);
//        cartBtn.click();
//
//        $(org.openqa.selenium.By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//                .shouldBe(Condition.visible);
//
//        SelenideElement body = $(org.openqa.selenium.By.tagName("body"));
//        body.shouldHave(Condition.text("Сумма: 204 ₽"));
//    }
//
//    @Test
//    void shouldShowNotificationAfterAdminLoginAndAddProduct() {
//        ensureProductExists2();
//        boolean productCardExists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест2'] and .//div[contains(.,'333')]]"))
//                .exists();
//
//        if (productCardExists) {
//            return;
//        }
//
//        SelenideElement toast = $(By.cssSelector("#toast-container .toast"))
//                .shouldBe(Condition.visible);
//
//        toast.shouldHave(Condition.text("Товар успешно добавлен!"));
//    }
//
//    @Test
//    void shouldLoginToAdminAndEditProductAndReturnToListVerifyChangesApplied() {
//        ensureProductExists3();
//    }
//
//
//
//}