package tempest_double.assets;

import java.time.LocalDateTime;

public class WindTurbine extends Asset {
    private double nominalPower;
    private double bladeLength; // in meters
    private final double cutInWindSpeed; // minimum wind speed to generate power
    private final double cutOutWindSpeed; // maximum wind speed for safe operation
    // Simplified power calculation using blade area and wind speed cubed
    private double sweepArea;
    private final double airDensity = 1.225; // kg/m³
    private double windSpeed = 5.0; // m/s base wind speed
    public WindTurbine(String name, String type, String role, Double efficiency, double bladeLength,
                       double cutInWindSpeed, double cutOutWindSpeed, double nominalPower) {
        super(name, type, role, efficiency);
        this.bladeLength = bladeLength;
        this.cutInWindSpeed = cutInWindSpeed;
        this.cutOutWindSpeed = cutOutWindSpeed;
        this.nominalPower = nominalPower;

        sweepArea = Math.PI * bladeLength * bladeLength;
    }

    // Calculate wind speed based on time of day and some simulated variability
    public double calculateWindSpeed(LocalDateTime timestamp) {
        double timeVariation = Math.sin(2 * Math.PI * timestamp.getHour() / 24.0);
        return windSpeed + (timeVariation * 3.0);
    }

    @Override
    public double simulate(Object input) {
        double windSpeed = calculateWindSpeed((LocalDateTime) input);

        // Power curve calculation
//        if (windSpeed < cutInWindSpeed || windSpeed > cutOutWindSpeed) return 0;

        double generatedPower = 0.5 * airDensity * sweepArea * efficiency * Math.pow(windSpeed, 3) * 0.593; // 0.593 is Betz limit
        // return generated power that is not bigger than nominal power;
        return Math.min(generatedPower, nominalPower);
    }
}
