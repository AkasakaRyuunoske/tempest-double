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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsAndTopologiesPageTests {
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
        driver.get("http://localhost:8080/assets");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testFooterButtons() {
        // Verify the "Add" button is clickable
        WebElement addButton = driver.findElement(By.id("add"));
        assertTrue(addButton.isEnabled(), "'Add' button should be enabled.");

        // Verify the "Delete" button is clickable
        WebElement deleteButton = driver.findElement(By.id("delete"));
        assertTrue(deleteButton.isEnabled(), "'Delete' button should be enabled.");

        // Verify the "Load" button is clickable
        WebElement loadButton = driver.findElement(By.id("load"));
        assertTrue(loadButton.isEnabled(), "'Load' button should be enabled.");

        // Verify the "Save" button is clickable
        WebElement saveButton = driver.findElement(By.id("save"));
        assertTrue(saveButton.isEnabled(), "'Save' button should be enabled.");
    }

    @Test
    void testAddPopup() {
        // Click the "Add" button to open the popup
        WebElement addButton = driver.findElement(By.id("add"));
        addButton.click();

        // Verify the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Add popup should be displayed.");

        // Verify the popup title
        WebElement popupTitle = driver.findElement(By.id("popup-title"));
        assertEquals("Add New Asset", popupTitle.getText(), "Popup title should match.");

        // Verify dynamic content in the popup
        WebElement nameField = driver.findElement(By.id("name"));
        assertNotNull(nameField, "Name field should be present in the popup.");
    }

    @Test
    void testCancelPopup() {
        // Open the popup
        WebElement addButton = driver.findElement(By.id("add"));
        addButton.click();

        // Click the cancel button
        WebElement cancelButton = driver.findElement(By.id("cancel-btn"));
        cancelButton.click();

        // Verify the popup is closed
        WebElement popup = driver.findElement(By.id("popup"));
        assertFalse(popup.isDisplayed(), "Popup should be closed after clicking cancel.");
    }

    @Test
    void testDynamicInputUpdate() {
        // Open the "Add" popup
        WebElement addButton = driver.findElement(By.id("add"));
        addButton.click();

        // Select the "Consumer" radio button
        WebElement consumerRadioButton = driver.findElement(By.id("type-consumer"));
        consumerRadioButton.click();

        // Verify the dynamic input fields for Consumer
        WebElement capacityInput = driver.findElement(By.id("capacity"));
        assertNotNull(capacityInput, "Capacity input should be present for Consumer.");
        assertEquals("Enter capacity", capacityInput.getAttribute("placeholder"), "Placeholder should match.");

        // Select the "Producer" radio button
        WebElement producerRadioButton = driver.findElement(By.id("type-producer"));
        producerRadioButton.click();

        // Verify the dynamic input fields for Producer
        WebElement areaInput = driver.findElement(By.id("area"));
        assertNotNull(areaInput, "Area input should be present for Producer.");
        assertEquals("Enter area", areaInput.getAttribute("placeholder"), "Placeholder should match.");
    }

    @Test
    void testSavePopup() {
        // Open the "Save" popup
        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        // Verify the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Save popup should be displayed.");

        // Fill in the scenario name
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Save Button");

        // Click the confirm button
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Handle the alert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert(); // Switch focus to the alert
        assertEquals("Canvas state saved successfully!", alert.getText(), "Alert text should match.");
        alert.accept(); // Click "OK" to close the alert

        // Verify the popup is closed
        assertFalse(popup.isDisplayed(), "Save popup should be closed after clicking confirm.");
    }

    @Test
    void testSavePopupWithAssets() {
        // Step 1: Add a Generic Consumer Asset
        WebElement addButton = driver.findElement(By.id("add"));
        addButton.click();

        // Verify the popup for adding an asset is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Add Asset popup should be displayed.");

        // Fill in the details for a Generic Consumer
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.sendKeys("Generic Consumer1");

        WebElement roleConsumer = driver.findElement(By.id("type-consumer"));
        roleConsumer.click();

        WebElement typeSelect = driver.findElement(By.id("asset-type-select"));
        new Select(typeSelect).selectByValue("generic_consumer");

        WebElement nominalPowerInput = driver.findElement(By.id("nominal-power"));
        nominalPowerInput.sendKeys("100");

        WebElement minConsumptionInput = driver.findElement(By.id("min-consumption"));
        minConsumptionInput.sendKeys("50");

        WebElement tauInput = driver.findElement(By.id("tau"));
        tauInput.sendKeys("10");

        WebElement confirmAssetButton = driver.findElement(By.id("confirm-btn"));
        confirmAssetButton.click();

        // Step 2: Add a Solar Panel Asset
        addButton.click();

        // Verify the popup for adding an asset is displayed again
        popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Add Asset popup should be displayed.");

        // Fill in the details for a Solar Panel
        nameInput = driver.findElement(By.id("name"));
        nameInput.clear();
        nameInput.sendKeys("Solar Panel1");

        WebElement roleProducer = driver.findElement(By.id("type-producer"));
        roleProducer.click();

        typeSelect = driver.findElement(By.id("asset-type-select"));
        new Select(typeSelect).selectByValue("solar_panel");

        WebElement nominalPowerSolarInput = driver.findElement(By.id("nominal-power"));
        nominalPowerSolarInput.sendKeys("200");

        WebElement areaInput = driver.findElement(By.id("area"));
        areaInput.sendKeys("25");

        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        temperatureInput.sendKeys("40");

        WebElement efficiencyInput = driver.findElement(By.id("efficiency"));
        efficiencyInput.sendKeys("90");

        confirmAssetButton = driver.findElement(By.id("confirm-btn"));
        confirmAssetButton.click();

        // Step 3: Save the Scenario
        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        // Verify the save popup is displayed
        popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Save popup should be displayed.");

        // Fill in the scenario name
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys("Test Save Button");

        // Click the confirm button
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Handle the alert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert(); // Switch focus to the alert
        assertEquals("Canvas state saved successfully!", alert.getText(), "Alert text should match.");
        alert.accept(); // Click "OK" to close the alert

        // Verify the save popup is closed
        assertFalse(popup.isDisplayed(), "Save popup should be closed after clicking confirm.");
    }

    @Test
    void testLoadPopup() {
        // Open the "Load" popup
        WebElement loadButton = driver.findElement(By.id("load"));
        loadButton.click();

        // Verify the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Load popup should be displayed.");

        // Verify the scenario list is populated (assuming the backend is running)
        WebElement scenarioList = driver.findElement(By.id("scenario-list"));
        assertNotNull(scenarioList, "Scenario list should be present.");
        assertTrue(scenarioList.getText().contains("Scenario 1"), "Scenario 1 should be listed."); // Mock or seed data required
    }

    @Test
    void testDeletePopup() {
        // Open the "Delete" popup
        WebElement deleteButton = driver.findElement(By.id("delete"));
        deleteButton.click();

        // Verify the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Delete popup should be displayed.");

        // Verify the popup title
        WebElement popupTitle = driver.findElement(By.id("popup-title"));
        assertEquals("Delete Scenario", popupTitle.getText(), "Popup title should match.");

        // Verify the input field and search button are present
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        assertNotNull(scenarioNameInput, "Scenario name input should be present.");
        WebElement searchButton = driver.findElement(By.id("search-button-load"));
        assertNotNull(searchButton, "Search button should be present.");
    }

    @Test
    void testAddAndDeleteScenario() {
        // Step 1: Add a new scenario using the "Save" popup
        String scenarioName = "ScenarioToDelete";

        // Open the "Save" popup
        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        // Verify the popup is displayed
        WebElement popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Save popup should be displayed.");

        // Fill in the scenario name
        WebElement scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys(scenarioName);

        // Click the confirm button
        WebElement confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Handle the success alert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Canvas state saved successfully!", successAlert.getText(), "Alert text should confirm successful save.");
        successAlert.accept(); // Close the alert

        // Verify the save popup is closed
        assertFalse(popup.isDisplayed(), "Save popup should be closed after saving the scenario.");

        // Step 2: Delete the newly created scenario using the "Delete" popup

        // Open the "Delete" popup
        WebElement deleteButton = driver.findElement(By.id("delete"));
        deleteButton.click();

        // Verify the popup is displayed
        popup = driver.findElement(By.id("popup"));
        assertTrue(popup.isDisplayed(), "Delete popup should be displayed.");

        // Fill in the scenario name
        scenarioNameInput = driver.findElement(By.id("scenario-name"));
        scenarioNameInput.sendKeys(scenarioName);

        // Click the search button to find the scenario
        WebElement searchButton = driver.findElement(By.id("search-button-load"));
        searchButton.click();

        // Verify the scenario is listed
        WebElement scenarioList = driver.findElement(By.id("scenario-list"));
        assertTrue(scenarioList.getText().contains(scenarioName), "Scenario list should contain the newly added scenario.");

        // Click the confirm button to delete the scenario
        confirmButton = driver.findElement(By.id("confirm-btn"));
        confirmButton.click();

        // Handle the success alert for deletion
        Alert deleteAlert = wait.until(ExpectedConditions.alertIsPresent());
        deleteAlert.accept(); // Close the alert

        // Verify the delete popup is closed
        assertFalse(popup.isDisplayed(), "Delete popup should be closed after deleting the scenario.");
    }


    @Test
    void testCancelDeletePopup() {
        // Open the "Delete" popup
        WebElement deleteButton = driver.findElement(By.id("delete"));
        deleteButton.click();

        // Click the cancel button
        WebElement cancelButton = driver.findElement(By.id("cancel-btn"));
        cancelButton.click();

        // Verify the popup is closed
        WebElement popup = driver.findElement(By.id("popup"));
        assertFalse(popup.isDisplayed(), "Delete popup should be closed after clicking cancel.");
    }
}

