package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;

public class InventoryTest extends TestBase {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAsStandardUser() {
        performStandardLogin();
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "На странице товаров отображаются все карточки каталога")
    public void catalogContainsSixItems() {
        Assert.assertEquals(inventoryPage.itemCount(), 6,
                "Ожидалось 6 товаров в каталоге");
        Assert.assertTrue(inventoryPage.itemNames().contains("Sauce Labs Backpack"),
                "В каталоге ожидался товар Sauce Labs Backpack");
    }

    @Test(description = "Позитив: добавление товара в корзину увеличивает счётчик")
    public void addItemToCartIncrementsBadge() {
        Assert.assertEquals(inventoryPage.cartBadgeCount(), 0, "Корзина должна быть пустой");

        inventoryPage.addToCart("Sauce Labs Backpack");

        Assert.assertEquals(inventoryPage.cartBadgeCount(), 1,
                "После добавления товара счётчик корзины должен быть равен 1");
        Assert.assertTrue(inventoryPage.isCartBadgeVisible(), "Счётчик корзины должен отображаться");
    }
}
