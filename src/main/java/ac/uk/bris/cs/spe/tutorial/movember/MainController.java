package ac.uk.bris.cs.spe.tutorial.movember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class MainController {

    @Autowired
    private BeardRepository beardRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/beard")
    public String showBeardForm(Beard beard, Model model) {
        model.addAttribute("beards", beardRepository.findAll());
        return "beard";
    }

    @PostMapping(value = "/beard")
    public String submitContact(@Valid Beard beard, BindingResult binding, RedirectAttributes attr) {
        if (binding.hasErrors()) {
            return "/beard";
        }
        beardRepository.save(beard);
        attr.addFlashAttribute("message", "Thank you for your beard.");
        return "redirect:/beard";
    }
}
