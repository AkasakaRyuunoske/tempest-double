package tempest_double;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import tempest_double.assets.WindTurbine;
import tempest_double.entity.Asset.Asset;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class WindTurbineLogicTests {
    private WindTurbine windTurbine;

    @BeforeEach
    void setUp() {
        windTurbine = new WindTurbine(
                "Test Turbine",
                "Wind",
                "Generator",
                0.9,          // Efficiency: 90%
                50.0,         // Blade length: 50 meters
                3.0,          // Cut-in wind speed: 3 m/s
                25.0,         // Cut-out wind speed: 25 m/s
                2000.0        // Nominal power: 2000 W
        );
    }

    @Test
    void testCalculateWindSpeed() {
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 21, 12, 0); // Noon

        double windSpeed = windTurbine.calculateWindSpeed(timestamp);

        assertTrue(windSpeed > 0, "Wind speed should be positive.");
        assertNotEquals(6.0, windSpeed, "Wind speed should change dynamically.");
    }

    @Test
    void testSimulateBelowCutInSpeed() {
        // Simulate early morning where wind speed is typically low
        LocalDateTime earlyMorning = LocalDateTime.of(2023, 6, 21, 4, 0);

        // Create a spy from the real object
        WindTurbine spyTurbine = spy(windTurbine);

        // Mock `calculateWindSpeed` to return a fixed value
        doReturn(2.0).when(spyTurbine).calculateWindSpeed(any(LocalDateTime.class));

        assertEquals(0.0, spyTurbine.simulate(earlyMorning),
                "Generated power should be 0 below cut-in wind speed.");
    }

    @Test
    void testSimulateAtNominalSpeed() {
        // Simulate midday where wind speed reaches nominal levels
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0);

        double generatedPower = windTurbine.simulate(noon);

        assertTrue(generatedPower > 0 && generatedPower <= 2000.0,
                "Generated power should be positive and not exceed nominal power.");
    }

    @Test
    void testSimulateAboveCutOutSpeed() {
        // Simulate extreme wind conditions (e.g., storm)
        LocalDateTime stormTime = LocalDateTime.of(2023, 6, 21, 14, 0);

        // Create a spy from the real object
        WindTurbine spyTurbine = spy(windTurbine);

        // Mock `calculateWindSpeed` to return a fixed value
        doReturn(50.0).when(spyTurbine).calculateWindSpeed(any(LocalDateTime.class));

        double generatedPower = spyTurbine.simulate(stormTime);

        assertEquals(0.0, generatedPower,
                "Generated power should be 0 above cut-out wind speed.");
    }

//    @Test
//    void testCalculatePowerFromWind() {
//        double windSpeed = 10.0; // Typical operational speed
//
//        double power = windTurbine.calculatePowerFromWind(windSpeed);
//
//        assertTrue(power > 0, "Calculated power should be positive.");
//        assertTrue(power <= 2000.0, "Calculated power should not exceed nominal power.");
//    }

    @Test
    void testSimulateDynamicResponse() {
        LocalDateTime noon = LocalDateTime.of(2023, 6, 21, 12, 0);

        double firstRun = windTurbine.simulate(noon);
        assertTrue(firstRun > 0, "First run should generate some power.");

        // Simulate several steps to observe dynamic behavior
        double previousPower = firstRun;
        for (int i = 0; i < 10; i++) {
            double currentPower = windTurbine.simulate(noon);
            assertTrue(currentPower >= previousPower,
                    "Power should gradually increase towards the target.");
            previousPower = currentPower;
        }
    }
}
