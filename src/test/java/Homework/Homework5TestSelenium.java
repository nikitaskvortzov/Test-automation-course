package Homework;

import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Homework5TestSelenium {

    private static final String PRODUCT_NAME = "Тест";
    private static final String PRODUCT_PRICE = "33";

    private static final By PRODUCT_CARD = By.xpath(
            "//div[contains(@class,'product-card')]" +
                    "[.//h4[normalize-space()='Тест']]" +
                    "[.//div[contains(normalize-space(),'33')]]"
    );

    private static final By ADD_TO_CART_BUTTON = By.xpath(
            "//div[contains(@class,'product-card')]" +
                    "[.//h4[normalize-space()='Тест']]" +
                    "[.//div[contains(normalize-space(),'33')]]" +
                    "//button[contains(.,'В корзину')]"
    );

    private static final By CART_BUTTON =
            By.id("open-cart-btn");

    private static final By CART_MODAL = By.xpath(
            "//div[" +
                    "@role='dialog' " +
                    "or @id='cart-modal' " +
                    "or contains(@class,'cart-modal') " +
                    "or contains(@class,'modal')" +
                    "]"
    );

    private Homework5 helper;
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    @Step("Подготовить браузер и открыть главную страницу")
    void setUp() {
        helper = new Homework5();
        driver = helper.getDriver();
        wait = helper.getWait();

        helper.navigateToHome();
    }

    @AfterEach
    @Step("Закрыть браузер")
    void tearDown() {
        helper.close();
    }

    @Test
    void addToCartAndCheckModalForPreviouslyAddedProduct() {
        ensureProductExists();

        addProductToCart();
        openCart();

        WebElement cartModal = getCartModal();

        checkProductInCart(cartModal);
    }

    @Test
    void addProductViaAdminAndCheckStorefront() {
        ensureProductExists();

        WebElement productCard = getProductCard();

        checkProductName(productCard);
        checkProductPrice(productCard);
    }

    @Test
    void adminLoginWithInvalidCredentialsShouldShowError() {
        openAdminPage();

        enterLogin("randomUser" + System.currentTimeMillis());
        enterPassword("randomPass" + System.currentTimeMillis());
        submitLogin();

        checkInvalidCredentialsError();
    }

    @Test
    void addToCartThenReloadShouldResetCartCountToZero() {
        ensureProductExists();

        addProductToCart();
        openCart();
        getCartModal();

        refreshPage();

        checkCartButtonIsVisible();
        checkCartCount(0);
    }


    @Step("Проверить наличие товара «{PRODUCT_NAME}» с ценой {PRODUCT_PRICE}")
    private void ensureProductExists() {
        helper.ensureProductExists();
    }


    @Step("Добавить товар «{PRODUCT_NAME}» в корзину")
    private void addProductToCart() {
        WebElement addToCartButton = waitForClickable(ADD_TO_CART_BUTTON);
        addToCartButton.click();
    }

    @Step("Открыть корзину")
    private void openCart() {
        WebElement cartButton = waitForClickable(CART_BUTTON);
        cartButton.click();
    }

    @Step("Открыть страницу администратора")
    private void openAdminPage() {
        WebElement adminButton = waitForClickable(
                By.xpath("//*[contains(text(),'Администрирование')]")
        );

        adminButton.click();
    }

    @Step("Ввести логин: {username}")
    private void enterLogin(String username) {
        WebElement usernameInput = waitForVisible(
                By.xpath("//input[@placeholder='Username']")
        );

        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    @Step("Ввести пароль")
    private void enterPassword(String password) {
        WebElement passwordInput = waitForVisible(
                By.xpath("//input[@placeholder='Password']")
        );

        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    @Step("Нажать кнопку Sign in")
    private void submitLogin() {
        WebElement signInButton = waitForClickable(
                By.xpath("//button[contains(.,'Sign in')]")
        );

        signInButton.click();
    }

    @Step("Обновить страницу")
    private void refreshPage() {
        driver.navigate().refresh();
    }

    @Step("Найти карточку товара «{PRODUCT_NAME}»")
    private WebElement getProductCard() {
        return waitForVisible(PRODUCT_CARD);
    }

    @Step("Найти модальное окно корзины")
    private WebElement getCartModal() {
        return waitForVisible(CART_MODAL);
    }

    @Step("Найти элемент по локатору")
    private WebElement waitForVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    @Step("Дождаться доступности элемента для клика")
    private WebElement waitForClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    @Step("Проверить название товара в карточке")
    private void checkProductName(WebElement productCard) {
        WebElement name = productCard.findElement(
                By.cssSelector("h4")
        );

        Assertions.assertThat(name.getText())
                .as("Название товара")
                .isEqualTo(PRODUCT_NAME);
    }

    @Step("Проверить цену товара в карточке")
    private void checkProductPrice(WebElement productCard) {
        WebElement price = productCard.findElement(
                By.xpath(".//div[contains(normalize-space(),'33')]")
        );

        Assertions.assertThat(price.getText())
                .as("Цена товара")
                .contains(PRODUCT_PRICE);
    }

    @Step("Проверить наличие товара в корзине")
    private void checkProductInCart(WebElement cartModal) {
        WebElement productName = cartModal.findElement(
                By.xpath(".//*[contains(text(),'Тест')]")
        );

        Assertions.assertThat(productName.getText())
                .as("Название товара в корзине")
                .contains(PRODUCT_NAME);
    }

    @Step("Проверить сообщение о неверных учетных данных")
    private void checkInvalidCredentialsError() {
        WebElement error = waitForVisible(
                By.xpath(
                        "//*[contains(text()," +
                                "'Неверные учетные данные пользователя')]"
                )
        );

        Assertions.assertThat(error.getText())
                .as("Сообщение об ошибке авторизации")
                .contains("Неверные учетные данные пользователя");
    }

    @Step("Проверить отображение кнопки корзины")
    private void checkCartButtonIsVisible() {
        WebElement cartButton = waitForVisible(CART_BUTTON);

        Assertions.assertThat(cartButton.isDisplayed())
                .as("Кнопка открытия корзины")
                .isTrue();
    }

    @Step("Проверить количество товаров в корзине: {expectedCount}")
    private void checkCartCount(int expectedCount) {
        WebElement cartButton = waitForVisible(CART_BUTTON);

        WebElement cartCount = cartButton.findElement(
                By.id("cart-count")
        );

        int actualCount = Integer.parseInt(cartCount.getText());

        Assertions.assertThat(actualCount)
                .as("Количество товаров в корзине")
                .isEqualTo(expectedCount);
    }
}


//package Homework;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class Homework5TestSelenium {
//
//    private Homework5 helper;
//    private WebDriver driver;
//    private WebDriverWait wait;
//
//    @BeforeEach
//    void setup() {
//        helper = new Homework5();
//        driver = helper.getDriver();
//        wait = helper.getWait();
//        helper.navigateToHome();
//    }
//
//    @AfterEach
//    void tearDown() {
//        helper.close();
//    }
//
//    @Test
//    void addToCartAndCheckModal_ForPreviouslyAddedProduct_NoPageObject() {
//        helper.ensureProductExists();
//
//        WebElement addToCartBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(
//                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]")
//                )
//        );
//        addToCartBtn.click();
//
//        WebElement cartBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))
//        );
//        cartBtn.click();
//
//        WebElement dialog = wait.until(
//                ExpectedConditions.presenceOfElementLocated(
//                        By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]")
//                )
//        );
//
//        WebElement modalCardName = dialog.findElement(By.xpath(".//*[contains(text(),'Тест')]"));
//        Assertions.assertThat(modalCardName.getText())
//                .as("В модальном окне корзины должно отображаться название товара 'Тест'")
//                .contains("Тест");
//    }
//
//    @Test
//    void addProductViaAdminAndCheckStorefront_NoPageObject() {
//        helper.ensureProductExists();
//
//        WebElement productCard = wait.until(
//                ExpectedConditions.presenceOfElementLocated(
//                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]")
//                )
//        );
//
//        WebElement nameInCard = productCard.findElement(By.cssSelector("h4"));
//        WebElement priceInCard = productCard.findElement(By.xpath(".//div[contains(.,'33')]"));
//
//        Assertions.assertThat(nameInCard.getText()).isEqualTo("Тест");
//        Assertions.assertThat(priceInCard.getText()).contains("33");
//    }
//
//    @Test
//    void adminLoginWithInvalidCredentials_ShouldShowError() {
//        WebElement adminBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Администрирование')]"))
//        );
//        adminBtn.click();
//
//        WebElement userInput = wait.until(
//                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']"))
//        );
//        userInput.clear();
//        userInput.sendKeys("randomUser" + System.currentTimeMillis());
//
//        WebElement passInput = wait.until(
//                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']"))
//        );
//        passInput.clear();
//        passInput.sendKeys("randomPass" + System.currentTimeMillis());
//
//        WebElement signInBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Sign in')]"))
//        );
//        signInBtn.click();
//
//        WebElement error = wait.until(
//                ExpectedConditions.visibilityOfElementLocated(
//                        By.xpath("//*[contains(text(),'Неверные учетные данные пользователя')]")
//                )
//        );
//
//        Assertions.assertThat(error.getText())
//                .contains("Неверные учетные данные пользователя");
//    }
//
//    @Test
//    void addToCartThenReload_ShouldResetCartCountToZero() {
//        helper.ensureProductExists();
//
//        WebElement addToCartBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(
//                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]")
//                )
//        );
//        addToCartBtn.click();
//
//        WebElement cartBtn = wait.until(
//                ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))
//        );
//        cartBtn.click();
//
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
//        );
//
//        driver.navigate().refresh();
//
//        WebElement cartBtnAfterReload = wait.until(
//                ExpectedConditions.presenceOfElementLocated(By.id("open-cart-btn"))
//        );
//        String cartCount = cartBtnAfterReload.findElement(By.id("cart-count")).getText();
//
//        int count = Integer.parseInt(cartCount);
//        Assertions.assertThat(count).isEqualTo(0);
//    }
//}
