package Homework;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;

public class Homework6 {

    private void ensureProductExists() {
        boolean exists = $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='ТестDnD'] and .//div[contains(.,'666')]]"))
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
                    .setValue("ТестDnD");

            $(By.xpath("//input[@placeholder='Цена']")).shouldBe(Condition.visible)
                    .setValue("666");

            $(By.xpath("//button[contains(.,'Создать')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//*[contains(text(),'Вернуться на сайт')]")).shouldBe(Condition.visible).click();

            $(By.xpath("//div[contains(@class,'product-card')][.//h4[text()='ТестDnD'] and .//div[contains(.,'666')]]")).shouldBe(Condition.visible);
        }
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    public void createProductAndDragToBasket() {

        ensureProductExists();

        SelenideElement card = $x("//div[@class='product-card' and @data-name='ТестDnD']");
        SelenideElement basket = $x("//*[@id='open-cart-btn']");
        card.dragAndDrop(DragAndDropOptions.to(basket));

    }

    @Test
    public void createProductAndDragToBasketAndDelete() {

        ensureProductExists();

        SelenideElement card = $x("//div[@class='product-card' and @data-name='ТестDnD']");
        SelenideElement basket = $x("//*[@id='open-cart-btn']");
        card.dragAndDrop(DragAndDropOptions.to(basket));
        $(By.xpath("//*[@id='open-cart-btn']")).shouldBe(Condition.visible).click();
        $x("//*[@data-action='remove']").shouldBe(Condition.visible).click();
        $("#total-price").shouldBe(Condition.visible).shouldHave(Condition.text("0"));
    }
}
