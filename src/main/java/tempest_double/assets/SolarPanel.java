package tempest_double.assets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
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
    public double simulate(Object input) {
        LocalDateTime timestamp = (LocalDateTime) input;

        double dayOfYear = timestamp.getDayOfYear();
        double hourOfDay = timestamp.getHour() + timestamp.getMinute() / 60.0;

        double declination = 23.45 * Math.sin(Math.toRadians(360.0 / 365 * (dayOfYear - 81)));
        double hourAngle = 15 * (hourOfDay - 12);

        double zenithAngle = Math.acos(
                Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(declination)) +
                        Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(declination)) * Math.cos(Math.toRadians(hourAngle))
        );

        if (zenithAngle < 0 || zenithAngle > Math.PI / 2) {
            return 0; // No solar radiation at night or invalid zenith angle
        }

        double incidenceAngle = Math.acos(
                Math.cos(zenithAngle) * Math.cos(Math.toRadians(tiltAngle)) +
                        Math.sin(zenithAngle) * Math.sin(Math.toRadians(tiltAngle)) * Math.cos(Math.toRadians(hourAngle))
        );

        double cosZenith = Math.cos(zenithAngle);
        if (cosZenith <= 0) {
            return 0; // Avoid invalid air mass calculation for high zenith angles
        }

        double airMassBase = Math.max(6.07995 + zenithAngle, 1e-6); // Prevent negative/zero values
        double airMass = 1 / (cosZenith + 0.50572 * Math.pow(airMassBase, -1.6364));

        double extraTerrestrialRadiation = 1367;
        double atmosphericTransmission = 0.7 * Math.pow(airMass, -0.678);

        double directRadiation = extraTerrestrialRadiation *
                Math.max(0, Math.cos(zenithAngle)) * atmosphericTransmission;

        double diffuseRadiation = extraTerrestrialRadiation *
                (0.3 + 0.7 * Math.pow(atmosphericTransmission, 1.5)) *
                (1 - Math.max(0, Math.cos(zenithAngle)));

        double totalRadiation = directRadiation * Math.cos(incidenceAngle) +
                diffuseRadiation * (1 + Math.cos(Math.toRadians(tiltAngle))) / 2;

        double cellTemperature = 25 + (totalRadiation / solarRadiation) * 20;
        double temperatureCoefficient = -0.005;

        double powerOutput = totalRadiation * panelArea * efficiency * (1 + temperatureCoefficient * (cellTemperature - 25));

        return Math.min(nominalPower, Math.max(0, powerOutput));
    }
}
