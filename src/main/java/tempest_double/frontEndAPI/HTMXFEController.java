package tempest_double.frontEndAPI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMXFEController {
    @GetMapping("/htmx")
    public String htmx(){
        return "htmx_test.html";
    }
}
