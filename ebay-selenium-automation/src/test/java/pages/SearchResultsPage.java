package pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SearchResultsPage {


    private static final By SHIPPING_FILTER =
            By.xpath("//span[.//span[@class='zipcode-text']]");

    private static final By SHIPPING_COUNTRY_FIELD =
            By.cssSelector("select[name='_fcid']");

    private static final By SHIPPING_APPLY_BUTTON =
            By.xpath("//button[contains(@class,'btn--primary') and normalize-space()='Apply']");

    private static final By RESULTS_HEADING =
            By.cssSelector(
                    ".srp-controls__count-heading, #srp-results-heading"
            );

    private static final By SEARCH_INPUT =
            By.id("gh-ac");

    private static final By MODERN_PRODUCT_CARDS =
            By.cssSelector("li.s-card");

    private static final By LEGACY_PRODUCT_CARDS =
            By.cssSelector("li.s-item");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SearchResultsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }


    public void openShippingFilter() {
        WebElement shippingFilter = wait.until(
                ExpectedConditions.elementToBeClickable(SHIPPING_FILTER)
        );

        shippingFilter.click();
    }

    public void openShippingCountryField() {
        WebElement countryField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        SHIPPING_COUNTRY_FIELD
                )
        );

        countryField.click();

        Select countryDropdown = new Select(countryField);
        countryDropdown.selectByVisibleText("Germany - DEU");



        WebElement applyButton = wait.until(
                ExpectedConditions.elementToBeClickable(SHIPPING_APPLY_BUTTON)
        );

        pause(2000);
        applyButton.click();
    }


    public void waitForSearchUrl() {
        wait.until(ExpectedConditions.urlContains("/sch/i.html"));
    }

    public String waitForResultsHeading() {
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        RESULTS_HEADING
                )
        );

        return heading.getText().trim();
    }



    public String getSearchKeywordDisplayed() {
        return driver.findElement(SEARCH_INPUT).getAttribute("value");
    }

    public int getVisibleProductCount() {
        List<WebElement> cards =
                driver.findElements(MODERN_PRODUCT_CARDS);

        if (cards.isEmpty()) {
            cards = driver.findElements(LEGACY_PRODUCT_CARDS);
        }

        return (int) cards.stream()
                .filter(WebElement::isDisplayed)
                .count();
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public String getTotalResultsCount() {
        String headingText = waitForResultsHeading();

        Pattern pattern = Pattern.compile(
                "(?i)([\\d,]+\\+?)\\s+results\\b"
        );

        Matcher matcher = pattern.matcher(headingText);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new AssertionError(
                "Could not extract the total result count from: "
                        + headingText
        );
    }

    public void applyTransmissionFilter(String filterCategory, String filterValue) {

        // Locate the Transmission section by its heading
        By transmissionHeading = By.xpath(
                "//span[@class='details__label' and normalize-space()='"
                        + filterCategory + "']"
        );

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(transmissionHeading)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                heading
        );

        // Locate the Manual option inside the Transmission section
        By filterOption = By.xpath(
                "//span[@class='details__label' and normalize-space()='"
                        + filterCategory
                        + "']/ancestor::summary/following-sibling::div"
                        + "//a[contains(@href, 'Transmission="
                        + filterValue + "')]"
        );

        WebElement option = wait.until(
                ExpectedConditions.presenceOfElementLocated(filterOption)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                option
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(filterOption)
        ).click();

        // Wait until eBay applies the filter
        wait.until(
                ExpectedConditions.urlContains(
                        filterCategory + "=" + filterValue
                )
        );

        waitForResultsHeading();
    }
}


