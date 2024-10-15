package tempest_double.frontEndAPI;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A class representing a blog entry.
 *
 * @param title The title of the blog post
 * @param author The author of the blog post
 * @return A formatted blog entry
 */
@Controller
public class ApolloController {
    @GetMapping("/")
    public String notEnough(Model model){
        model.addAttribute("face","лиц");
        return "ghost.html";
    }
}
