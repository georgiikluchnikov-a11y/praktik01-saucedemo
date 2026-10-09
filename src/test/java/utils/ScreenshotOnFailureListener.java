package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * При падении теста сохраняет скриншот и DOM страницы в target/screenshots.
 * Артефакты помогают разобрать причину падения без повторного запуска.
 */
public class ScreenshotOnFailureListener implements ITestListener {

    private static final Path DIRECTORY = Paths.get("target", "screenshots");

    @Override
    public void onTestFailure(ITestResult result) {
        Object instance = result.getInstance();
        WebDriver driver = extractDriver(instance);
        if (driver == null) {
            System.out.println("Скриншот недоступен: драйвер не найден");
            return;
        }
        try {
            Files.createDirectories(DIRECTORY);
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(DIRECTORY.resolve(result.getName() + ".png"), png);
            Files.writeString(DIRECTORY.resolve(result.getName() + ".html"), driver.getPageSource());
            System.out.println("Скриншот падения: " + DIRECTORY.resolve(result.getName() + ".png"));
        } catch (IOException | RuntimeException error) {
            System.out.println("Не удалось сохранить скриншот падения: " + error);
        }
    }

    private WebDriver extractDriver(Object instance) {
        for (Class<?> type = instance.getClass(); type != null; type = type.getSuperclass()) {
            try {
                java.lang.reflect.Field field = type.getDeclaredField("driver");
                field.setAccessible(true);
                Object value = field.get(instance);
                if (value instanceof WebDriver webDriver) {
                    return webDriver;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // ищем поле driver в родительском классе
            }
        }
        return null;
    }
}
