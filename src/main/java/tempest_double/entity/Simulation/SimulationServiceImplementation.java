package tempest_double.entity.Simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tempest_double.assets.*;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // in this class it is acceptable to use literals
public class SimulationServiceImplementation implements SimulationService {
    @Autowired
    SimulationRepository simulationRepository;
    @Autowired
    ScenarioRepository scenarioRepository;
    @Autowired
    AssetRepository assetRepository;

    ArrayList<tempest_double.assets.Asset> assets = new ArrayList<>();

    @Override
    public Simulation getSimulationById(int id) {
        return simulationRepository.findById(id);
    }

    @Override
    public List<Simulation> getSimulations() {
        return simulationRepository.findAll();
    }

    @Override
    public ResponseEntity<Map<String, Object>> postSimulation(String scenario_name) {
        Map<String, Object> response = new HashMap<>();

        assets = new ArrayList<>(); // clean from any previous data

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(scenario_name);
            scenario_name = jsonNode.get("name").asText();
        } catch (Exception e) {
            response.put("error", "Error parsing JSON");
            return ResponseEntity.badRequest().body(response);
        }
        Scenario scenario = scenarioRepository.findByName(scenario_name);

        Simulation simulation = new Simulation();
        simulation.setScenario(scenario);
        simulationRepository.save(simulation);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) scenario.getTopology().get("nodes");

        // Simple loop
        int i = 0;
        for (Map<String, Object> node : nodes) {
            String name = (String) node.get("name");
            Asset assetFromDb = assetRepository.findByName(name);

            if (assetFromDb == null) {  // if not found, return error
                response.put("error", "One of the assets is of incorrect type or doesn't exist.");
                return ResponseEntity.badRequest().body(response);
            }


            tempest_double.assets.Asset assetToSimulate;
            double nominalPower;
            double efficiency;
            Map<String, Object> assetInfo;
            String type = assetFromDb.getType();
            switch (type) {
                case "solar_panel" -> {
                    double panelArea = Double.parseDouble(assetFromDb.getConfiguration().get("area").toString());
                    efficiency = Double.parseDouble(assetFromDb.getConfiguration().get("efficiency").toString());
                    nominalPower = Double.parseDouble(assetFromDb.getConfiguration().get("nominal-power").toString());
                    assetToSimulate = new SolarPanel(name, type, "Producer", efficiency, panelArea, 45.0, nominalPower, 0.0);
                    assets.add(assetToSimulate);
                    assetInfo = new HashMap<>();
                    assetInfo.put("name", name);
                    assetInfo.put("type", type);
                    assetInfo.put("role", "Producer");
                    assetInfo.put("nominal_power", nominalPower);
                    response.put("Asset_" + i, assetInfo);
                }
                case "wind_turbine" -> {
                    efficiency = Double.parseDouble(assetFromDb.getConfiguration().get("dissipation-factor").toString());
                    nominalPower = Double.parseDouble(assetFromDb.getConfiguration().get("nominal-power").toString());
                    double bladeLength = Double.parseDouble(assetFromDb.getConfiguration().get("blade-length").toString());
                    assetToSimulate = new WindTurbine(name, type, "Producer", efficiency, bladeLength, 45.0, 45.0, nominalPower);
                    assets.add(assetToSimulate);
                    assetInfo = new HashMap<>();
                    assetInfo.put("name", name);
                    assetInfo.put("type", type);
                    assetInfo.put("role", "Producer");
                    assetInfo.put("nominal_power", nominalPower);
                    response.put("Asset_" + i, assetInfo);
                }
                case "fuel_cell" -> {
                    nominalPower = Double.parseDouble(assetFromDb.getConfiguration().get("nominal-power").toString());
                    double fuelCapacity = Double.parseDouble(assetFromDb.getConfiguration().get("fuel-capacity").toString());
                    double currentFuel = Double.parseDouble(assetFromDb.getConfiguration().get("current-fuel").toString());
                    assetToSimulate = new FuelCell(name, type, "Producer", 0.9, fuelCapacity, nominalPower, currentFuel);
                    assets.add(assetToSimulate);
                    assetInfo = new HashMap<>();
                    assetInfo.put("name", name);
                    assetInfo.put("type", type);
                    assetInfo.put("role", "Producer");
                    assetInfo.put("nominal_power", nominalPower);
                    response.put("Asset_" + i, assetInfo);
                }
//                case "accumulator" -> System.out.println("It's a Accumulator!");
                case "generic_consumer" -> {
                    nominalPower = Double.parseDouble(assetFromDb.getConfiguration().get("nominal-power").toString());
                    double tau = Double.parseDouble(assetFromDb.getConfiguration().get("tau").toString());
                    double minConsumption = Double.parseDouble(assetFromDb.getConfiguration().get("min-consumption").toString());
                    efficiency = 100;
                    assetToSimulate = new GenericConsumer(name, type, "Consumer", efficiency, minConsumption, nominalPower, tau);
                    assets.add(assetToSimulate);
                    assetInfo = new HashMap<>();
                    assetInfo.put("name", name);
                    assetInfo.put("type", type);
                    assetInfo.put("role", "Consumer");
                    assetInfo.put("nominal_power", nominalPower);
                    response.put("Asset_" + i, assetInfo);
                }
                default -> log.info("Non supported type?");
            }
            i++;
        }

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Map<String, Object>> simulate(String scenario_name) {
        Map<String, Object> result = new HashMap<>();
        double simulationResult;
        double totalEnergyProduced = 0.0;
        double totalEnergyConsumed = 0.0;

        // Loop Through Producers
        for (tempest_double.assets.Asset asset : assets) {
            simulationResult = 0;
            switch (asset.getType()) {
                case "solar_panel", "wind_turbine" -> {
                    simulationResult = asset.simulate(LocalDateTime.now());
                }
                case "fuel_cell" -> {
                    simulationResult = asset.simulate(null); // doesn't require a input
                }

                default -> log.info("Not supported");
            }
            totalEnergyProduced += simulationResult;
            result.put(asset.getName(), simulationResult);
        }

        result.put("total_energy_produced", totalEnergyProduced);

        // Loop Through Consumers
        for (tempest_double.assets.Asset asset : assets) {
            switch (asset.getType()) {
                case "accumulator" -> {
                    asset.simulate(null); // doesn't require a input

                    double amountCharged = ((Accumulator) asset).charge(totalEnergyProduced);

                    totalEnergyProduced -= amountCharged;
                    totalEnergyConsumed += amountCharged;
                    simulationResult = ((Accumulator) asset).getCurrentCharge();

                    result.put(asset.getName(), simulationResult);
                }
                case "generic_consumer" -> {
                    double energyConsumed = asset.simulate(totalEnergyProduced);

                    simulationResult = energyConsumed;
                    totalEnergyConsumed += energyConsumed;
                    totalEnergyProduced -= energyConsumed;

                    result.put(asset.getName(), simulationResult);
                }

                default -> log.info("Not supported");
            }
        }

        result.put("total_energy_consumed", totalEnergyConsumed);

        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<String> deleteSimulations(int[] simulation_ids) {
        for (int simulationId : simulation_ids) {
            simulationRepository.deleteById(simulationId);
        }

        return ResponseEntity.ok("Deleted all without errors");
    }

    @Override
    public ResponseEntity<String> deleteSimulation(int simulation_id) {
        simulationRepository.deleteById(simulation_id);

        return ResponseEntity.ok("Deleted without errors");
    }
}
