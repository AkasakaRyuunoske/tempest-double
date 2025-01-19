package tempest_double;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tempest_double.assets.GenericConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class GenericConsumerLogicTests {
    private GenericConsumer genericConsumer;

    @BeforeEach
    void setUp() {
        genericConsumer = new GenericConsumer(
                "Test Consumer",
                "Consumer",
                "Load",
                0.9,         // Efficiency: 90%
                100.0,       // Min Consumption: 100 W
                1000.0,      // Max Consumption: 1000 W
                6.0          // Time Constant: 5 seconds
        );
    }

    @Test
    void testInitialCurrentConsumption() {
        assertEquals(0.0, genericConsumer.getCurrentConsumption(),
                "Initial current consumption should be 0.");
    }

    @Test
    void testSimulateWithNoAvailablePower() {
        double simulatedConsumption = genericConsumer.simulate(0.0);

        assertEquals(0.0, simulatedConsumption,
                "Current consumption should be 0 when no power is available.");
    }

    @Test
    void testSimulateWithinMinAndMaxConsumption() {
        double availablePower = 500.0; // Within min and max consumption range
        double simulatedConsumption = genericConsumer.simulate(availablePower);

        assertTrue(simulatedConsumption >= 100.0 && simulatedConsumption <= 1000.0,
                "Simulated consumption should respect min and max constraints.");
    }

    @Test
    void testSimulateBelowMinConsumption() {
        double availablePower = 50.0; // Below min consumption
        double simulatedConsumption = genericConsumer.simulate(availablePower);

        assertEquals(0, simulatedConsumption,
                "Simulated consumption should be clamped to the minimum consumption.");
    }

    @Test
    void testSimulateAboveMaxConsumption() {
        double availablePower = 1500.0; // Above max consumption
        double simulatedConsumption = 0;

        for (int i = 0; i < 60; i++){ // simulate one min
            simulatedConsumption = genericConsumer.simulate(availablePower);
        }

        assertTrue(simulatedConsumption <= 1000 && simulatedConsumption > 999,
                "Simulated consumption should be clamped to the maximum consumption.");
    }

    @Test
    void testSimulateDynamicResponse() {
        double availablePower = 800.0;
        genericConsumer.simulate(availablePower); // First step
        double firstStepConsumption = genericConsumer.getCurrentConsumption();

        assertTrue(firstStepConsumption > 0 && firstStepConsumption < 800.0,
                "Current consumption should approach target consumption dynamically.");

        for (int i = 0; i < 12; i++) {
            genericConsumer.simulate(availablePower);
        }
        double steadyStateConsumption = genericConsumer.getCurrentConsumption();

        assertEquals(800.0, steadyStateConsumption, 10.0,
                "Current consumption should approach the target consumption over time.");
    }

    @Test
    void testSimulateZeroPowerAfterDynamicResponse() {
        // Simulate with available power to set a non-zero target
        genericConsumer.simulate(800.0);
        double intermediateConsumption = genericConsumer.getCurrentConsumption();

        // Simulate with no available power
        double simulatedConsumption = genericConsumer.simulate(0.0);

        assertEquals(0.0, simulatedConsumption,
                "Simulated consumption should return to 0 when no power is available.");
        assertTrue(intermediateConsumption > 0,
                "Intermediate consumption should reflect prior available power.");
    }
}
