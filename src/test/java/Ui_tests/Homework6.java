package Ui_tests;

import Common.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

@Tag("smoke")

public class Homework6 {

    private static final By ADMIN_LINK =
            By.cssSelector("a[href='/admin'].btn-outline");

    private static final By USERNAME_INPUT =
            By.xpath("//input[@placeholder='Username']");

    private static final By PASSWORD_INPUT =
            By.xpath("//input[@placeholder='Password']");

    private static final By SIGN_IN_BUTTON =
            By.xpath("//button[contains(.,'Sign in')]");

    private static final By PRODUCT_NAME_INPUT =
            By.xpath("//input[@placeholder='Название']");

    private static final By PRODUCT_PRICE_INPUT =
            By.xpath("//input[@placeholder='Цена']");

    private static final By CREATE_BUTTON =
            By.xpath("//button[contains(.,'Создать')]");

    private static final By BACK_TO_STORE_BUTTON =
            By.xpath("//*[contains(text(),'Вернуться на сайт')]");

    private static final By CART_BUTTON =
            By.id("open-cart-btn");

    private static final By CART_COUNT =
            By.id("cart-count");

    private static final By REMOVE_BUTTON =
            By.xpath("//*[@data-action='remove']");

    private static final By TOTAL_PRICE =
            By.id("total-price");

    private static final By EMPTY_CART_MESSAGE =
            By.xpath("//*[@id='cart-items']//p[@id='empty-cart']");

    @BeforeAll
    @Step("Настроить Allure Selenide Listener и вывести конфигурацию")
    static void setUpAllureAndPrintConfig() {
        if (!SelenideLogger.hasListener("allure")) {
            SelenideLogger.addListener(
                    "allure",
                    new AllureSelenide()
                            .screenshots(true)
                            .savePageSource(true)
            );
        }

        printConfig();
    }

    @BeforeEach
    @Step("Открыть главную страницу приложения")
    void setUp() {
        Selenide.open(Config.getBaseUrl());
    }

    @Test
    @Step("Создать товар и перетащить его в корзину")
    void createProductAndDragToBasket() {
        ensureProductExists();

        dragProductToBasket();

        checkCartCount("1");
        checkProductAddedToast();
    }

    @Test
    @Step("Создать товар, перетащить его в корзину и удалить")
    void createProductAndDragToBasketAndDelete() {
        ensureProductExists();

        dragProductToBasket();
        openCart();
        removeProductFromCart();

        checkTotalPriceIsZero();
        checkCartIsEmpty();
    }

    @Step("Вывести текущую конфигурацию приложения")
    private static void printConfig() {
        System.out.println("Config:");
        System.out.println("  baseUrl=" + Config.getBaseUrl());
        System.out.println("  baseApi=" + Config.getBaseApi());
        System.out.println(
                "  timeoutFindElements=" +
                        Config.getTimeoutFindElements()
        );
        System.out.println("  loggingMode=" + Config.getLoggingMode());
        System.out.println(
                "  startProduct.name=" +
                        Config.getStartProductName()
        );
        System.out.println(
                "  startProduct.price=" +
                        Config.getStartProductPrice()
        );
    }

    @Step("Проверить наличие стартового товара")
    private void ensureProductExists() {
        if (isProductExists()) {
            return;
        }

        openAdminPage();
        loginAsAdmin();
        createStartProduct();
        returnToStore();

        checkProductIsVisible();
    }

    @Step("Проверить наличие стартового товара в каталоге")
    private boolean isProductExists() {
        return getProductCard().exists();
    }

    @Step("Создать стартовый товар через панель администратора")
    private void createStartProduct() {
        enterProductName(Config.getStartProductName());
        enterProductPrice(Config.getStartProductPrice());
        clickCreateButton();
    }

    @Step("Открыть панель администратора")
    private void openAdminPage() {
        $(ADMIN_LINK)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Авторизоваться под учетной записью администратора")
    private void loginAsAdmin() {
        enterUsername(Config.getAdminUsername());
        enterPassword(Config.getAdminPassword());
        clickSignInButton();
    }

    @Step("Ввести логин администратора")
    private void enterUsername(String username) {
        $(USERNAME_INPUT)
                .shouldBe(Condition.visible)
                .setValue(username);
    }

    @Step("Ввести пароль администратора")
    private void enterPassword(String password) {
        $(PASSWORD_INPUT)
                .shouldBe(Condition.visible)
                .setValue(password);
    }

    @Step("Нажать кнопку Sign in")
    private void clickSignInButton() {
        $(SIGN_IN_BUTTON)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Ввести название товара: {productName}")
    private void enterProductName(String productName) {
        $(PRODUCT_NAME_INPUT)
                .shouldBe(Condition.visible)
                .setValue(productName);
    }

    @Step("Ввести цену товара: {price}")
    private void enterProductPrice(String price) {
        $(PRODUCT_PRICE_INPUT)
                .shouldBe(Condition.visible)
                .setValue(price);
    }

    @Step("Нажать кнопку создания товара")
    private void clickCreateButton() {
        $(CREATE_BUTTON)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Вернуться на страницу магазина")
    private void returnToStore() {
        $(BACK_TO_STORE_BUTTON)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Перетащить товар в корзину")
    private void dragProductToBasket() {
        SelenideElement productCard = getProductCard();
        SelenideElement basket = $(CART_BUTTON)
                .shouldBe(Condition.visible);

        productCard.dragAndDrop(
                DragAndDropOptions.to(basket)
        );
    }

    @Step("Открыть корзину")
    private void openCart() {
        $(CART_BUTTON)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Удалить товар из корзины")
    private void removeProductFromCart() {
        $(REMOVE_BUTTON)
                .shouldBe(Condition.visible)
                .click();
    }


    @Step("Получить карточку стартового товара")
    private SelenideElement getProductCard() {
        return $x(
                "//div[contains(@class,'product-card')]" +
                        "[@data-name='" +
                        Config.getStartProductName() +
                        "']"
        );
    }

    @Step("Проверить отображение стартового товара")
    private void checkProductIsVisible() {
        getProductCard()
                .shouldBe(Condition.visible);
    }

    @Step("Проверить количество товаров в корзине: {expectedCount}")
    private void checkCartCount(String expectedCount) {
        $(CART_COUNT)
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedCount));
    }

    @Step("Проверить уведомление о добавлении товара в корзину")
    private void checkProductAddedToast() {
        $(".toast")
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.text(
                                Config.getStartProductName() +
                                        " (1 шт.) добавлен в корзину"
                        )
                );
    }

    @Step("Проверить, что итоговая стоимость корзины равна нулю")
    private void checkTotalPriceIsZero() {
        $(TOTAL_PRICE)
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("0"));
    }

    @Step("Проверить, что корзина пустая")
    private void checkCartIsEmpty() {
        $(EMPTY_CART_MESSAGE)
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Пусто"));
    }
}


