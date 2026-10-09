package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

public class LoginTest extends TestBase {

    @Test(description = "Позитив: standard_user успешно входит на страницу товаров")
    public void successfulLogin() {
        openWithRetry("/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(STANDARD_USER, PASSWORD);

        wait.until(d -> d.getCurrentUrl().contains("/inventory.html"));
        InventoryPage inventoryPage = new InventoryPage(driver);

        Assert.assertTrue(inventoryPage.currentUrl().contains("/inventory.html"),
                "Ожидался переход на /inventory.html, фактически: " + inventoryPage.currentUrl());
        Assert.assertEquals(inventoryPage.title(), "Products",
                "На странице товаров ожидался заголовок Products");
    }

    @Test(description = "Негатив: заблокированный пользователь получает сообщение об ошибке")
    public void lockedOutUserSeesError() {
        openWithRetry("/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(LOCKED_OUT_USER, PASSWORD);

        Assert.assertTrue(loginPage.isErrorVisible(), "Ожидалось сообщение об ошибке");
        Assert.assertTrue(loginPage.errorText().contains("Sorry, this user has been locked out"),
                "Ожидался текст о блокировке, фактически: " + loginPage.errorText());
        Assert.assertFalse(loginPage.currentUrl().contains("/inventory.html"),
                "Заблокированный пользователь не должен попадать на /inventory.html");
    }

    @Test(description = "Негатив: неверный пароль не пускает в приложение")
    public void wrongPasswordShowsError() {
        openWithRetry("/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(STANDARD_USER, "wrong_password");

        Assert.assertTrue(loginPage.isErrorVisible(), "Ожидалось сообщение об ошибке");
        Assert.assertTrue(loginPage.errorText().contains("Username and password do not match"),
                "Ожидался текст о неверной паре логин/пароль, фактически: " + loginPage.errorText());
    }
}
