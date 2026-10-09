# Практическое занятие №13
## Автоматизированное тестирование проекта SauceDemo

### 1. Титульный лист
Дисциплина: ____________________
Группа: ____________________
ФИО: ____________________
Дата: ____________________

### 2. Цель
Сформировать практические навыки проектирования, разработки и сопровождения UI-автотестов
на Java с использованием Selenium WebDriver и TestNG, включая Maven-сборку, Page Object,
явные ожидания, анализ отчётов Surefire и подготовку подтверждающих материалов.

### 3. Среда
ОС: ____________________
JDK: 17
Maven: 3.9+
Браузер: Google Chrome ____________________
Драйверы: WebDriverManager 5.9.2
Selenium WebDriver: 4.49.0
TestNG: 7.12.0
IDE: IntelliJ IDEA
Тестируемый стенд: https://www.saucedemo.com (Swag Labs)

### 4. Реализованные сценарии

| № | Сценарий | Шаги | Ожидаемый результат |
|---|---|---|---|
| 1 | Авторизация — позитив | открыть `/`, ввести `standard_user` / `secret_sauce`, нажать Login | переход на `/inventory.html`, заголовок «Products» |
| 2 | Авторизация — негатив (блокировка) | ввести `locked_out_user` / `secret_sauce`, Login | сообщение «Epic sadface: Sorry, this user has been locked out.» |
| 3 | Авторизация — негатив (пароль) | `standard_user` / `wrong_password` | сообщение «Username and password do not match…» |
| 4 | Каталог | войти под `standard_user` | 6 карточек товаров, среди них Sauce Labs Backpack |
| 5 | Добавление в корзину | нажать Add to cart у Sauce Labs Backpack | счётчик корзины = 1 |
| 6 | Состав корзины | открыть корзину | 1 товар, название Sauce Labs Backpack, цена $29.99, количество 1 |
| 7 | Удаление из корзины | добавить 2 товара, удалить один | счётчик корзины = 1 |
| 8 | Пустая корзина — негатив | открыть корзину без товаров | 0 товаров |
| 9 | Оформление заказа | Checkout → Ivan / Petrov / 123456 → Continue → Finish | шаг Overview с товаром и суммой, затем «Thank you for your order!» |
| 10 | Валидация First Name — негатив | Checkout → Continue без данных | «Error: First Name is required», остаёмся на шаге ввода |
| 11 | Валидация Postal Code — негатив | заполнить имя и фамилию, оставить индекс пустым | «Error: Postal Code is required» |
| 12 | Сортировка Name A→Z | выбрать Name (A to Z) | первый товар Sauce Labs Backpack, порядок алфавитный |
| 13 | Сортировка Name Z→A | выбрать Name (Z to A) | первый товар Test.allTheThings() T-Shirt (Red) |
| 14 | Сортировка Price low→high | выбрать Price (low to high) | цены возрастают, минимальная $7.99 |
| 15 | Сортировка Price high→low | выбрать Price (high to low) | цены убывают, максимальная $49.99 |

### 5. Локаторы и ожидания
- Логин: `By.id("user-name")`, `By.id("password")`, `By.id("login-button")`, ошибки — `[data-test='error']`.
- Каталог: `By.className("inventory_item")`, кнопки `button[data-test^='add-to-cart']`, счётчик `shopping_cart_badge`.
- Корзина/заказ: `cart_item`, `inventory_item_name`, `inventory_item_price`, `cart_quantity`, `checkout`, `first-name`, `last-name`, `postal-code`, `continue`, `finish`.
- Все динамические элементы ожидаются явно через `WebDriverWait` + `ExpectedConditions` (visibility, elementToBeClickable, ожидание смены URL и появления блоков).
- Драйвер закрывается в `@AfterMethod(alwaysRun = true)` через `driver.quit()`.

### 6. Запуск
```bash
mvn clean test               # видимый Chrome
mvn clean test -Dheadless=true   # как в CI
```
Результат: Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
Скриншот зелёного прогона: ____________________

### 7. Отчёты
Путь: `target/surefire-reports/` (TestSuite.txt, TEST-TestSuite.xml, emailable-report.html)
Скриншот отчёта и количество passed/failed/skipped: ____________________

### 8. Git
Репозиторий: ____________________
Feature-ветка: `feature/<Группа>_<Фамилия>_saucedemo`
Pull Request: ____________________
Reviewer (ментор): ____________________
Hash последнего коммита: ____________________

### 9. Выводы
Проект собран на Maven с разделением на слои BaseTest / BasePage / Page Objects / Tests.
Все сценарии используют устойчивые локаторы (id и data-test) и явные ожидания, драйвер
гарантированно закрывается. В ходе работы выявлена особенность демо-стенда: страница корзины
отрисовывается асинхронно, поэтому перед подсчётом товаров добавлено ожидание появления блока
`cart_list`. Дальнейшие улучшения: подключение Allure-отчётности и кроссбраузерный прогон
(Chrome/Firefox/Edge) — реализованы в практических занятиях №14–16.
