package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;

public class CartTest extends TestBase {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAsStandardUser() {
        openWithRetry("/");
        new LoginPage(driver).login(STANDARD_USER, PASSWORD);
        wait.until(d -> d.getCurrentUrl().contains("/inventory.html"));
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "Позитив: товар из каталога попадает в корзину с названием и ценой")
    public void addedItemAppearsInCart() {
        inventoryPage.addToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(driver);
        wait.until(d -> d.getCurrentUrl().contains("/cart.html"));

        Assert.assertEquals(cartPage.itemCount(), 1, "В корзине ожидался один товар");
        Assert.assertEquals(cartPage.firstItemName(), "Sauce Labs Backpack",
                "Ожидалось название Sauce Labs Backpack");
        Assert.assertEquals(cartPage.firstItemPrice(), "$29.99",
                "Ожидалась цена $29.99");
        Assert.assertEquals(cartPage.firstItemQuantity(), "1",
                "Ожидалось количество 1");
    }

    @Test(description = "Удаление товара из каталога уменьшает счётчик корзины")
    public void removeItemDecrementsBadge() {
        inventoryPage.addToCartByIndex(0);
        inventoryPage.addToCartByIndex(1);
        Assert.assertEquals(inventoryPage.cartBadgeCount(), 2, "Ожидалось два товара в корзине");

        inventoryPage.removeFromCartByIndex(0);
        wait.until(d -> inventoryPage.cartBadgeCount() == 1);

        Assert.assertEquals(inventoryPage.cartBadgeCount(), 1,
                "После удаления одного товара счётчик должен быть равен 1");
    }

    @Test(description = "Негатив: пустая корзина не содержит товаров")
    public void emptyCartHasNoItems() {
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        Assert.assertEquals(cartPage.itemCount(), 0,
                "В пустой корзине не должно быть товаров");
    }
}
