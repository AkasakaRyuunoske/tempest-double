package tempest_double.frontEndAPI.service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SimulationController {

    @GetMapping("/Simulation")
    public String assets(Model model) {
        return "SimulationPage/simulation";
    }
}