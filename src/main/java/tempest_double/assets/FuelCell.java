package tempest_double.assets;

import java.time.LocalDateTime;

public class FuelCell extends Asset {
    private final double fuelCapacity;   // in kg
    private double currentFuel;    // remaining fuel
    private final int nominalPower;    // maximum power output

    private final double specificConsumption = 0.3; // kg of fuel per kWh

    public FuelCell(String name, String type, String role, double efficiency, double fuelCapacity, int nominalPower) {
        super(name, type, role, efficiency);
        this.fuelCapacity = fuelCapacity;
        this.nominalPower = nominalPower;
    }


    // Calculate fuel consumption
    public double calculateFuelConsumption(double powerOutput) {
        // Simplified fuel consumption model
        // Assume specific fuel consumption rate
        return powerOutput * specificConsumption / 1000;
    }

    // Remaining fuel percentage
    public double getFuelPercentage() {
        return (currentFuel / fuelCapacity) * 100;
    }

    @Override
    public double simulate(LocalDateTime timestamp) {
        if (currentFuel <= 0) {
            return 0;
        }

        // Generate power at rated capacity or remaining fuel capacity
        double potentialPower = Math.min(nominalPower * efficiency, currentFuel * 3000); // 3000 Conversion factor

        // Consume fuel
        double fuelConsumed = calculateFuelConsumption(potentialPower);
        currentFuel -= fuelConsumed;

        return potentialPower;
    }
}
