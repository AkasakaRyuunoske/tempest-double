package tempest_double.frontEndAPI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SimulationController {

    @GetMapping("/simulation")
    public String simulation() {
        return "simulation";
    }
}