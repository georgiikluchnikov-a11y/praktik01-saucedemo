package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Оформление заказа: шаг с данными покупателя и шаг подтверждения.
 */
public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemTotal = By.className("summary_subtotal_label");
    private final By total = By.className("summary_total_label");
    private final By completeHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillCustomerData(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
    }

    /**
     * Переход к подтверждению заказа. Демо-стенд периодически не успевает обработать
     * клик, поэтому при отсутствии перехода выполняется одна повторная попытка.
     */
    public void continueToOverview() {
        clickAndWaitForUrl(continueButton, "/checkout-step-two.html");
    }

    /** Клик «Continue» без ожидания перехода — для негативных сценариев валидации. */
    public void continueExpectingValidationError() {
        click(continueButton);
    }

    /** Завершение заказа с одной повторной попыткой. */
    public void finish() {
        clickAndWaitForUrl(finishButton, "/checkout-complete.html");
    }

    private void clickAndWaitForUrl(By button, String urlPart) {
        click(button);
        try {
            wait.until(d -> d.getCurrentUrl().contains(urlPart));
        } catch (org.openqa.selenium.TimeoutException firstAttempt) {
            click(button);
            wait.until(d -> d.getCurrentUrl().contains(urlPart));
        }
    }

    public CheckoutPage submitCustomerData(String first, String last, String zip) {
        fillCustomerData(first, last, zip);
        continueToOverview();
        return this;
    }

    public String errorText() {
        return textOf(errorMessage);
    }

    /** Ждёт отрисовки списка товаров на шаге подтверждения. */
    public int overviewItemCount() {
        wait.until(d -> !d.findElements(total).isEmpty());
        return driver.findElements(cartItems).size();
    }

    public String overviewItemName() {
        return visible(itemNames).getText();
    }

    public String itemTotalText() {
        return textOf(itemTotal);
    }

    public String totalText() {
        return textOf(total);
    }

    public String completeHeaderText() {
        return textOf(completeHeader);
    }
}
