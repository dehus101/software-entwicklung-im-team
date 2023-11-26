package de.hhu.propra.chicken.web.controller.student;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetailsDto;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.web.annotations.StudentRoute;
import de.hhu.propra.chicken.web.dto.KlausurDto;
import de.hhu.propra.chicken.web.dto.UrlaubDto;
import java.security.Principal;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@StudentRoute
public class StudentController {

  private final ChickenService service;

  private final LocalDate praktikumsstart;
  private final LocalDate praktikumsende;

  public StudentController(ChickenService service,
                           @Value("${praktikumszeitraum.praktikumsstart}") String praktikumsstart,
                           @Value("${praktikumszeitraum.praktikumsende}") String praktikumsende) {
    this.service = service;
    this.praktikumsstart = LocalDate.parse(praktikumsstart);
    this.praktikumsende = LocalDate.parse(praktikumsende);
  }

  @ModelAttribute("handle")
  String handle(Principal user) {
    return user.getName();
  }

  @GetMapping("/")
  public String index(Model model, @ModelAttribute("handle") String handle, UrlaubDto urlaubDto) {
    try {
      service.holeStudent(handle);
    } catch (Exception e) {
      Student student = new Student(null, handle);
      service.studentSpeichern(student);
    }
    StudentDetailsDto studentDetailsDto = service.studentDetails(handle);
    model.addAttribute("details", studentDetailsDto);
    model.addAttribute("urlaubDto", urlaubDto);
    model.addAttribute("fehler", "");
    return "index";
  }

  @PostMapping("/urlaubstornieren")
  public String urlaubStornieren(Model model, @ModelAttribute("handle") String handle,
                                 @Valid UrlaubDto urlaubDto) {

    ZeitraumDto urlaub = ZeitraumDto.erstelleZeitraum(urlaubDto.urlaubsDatum(),
        urlaubDto.urlaubsStart(), urlaubDto.urlaubsEnde(), praktikumsstart, praktikumsende);
    try {
      service.storniereUrlaub(handle, urlaub);
    } catch (Exception e) {
      StudentDetailsDto studentDetailsDto = service.studentDetails(handle);
      model.addAttribute("details", studentDetailsDto);
      model.addAttribute("urlaubDto", new UrlaubDto(null, null, null));
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @PostMapping("/klausurstornieren")
  public String klausurStornieren(Model model,
                                  @ModelAttribute("handle") String handle,
                                  @NotNull @NotBlank @NotEmpty String veranstaltungsId) {
    try {
      Klausur klausur = service.holeKlausur(veranstaltungsId);
      service.storniereKlausur(handle, klausur);
    } catch (KlausurException e) {
      StudentDetailsDto studentDetailsDto = service.studentDetails(handle);
      model.addAttribute("details", studentDetailsDto);
      model.addAttribute("urlaubDto", new UrlaubDto(null, null, null));
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @GetMapping("/urlaubbelegen")
  public String urlaubBelegen(Model model, UrlaubDto urlaubDto) {
    model.addAttribute("fehler", "");
    model.addAttribute("urlaubDto", urlaubDto);
    return "urlaubbelegen";
  }

  @PostMapping("/urlaubbelegen")
  public String urlaubSpeichern(@ModelAttribute("handle") String handle, Model model,
                                @Valid UrlaubDto urlaubDto, BindingResult result) {
    if (result.hasErrors()) {
      model.addAttribute("fehler", "");
      return "urlaubbelegen";
    }
    try {
      ZeitraumDto urlaub = ZeitraumDto.erstelleZeitraum(urlaubDto.urlaubsDatum(),
          urlaubDto.urlaubsStart(),
          urlaubDto.urlaubsEnde(), praktikumsstart, praktikumsende);
      service.belegeUrlaub(handle, urlaub);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      return "urlaubbelegen";
    }
  }

  @GetMapping("/klausurbelegen")
  public String klausurBelegen(Model model) {
    model.addAttribute("fehler", "");
    model.addAttribute("klausuren", service.alleKlausuren());
    return "klausurbelegen";
  }

  @PostMapping("/klausurbelegen")
  public String klausurBelegung(@ModelAttribute("handle") String handle, Model model,
                                String veranstaltungsId,
                                BindingResult result) {
    if (veranstaltungsId == null) {
      model.addAttribute("fehler", "");
      return "klausurbelegen";
    }
    try {
      Klausur klausur = service.holeKlausur(veranstaltungsId);
      service.belegeKlausur(handle, klausur);
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      model.addAttribute("klausuren", service.alleKlausuren());
      return "klausurbelegen";
    }
    return "redirect:/";
  }

  @GetMapping("/klausuranmelden")
  public String klausurAnmelden(Model model, KlausurDto klausurDto) {
    model.addAttribute("fehler", "");
    model.addAttribute("klausurDto", klausurDto);
    return "klausuranmelden";
  }

  @PostMapping("/klausuranmelden")
  public String klausurAnmeldenPost(@Valid KlausurDto klausurDto, BindingResult result, Model model
  ) {
    if (result.hasErrors()) {
      model.addAttribute("fehler", "");
      return "klausuranmelden";
    }
    try {
      ZeitraumDto klausurZeitraum = ZeitraumDto.erstelleZeitraum(klausurDto.klausurdatum(),
          klausurDto.klausurstart(), klausurDto.klausurende(), praktikumsstart, praktikumsende);
      service.klausurAnmelden(klausurDto.veranstaltungsId(), klausurDto.veranstaltungsName(),
          klausurZeitraum,
          klausurDto.praesenz());
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      return "klausuranmelden";
    }
    return "redirect:/klausurbelegen";
  }

}
