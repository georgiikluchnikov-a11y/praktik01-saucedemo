package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.InventoryPage;

public class CheckoutTest extends TestBase {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAsStandardUser() {
        performStandardLogin();
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "Позитив: оформление заказа завершается сообщением Thank you for your order!")
    public void successfulCheckout() {
        inventoryPage.addToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CheckoutPage checkoutPage = new pages.CartPage(driver).checkout();
        wait.until(d -> d.getCurrentUrl().contains("/checkout-step-one.html"));

        checkoutPage.submitCustomerData("Ivan", "Petrov", "123456");

        Assert.assertEquals(checkoutPage.overviewItemCount(), 1,
                "На шаге подтверждения ожидался один товар");
        Assert.assertEquals(checkoutPage.overviewItemName(), "Sauce Labs Backpack",
                "Ожидалось название Sauce Labs Backpack");
        Assert.assertTrue(checkoutPage.totalText().contains("Total:"),
                "Ожидалась итоговая сумма, фактически: " + checkoutPage.totalText());

        checkoutPage.finish();

        Assert.assertEquals(checkoutPage.completeHeaderText(), "Thank you for your order!",
                "Ожидалось подтверждение оформления заказа");
    }

    @Test(description = "Негатив: без имени заказ не оформляется и выводится ошибка")
    public void checkoutWithoutFirstNameShowsError() {
        inventoryPage.addToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CheckoutPage checkoutPage = new pages.CartPage(driver).checkout();
        wait.until(d -> d.getCurrentUrl().contains("/checkout-step-one.html"));

        checkoutPage.continueExpectingValidationError();

        Assert.assertEquals(checkoutPage.errorText(), "Error: First Name is required",
                "Ожидалась ошибка об обязательном поле First Name");
        Assert.assertTrue(checkoutPage.currentUrl().contains("/checkout-step-one.html"),
                "Пользователь должен остаться на шаге ввода данных");
    }

    @Test(description = "Негатив: без индекса заказ не оформляется и выводится ошибка")
    public void checkoutWithoutPostalCodeShowsError() {
        inventoryPage.addToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CheckoutPage checkoutPage = new pages.CartPage(driver).checkout();
        wait.until(d -> d.getCurrentUrl().contains("/checkout-step-one.html"));

        checkoutPage.fillCustomerData("Ivan", "Petrov", "");
        checkoutPage.continueExpectingValidationError();

        Assert.assertEquals(checkoutPage.errorText(), "Error: Postal Code is required",
                "Ожидалась ошибка об обязательном поле Postal Code");
    }
}
