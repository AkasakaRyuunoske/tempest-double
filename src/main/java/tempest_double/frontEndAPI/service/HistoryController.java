package tempest_double.frontEndAPI.service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HistoryController {

    @GetMapping("/history")
    public String assets() {
        return "HistoryPage/history";
    }
}

