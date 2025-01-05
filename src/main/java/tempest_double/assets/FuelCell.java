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
            return 0;
        }

        // Generate power at rated capacity or remaining fuel capacity
        double potentialPower = Math.min(nominalPower * efficiency, currentFuel * 3000); // 3000 Conversion factor

        // Consume fuel
        double fuelConsumed = calculateFuelConsumption(potentialPower);
        currentFuel -= fuelConsumed / 1000;

        return potentialPower;
    }
}
