package tempest_double;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApolloController {
    @GetMapping("/")
    public String notEnough(Model model){
        model.addAttribute("face","лиц");
        return "ghost.html";
    }
}
