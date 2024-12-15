package tempest_double.assets;

import java.time.LocalDateTime;

public class Accumulator extends Asset {
    private final double capacity;       // in kWh
    private final double maxChargeCurrent;   // maximum charge current in A
    private final double maxDischargeCurrent; // maximum discharge current in A
    private final double nominalVoltage;     // nominal battery voltage in V
    private double currentCharge;  // current stored energy in kWh

    public Accumulator(String name, String type, String role, double efficiency, double capacity, double maxChargeCurrent, double maxDischargeCurrent, double nominalVoltage, double currentCharge) {
        super(name, type, role, efficiency);
        this.capacity = capacity;
        this.maxChargeCurrent = maxChargeCurrent;
        this.maxDischargeCurrent = maxDischargeCurrent;
        this.nominalVoltage = nominalVoltage;
        this.currentCharge = currentCharge;
    }

    // Charge the accumulator with advanced current limitation
    public double charge(double energyInput, LocalDateTime timestamp) {
        // Calculate maximum possible charge based on max charge current
        double maxChargeEnergy = calculateMaxChargeEnergy();

        // Limit input energy to max charge capacity and device efficiency
        double actualChargeEnergy = Math.min(
                energyInput * efficiency,  // Efficiency reduction
                maxChargeEnergy
        );

        // Prevent overcharging
        currentCharge = Math.min(
                currentCharge + actualChargeEnergy,
                capacity
        );

        return actualChargeEnergy;
    }

    // Discharge the accumulator with advanced current limitation
    public double discharge(double requestedPower, LocalDateTime timestamp) {
        // Calculate maximum possible discharge based on max discharge current
        double maxDischargeEnergy = calculateMaxDischargeEnergy();

        // Limit requested power to available energy and max discharge
        double actualDischargePower = Math.min(
                requestedPower,
                maxDischargeEnergy
        );

        // Adjust for efficiency (discharge losses)
        double netDischargePower = actualDischargePower / efficiency;

        // Reduce current charge
        currentCharge = Math.max(
                currentCharge - (netDischargePower / nominalVoltage),
                0
        );

        return actualDischargePower;
    }

    // Calculate maximum charge energy based on max charge current
    private double calculateMaxChargeEnergy() {
        // Convert max charge current to energy (kWh)
        // P = I * V
        return (maxChargeCurrent * nominalVoltage) / 1000.0;
    }

    // Calculate maximum discharge energy based on max discharge current
    private double calculateMaxDischargeEnergy() {
        // Limit discharge to either requested power or max discharge capacity
        double maxDischargeEnergy = (maxDischargeCurrent * nominalVoltage) / 1000.0;

        // Also limit by current stored energy
        return Math.min(
                maxDischargeEnergy,
                currentCharge * nominalVoltage
        );
    }

    // Health and state of charge methods
    public double getChargePercentage() {
        return (currentCharge / capacity) * 100;
    }

    // Estimate remaining time at current discharge rate
    public double estimateRemainingTime(double constantPowerDraw) {
        if (constantPowerDraw <= 0) return Double.POSITIVE_INFINITY;

        double remainingEnergy = currentCharge;
        return (remainingEnergy / (constantPowerDraw / nominalVoltage)) * efficiency;
    }

    // State of health estimation (simplified)
    public double getStateOfHealth() {
        // Simplified state of health calculation
        // In a real system, this would track cycle count, depth of discharge, etc.
        return 100.0; // Placeholder for full health
    }

    @Override
    public double simulate(LocalDateTime timestamp) {
        // Accumulator doesn't generate power, it stores and provides power
        // This method could be used for self-discharge or internal losses simulation
        double selfDischargeRate = 0.001; // 0.1% per hour
        currentCharge = Math.max(0, currentCharge * (1 - selfDischargeRate));

        return 0;
    }
}
