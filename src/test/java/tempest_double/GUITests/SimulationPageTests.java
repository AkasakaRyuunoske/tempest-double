package tempest_double.GUITests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class SimulationPageTests {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Set up ChromeDriver in headless mode for CI
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        // Launch the application (replace with the actual URL)
        driver.get("http://localhost:8080/simulation");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testPopupFunctionality() {
        // Click the "Start" button to open the popup
        WebElement startButton = driver.findElement(By.id("start-button"));
        startButton.click();

        // Verify that the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Popup should be displayed after clicking the start button.");

        // Verify the popup title
        WebElement popupTitle = driver.findElement(By.id("popup-title"));
        assertEquals("Enter Scenario Name", popupTitle.getText(), "Popup title should match.");

        // Verify dynamic content in the popup
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        assertNotNull(scenarioNameInput, "Scenario name input should be present in the popup.");

        // Click the cancel button
        WebElement cancelButton = driver.findElement(By.id("cancel-btn"));
        cancelButton.click();

        // Verify that the popup is closed
        assertFalse(popup.isDisplayed(), "Popup should be closed after clicking the cancel button.");
    }

    @Test
    void testScenarioSearch() {
        // Open the popup
        WebElement startButton = driver.findElement(By.id("start-button"));
        startButton.click();

        // Enter a scenario name
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Save Button");

        // Click the search button
        WebElement searchButton = driver.findElement(By.id("search-button"));
        searchButton.click();

        // Verify the search display text changes
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_display")));
        assertEquals("Scenario found!", searchDisplay.getText(), "Search display text should confirm the scenario is found.");

        // Verify the confirm button is enabled
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        assertTrue(confirmButton.isEnabled(), "Confirm button should be enabled after scenario is found.");
    }


    @Test
    void testStartSimulation() {
        // Open the popup
        WebElement startButton = driver.findElement(By.id("start-button"));
        startButton.click();

        // Enter a scenario name
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Save Button");

        // Click the confirm button to start the simulation
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Wait for the simulation to update
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement currentProduction = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("current-production")));
        assertNotEquals("1", currentProduction.getText(), "Production value should update after simulation starts.");
    }

    @Test
    void testProgressBarsUpdate() {
        // Open the popup and start the simulation
        WebElement startButton = driver.findElement(By.id("start-button"));
        startButton.click();

        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Scenario");

        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Wait for the satisfaction bar to update
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement satisfactionBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("satisfactionBar")));

        String width = satisfactionBar.getCssValue("width");
        assertTrue(Double.parseDouble(width.replace("px", "")) > 0, "Satisfaction bar width should increase during the simulation.");
    }

    @Test
    void testStopSimulation() throws InterruptedException {
        // Step 1: Start the simulation
        WebElement startButton = driver.findElement(By.id("start-button"));
        startButton.click();

        // Enter a valid scenario name in the popup
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Save Button");

        // Click the search button
        WebElement searchButton = driver.findElement(By.id("search-button"));
        searchButton.click();

        // Verify the search display text changes
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_display")));
        assertEquals("Scenario found!", searchDisplay.getText(), "Search display text should confirm the scenario is found.");

        // Click the confirm button to start the simulation
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Wait for the simulation to start updating
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement currentProduction = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("current-production")));

        // Capture the production value after the simulation starts
        String productionValueBeforeStop = currentProduction.getText();
        assertNotEquals("1", productionValueBeforeStop, "Production value should update after simulation starts.");

        // Wait for a few seconds to ensure the simulation stops
        Thread.sleep(9000);

        // Step 2: Stop the simulation
        startButton.click(); // Clicking the "Stop" button
    }
}
