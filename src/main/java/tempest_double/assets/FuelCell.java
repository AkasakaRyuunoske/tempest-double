package tempest_double.assets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FuelCell extends Asset {
    private final double fuelCapacity;   // in kg
    private double currentFuel;    // remaining fuel
    private final double nominalPower;    // maximum power output
    private final double specificConsumption = 0.3; // kg of fuel per kWh
    private double currentPower = 0.0;    // current power output

    public FuelCell(String name, String type, String role, double efficiency, double fuelCapacity, double nominalPower, double currentFuel) {
        super(name, type, role, efficiency);
        this.fuelCapacity = fuelCapacity;
        this.nominalPower = nominalPower;
        this.currentFuel = currentFuel;
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
    public double simulate(Object input) {
        if (currentFuel <= 0) {
            currentPower = 0;
            return 0;
        }

        // Calculate potential power output
        double potentialPower = Math.min(nominalPower, currentFuel * 1000 / specificConsumption); // Max power limited by fuel

        // First-order response to adjust `currentPower` towards `potentialPower`
        double responseFactor = 0.2; // Adjust for smoother or quicker response
        currentPower += (potentialPower - currentPower) * responseFactor;

        // Calculate fuel consumption based on `currentPower`
        double fuelConsumed = calculateFuelConsumption(currentPower) / 5;
        currentFuel = Math.max(0, currentFuel - fuelConsumed);

        return currentPower;
    }
}

