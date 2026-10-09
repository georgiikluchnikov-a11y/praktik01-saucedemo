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
     * Открывает страницу с одной повторной попыткой: демо-стенд периодически
     * отвечает медленно, и первая загрузка может завершиться таймаутом.
     */
    protected void openWithRetry(String path) {
        try {
            driver.get(BASE_URL + path);
        } catch (org.openqa.selenium.TimeoutException firstAttempt) {
            driver.get(BASE_URL + path);
        }
    }

    protected static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"))
                || Boolean.parseBoolean(System.getenv("HEADLESS"));
    }
}
