import java.time.Duration;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.SearchResultsPage;
import pages.TestDataReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class WebsiteTest {

    private static final Logger LOGGER =
            Logger.getLogger(WebsiteTest.class.getName());

    private static WebDriver driver;

    private static TestDataReader testData;
    private static HomePage homePage;
    private static SearchResultsPage searchResultsPage;

    @BeforeAll
    static void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(20)
        );

        testData = new TestDataReader();
        homePage = new HomePage(driver, wait);
        searchResultsPage = new SearchResultsPage(driver, wait);
    }


    @Test
    void searchForKeyword() {
        // 1. Open eBay and search for the keyword.
        searchForKeywordAndValidate();
    }

    @Test
    void filterByManualTransmission() {

        // 2. Change the shipping location to Germany.
        searchResultsPage.openShippingFilter();
        searchResultsPage.openShippingCountryField();

        // 3. Apply the transmission filter using test data.
        String filterCategory = testData.get("filter.category");
        String filterValue = testData.get("filter.value");

        LOGGER.info("URL before transmission filter: "
                + driver.getCurrentUrl());

        searchResultsPage.applyTransmissionFilter(
                filterCategory,
                filterValue
        );

        // 4. Validate that the filter was applied.
        assertTrue(
                driver.getCurrentUrl().contains(
                        filterCategory + "=" + filterValue
                ),
                "Expected the " + filterValue + " "
                        + filterCategory + " filter to be applied."
        );

        // 5. Validate the filtered results.
        String filteredHeading =
                searchResultsPage.waitForResultsHeading();

        assertFalse(
                filteredHeading.isBlank(),
                "Filtered search results should be displayed."
        );

        int filteredProductCount =
                searchResultsPage.getVisibleProductCount();

        assertTrue(
                filteredProductCount > 0,
                "Expected at least one visible product after filtering."
        );

        LOGGER.info("Shipping location: Germany");
        LOGGER.info("Applied filter: "
                + filterCategory + " = " + filterValue);
        LOGGER.info("Filtered results: " + filteredHeading);
        LOGGER.info("Visible filtered products: "
                + filteredProductCount);
    }

    private static void searchForKeywordAndValidate() {
        String keyword = testData.get("search.keyword");

        // Open eBay and validate the homepage.
        homePage.open();
        homePage.waitUntilLoaded();

        assertTrue(
                homePage.getTitle().toLowerCase().contains("ebay"),
                "Expected the eBay homepage."
        );

        // Search using the keyword from the properties file.
        homePage.searchFor(keyword);

        // Wait for the search results page.
        searchResultsPage.waitForSearchUrl();

        String headingText =
                searchResultsPage.waitForResultsHeading();

        assertFalse(
                headingText.isBlank(),
                "The search results heading should not be empty."
        );

        // Verify the displayed keyword.
        String displayedKeyword =
                searchResultsPage.getSearchKeywordDisplayed();

        assertEquals(
                keyword,
                displayedKeyword,
                "The displayed keyword should match the test data."
        );

        // Verify that products are displayed.
        int visibleProductCount =
                searchResultsPage.getVisibleProductCount();

        assertTrue(
                visibleProductCount > 0,
                "Expected at least one visible product result."
        );

        // Extract and log the total number of results.
        String totalResults =
                searchResultsPage.getTotalResultsCount();

        LOGGER.info("Search heading: " + headingText);
        LOGGER.info("Visible products on current page: "
                + visibleProductCount);
        LOGGER.info("Total results reported by eBay: "
                + totalResults);

        System.out.println("Search heading: " + headingText);
        System.out.println("Visible products: " + visibleProductCount);
        System.out.println("Total search results: " + totalResults);
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

