package de.hhu.propra.chicken.web.controller;

import static de.hhu.propra.chicken.web.StudentTemplate.DENNIS;
import static de.hhu.propra.chicken.web.StudentTemplate.FEDERICO;
import static de.hhu.propra.chicken.web.StudentTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.web.StudentTemplate.ZEITRAUM_03_10_1030_1300;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetailsDto;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import de.hhu.propra.chicken.web.StudentTemplate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ChickenService chickenService;

  StudentTemplate template = new StudentTemplate();

  @Test
  @DisplayName("Startseite aufrufen und Klausur wird richtig angezeigt")
  void test_01() throws Exception {
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(
        template.getDennisDetails());
    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult mvcResult = mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(model().attribute("details", template.getDennisDetails()))
        .andExpect(model().attribute("fehler", "")).andReturn();

    assertThat(mvcResult.getResponse().getContentAsString()).contains("Propra2");
  }

  @Test
  @DisplayName("Startseite aufrufen und Urlaub wird richtig angezeigt")
  void test_02() throws Exception {
    DENNIS.fuegeUrlaubHinzu(ZEITRAUM_03_10_1030_1300);
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(
        new StudentDetailsDto(DENNIS, Set.of(KL_PROPRA_03_09_1130_1230)));
    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult result = mockMvc.perform(getRequest).andReturn();

    String html = result.getResponse().getContentAsString();

    assertThat(html).contains("2022-03-10");
    assertThat(html).contains("10:30");
    assertThat(html).contains("13:00");
  }

  @Test
  @DisplayName("Student noch nicht in Datenbank. Student wird in Datenbank angelegt.")
  void test_03() throws Exception {
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenThrow(StudentNichtGefundenException.class)
        .thenReturn(FEDERICO);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());

    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", FEDERICO.getGithubHandle());

    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andReturn();

    verify(chickenService).holeStudent(FEDERICO.getGithubHandle());
    verify(chickenService).studentSpeichern(FEDERICO);
    verify(chickenService).studentDetails(FEDERICO.getGithubHandle());

    MockHttpServletRequestBuilder postRequest =
        post("/urlaubstornieren").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("urlaubsDatum", LocalDate.of(2022, 03, 10).toString())
            .param("urlaubsStart", LocalTime.of(10, 30).toString())
            .param("urlaubsEnde", LocalTime.of(13, 00).toString());

    mockMvc.perform(postRequest).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andReturn();

    verify(chickenService).storniereUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);

  }

  @Test
  @DisplayName("Urlaub wird nicht storniert wenn ein Fehler bei stornieUrlaub auftritt")
  void test_04a() throws Exception {
    FEDERICO.fuegeUrlaubHinzu(ZEITRAUM_03_10_1030_1300);
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenReturn(FEDERICO);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());
    doThrow(new UrlaubException("Urlaub konnte nicht storniert werden"))
        .when(chickenService).storniereUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);

    MockHttpServletRequestBuilder postRequest =
        post("/urlaubstornieren").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("urlaubsDatum", LocalDate.of(2022, 03, 10).toString())
            .param("urlaubsStart", LocalTime.of(10, 30).toString())
            .param("urlaubsEnde", LocalTime.of(13, 00).toString());

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "Urlaub konnte nicht storniert werden"))
        .andReturn();

    verify(chickenService).storniereUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);

  }

  @Test
  @DisplayName("Klausur wird storniert")
  void test_05() throws Exception {
    FEDERICO.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenThrow(StudentNichtGefundenException.class)
        .thenReturn(FEDERICO);
    when(chickenService.holeKlausur(anyString())).thenReturn(KL_PROPRA_03_09_1130_1230);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());

    MockHttpServletRequestBuilder postRequest =
        post("/klausurstornieren").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", KL_PROPRA_03_09_1130_1230.getVeranstaltungsId());

    mockMvc.perform(postRequest).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andReturn();

    verify(chickenService).storniereKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);

  }

  @Test
  @DisplayName("Klausur wird nicht storniert wenn ein Fehler bei storniereKlausur auftritt")
  void test_05a() throws Exception {
    FEDERICO.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenReturn(FEDERICO);
    when(chickenService.holeKlausur(anyString())).thenReturn(KL_PROPRA_03_09_1130_1230);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());
    doThrow(new KlausurException("Klausur konnte nicht sotrniert werden"))
        .when(chickenService)
        .storniereKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);

    MockHttpServletRequestBuilder postRequest =
        post("/klausurstornieren").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", KL_PROPRA_03_09_1130_1230.getVeranstaltungsId());

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "Klausur konnte nicht sotrniert werden"))
        .andReturn();

    verify(chickenService).storniereKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);

  }

  @Test
  @DisplayName("Urlaubelegung ist erreichbar")
  void test_06() throws Exception {
    MockHttpServletRequestBuilder getRequest =
        get("/urlaubbelegen").flashAttr("handle", FEDERICO.getGithubHandle());

    MvcResult result = mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "")).andReturn();

    assertThat(result.getResponse().getContentAsString()).contains("Urlaub belegen");
  }

  @Test
  @DisplayName("Urlaub wird belegt")
  void test_07() throws Exception {
    MockHttpServletRequestBuilder postRequest =
        post("/urlaubbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("urlaubsDatum", LocalDate.of(2022, 03, 10).toString())
            .param("urlaubsStart", LocalTime.of(10, 30).toString())
            .param("urlaubsEnde", LocalTime.of(13, 00).toString());

    mockMvc.perform(postRequest)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andReturn();

    verify(chickenService).belegeUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);
  }

  @Test
  @DisplayName("Urlaub wird nicht belegt wenn eins der Felder null ist")
  void test_08() throws Exception {

    MockHttpServletRequestBuilder postRequest =
        post("/urlaubbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("urlaubsDatum", "")
            .param("urlaubsStart", LocalTime.of(10, 30).toString())
            .param("urlaubsEnde", LocalTime.of(13, 00).toString());

    MvcResult result = mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andReturn();
    String html = result.getResponse().getContentAsString();

    assertThat(html).contains("Darf nicht leer sein");

  }

  @Test
  @DisplayName("Urlaub wird nicht belegt wenn belegeUrlaub fehlschlägt")
  void test_09() throws Exception {
    doThrow(new UrlaubException("Urlaub schon gebucht")).when(chickenService)
        .belegeUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);

    MockHttpServletRequestBuilder postRequest =
        post("/urlaubbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("urlaubsDatum", LocalDate.of(2022, 03, 10).toString())
            .param("urlaubsStart", LocalTime.of(10, 30).toString())
            .param("urlaubsEnde", LocalTime.of(13, 00).toString());

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "Urlaub schon gebucht"))
        .andReturn();

    verify(chickenService).belegeUrlaub(FEDERICO.getGithubHandle(), ZEITRAUM_03_10_1030_1300);

  }

  @Test
  @DisplayName("Klausurbelegung ist erreichbar")
  void test_10() throws Exception {
    MockHttpServletRequestBuilder getRequest =
        get("/klausurbelegen").flashAttr("handle", FEDERICO.getGithubHandle());

    MvcResult result = mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "")).andReturn();

    verify(chickenService).alleKlausuren();
    assertThat(result.getResponse().getContentAsString()).contains("Anmeldung zu einer Klausur");
  }

  @Test
  @DisplayName("Klausur wird belegt")
  void test_11() throws Exception {
    when(chickenService.holeKlausur("215783")).thenReturn(KL_PROPRA_03_09_1130_1230);
    MockHttpServletRequestBuilder postRequest =
        post("/klausurbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", "215783");

    mockMvc.perform(postRequest)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andReturn();

    verify(chickenService).belegeKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);
  }

  @Test
  @DisplayName("Klausur wird nicht belegt wenn die Veranstaltungsid null ist")
  void test_12() throws Exception {
    MockHttpServletRequestBuilder postRequest =
        post("/klausurbelegen").flashAttr("handle", FEDERICO.getGithubHandle());

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", ""))
        .andReturn();
  }

  @Test
  @DisplayName("Klausur wird nicht belegt wenn belegeKlausur fehlschlägt")
  void test_13() throws Exception {
    doThrow(new KlausurException("Klausur gibt es nicht"))
        .when(chickenService).belegeKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);
    when(chickenService.holeKlausur("215783")).thenReturn(KL_PROPRA_03_09_1130_1230);
    when(chickenService.alleKlausuren()).thenReturn(Set.of(KL_PROPRA_03_09_1130_1230));

    MockHttpServletRequestBuilder postRequest =
        post("/klausurbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", "215783");

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "Klausur gibt es nicht"))
        .andExpect(model().attribute("klausuren", Set.of(KL_PROPRA_03_09_1130_1230)))
        .andReturn();

    verify(chickenService).belegeKlausur(FEDERICO.getGithubHandle(), KL_PROPRA_03_09_1130_1230);
    verify(chickenService).holeKlausur("215783");
  }

  @Test
  @DisplayName("Klausur wird nicht belegt wenn holeKlausur fehlschlägt")
  void test_14() throws Exception {
    when(chickenService.holeKlausur("Käse")).thenThrow(
        new KlausurException("Klausur gibt es nicht"));
    when(chickenService.alleKlausuren()).thenReturn(Set.of(KL_PROPRA_03_09_1130_1230));

    MockHttpServletRequestBuilder postRequest =
        post("/klausurbelegen").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", "Käse");

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "Klausur gibt es nicht"))
        .andExpect(model().attribute("klausuren", Set.of(KL_PROPRA_03_09_1130_1230)))
        .andReturn();

    verify(chickenService).holeKlausur("Käse");
  }

  @Test
  @DisplayName("Klausuranmeldung ist erreichbar")
  void test_15() throws Exception {
    MockHttpServletRequestBuilder getRequest =
        get("/klausuranmelden").flashAttr("handle", FEDERICO.getGithubHandle());

    MvcResult result = mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "")).andReturn();

    assertThat(result.getResponse().getContentAsString()).contains("Neue Klausur eintragen");
  }

  @Test
  @DisplayName("Klausur wird angemeldet")
  void test_16() throws Exception {
    MockHttpServletRequestBuilder postRequest =
        post("/klausuranmelden").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", "215783")
            .param("veranstaltungsName", "Propra2")
            .param("praesenz", "true")
            .param("klausurdatum", LocalDate.of(2022, 3, 23).toString())
            .param("klausurstart", LocalTime.of(11, 30).toString())
            .param("klausurende", LocalTime.of(13, 0).toString());

    mockMvc.perform(postRequest)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/klausurbelegen"))
        .andReturn();

    ZeitraumDto zeitraum = ZeitraumDto
        .erstelleZeitraum(LocalDate.of(2022, 3, 23),
            LocalTime.of(11, 30), LocalTime.of(13, 0),
            LocalDate.of(2022, 3, 7),
            LocalDate.of(2022, 3, 25));

    verify(chickenService).klausurAnmelden("215783", "Propra2", zeitraum, true);
  }

  @Test
  @DisplayName("Klausur wird nicht angemeldet wenn ein Feld null ist")
  void test_17() throws Exception {
    MockHttpServletRequestBuilder postRequest =
        post("/klausuranmelden").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsName", "Propra2")
            .param("praesenz", "true")
            .param("klausurdatum", LocalDate.of(2022, 03, 23).toString())
            .param("klausurstart", LocalTime.of(11, 30).toString())
            .param("klausurende", LocalTime.of(13, 00).toString());

    MvcResult result = mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", ""))
        .andReturn();
    String html = result.getResponse().getContentAsString();

    assertThat(html).contains("Darf nicht leer sein");
  }

  @Test
  @DisplayName("Klausur wird nicht angemeldet wenn ")
  void test_18() throws Exception {
    doThrow(new KlausurException("VeranstaltungsId ist falsch")).when(chickenService)
        .klausurAnmelden(anyString(), anyString(), any(ZeitraumDto.class), anyBoolean());

    MockHttpServletRequestBuilder postRequest =
        post("/klausuranmelden").flashAttr("handle", FEDERICO.getGithubHandle())
            .param("veranstaltungsId", "Käse")
            .param("veranstaltungsName", "Propra2")
            .param("praesenz", "true")
            .param("klausurdatum", LocalDate.of(2022, 03, 23).toString())
            .param("klausurstart", LocalTime.of(11, 30).toString())
            .param("klausurende", LocalTime.of(13, 00).toString());

    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("fehler", "VeranstaltungsId ist falsch"))
        .andReturn();
  }
}
