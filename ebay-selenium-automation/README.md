# eBay Search Automation

## Overview

This project automates the search and filtering functionality on [eBay](https://www.ebay.com/) using Selenium WebDriver, Java, JUnit 5, and Maven.

It follows the Page Object Model (POM) to separate page interactions from test logic, making the code easier to maintain and reuse.

## Test Scenarios

### 1. Search for a Keyword

The `searchForKeyword` test:

- Opens the eBay homepage.
- Validates that the homepage title contains "eBay".
- Searches for `mazda mx-5` using the configured test data.
- Validates that the search results heading is displayed.
- Verifies that the displayed search keyword matches the expected keyword.
- Checks that at least one product is visible.
- Logs the search results heading, visible product count, and total results count reported by eBay when available.

### 2. Filter by Manual Transmission

The `filterByManualTransmission` test:

- Performs its own search for `mazda mx-5`.
- Applies the Transmission filter with the value Manual.
- Verifies that the URL reflects the applied filter.
- Validates that the filtered results heading is displayed.
- Checks that at least one product remains visible after filtering.
- Logs the filtered results heading, visible product count, and resulting URL.

Each test performs its own search setup, so neither test depends on the execution order of the other.

## Project Structure

```text
.
├── pom.xml
├── .gitignore
└── src
    ├── main
    │   └── java
    │       └── Main.java
    └── test
        ├── java
        │   ├── WebsiteTest.java
        │   └── pages
        │       ├── HomePage.java
        │       ├── SearchResultsPage.java
        │       └── TestDataReader.java
        └── resources
            └── test-data.properties
```

Maven generates the `target/` directory during builds and test execution. It contains compiled classes, test reports, and other generated files.

## Technologies Used

- **Java 17** — programming language.
- **Selenium WebDriver** — browser automation.
- **JUnit 5** — test execution and assertions.
- **Maven** — dependency and build management.
- **Chrome** — browser used for automation.
- **Selenium Manager** — automatic driver management when supported by the environment.

## Test Data

Test inputs are stored in `src/test/resources/test-data.properties`:

```properties
search.keyword=mazda mx-5
filter.category=Transmission
filter.value=Manual
```

The `TestDataReader` class loads the properties file from the classpath and provides a `get(key)` method to retrieve the configured values.

This keeps test data separate from the test implementation and makes it easier to change search keywords or filter criteria without modifying the Java code.

## Project Design

The project follows the Page Object Model:

- **`HomePage.java`** — handles homepage interactions, including opening eBay and searching for a keyword.
- **`SearchResultsPage.java`** — handles search results, product counting, results heading retrieval, and transmission filtering.
- **`TestDataReader.java`** — loads and provides access to test data.
- **`WebsiteTest.java`** — orchestrates the test scenarios, performs assertions, and logs results.

Explicit waits are used to synchronize browser interactions with page elements instead of relying on fixed delays.

## Prerequisites

Before running the tests, ensure you have:

- Java Development Kit (JDK) 17.
- Apache Maven.
- Google Chrome installed.
- An internet connection to access eBay.

Selenium Manager can automatically resolve the required browser driver with supported Selenium versions and environments.

## Running the Tests

Open a terminal in the project root directory and execute:

```bash
mvn test
```

Maven will compile the project, execute the JUnit tests, and generate test reports.

To run the test class directly from IntelliJ IDEA, open `WebsiteTest.java` and select the run option next to the class or individual test methods.

## Test Reports and Logs

After execution, Maven Surefire reports are available under:

```text
target/surefire-reports/
```

The tests use Java's built-in logging to report search information, visible product counts, and filter execution details.

## Build Configuration

The `pom.xml` configures:

- Java source and target compatibility.
- Selenium Java dependencies.
- JUnit Jupiter for test execution.
- Maven Surefire for running JUnit tests.

The Maven Surefire plugin should be declared only once in the build configuration to avoid duplicate plugin declarations.

## Notes and Limitations

- eBay's page structure, available filters, and URL parameters may change, which can require locator updates.
- Search results and total result counts can vary depending on eBay's live inventory and search behavior.
- The tests validate that results are displayed and that the filter is reflected in the URL. They do not independently verify every product's transmission specification.
- Successful execution requires access to eBay and a functioning browser environment.

