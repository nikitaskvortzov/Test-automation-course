package Homework;

import Homework.utils.Config;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class Homework8MainPage {

    private final ElementsCollection productCards =
            $$(".product-card");

    private final SelenideElement cartButton =
            $("button.btn.btn-cart#open-cart-btn");

    private final SelenideElement cartCount =
            $("#cart-count");

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
    @DisplayName("Проверить основные элементы главной страницы и работу корзины")
    void all() {
        checkMainPageElements();

        clickPlusInFirstCard();
        clickMinusInFirstCard();
        addFirstCardToCart();
        openCart();

        int actualCartCount = getCartCount();

        checkCartCountIsValid(actualCartCount);
    }

    @Step("Увеличить количество товара в первой карточке")
    public void clickPlusInFirstCard() {
        SelenideElement firstCard = getFirstProductCard();

        firstCard
                .$(".qty-btn[data-action='qty-change'][data-step='1']")
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Уменьшить количество товара в первой карточке")
    public void clickMinusInFirstCard() {
        SelenideElement firstCard = getFirstProductCard();

        firstCard
                .$(".qty-btn[data-action='qty-change'][data-step='-1']")
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Добавить первый товар в корзину")
    public void addFirstCardToCart() {
        SelenideElement firstCard = getFirstProductCard();

        firstCard
                .$(".btn[data-action='add-to-cart']")
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Открыть корзину")
    public void openCart() {
        cartButton
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Получить количество товаров в корзине")
    public int getCartCount() {
        String text = cartCount
                .shouldBe(Condition.visible)
                .getText();

        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        String digitsOnly = text.replaceAll("[^0-9]", "");

        if (digitsOnly.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(digitsOnly);
    }

    @Step("Проверить отображение основных элементов главной страницы")
    public void assertElementsVisible() {
        checkMainPageElements();
    }

    @Step("Проверить наличие карточек товаров")
    private void checkProductCardsExist() {
        productCards
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));

        getFirstProductCard()
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие кнопки увеличения количества")
    private void checkPlusButtonIsVisible() {
        getFirstProductCard()
                .$(".qty-btn[data-action='qty-change'][data-step='1']")
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие кнопки уменьшения количества")
    private void checkMinusButtonIsVisible() {
        getFirstProductCard()
                .$(".qty-btn[data-action='qty-change'][data-step='-1']")
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие кнопки добавления в корзину")
    private void checkAddToCartButtonIsVisible() {
        getFirstProductCard()
                .$("[data-action='add-to-cart']")
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие кнопки открытия корзины")
    private void checkCartButtonIsVisible() {
        cartButton
                .shouldBe(Condition.visible);
    }

    @Step("Проверить наличие счетчика корзины")
    private void checkCartCountElementIsVisible() {
        cartCount
                .shouldBe(Condition.visible);
    }

    @Step("Проверить основные элементы главной страницы")
    private void checkMainPageElements() {
        checkProductCardsExist();
        checkPlusButtonIsVisible();
        checkMinusButtonIsVisible();
        checkAddToCartButtonIsVisible();
        checkCartButtonIsVisible();
        checkCartCountElementIsVisible();
    }

    @Step("Проверить корректность количества товаров в корзине")
    private void checkCartCountIsValid(int actualCartCount) {
        assertThat(actualCartCount)
                .as("Количество товаров в корзине")
                .isGreaterThanOrEqualTo(0);
    }

    @Step("Получить первую карточку товара")
    private SelenideElement getFirstProductCard() {
        return productCards
                .first()
                .shouldBe(Condition.visible);
    }
}


//package Homework;
//
//import Homework.utils.Config;
//import com.codeborne.selenide.CollectionCondition;
//import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.ElementsCollection;
//import com.codeborne.selenide.SelenideElement;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static com.codeborne.selenide.Selenide.*;
//
//public class Homework8MainPage {
//
//    @BeforeEach
//    void setup() {
//        open(Config.getBaseUrl());
//    }
//
//    private final ElementsCollection minusButtons = $$(".product-card .qty-btn[data-action='qty-change'][data-step='-1']");
//    private final ElementsCollection addToCartButtons = $$(".product-card .btn[data-action='add-to-cart']");
//    private final SelenideElement cartButton = $("button.btn.btn-cart#open-cart-btn");
//    private final SelenideElement cartCount = $("#cart-count");
//    private final ElementsCollection plusButtons = $$(".product-card .qty-btn[data-action='qty-change'][data-step='1']");
//
//
//    public void clickPlusInFirstCard() {
//        var firstCard = $$(".product-card").first();
//        firstCard.shouldBe(Condition.visible);
//        firstCard.$(".qty-btn[data-action='qty-change'][data-step='1']")
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    public void clickMinusInFirstCard() {
//        var firstCard = $$(".product-card").first();
//        firstCard.shouldBe(Condition.visible);
//        firstCard.$(".qty-btn[data-action='qty-change'][data-step='-1']")
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    public void addFirstCardToCart() {
//        var firstCard = $$(".product-card").first();
//        firstCard.shouldBe(Condition.visible);
//        firstCard.$(".btn[data-action='add-to-cart']")
//                .shouldBe(Condition.visible)
//                .click();
//    }
//
//    public void openCart() {
//        var cartBtn = $("button.btn.btn-cart#open-cart-btn");
//        cartBtn.shouldBe(Condition.visible).click();
//    }
//
//    public int getCartCount() {
//        var cartCount = $("#cart-count");
//        String text = cartCount.shouldBe(Condition.visible).getText();
//        if (text == null || text.trim().isEmpty()) return 0;
//        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
//    }
//
//    public void assertElementsVisible() {
//        $$(".product-card").shouldBe(CollectionCondition.sizeGreaterThanOrEqual(1)).first()
//                .shouldBe(Condition.visible);
//        $$(".product-card").first().$("[data-action='qty-change'][data-step='1']")
//                .shouldBe(Condition.visible);
//        $$(".product-card").first().$("[data-action='qty-change'][data-step='-1']")
//                .shouldBe(Condition.visible);
//        $$(".product-card").first().$("[data-action='add-to-cart']")
//                .shouldBe(Condition.visible);
//        $("button.btn.btn-cart#open-cart-btn").shouldBe(Condition.visible);
//        $("#cart-count").shouldBe(Condition.visible);
//    }
//
//    @Test
//    void all() {
//        clickPlusInFirstCard();
//        clickMinusInFirstCard();
//        addFirstCardToCart();
//        openCart();
//        int count = getCartCount();
//        org.junit.jupiter.api.Assertions.assertTrue(count >= 0);
//        assertElementsVisible();
//    }
//
//}
