package tempest_double.entity.Simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tempest_double.assets.GenericConsumer;
import tempest_double.assets.SolarPanel;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(scenario_name);
            scenario_name = jsonNode.get("name").asText();
        } catch (Exception e) {
            response.put("error", "Error parsing JSON");
            return ResponseEntity.badRequest().body(response);
        }
        System.out.println("Name is: " + scenario_name);
        Scenario scenario = scenarioRepository.findByName(scenario_name);
        System.out.println("scenario is: " + scenario);

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

            System.out.println("Name is: " + name);
            System.out.println("Asset from db: " + assetFromDb);


            tempest_double.assets.Asset assetToSimulate;
            double nominalPower;
            double efficiency;
            Map<String, Object> assetInfo;
            String type = assetFromDb.getType();
            switch (type){
                case "solar_panel":
                    System.out.println("It's a solar panel!");
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
                    break;
                case "wind_turbine":
                    System.out.println("It's a Wind Turbine!");
                    break;
                case "fuel_cell":
                    System.out.println("It's a Fuel Cell!");
                    break;
                case "accumulator":
                    System.out.println("It's a Accumulator!");
                    break;
                case "generic_consumer":
                    System.out.println("It's a Generic Consumer!");
                    nominalPower = Double.parseDouble(assetFromDb.getConfiguration().get("nominal-power").toString());
                    double tau = Double.parseDouble(assetFromDb.getConfiguration().get("tau").toString());
                    double minConsumption = Double.parseDouble(assetFromDb.getConfiguration().get("min-consumption").toString());
                    efficiency = 100;

                    assetToSimulate = new GenericConsumer(name, type, "Consumer", efficiency, minConsumption,nominalPower, tau);
                    assets.add(assetToSimulate);

                    assetInfo = new HashMap<>();
                    assetInfo.put("name", name);
                    assetInfo.put("type", type);
                    assetInfo.put("role", "Consumer");
                    assetInfo.put("nominal_power", nominalPower);

                    response.put("Asset_" + i, assetInfo);
                    break;
                default:
                    System.out.println("Non supported type?");
            }
            i++;
        }

        return ResponseEntity.ok().body(response);
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
