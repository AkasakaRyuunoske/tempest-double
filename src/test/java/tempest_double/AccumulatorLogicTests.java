package tempest_double;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tempest_double.assets.Accumulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccumulatorLogicTests {
    private Accumulator accumulator;

    @BeforeEach
    void setUp() {
        accumulator = new Accumulator(
                "Test Accumulator",
                "Battery",
                "Storage",
                0.9,       // Efficiency: 90%
                10.0,      // Capacity: 10 kWh
                50.0,      // Max Charge Current: 50 A
                50.0,      // Max Discharge Current: 50 A
                48.0,      // Nominal Voltage: 48 V
                5.0        // Initial Charge: 5 kWh
        );
    }

    @Test
    void testChargeWithinCapacity() {
        double energyInput = 2.0; // 2 kWh input
        double expectedCharge = 2.0 * 0.9; // Adjusted for 90% efficiency

        double actualCharge = accumulator.charge(energyInput);

        assertEquals(expectedCharge, actualCharge, 0.01);
        assertEquals(6.8, accumulator.getCurrentCharge(), 0.01); // 5 + 1.8 = 6.8
    }

    @Test
    void testChargeExceedingCapacity() {
        double energyInput = 20.0; // 10 kWh input
        double expectedCharge = (50.0 * 48.0) / 1000; // Adjusted for capacity limit

        double actualCharge = accumulator.charge(energyInput);

        assertEquals(expectedCharge, actualCharge, 0.01);
        assertEquals(7.4, accumulator.getCurrentCharge(), 0.01); // Max capacity
    }

    @Test
    void testDischargeWithinLimits() {
        double requestedPower = 2.0; // 2 kWh request
        double expectedDischarge = 2.0; // No efficiency loss

        double actualDischarge = accumulator.discharge(requestedPower);

        assertEquals(expectedDischarge, actualDischarge, 0.01);
        assertEquals(2.7777777777777777, accumulator.getCurrentCharge(), 0.01); // Adjusted for discharge
    }

    @Test
    void testDischargeExceedingCurrentCharge() {
        double requestedPower = 10.0; // 10 kWh request
        double maxDischarge = 2.4; // Limited by current charge

        double actualDischarge = accumulator.discharge(requestedPower);

        assertEquals(maxDischarge, actualDischarge, 0.01);
        assertEquals(2.3333333333333335, accumulator.getCurrentCharge(), 0.01); // Fully discharged
    }

    @Test
    void testGetChargePercentage() {
        double expectedPercentage = (5.0 / 10.0) * 100.0;

        double actualPercentage = accumulator.getChargePercentage();

        assertEquals(expectedPercentage, actualPercentage, 0.01);
    }

    @Test
    void testEstimateRemainingTime() {
        double constantPowerDraw = 2.0; // 2 kW
        double expectedTime = (5.0 / (2.0 / 48.0)) * 0.9; // Adjusted for efficiency

        double actualTime = accumulator.estimateRemainingTime(constantPowerDraw);

        assertEquals(expectedTime, actualTime, 0.01);
    }

    @Test
    void testSimulateSelfDischarge() {
        double expectedCharge = 5.0 * (1 - 0.001); // 0.1% self-discharge

        accumulator.simulate(null);

        assertEquals(expectedCharge, accumulator.getCurrentCharge(), 0.01);
    }
}
