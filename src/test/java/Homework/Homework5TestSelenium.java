package Homework;

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

    private Homework5 helper;
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        helper = new Homework5();
        driver = helper.getDriver();
        wait = helper.getWait();
        helper.navigateToHome();
    }

    @AfterEach
    void tearDown() {
        helper.close();
    }

    @Test
    void addToCartAndCheckModal_ForPreviouslyAddedProduct_NoPageObject() {
        helper.ensureProductExists();

        WebElement addToCartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]")
                )
        );
        addToCartBtn.click();

        WebElement cartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))
        );
        cartBtn.click();

        WebElement dialog = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]")
                )
        );

        WebElement modalCardName = dialog.findElement(By.xpath(".//*[contains(text(),'Тест')]"));
        Assertions.assertThat(modalCardName.getText())
                .as("В модальном окне корзины должно отображаться название товара 'Тест'")
                .contains("Тест");
    }

    @Test
    void addProductViaAdminAndCheckStorefront_NoPageObject() {
        helper.ensureProductExists();

        WebElement productCard = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]")
                )
        );

        WebElement nameInCard = productCard.findElement(By.cssSelector("h4"));
        WebElement priceInCard = productCard.findElement(By.xpath(".//div[contains(.,'33')]"));

        Assertions.assertThat(nameInCard.getText()).isEqualTo("Тест");
        Assertions.assertThat(priceInCard.getText()).contains("33");
    }

    @Test
    void adminLoginWithInvalidCredentials_ShouldShowError() {
        WebElement adminBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Администрирование')]"))
        );
        adminBtn.click();

        WebElement userInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']"))
        );
        userInput.clear();
        userInput.sendKeys("randomUser" + System.currentTimeMillis());

        WebElement passInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']"))
        );
        passInput.clear();
        passInput.sendKeys("randomPass" + System.currentTimeMillis());

        WebElement signInBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Sign in')]"))
        );
        signInBtn.click();

        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Неверные учетные данные пользователя')]")
                )
        );

        Assertions.assertThat(error.getText())
                .contains("Неверные учетные данные пользователя");
    }

    @Test
    void addToCartThenReload_ShouldResetCartCountToZero() {
        helper.ensureProductExists();

        WebElement addToCartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]//button[contains(.,'В корзину')]")
                )
        );
        addToCartBtn.click();

        WebElement cartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))
        );
        cartBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@role='dialog' or @id='cart-modal' or contains(@class,'cart-modal') or contains(@class,'modal')]"))
        );

        driver.navigate().refresh();

        WebElement cartBtnAfterReload = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("open-cart-btn"))
        );
        String cartCount = cartBtnAfterReload.findElement(By.id("cart-count")).getText();

        int count = Integer.parseInt(cartCount);
        Assertions.assertThat(count).isEqualTo(0);
    }
}
