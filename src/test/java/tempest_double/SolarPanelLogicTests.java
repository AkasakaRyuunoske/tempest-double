package tempest_double;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tempest_double.assets.SolarPanel;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SolarPanelLogicTests {
    private SolarPanel solarPanel;

    @BeforeEach
    void setUp() {
        solarPanel = new SolarPanel(
                "Test Solar Panel",
                "Solar",
                "Generator",
                0.2,       // Efficiency: 20%
                10.0,      // Panel Area: 10 m²
                35.0,      // Latitude: 35 degrees
                1000.0,    // Nominal Power: 1000 W
                30.0       // Tilt Angle: 30 degrees
        );
    }

    @Test
    void testCalculateSolarAngle() {
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0); // Summer solstice at noon
        double expectedAngle = 78;

        double actualAngle = solarPanel.calculateSolarAngle(noon);

        assertTrue(actualAngle > expectedAngle - 10 && actualAngle < expectedAngle + 10,
                "Solar angle should approximate zenith angle minus latitude.");
    }

    @Test
    void testCalculateSolarAngleAtNight() {
        LocalDateTime midnight = LocalDateTime.of(2023, 6, 21, 0, 0); // Midnight
        double actualAngle = solarPanel.calculateSolarAngle(midnight);

        assertTrue(actualAngle < 0, "Solar angle should be negative or close to zero at night.");
    }

    @Test
    void testSimulateDaylight() {
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0); // Summer solstice at noon
        double simulatedPower = solarPanel.simulate(noon);

        assertTrue(simulatedPower > 0 && simulatedPower <= solarPanel.getNominalPower(),
                "Simulated power should be greater than 0 and within nominal power during daylight.");
    }

    @Test
    void testSimulateNight() {
        LocalDateTime midnight = LocalDateTime.of(2023, 6, 21, 0, 0); // Midnight
        double simulatedPower = solarPanel.simulate(midnight);

        assertEquals(0.0, simulatedPower, "Simulated power should be 0 at night.");
    }

    @Test
    void testSimulateCloudyConditions() {
        // Mocking cloudy conditions by altering efficiency dynamically (hypothetically)
        SolarPanel cloudyPanel = new SolarPanel(
                "Cloudy Panel",
                "Solar",
                "Generator",
                0.1,       // Reduced efficiency: 10%
                10.0,      // Panel Area: 10 m²
                35.0,      // Latitude: 35 degrees
                1000.0,    // Nominal Power: 1000 W
                30.0       // Tilt Angle: 30 degrees
        );
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0); // Summer solstice at noon

        double simulatedPower = cloudyPanel.simulate(noon);

        assertTrue(simulatedPower > 0 && simulatedPower <= cloudyPanel.getNominalPower(),
                "Simulated power should be within nominal power under cloudy conditions.");
    }

    @Test
    void testSimulateExtremeAngles() {
        LocalDateTime sunrise = LocalDateTime.of(2023, 6, 21, 6, 0); // Sunrise
        LocalDateTime sunset = LocalDateTime.of(2023, 6, 21, 18, 0); // Sunset

        double sunrisePower = solarPanel.simulate(sunrise);
        double sunsetPower = solarPanel.simulate(sunset);

        assertTrue(sunrisePower > 0, "Simulated power should be greater than 0 at sunrise.");
        assertTrue(sunsetPower > 0, "Simulated power should be greater than 0 at sunset.");
    }

    @Test
    void testSimulateMaximumPower() {
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0); // Ideal conditions
        double simulatedPower = solarPanel.simulate(noon);

        assertTrue(simulatedPower <= solarPanel.getNominalPower(),
                "Simulated power should not exceed the nominal power of the solar panel.");
    }

    @Test
    void testZenithAngleGreaterThanPiOverTwo() {
        // Simulate a timestamp at midnight to produce a zenith angle > π/2
        LocalDateTime midnight = LocalDateTime.of(2023, 6, 21, 0, 0);

        double simulatedPower = solarPanel.simulate(midnight);

        assertEquals(0.0, simulatedPower, "Power should be 0 when zenith angle > π/2 (nighttime).");
    }
}
