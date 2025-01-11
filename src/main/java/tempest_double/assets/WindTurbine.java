package tempest_double.assets;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Log4j2
public class WindTurbine extends Asset {
    private final double nominalPower;
    private final double bladeLength; // in meters
    private final double cutInWindSpeed; // minimum wind speed to generate power
    private final double cutOutWindSpeed; // maximum wind speed for safe operation
    private final double airDensity = 1.225; // Air density at sea level (kg/m³)
    private double windSpeed = 6.0;
    private double generatedPower = 0.0;

    public WindTurbine(String name, String type, String role, Double efficiency, double bladeLength,
                       double cutInWindSpeed, double cutOutWindSpeed, double nominalPower) {
        super(name, type, role, efficiency);
        this.bladeLength = bladeLength;
        this.cutInWindSpeed = cutInWindSpeed;
        this.cutOutWindSpeed = cutOutWindSpeed;
        this.nominalPower = nominalPower;
    }

    // Calculate wind speed based on time of day and some simulated variability
    public double calculateWindSpeed(LocalDateTime timestamp) {
        // Base wind speed (e.g., prevailing wind conditions in m/s)
        double baseWindSpeed = 5.0;

        // Diurnal variation: sinusoidal pattern peaking at noon (12:00)
        double diurnalVariation = Math.sin(2 * Math.PI * (timestamp.getHour() + timestamp.getMinute() / 60.0) / 24.0) * 3.0;

        // Random gusts: small, rapidly changing variations
        double gusts = (Math.random() - 1) * 2.0;

        // Smooth response to changes (first-order system)
        double responseFactor = 0.4; // Adjust for smoother or quicker changes
        double targetWindSpeed = baseWindSpeed + diurnalVariation + gusts;
        windSpeed += (targetWindSpeed - windSpeed) * responseFactor;

        return windSpeed;
    }

    @Override
    public double simulate(Object input) {
        double windSpeed = calculateWindSpeed((LocalDateTime) input);

        // Power output variables
        double responseFactor = 0.1; // First-order response factor (adjust for responsiveness)

        // Determine power output based on wind speed
        if (windSpeed < cutInWindSpeed) {
            // Below cut-in speed: no power
            generatedPower = 0.0;
        } else if (windSpeed >= cutInWindSpeed && windSpeed < cutOutWindSpeed) {
            // Between cut-in and cut-out speed: power grows smoothly
            double powerPotential = Math.min(nominalPower, calculatePowerFromWind(windSpeed));
            generatedPower += (powerPotential - generatedPower) * responseFactor;
        } else {
            // Above cut-out speed: shut down for safety
            generatedPower = 0.0;
        }

        return generatedPower;
    }

    private double calculatePowerFromWind(double windSpeed) {
        double sweepArea = Math.PI * bladeLength * bladeLength; // Blade swept area
        double airDensity = 1.225; // Air density at sea level (kg/m³)

        // Power formula: 0.5 * airDensity * sweepArea * windSpeed³ * Betz limit * efficiency
        return 0.5 * airDensity * sweepArea * Math.pow(windSpeed, 3) * 0.593 * efficiency;
    }
}
