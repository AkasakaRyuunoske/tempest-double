package tempest_double.backEndAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HTMXTestController {
    @GetMapping("/clicked")
    public String htmxReply(){
        return "<h1>Flux Silences alone targets</h1>";
    }
}
