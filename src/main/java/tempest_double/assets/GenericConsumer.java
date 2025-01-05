package tempest_double.assets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class GenericConsumer extends Asset{
    private final double maxConsumption;  // Maximum power consumption in W
    private final double minConsumption;  // Minimum power consumption in W
    private final double timeConstant;    // Time constant or TAU in seconds
    private double currentConsumption;    // Current power consumption in W
    private double targetConsumption;     // Target consumption based on available power

    public GenericConsumer(String name, String type, String role, double efficiency, double minConsumption, double maxConsumption, double timeConstant) {
        super(name, type, role, efficiency);
        this.minConsumption = minConsumption;
        this.maxConsumption = maxConsumption;
        this.timeConstant = timeConstant;
        this.currentConsumption = 0;
        this.targetConsumption = 0;
    }

    public double getCurrentConsumption() {
        return currentConsumption;
    }

    @Override
    public double simulate(Object input) {
        double availablePower = (double) input;

        // Determine target consumption based on available power
        if (availablePower <= 0) {
            targetConsumption = 0;
        } else {
            targetConsumption = Math.min(maxConsumption, availablePower);
            targetConsumption = Math.max(targetConsumption, minConsumption);
        }

        // First-order response formula
        // newValue = currentValue + (targetValue - currentValue) * (1 - e^(-dt/τ))
        // For 1-second steps, dt = 1
        double stepResponse = 1 - Math.exp(-1.0 / timeConstant);
        currentConsumption += (Math.max(targetConsumption, maxConsumption) - currentConsumption) * stepResponse;

        return currentConsumption;
    }
}