package tempest_double.GUITests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryPageTests {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        try {
            // Set up ChromeOptions for Selenium Grid
            ChromeOptions options = new ChromeOptions();

            driver = new ChromeDriver(options);

            // Launch the application (replace with the actual URL)
            driver.get("http://localhost:8080/history");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up WebDriver: " + e.getMessage(), e);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testScenarioRendering() {
        // Wait for the cards to render
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cardsContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cards-container")));

        // Verify that cards are rendered
        List<WebElement> cards = cardsContainer.findElements(By.className("session-card"));
        assertFalse(cards.isEmpty(), "Cards container should have at least one scenario card rendered.");

        // Verify the content of the first card
        WebElement firstCard = cards.get(0);
        WebElement cardHeader = firstCard.findElement(By.className("card-header"));
        assertNotNull(cardHeader.findElement(By.tagName("h3")), "First card should have a scenario name.");
    }

    @Test
    void testSelectDeselectAllCards() {
        // Wait for the select-all checkbox
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement selectAllCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("select-all")));

        // Click the select-all checkbox
        selectAllCheckbox.click();

        // Verify all card checkboxes are selected
        List<WebElement> cardCheckboxes = driver.findElements(By.cssSelector(".card-header input[type='checkbox']"));
        for (WebElement checkbox : cardCheckboxes) {
            assertTrue(checkbox.isSelected(), "All card checkboxes should be selected when select-all is checked.");
        }

        // Deselect all checkboxes
        selectAllCheckbox.click();
        for (WebElement checkbox : cardCheckboxes) {
            assertFalse(checkbox.isSelected(), "All card checkboxes should be deselected when select-all is unchecked.");
        }
    }

    @Test
    void testDeleteSelectedScenarios() {
        // Select the first scenario card
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement firstCardCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".card-header input[type='checkbox']")));
        firstCardCheckbox.click();

        // Verify the checkbox is selected
        assertTrue(firstCardCheckbox.isSelected(), "First card checkbox should be selected.");

        // Click the delete button
        WebElement deleteButton = driver.findElement(By.id("delete-selected"));
        deleteButton.click();
    }

    @Test
    void testViewScenarioDetailsInModal() {
        // Wait for the first card to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement firstCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("session-card")));

        // Click on the first card content to open the modal
        WebElement cardContent = firstCard.findElement(By.className("card-content"));
        cardContent.click();

        // Verify the modal is displayed
        WebElement modalOverlay = driver.findElement(By.id("modal-overlay"));
        assertTrue(modalOverlay.isDisplayed(), "Modal should be displayed when card content is clicked.");

        // Verify the modal contains the correct title
        WebElement modalContent = driver.findElement(By.id("modal-content"));
        WebElement modalTitle = modalContent.findElement(By.tagName("h2"));
        assertTrue(modalTitle.getText().contains("Detailed Data"), "Modal title should include 'Detailed Data'.");

        // Close the modal
        WebElement closeButton = modalContent.findElement(By.tagName("button"));
        closeButton.click();

        // Verify the modal is no longer visible
        assertFalse(modalOverlay.isDisplayed(), "Modal should be hidden after clicking the close button.");
    }

}
