package tempest_double.assets;

import java.time.LocalDateTime;

public class SolarPanel extends Asset {
    private final double panelArea;      // in square meters
    private final double latitude;       // geographic latitude
    private final double nominalPower;      // maximum power output under ideal conditions
    private final double tiltAngle;      // fixed tilt angle of the panel
    private final double solarRadiation = 1000; // W/m² (standard test condition)

    public SolarPanel(String name, String type, String role, double efficiency, double panelArea, double latitude, double nominalPower, double tiltAngle) {
        super(name, type, role, efficiency);
        this.panelArea = panelArea;
        this.latitude = latitude;
        this.nominalPower = nominalPower;
        this.tiltAngle = tiltAngle;
    }

    // Calculate solar panel angle to the sun
    public double calculateSolarAngle(LocalDateTime timestamp) {
        // Solar angle calculation based on time and latitude
        double dayOfYear = timestamp.getDayOfYear();
        double hourOfDay = timestamp.getHour() + timestamp.getMinute() / 60.0;

        // Solar declination angle
        double declination = 23.45 * Math.sin(Math.toRadians(360.0/365 * (dayOfYear - 81)));

        // Hour angle (15 degrees per hour)
        double hourAngle = 15 * (hourOfDay - 12);

        // Solar zenith angle calculation
        double zenithAngle = Math.acos(
                Math.sin(Math.toRadians(latitude))
                * Math.sin(Math.toRadians(declination))
                + Math.cos(Math.toRadians(latitude))
                * Math.cos(Math.toRadians(declination))
                * Math.cos(Math.toRadians(hourAngle)
                )
        );

        return 90 - Math.toDegrees(zenithAngle);
    }

    @Override
    public double simulate(LocalDateTime timestamp) {
        // Calculate solar radiation more comprehensively
        double dayOfYear = timestamp.getDayOfYear();
        double hourOfDay = timestamp.getHour() + timestamp.getMinute() / 60.0;

        // Solar declination angle
        double declination = 23.45 * Math.sin(Math.toRadians(360.0/365 * (dayOfYear - 81)));

        // Hour angle (15 degrees per hour)
        double hourAngle = 15 * (hourOfDay - 12);

        // Calculate solar zenith angle
        double zenithAngle = Math.acos(
                Math.sin(Math.toRadians(latitude))
                    * Math.sin(Math.toRadians(declination))
                    + Math.cos(Math.toRadians(latitude))
                    * Math.cos(Math.toRadians(declination))
                    * Math.cos(Math.toRadians(hourAngle))
        );

        // Calculate incidence angle considering panel tilt
        double incidenceAngle = Math.acos(
                Math.cos(zenithAngle)
                    * Math.cos(Math.toRadians(tiltAngle))
                    + Math.sin(zenithAngle)
                    * Math.sin(Math.toRadians(tiltAngle))
                    * Math.cos(Math.toRadians(hourAngle))
        );

        // Air Mass calculation (Kasten and Young model)
        double airMass = 1 / (Math.cos(zenithAngle) + 0.50572 * Math.pow(6.07995 + zenithAngle, -1.6364));

        // Extra-terrestrial radiation
        double extraTerrestrialRadiation = 1367; // Solar constant in W/m²

        // Atmospheric transmission (simplified Ångström formula)
        double atmosphericTransmission = 0.7 * Math.pow(airMass, -0.678);

        // Calculate direct and diffuse solar radiation
        double directRadiation = extraTerrestrialRadiation
                * Math.max(0, Math.cos(zenithAngle))
                * atmosphericTransmission;

        double diffuseRadiation = extraTerrestrialRadiation *
                (0.3 + 0.7 * Math.pow(atmosphericTransmission, 1.5)) *
                (1 - Math.max(0, Math.cos(zenithAngle)));

        // Total solar radiation on tilted surface
        double totalRadiation = directRadiation * Math.cos(incidenceAngle) +
                diffuseRadiation * (1 + Math.cos(Math.toRadians(tiltAngle))) / 2;

        // Account for temperature effects (simplified)
        double cellTemperature = 25 + (totalRadiation / solarRadiation) * 20; // Estimated cell temperature
        double temperatureCoefficient = -0.005; // Typical temperature coefficient for silicon cells

        // Power generation calculation
        double powerOutput = totalRadiation * panelArea * efficiency * (1 + temperatureCoefficient * (cellTemperature - 25));

        // Ensure non-negative power output
        return Math.min(nominalPower, Math.max(0, powerOutput));
    }
}
