package de.hhu.propra.chicken.web.controller.tutor;

import de.hhu.propra.chicken.web.annotations.TutorRoute;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutor")
@TutorRoute
public class TutorController {

  @GetMapping("logs")
  public String logs(@ModelAttribute("handle") String tutorHandle) {
    return "logs";
  }

  @ModelAttribute("handle")
  String handle(Principal user) {
    return user.getName();
  }
}
