package tempest_double.frontEndAPI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HistoryController {

    @GetMapping("/history")
    public String history() {
        return "history";
    }
}

