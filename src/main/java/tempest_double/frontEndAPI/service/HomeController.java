package tempest_double.frontEndAPI.service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import tempest_double.backEndAPI.service.HomeExample;

@Controller
public class HomeController {

    private HomeExample homeExample;

    public HomeController(HomeExample homeExample) {
        this.homeExample = homeExample;
    }

    @GetMapping("/Home")
    public String home(Model model) {
        model.addAttribute("currentValue", homeExample.getCurrentEmoji());
        return "home";
    }

    @GetMapping("/currentEmoji")
    @ResponseBody
    public String getCurrentEmoji() {
        return homeExample.getCurrentEmoji();
    }
}
