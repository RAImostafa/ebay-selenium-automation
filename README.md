# eBay Search & Filter Automation Framework

A Selenium + TestNG automation framework (Page Object Model) that automates:

1. Navigate to https://www.ebay.com/
2. Validate the home page has loaded (eBay logo check)
3. Search for a term (e.g. "mazda mx-5")
4. Validate the search results page
5. Log/print the number of results obtained (console + Allure report)
6. Filter results on the left-hand side by **Transmission -> Manual**

## Tech stack

- **Java 17**
- **Selenium 4** – browser automation
- **WebDriverManager** – automatic driver binary management (no manual chromedriver setup)
- **TestNG** – test runner
- **Allure** – execution/HTML report (steps, attachments, screenshots on failure)
- **Jackson** – reads external JSON test data (no hardcoded test data)
- **Maven** – build/dependency management

## Project structure

```
src/main/java/com/automation
├── config/ConfigReader.java        # reads config.properties (base URL, browser, timeouts)
├── driver/DriverFactory.java        # creates WebDriver instances per browser
├── driver/DriverManager.java        # thread-safe WebDriver holder
├── pages/BasePage.java              # common reusable browser actions (click, type, waits...)
├── pages/HomePage.java              # eBay home page (POM)
├── pages/SearchResultsPage.java     # eBay search results page (POM) + generic filter helper
├── testdata/EbaySearchTestData.java # POJO mapped from JSON test data
├── testdata/JsonDataReader.java     # generic JSON -> POJO reader
└── utils/ReportLogger.java          # logs to console + Allure (steps/attachments)

src/main/resources/config.properties # base url / browser / timeouts (externalized config)

src/test/java/com/automation/tests
├── base/BaseTest.java                       # driver lifecycle + screenshot-on-failure
└── EbaySearchAndFilterTest.java              # the end-to-end scenario

src/test/resources/testdata.json      # externalized test data (search term, filter value)
testng.xml                            # TestNG suite definition
```

## How test data is externalized

All variable data (search term, filter value) lives in `src/test/resources/testdata.json`:

```json
{
  "searchTerm": "mazda mx-5",
  "transmissionFilterValue": "Manual"
}
```

Add more fields/files and corresponding POJOs to extend coverage (e.g. different search
terms or filters) without touching any Java test logic. The `JsonDataReader` utility is
generic and can be reused for additional JSON files; similarly, you could add an
`XlsDataReader` (e.g. using Apache POI) following the same pattern for Excel-based data.

Browser/environment configuration (base URL, browser, headless mode, timeouts) lives in
`src/main/resources/config.properties` and can also be overridden via `-D` system
properties, e.g.:

```
mvn test -Dbrowser=firefox -Dheadless=true
```

## Running the tests

Prerequisite: Maven and a Chrome (or Firefox) browser installed locally.

```bash
mvn clean test
```

This runs `testng.xml`, launches the browser (Chrome by default, driver managed
automatically by WebDriverManager), and writes raw Allure results to
`target/allure-results`.

## Generating / viewing the Allure report

```bash
mvn allure:report   # generates an HTML report under target/site/allure-maven-plugin
mvn allure:serve     # builds the report and opens it in your browser immediately
```

The report includes:
- Each scenario step (navigate, search, validate, filter) as an Allure **Step**
- The **number of search results** (before and after filtering) attached as text attachments
- Full Epic/Feature/Story/Severity grouping for readability

## Notes on the Transmission filter locator

eBay's left-hand "Refine" panel DOM can change over time. `SearchResultsPage.applyFilter`
is written generically (`applyFilter(String groupName, String value)`) so it is not
hardcoded to "Transmission"/"Manual" — it locates the refine group whose heading matches
`groupName` and clicks the option whose label matches `value` inside that group. If eBay
changes its markup, only the locator inside `SearchResultsPage` needs to be adjusted; no
test code changes are required.
