package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Корзина (/cart.html).
 */
public class CartPage extends BasePage {

    private final By cartList = By.className("cart_list");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By itemQuantities = By.className("cart_quantity");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /** Ждёт отрисовки списка корзины и возвращает количество товаров. */
    public int itemCount() {
        wait.until(d -> !d.findElements(cartList).isEmpty());
        return driver.findElements(cartItems).size();
    }

    public String firstItemName() {
        return visible(itemNames).getText();
    }

    public String firstItemPrice() {
        return visible(itemPrices).getText();
    }

    public String firstItemQuantity() {
        return visible(itemQuantities).getText();
    }

    public CheckoutPage checkout() {
        retryAction(() -> {
            click(checkoutButton);
            wait.until(d -> d.getCurrentUrl().contains("/checkout-step-one.html"));
        });
        return new CheckoutPage(driver);
    }

    public void continueShopping() {
        click(continueShoppingButton);
    }
}
