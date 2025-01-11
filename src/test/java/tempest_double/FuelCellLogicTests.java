package tempest_double;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tempest_double.assets.FuelCell;

import static org.junit.jupiter.api.Assertions.*;

public class FuelCellLogicTests {
    private FuelCell fuelCell;

    @BeforeEach
    void setUp() {
        // Initialize FuelCell with sample parameters
        fuelCell = new FuelCell(
                "Test Fuel Cell",
                "Fuel",
                "Generator",
                0.9,            // Efficiency: 90%
                10.0,           // Fuel Capacity: 10 l
                5.0,            // Nominal Power: 5 kW
                10.0            // Current Fuel: 10 l
        );
    }

    @Test
    void testInitialFuelPercentage() {
        double fuelPercentage = fuelCell.getFuelPercentage();
        assertEquals(100.0, fuelPercentage, "Fuel percentage should be 100% initially.");
    }

    @Test
    void testCalculateFuelConsumption() {
        double powerOutput = 2.5; // kW
        double expectedConsumption = powerOutput * 0.3 / 1000; // Specific consumption formula

        double actualConsumption = fuelCell.calculateFuelConsumption(powerOutput);
        assertEquals(expectedConsumption, actualConsumption, 0.0001, "Fuel consumption should match the formula.");
    }

    @Test
    void testSimulateWithSufficientFuel() {
        double powerOutput = fuelCell.simulate(null); // Simulate one step

        assertTrue(powerOutput > 0, "Generated power should be greater than 0 when fuel is available.");
        assertTrue(powerOutput <= 5.0, "Generated power should not exceed nominal power.");
    }

    @Test
    void testSimulateWithGradualPowerIncrease() {
        // Simulate multiple steps to observe ramp-up behavior
        double previousPower = 0.0;
        for (int i = 0; i < 10; i++) {
            double currentPower = fuelCell.simulate(null);
            assertTrue(currentPower >= previousPower, "Power output should gradually increase.");
            assertTrue(currentPower <= 5.0, "Power should not exceed nominal power.");
            previousPower = currentPower;
        }
    }

    @Test
    void testSimulateWithFuelDepletion() {
        // Simulate until fuel is depleted
        while (fuelCell.getFuelPercentage() > 0) {
            fuelCell.simulate(null);
        }

        double powerOutput = fuelCell.simulate(null);
        assertEquals(0.0, powerOutput, "Power output should be 0 when fuel is depleted.");
        assertEquals(0.0, fuelCell.getFuelPercentage(), "Fuel percentage should be 0 when fuel is depleted.");
    }

    @Test
    void testSimulateWithLowFuel() {
        // Reduce fuel to a small amount
        fuelCell = new FuelCell(
                "Low Fuel Test",
                "Fuel",
                "Generator",
                0.9,
                10.0,
                5.0,
                0.1 // Current fuel: 0.1 l
        );

        double powerOutput = fuelCell.simulate(null);
        assertTrue(powerOutput > 0, "Power output should be positive with low fuel.");
        assertTrue(powerOutput < 5.0, "Power output should be limited by the remaining fuel.");
    }

    @Test
    void testSimulatePowerShutdown() {
        // Deplete fuel completely
        fuelCell = new FuelCell(
                "No Fuel Test",
                "Fuel",
                "Generator",
                0.9,
                10.0,
                5.0,
                0.0 // Current fuel: 0 kg
        );

        double powerOutput = fuelCell.simulate(null);
        assertEquals(0.0, powerOutput, "Power output should be 0 with no fuel.");
    }

    @Test
    void testFuelPercentageAfterConsumption() {
        double initialFuel = fuelCell.getFuelPercentage();
        fuelCell.simulate(null); // Simulate one step
        double finalFuel = fuelCell.getFuelPercentage();

        assertTrue(finalFuel < initialFuel, "Fuel percentage should decrease after consumption.");
    }
}
