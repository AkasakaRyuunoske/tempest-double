package tempest_double.assets;

import java.time.LocalDateTime;

abstract public class Asset {
    protected final String name;
    protected final String type;
    protected final String role;
    protected double efficiency;

    // Must be implemented by every asset.
    public abstract double simulate(Object input);

    public Asset(String name, String type, String role, double efficiency) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.efficiency = efficiency;
    }
}
