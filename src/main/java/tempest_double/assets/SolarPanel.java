package tempest_double.assets;

import java.time.LocalDateTime;

public class SolarPanel extends Asset {
    public SolarPanel(String name, String type, String role, double efficiency) {
        super(name, type, role, efficiency);
    }

    @Override
    public double simulate(LocalDateTime timestamp) {
        return 0;
    }
}
