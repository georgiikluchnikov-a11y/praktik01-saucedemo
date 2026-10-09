package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Базовый класс тестов: поднимает и закрывает браузер, хранит явное ожидание.
 * Закрытие драйвера гарантировано даже при падении теста.
 */
public abstract class TestBase {

    protected static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com");
    protected static final Duration TIMEOUT = Duration.ofSeconds(10);

    /** Демонстрационные учётные записи Swag Labs. */
    protected static final String PASSWORD = "secret_sauce";
    protected static final String STANDARD_USER = "standard_user";
    protected static final String LOCKED_OUT_USER = "locked_out_user";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        if (isHeadless()) {
            options.addArguments("--headless=new");
        } else {
            options.addArguments("--start-maximized");
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected void open(String path) {
        driver.get(BASE_URL + path);
    }

    /**
     * Открывает страницу с повторными попытками: демо-стенд периодически отвечает
     * медленно или отдаёт страницу ошибки вместо формы.
     */
    protected void openWithRetry(String path) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                driver.get(BASE_URL + path);
                return;
            } catch (org.openqa.selenium.TimeoutException timeout) {
                // стенд не ответил — пробуем снова
            }
        }
        throw new IllegalStateException("Не удалось открыть " + BASE_URL + path);
    }

    /** Вход под standard_user с повторными попытками при медленном ответе стенда. */
    protected void performStandardLogin() {
        for (int attempt = 0; attempt < 3; attempt++) {
            openWithRetry("/");
            new pages.LoginPage(driver).login(STANDARD_USER, PASSWORD);
            try {
                wait.until(d -> d.getCurrentUrl().contains("/inventory.html"));
                return;
            } catch (org.openqa.selenium.TimeoutException notLoggedIn) {
                // страница не загрузилась или форма не отрисовалась — ещё попытка
            }
        }
        throw new IllegalStateException("Не удалось войти под " + STANDARD_USER + " за три попытки");
    }

    protected static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"))
                || Boolean.parseBoolean(System.getenv("HEADLESS"));
    }
}
