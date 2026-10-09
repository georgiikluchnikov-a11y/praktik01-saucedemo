package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовый Page Object: драйвер, явное ожидание и общие действия над элементами.
 */
public abstract class BasePage {

    protected static final Duration TIMEOUT = Duration.ofSeconds(20);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    /** Ждёт появления элемента и возвращает его. */
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Ждёт, пока элемент станет кликабельным, и кликает по нему. */
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Ждёт, пока счётчик корзины станет равным ожидаемому значению. */
    protected void waitCartBadge(int expected) {
        wait.until(d -> {
            java.util.List<org.openqa.selenium.WebElement> badges =
                    d.findElements(org.openqa.selenium.By.className("shopping_cart_badge"));
            return badges.isEmpty() ? expected == 0 : Integer.parseInt(badges.get(0).getText()) == expected;
        });
    }

    /** Очищает поле и вводит значение. */
    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return visible(locator).getText();
    }

    /** Повторяет действие при медленном ответе стенда (до трёх попыток). */
    protected void retryAction(Runnable action) {
        org.openqa.selenium.WebDriverException last = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                action.run();
                return;
            } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException e) {
                last = e;
            }
        }
        throw last;
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
