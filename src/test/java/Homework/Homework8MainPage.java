package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class Homework8MainPage {

    @BeforeEach
    void setup() {
        open(Config.getBaseUrl());
    }

    private final ElementsCollection minusButtons = $$(".product-card .qty-btn[data-action='qty-change'][data-step='-1']");
    private final ElementsCollection addToCartButtons = $$(".product-card .btn[data-action='add-to-cart']");
    private final SelenideElement cartButton = $("button.btn.btn-cart#open-cart-btn");
    private final SelenideElement cartCount = $("#cart-count");
    private final ElementsCollection plusButtons = $$(".product-card .qty-btn[data-action='qty-change'][data-step='1']");


    public void clickPlusInFirstCard() {
        var firstCard = $$(".product-card").first();
        firstCard.shouldBe(Condition.visible);
        firstCard.$(".qty-btn[data-action='qty-change'][data-step='1']")
                .shouldBe(Condition.visible)
                .click();
    }

    public void clickMinusInFirstCard() {
        var firstCard = $$(".product-card").first();
        firstCard.shouldBe(Condition.visible);
        firstCard.$(".qty-btn[data-action='qty-change'][data-step='-1']")
                .shouldBe(Condition.visible)
                .click();
    }

    public void addFirstCardToCart() {
        var firstCard = $$(".product-card").first();
        firstCard.shouldBe(Condition.visible);
        firstCard.$(".btn[data-action='add-to-cart']")
                .shouldBe(Condition.visible)
                .click();
    }

    public void openCart() {
        var cartBtn = $("button.btn.btn-cart#open-cart-btn");
        cartBtn.shouldBe(Condition.visible).click();
    }

    public int getCartCount() {
        var cartCount = $("#cart-count");
        String text = cartCount.shouldBe(Condition.visible).getText();
        if (text == null || text.trim().isEmpty()) return 0;
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    public void assertElementsVisible() {
        $$(".product-card").shouldBe(CollectionCondition.sizeGreaterThanOrEqual(1)).first()
                .shouldBe(Condition.visible);
        $$(".product-card").first().$("[data-action='qty-change'][data-step='1']")
                .shouldBe(Condition.visible);
        $$(".product-card").first().$("[data-action='qty-change'][data-step='-1']")
                .shouldBe(Condition.visible);
        $$(".product-card").first().$("[data-action='add-to-cart']")
                .shouldBe(Condition.visible);
        $("button.btn.btn-cart#open-cart-btn").shouldBe(Condition.visible);
        $("#cart-count").shouldBe(Condition.visible);
    }

    @Test
    void all() {
        clickPlusInFirstCard();
        clickMinusInFirstCard();
        addFirstCardToCart();
        openCart();
        int count = getCartCount();
        org.junit.jupiter.api.Assertions.assertTrue(count >= 0);
        assertElementsVisible();
    }

}
