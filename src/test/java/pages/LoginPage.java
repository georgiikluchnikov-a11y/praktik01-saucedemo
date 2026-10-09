package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Страница авторизации Swag Labs (https://www.saucedemo.com/).
 */
public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(System.getProperty("baseUrl", "https://www.saucedemo.com") + "/");
        return this;
    }

    public void login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
    }

    public String errorText() {
        return textOf(errorMessage);
    }

    public boolean isErrorVisible() {
        return !driver.findElements(errorMessage).isEmpty() && visible(errorMessage).isDisplayed();
    }
}
