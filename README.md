# Практическое занятие №13 — UI-автотесты SauceDemo

[![UI tests](https://github.com/georgiikluchnikov-a11y/praktik01-saucedemo/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/georgiikluchnikov-a11y/praktik01-saucedemo/actions/workflows/ui-tests.yml)

Maven-проект UI-автотестов на Java для учебного сайта
[Swag Labs (saucedemo.com)](https://www.saucedemo.com/): Selenium WebDriver 4, TestNG,
паттерн Page Object, явные ожидания, отчёты Maven Surefire.

## Стек

| Технология | Версия |
|---|---|
| Java | 17 |
| Maven | 3.9+ |
| Selenium WebDriver | 4.49.0 |
| TestNG | 7.12.0 |
| WebDriverManager | 5.9.2 |

## Структура проекта

```text
praktik01-saucedemo/
├── pom.xml
├── testng.xml                       # TestNG suite
├── REPORT_TEMPLATE.md               # шаблон отчёта по практическому занятию
├── src/test/java/
│   ├── base/TestBase.java           # драйвер, ожидания, учётные записи, driver.quit()
│   ├── pages/                       # Page Object: Login, Inventory, Cart, Checkout
│   └── tests/                       # 5 тест-классов
└── .github/workflows/ui-tests.yml   # CI: headless Chrome + артефакты отчётов
```

## Требования

- JDK 17+ (`java -version`)
- Maven 3.9+ (`mvn -v`)
- Google Chrome (WebDriverManager сам скачает chromedriver)

## Запуск

```bash
# все тесты (видимый браузер)
mvn clean test

# без графического интерфейса, как в CI
mvn clean test -Dheadless=true

# один класс или один метод
mvn test -Dtest=LoginTest
mvn test -Dtest=CheckoutTest#successfulCheckout

# другой стенд
mvn test -DbaseUrl=https://www.saucedemo.com
```

Отчёты: `target/surefire-reports/`.

## Реализованные сценарии

| Тест-класс | Что проверяет |
|---|---|
| `LoginTest` | позитивный вход `standard_user` → `/inventory.html` + заголовок Products; блокировка `locked_out_user`; неверный пароль |
| `InventoryTest` | 6 карточек каталога; добавление товара увеличивает счётчик корзины |
| `CartTest` | товар в корзине с названием, ценой и количеством; удаление уменьшает счётчик; пустая корзина |
| `CheckoutTest` | полный цикл заказа до «Thank you for your order!»; ошибки при пустом First Name и Postal Code |
| `SortingTest` | сортировки Name A→Z, Z→A, Price low→high, high→low |

Учётные записи Swag Labs: `standard_user`, `locked_out_user`, `problem_user`,
`performance_glitch_user`; пароль — `secret_sauce`.

## CI

Workflow запускается на `push` и `pull_request` в `main`, выполняет
`mvn -B clean test -Dheadless=true` на Ubuntu с JDK 17 и загружает `surefire-reports` артефактом.

## Git-процесс

```bash
git checkout -b feature/<Группа>_<Фамилия>_saucedemo
git add .
git commit -m "feat: add positive/negative login tests"
git push -u origin feature/<Группа>_<Фамилия>_saucedemo
```

Далее открыть Pull Request в `main` и добавить ментора в Reviewers.
