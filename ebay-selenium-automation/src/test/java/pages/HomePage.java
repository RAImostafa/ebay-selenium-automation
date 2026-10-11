package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page object for the ebay homepage. */
public class HomePage {

    private static final String WEBSITE_URL = "https://www.ebay.com/";

    private static final By SEARCH_INPUT = By.id("gh-ac");
    private static final By SEARCH_BUTTON = By.id("gh-search-btn");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open() {
        driver.get(WEBSITE_URL);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void searchFor(String keyword) {
        WebElement searchInput = driver.findElement(SEARCH_INPUT);
        searchInput.sendKeys(keyword);
        driver.findElement(SEARCH_BUTTON).click();
    }
}


