package Homework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Homework5 {

    private WebDriver driver;
    private WebDriverWait wait;

    public Homework5() {
        initBrowser();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void navigateToHome() {
        if (driver != null) {
            driver.get("http://localhost:8080");
        }
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void initBrowser() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void ensureProductExists() {
        boolean exists = true;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'product-card')][.//h4[text()='Тест'] and .//div[contains(.,'33')]]")));
        } catch (Exception e) {
            exists = false;
        }
        if (!exists) {
            WebElement adminBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Администрирование')]"))
            );
            adminBtn.click();

            WebElement userInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']"))
            );
            userInput.clear();
            userInput.sendKeys("admin");

            WebElement passInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']"))
            );
            passInput.clear();
            passInput.sendKeys("secret123");

            WebElement signInBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Sign in')]"))
            );
            signInBtn.click();

            WebElement nameInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Название']"))
            );
            nameInput.clear();
            nameInput.sendKeys("Тест");

            WebElement priceInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Цена']"))
            );
            priceInput.clear();
            priceInput.sendKeys("33");

            WebElement createBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Создать')]"))
            );
            createBtn.click();

            WebElement backBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Вернуться на сайт')]"))
            );
            backBtn.click();
        }
    }
}
