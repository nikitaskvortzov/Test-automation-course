package Homework;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import Homework.utils.Config;

public class Homework6 {

    @BeforeAll
    static void printConfigBeforeTests() {
        System.out.println("Config:");
        System.out.println("  baseUrl=" + Config.getBaseUrl());
        System.out.println("  baseApi=" + Config.getBaseApi());
        System.out.println("  timeoutFindElements=" + Config.getTimeoutFindElements());
        System.out.println("  loggingMode=" + Config.getLoggingMode());
        System.out.println("  startProduct.name=" + Config.getStartProductName());
        System.out.println("  startProduct.price=" + Config.getStartProductPrice());
    }

    @BeforeEach
    void setup() {
        open(Config.getBaseUrl());
    }

    private void ensureProductExists() {
        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + Config.getStartProductName() + "'] and .//div[contains(.,'" + Config.getStartProductPrice() + "')]]"))
                .exists();

        if (!exists) {
            SelenideElement adminLink = $(By.cssSelector("a[href='/admin'].btn-outline")).shouldBe(Condition.visible);
            adminLink.click();

            $(By.xpath("//input[@placeholder='Username']")).shouldBe(Condition.visible)
                    .setValue(Config.getAdminUsername());

            $(By.xpath("//input[@placeholder='Password']")).shouldBe(Condition.visible)
                    .setValue(Config.getAdminPassword());

            $(By.xpath("//button[contains(.,'Sign in')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//input[@placeholder='Название']")).shouldBe(Condition.visible)
                    .setValue(Config.getStartProductName());

            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
                    .setValue(Config.getStartProductPrice());

            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='" + Config.getStartProductName() + "'] and .//div[contains(.,'" + Config.getStartProductPrice() + "')]]"))
                    .shouldBe(Condition.visible);
        }
    }

    @Test
    public void createProductAndDragToBasket() {
        ensureProductExists();

        SelenideElement card = $x("//div[@class='product-card' and @data-name='" + Config.getStartProductName() + "']");
        SelenideElement basket = $x("//*[@id='open-cart-btn']");
        card.dragAndDrop(DragAndDropOptions.to(basket));

        $x("//span[@id='cart-count']").shouldHave(Condition.text("1"));
        $(".toast").shouldHave(Condition.text("TestDnD (1 шт.) добавлен в корзину"));
    }

    @Test
    public void createProductAndDragToBasketAndDelete() {
        ensureProductExists();

        SelenideElement card = $x("//div[@class='product-card' and @data-name='" + Config.getStartProductName() + "']");
        SelenideElement basket = $x("//*[@id='open-cart-btn']");
        card.dragAndDrop(DragAndDropOptions.to(basket));

        $(By.xpath("//*[@id='open-cart-btn']")).shouldBe(Condition.visible).click();
        $x("//*[@data-action='remove']").shouldBe(Condition.visible).click();

        $("#total-price").shouldBe(Condition.visible).shouldHave(Condition.text("0"));
        $x("//*[@id='cart-items']//p[@id='empty-cart']").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Пусто"));
    }

}
