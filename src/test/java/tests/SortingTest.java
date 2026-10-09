package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;

import java.util.ArrayList;
import java.util.List;

public class SortingTest extends TestBase {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAsStandardUser() {
        performStandardLogin();
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "Сортировка Name (A to Z) даёт алфавитный порядок")
    public void sortByNameAscending() {
        inventoryPage.sortBy("az");

        List<String> names = inventoryPage.itemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(String::compareTo);

        Assert.assertEquals(names, sorted, "Ожидался алфавитный порядок названий");
        Assert.assertEquals(names.get(0), "Sauce Labs Backpack",
                "Первым по алфавиту ожидался Sauce Labs Backpack");
    }

    @Test(description = "Сортировка Name (Z to A) даёт обратный алфавитный порядок")
    public void sortByNameDescending() {
        inventoryPage.sortBy("za");

        List<String> names = inventoryPage.itemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort((a, b) -> b.compareTo(a));

        Assert.assertEquals(names, sorted, "Ожидался обратный алфавитный порядок названий");
        Assert.assertEquals(names.get(0), "Test.allTheThings() T-Shirt (Red)",
                "Первым при обратной сортировке ожидался Test.allTheThings() T-Shirt (Red)");
    }

    @Test(description = "Сортировка Price (low to high) даёт возрастание цен")
    public void sortByPriceAscending() {
        inventoryPage.sortBy("lohi");

        List<Double> prices = toNumbers(inventoryPage.itemPrices());
        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Цены должны возрастать: " + prices);
        }
        Assert.assertEquals(prices.get(0), 7.99, 0.001, "Минимальная цена должна быть 7.99");
    }

    @Test(description = "Сортировка Price (high to low) даёт убывание цен")
    public void sortByPriceDescending() {
        inventoryPage.sortBy("hilo");

        List<Double> prices = toNumbers(inventoryPage.itemPrices());
        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i - 1),
                    "Цены должны убывать: " + prices);
        }
        Assert.assertEquals(prices.get(0), 49.99, 0.001, "Максимальная цена должна быть 49.99");
    }

    private static List<Double> toNumbers(List<String> prices) {
        List<Double> numbers = new ArrayList<>();
        for (String price : prices) {
            numbers.add(Double.parseDouble(price.replace("$", "")));
        }
        return numbers;
    }
}
