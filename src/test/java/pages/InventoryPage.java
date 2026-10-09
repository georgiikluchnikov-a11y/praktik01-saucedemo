package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Страница товаров (/inventory.html): карточки, сортировка, добавление в корзину.
 */
public class InventoryPage extends BasePage {

    private final By title = By.className("title");
    private final By items = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By addToCartButtons = By.cssSelector("button[data-test^='add-to-cart']");
    private final By removeButtons = By.cssSelector("button[data-test^='remove']");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortSelect = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return textOf(title);
    }

    public int itemCount() {
        return visible(items) != null ? driver.findElements(items).size() : 0;
    }

    public List<String> itemNames() {
        List<String> names = new ArrayList<>();
        for (WebElement element : driver.findElements(itemNames)) {
            names.add(element.getText());
        }
        return names;
    }

    public List<String> itemPrices() {
        List<String> prices = new ArrayList<>();
        for (WebElement element : driver.findElements(itemPrices)) {
            prices.add(element.getText());
        }
        return prices;
    }

    /** Добавляет в корзину товар с указанным названием. */
    public void addToCart(String itemName) {
        By button = By.xpath("//div[@class='inventory_item'][.//div[text()='" + itemName + "']]"
                + "//button[starts-with(@data-test,'add-to-cart')]");
        click(button);
    }

    /** Добавляет в корзину товар по индексу карточки. */
    public void addToCartByIndex(int index) {
        wait.until(d -> d.findElements(addToCartButtons).size() > index);
        driver.findElements(addToCartButtons).get(index).click();
    }

    public void removeFromCartByIndex(int index) {
        wait.until(d -> d.findElements(removeButtons).size() > index);
        driver.findElements(removeButtons).get(index).click();
    }

    public int cartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public boolean isCartBadgeVisible() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public void openCart() {
        click(cartLink);
    }

    public void sortBy(String value) {
        new Select(visible(sortSelect)).selectByValue(value);
    }
}
