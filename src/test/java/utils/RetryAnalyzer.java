package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Повтор упавшего теста. Демо-стенд saucedemo.com периодически отвечает медленно
 * или недоступен, поэтому один сетевой сбой не должен «ронять» весь набор.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;

    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            return true;
        }
        return false;
    }
}
