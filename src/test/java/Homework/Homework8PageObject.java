package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class Homework8PageObject {

    private final By openCartButton =
            By.id("open-cart-btn");

    private final By cartModalLocator =
            By.xpath(
                    "//div[" +
                            "@role='dialog' " +
                            "or @id='cart-modal' " +
                            "or contains(@class,'cart-modal') " +
                            "or contains(@class,'modal')" +
                            "]"
            );

    private final By checkoutButton =
            By.xpath("//button[contains(normalize-space(),'Оформить заказ')]");

    private final By toastContainer =
            By.xpath("//*[@id='toast-container']/div[2]");

    private final By totalPrice =
            By.id("total-price");

    private final By adminLink =
            By.cssSelector("a[href='/admin'].btn-outline");

    private final By usernameInput =
            By.xpath("//input[@placeholder='Username']");

    private final By passwordInput =
            By.xpath("//input[@placeholder='Password']");

    private final By signInButton =
            By.xpath("//button[contains(normalize-space(),'Sign in')]");

    private final By productNameInput =
            By.xpath("//input[@placeholder='Название']");

    private final By productPriceInput =
            By.xpath("//input[@placeholder='Цена']");

    private final By createProductButton =
            By.xpath("//button[contains(normalize-space(),'Создать')]");

    private final By toast =
            By.cssSelector("#toast-container .toast");

    private final By returnBack =
            By.xpath(
                    "//a[@href='/' and normalize-space()='Вернуться на сайт']"
            );

    @BeforeAll
    @Step("Настроить Allure Selenide Listener")
    static void setUpAllure() {
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
        open(Config.getBaseUrl());
    }


    @Test
    @DisplayName("Добавление товара в корзину и оформление заказа")
    void addThreeUnitsToCartAndCheckoutWithinBudget() {
        String productName = Config.getStartProductName2();
        String priceText = Config.getStartProductPrice2();

        ensureProductExists(productName, priceText);
        addThreeUnitsToCart(productName, priceText);
        openCart();
        checkout();

        checkOrderProcessingNotification();
    }

    @Test
    @DisplayName("Проверка итоговой стоимости нескольких товаров")
    void addDifferentProductsToCartShouldCalculateTotalPriceCorrectly() {
        String firstProductName = Config.getStartProductName2();
        String firstProductPrice = Config.getStartProductPrice2();

        String secondProductName = Config.getStartProductName();
        String secondProductPrice = Config.getStartProductPrice();

        ensureProductExists(firstProductName, firstProductPrice);
        ensureProductExists(secondProductName, secondProductPrice);

        addThreeUnitsToCart(firstProductName, firstProductPrice);
        addTwoUnitsToCart(secondProductName, secondProductPrice);

        openCart();

        checkCartTotal("1431");
    }

    @Test
    @DisplayName("Администратор может добавить товар")
    void adminCanAddProductAndSeeSuccessNotification() {
        String productName = Config.getStartProductName2();
        String priceText = Config.getStartProductPrice2();

        ensureProductExists(productName, priceText);
    }

    @Test
    @DisplayName("Администратор может изменить товар")
    void adminCanEditProductAndSeeChangesOnProductList() {
        ensureProductForEditingExists();

        openAdminPage();
        loginAsAdmin();
        editProductPrice(Config.getStartProductPrice());

        returnToStore();

        checkEditedProductIsVisible();
    }


    @Step("Добавить две единицы товара {productName} в корзину")
    public void addThreeUnitsToCart(
            String productName,
            String priceText
    ) {
        increaseProductQuantity(productName, 2);
        addProductToCart(productName, priceText);
    }

    @Step("Добавить две единицы второго товара {productName} в корзину")
    public void addTwoUnitsToCart(
            String productName,
            String priceText
    ) {
        increaseProductQuantity(productName, 1);
        addProductToCart(productName, priceText);
    }

    @Step("Увеличить количество товара {productName} на {count}")
    private void increaseProductQuantity(
            String productName,
            int count
    ) {
        By plusButton = plusButtonLocator(productName);

        for (int i = 0; i < count; i++) {
            $(plusButton)
                    .shouldBe(Condition.visible)
                    .click();
        }
    }

    @Step("Добавить товар {productName} в корзину")
    private void addProductToCart(
            String productName,
            String priceText
    ) {
        $(addToCartButtonLocator(productName, priceText))
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Открыть корзину")
    private void openCart() {
        $(openCartButton)
                .shouldBe(Condition.visible)
                .click();

        $(cartModalLocator)
                .shouldBe(Condition.visible);
    }

    @Step("Оформить заказ")
    private void checkout() {
        $(checkoutButton)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Проверить наличие товара {productName} с ценой {priceText}")
    private void ensureProductExists(
            String productName,
            String priceText
    ) {
        if (isProductExists(productName, priceText)) {
            return;
        }

        openAdminPage();
        loginAsAdmin();
        createProduct(productName, priceText);
        returnToStore();

        checkProductIsVisible(productName, priceText);
    }

    @Step("Проверить наличие товара в каталоге")
    private boolean isProductExists(
            String productName,
            String priceText
    ) {
        return $(productCardLocator(productName, priceText))
                .exists();
    }

    @Step("Открыть панель администратора")
    private void openAdminPage() {
        $(adminLink)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Авторизоваться в панели администратора")
    private void loginAsAdmin() {
        $(usernameInput)
                .shouldBe(Condition.visible)
                .setValue(Config.getAdminUsername());

        $(passwordInput)
                .shouldBe(Condition.visible)
                .setValue(Config.getAdminPassword());

        $(signInButton)
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Создать товар {productName} с ценой {priceText}")
    private void createProduct(
            String productName,
            String priceText
    ) {
        $(productNameInput)
                .shouldBe(Condition.visible)
                .setValue(productName);

        $(productPriceInput)
                .shouldBe(Condition.visible)
                .setValue(priceText);

        $(createProductButton)
                .shouldBe(Condition.visible)
                .click();

        checkProductCreatedNotification();
    }

    @Step("Изменить цену товара на {priceText}")
    private void editProductPrice(String priceText) {
        SelenideElement productRow = getProductRowForEditing();

        productRow
                .$("input[type='number']")
                .shouldBe(Condition.visible)
                .setValue(priceText);

        productRow
                .$("button[data-action='update']")
                .shouldBe(Condition.visible)
                .click();

        $(toast)
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.matchText("Товар #\\d+ обновлен")
                );
    }

    @Step("Открыть страницу магазина")
    private void returnToStore() {
        open(Config.getBaseUrl());
    }


    @Step("Проверить уведомление о принятии заказа")
    private void checkOrderProcessingNotification() {
        String toastText = getToastText();

        assertThat(toastText)
                .as("Текст уведомления после оформления заказа")
                .contains("Заказ принят в обработку!");
    }

    @Step("Получить текст уведомления")
    public String getToastText() {
        return $(toastContainer)
                .shouldBe(Condition.visible)
                .getText();
    }

    @Step("Проверить итоговую сумму корзины: {expectedTotal}")
    private void checkCartTotal(String expectedTotal) {
        $(totalPrice)
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedTotal));
    }

    @Step("Проверить уведомление об успешном создании товара")
    private void checkProductCreatedNotification() {
        $(toast)
                .shouldBe(Condition.visible)
                .shouldHave(
                        Condition.text("Товар успешно добавлен!")
                );
    }

    @Step("Проверить отображение товара {productName}")
    private void checkProductIsVisible(
            String productName,
            String priceText
    ) {
        $(productCardLocator(productName, priceText))
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие товара для редактирования")
    private void ensureProductForEditingExists() {
        String productName = Config.getStartProductName2();
        String priceText = Config.getStartProductPrice();

        if (!isProductExists(productName, priceText)) {
            ensureProductExists(productName, priceText);
        }
    }

    @Step("Получить строку товара в панели администратора")
    private SelenideElement getProductRowForEditing() {
        return $(
                By.xpath(
                        "//tr[" +
                                ".//input[@type='text' and @value='" +
                                Config.getStartProductName2() +
                                "']" +
                                "]"
                )
        ).shouldBe(Condition.visible);
    }

    @Step("Проверить товар после редактирования")
    private void checkEditedProductIsVisible() {
        String productName = Config.getStartProductName2();
        String priceText = Config.getStartProductPrice();

        $(productCardLocator(productName, priceText))
                .shouldBe(Condition.visible);
    }


    @Step("Сформировать локатор кнопки увеличения количества товара")
    private By plusButtonLocator(String productName) {
        return By.xpath(
                "//div[contains(@class,'product-card')]" +
                        "[.//h4[normalize-space()='" +
                        productName +
                        "']]" +
                        "//button[" +
                        "@class='qty-btn' " +
                        "and @data-action='qty-change' " +
                        "and @data-step='1'" +
                        "]"
        );
    }

    @Step("Сформировать локатор кнопки добавления товара в корзину")
    private By addToCartButtonLocator(
            String productName,
            String priceText
    ) {
        return By.xpath(
                "//div[contains(@class,'product-card')]" +
                        "[.//h4[normalize-space()='" +
                        productName +
                        "']]" +
                        "[.//div[contains(normalize-space(),'" +
                        priceText +
                        "')]]" +
                        "//button[contains(normalize-space(),'В корзину')]"
        );
    }

    @Step("Сформировать локатор карточки товара")
    private By productCardLocator(
            String productName,
            String priceText
    ) {
        return By.xpath(
                "//div[contains(@class,'product-card')]" +
                        "[.//h4[normalize-space()='" +
                        productName +
                        "']]" +
                        "[.//div[contains(normalize-space(),'" +
                        priceText +
                        "')]]"
        );
    }
}


//package Homework;

//import Homework.utils.Config;
//import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.SelenideElement;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;

//import static com.codeborne.selenide.Selenide.*;

//public class Homework8PageObject {

//    @BeforeEach
//    void setup() {
//        open(Config.getBaseUrl());
//    }
//
//    private By plusButtonLocator(String productName) {
//        return By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + productName + "']]//button[@class='qty-btn' and @data-action='qty-change' and @data-step='1']");
//    }
//    private By addToCartButtonLocator(String productName, String priceText) {
//        return By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + productName + "'] and .//div[contains(.,'" + priceText + "')]]//button[contains(.,'В корзину')]");
//    }
//
//    private By plusButtonLocator2(String productName2) {
//        return By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + productName2 + "']]//button[@class='qty-btn' and @data-action='qty-change' and @data-step='1']");
//    }
//    private By addToCartButtonLocator2(String productName2, String priceText2) {
//        return By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + productName2 + "'] and .//div[contains(.,'" + priceText2 + "')]]//button[contains(.,'В корзину')]");
//    }
//
//    private By openCartButton = By.id("open-cart-btn");
//    private By cartModalLocator = By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]");
//    private By checkoutButton = By.xpath("//button[contains(.,'Оформить заказ')]");
//    private By toastContainer = By.xpath("//*[@id='toast-container']/div[2]");
//    private final By totalPrice = By.id("total-price");
//
//
//    public void addThreeUnitsToCart(String productName, String priceText) {
//        By plusBtnLocator = plusButtonLocator(productName);
//        for (int i = 0; i < 2; i++) {
//            SelenideElement plusBtn = $(plusBtnLocator).shouldBe(Condition.visible);
//            plusBtn.click();
//        }
//
//        $(addToCartButtonLocator(productName, priceText)).shouldBe(Condition.visible).click();
//
//    }
//
//    public void addTwoUnitsToCart(String productName2, String priceText2) {
//        By plusBtnLocator2 = plusButtonLocator2(productName2);
//        for (int i = 0; i < 1; i++) {
//            SelenideElement plusBtn = $(plusBtnLocator2).shouldBe(Condition.visible);
//            plusBtn.click();
//        }
//
//        $(addToCartButtonLocator2(productName2, priceText2)).shouldBe(Condition.visible).click();
//    }
//
//    public String getToastText() {
//        return $(toastContainer).shouldBe(Condition.visible).getText();
//    }
//
//    @Test
//    public void addThreeUnitsToCartAndCheckout_WithinBudget_ShouldShowOrderProcessingNotification() {
//        String productName = Config.getStartProductName2();
//        String priceText = Config.getStartProductPrice2();
//
//        addThreeUnitsToCart(productName, priceText);
//        sleep(3000);
//
//        $(openCartButton).shouldBe(Condition.visible).click();
//        $(cartModalLocator).shouldBe(Condition.visible);
//        $(checkoutButton).shouldBe(Condition.visible).click();
//
//        String toastText = getToastText();
//        org.junit.jupiter.api.Assertions.assertTrue(
//                toastText.contains("Заказ принят в обработку!"),
//                "Ожидалось уведомление 'Заказ принят в обработку!', но получили: " + toastText
//        );
//    }
//
//    @Test
//    public void addDifferentProductsToCart_ShouldCalculateTotalPriceCorrectly() {
//        String productName = Config.getStartProductName2();
//        String priceText = Config.getStartProductPrice2();
//        String productName2 = Config.getStartProductName();
//        String priceText2 = Config.getStartProductPrice();
//
//        addThreeUnitsToCart(productName, priceText);
//        addTwoUnitsToCart(productName2, priceText2);
//
//        $(openCartButton).shouldBe(Condition.visible).click();
//        $(cartModalLocator).shouldBe(Condition.visible);
//        $(totalPrice)
//                .shouldBe(Condition.visible)
//                .shouldHave(Condition.text("1431"));
//
//    }
//
//    private final By adminLink =
//            By.cssSelector("a[href='/admin'].btn-outline");
//    private final By usernameInput =
//            By.xpath("//input[@placeholder='Username']");
//    private final By passwordInput =
//            By.xpath("//input[@placeholder='Password']");
//    private final By signInButton =
//            By.xpath("//button[contains(normalize-space(), 'Sign in')]");
//    private final By productNameInput =
//            By.xpath("//input[@placeholder='Название']");
//    private final By productPriceInput =
//            By.xpath("//input[@placeholder='Цена']");
//    private final By createProductButton =
//            By.xpath("//button[contains(normalize-space(), 'Создать')]");
//    private final By toast =
//            By.cssSelector("#toast-container .toast");
//
//    private boolean isProductExists(String productName, String priceText) {
//        By productCard = By.xpath(
//                "//div[contains(@class, 'product-card')]"
//                        + "[.//h4[normalize-space()='" + productName + "']"
//                        + " and .//div[contains(normalize-space(), '" + priceText + "')]]"
//        );
//        return $(productCard).exists();
//    }
//
//    private void openAdminPage() {
//        $(adminLink)
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    private void loginAsAdmin(String username, String password) {
//        $(usernameInput)
//                .shouldBe(Condition.visible)
//                .setValue(username);
//        $(passwordInput)
//                .shouldBe(Condition.visible)
//                .setValue(password);
//        $(signInButton)
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    private void createProduct(String productName, String priceText) {
//        $(productNameInput)
//                .shouldBe(Condition.visible)
//                .setValue(productName);
//        $(productPriceInput)
//                .shouldBe(Condition.visible)
//                .setValue(priceText);
//        $(createProductButton)
//                .shouldBe(Condition.visible)
//                .click();
//        $(toast)
//                .shouldBe(Condition.visible)
//                .shouldHave(Condition.text("Товар успешно добавлен!"));
//    }
//
//    private void ensureProductExists() {
//        String productName = Config.getStartProductName2();
//        String priceText = Config.getStartProductPrice2();
//        String username = Config.getAdminUsername();
//        String password = Config.getAdminPassword();
//
//        if (isProductExists(productName, priceText)) {
//            return;
//        }
//
//        openAdminPage();
//        loginAsAdmin(username, password);
//        createProduct(productName, priceText);
//        open(Config.getBaseUrl());
//    }
//
//    private final By productPriceInput2 =
//            By.xpath("//input[@value='33']");
//    private final By saveProductButton =
//            By.xpath("//tr[.//input[@type='text' and @value='Test33'] and .//input[@type='number' and @value='33']]//button[@data-action='update']");
//    private final By returnBack =
//            By.xpath("//a[@href='/' and normalize-space()='Вернуться на сайт']");
//
//    private void editProduct(String priceText) {
//        $(productPriceInput2)
//                .shouldBe(Condition.visible)
//                .setValue(priceText);
//        $(saveProductButton)
//                .shouldBe(Condition.visible)
//                .click();
//        $(toast)
//                .shouldBe(Condition.visible)
//                .shouldHave(Condition.matchText("Товар #\\d+ обновлен"));
//        $(returnBack)
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    private boolean isProductExists2() {
//        By productCard = By.xpath(
//                "//div[contains(@class,'product-card')"
//                        + " and @data-name='Test33'"
//                        + " and @data-price='666']"
//        );
//
//        return $(productCard).exists();
//    }
//
//    private void ensureProductExists2() {
//        String productName = Config.getStartProductName2();
//        String priceText = Config.getStartProductPrice();
//        String username = Config.getAdminUsername();
//        String password = Config.getAdminPassword();
//
//        if (isProductExists2()) {
//            return;
//        }
//
//        openAdminPage();
//        loginAsAdmin(username, password);
//        editProduct(priceText);
//        isProductExists2();
//        open(Config.getBaseUrl());
//    }
//
//    @Test
//    public void adminCanAddProductAndSeeSuccessNotification() {
//        ensureProductExists();
//    }
//
//    @Test
//    public void adminCanEditProductAndSeeChangesOnProductList() {
//        ensureProductExists2();
//    }
//
//}
