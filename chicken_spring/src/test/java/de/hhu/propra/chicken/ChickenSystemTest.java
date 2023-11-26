package de.hhu.propra.chicken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc()
@Transactional
public class ChickenSystemTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  HeutigesDatumRepository heutigesDatumRepository;

  WebClient webClient;

  @BeforeEach
  void setupWebClient() {
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 8));
    webClient = MockMvcWebClientBuilder
        .mockMvcSetup(mvc)
        .build();
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
  }

  @Test
  @DisplayName("Nachdem ein Urlaub belegt wurde, wird der Urlaub in der Übersicht angezeigt.")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_1() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor urlaubBelegenNavButton = page.getElementByName("urlaub-belegen-a");
    page = urlaubBelegenNavButton.click();
    HtmlForm urlaubBelegenForm = page.getFormByName("urlaub_anmelden");
    HtmlInput urlaubsDatum = urlaubBelegenForm.getInputByName("urlaubsDatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = urlaubBelegenForm.getInputByName("urlaubsStart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = urlaubBelegenForm.getInputByName("urlaubsEnde");
    urlaubsEnde.setValueAttribute("11:00");

    HtmlSubmitInput button = urlaubBelegenForm.getInputByName("abschicken");
    page = button.click();
    HtmlTable urlaubsTabelle = page.getElementByName("urlaub_table");
    HtmlTableCell urlaubsDatumZelle = urlaubsTabelle.getCellAt(1, 0);
    assertThat(urlaubsDatumZelle.getTextContent()).contains("2022-03-10");
  }

  @Test
  @DisplayName("Nachdem ein Urlaub belegt wurde, wird der Urlaub in der Übersicht angezeigt "
      + "und kann korrekt storniert werden.")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_2() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor urlaubBelegenNavButton = page.getElementByName("urlaub-belegen-a");
    page = urlaubBelegenNavButton.click();
    HtmlForm urlaubBelegenForm = page.getFormByName("urlaub_anmelden");
    HtmlInput urlaubsDatum = urlaubBelegenForm.getInputByName("urlaubsDatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = urlaubBelegenForm.getInputByName("urlaubsStart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = urlaubBelegenForm.getInputByName("urlaubsEnde");
    urlaubsEnde.setValueAttribute("11:00");

    HtmlSubmitInput button = urlaubBelegenForm.getInputByName("abschicken");
    page = button.click();

    HtmlTable urlaubsTabelle = page.getElementByName("urlaub_table");
    HtmlTableCell urlaubsStornierenZelle = urlaubsTabelle.getCellAt(1, 4);
    HtmlForm stornoForm = (HtmlForm) urlaubsStornierenZelle.getFirstElementChild();
    page = stornoForm.getInputByName("storniere Datum: 2022-03-10 Startzeit: 10:00 Enduhrzeit: "
        + "11:00").click();

    urlaubsTabelle = page.getElementByName("urlaub_table");
    urlaubsTabelle.getRowCount();
    assertThat(urlaubsTabelle.getRowCount()).isEqualTo(3);
  }

  @Test
  @DisplayName("Nachdem eine Klausur erstellt wurde, wird die Klausur in der "
      + "Klausurbelege-Übersicht angezeigt.")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_3() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor klausurBelegen = page.getElementByName("klausur-belegen-a");
    page = klausurBelegen.click();
    HtmlAnchor klausurAnmelden = page.getElementByName("klausur-anmelden-a");
    page = klausurAnmelden.click();

    HtmlForm klausurAnmeldenform = page.getFormByName("klausurform");

    HtmlInput veranstaltungsName = klausurAnmeldenform.getInputByName("veranstaltungsName");
    veranstaltungsName.setValueAttribute("ProPra 2");
    HtmlInput veranstaltungsId = klausurAnmeldenform.getInputByName("veranstaltungsId");
    veranstaltungsId.setValueAttribute("1234");
    HtmlCheckBoxInput praesenz = klausurAnmeldenform.getInputByName("praesenz");
    praesenz.setChecked(true);
    HtmlInput urlaubsDatum = klausurAnmeldenform.getInputByName("klausurdatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = klausurAnmeldenform.getInputByName("klausurstart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = klausurAnmeldenform.getInputByName("klausurende");
    urlaubsEnde.setValueAttribute("11:00");

    HtmlSubmitInput button = klausurAnmeldenform.getInputByName("klausurSpeichern");
    page = button.click();
    HtmlForm klausurBelegenFrom = page.getElementByName("klausur_anmelden");
    HtmlSelect klasurSelect = klausurBelegenFrom.getSelectByName("veranstaltungsId");
    HtmlOption optionByValue = klasurSelect.getOptionByValue("1234");
    assertThat(optionByValue.getTextContent()).contains("ProPra 2");
  }


  @Test
  @DisplayName("Nachdem eine Klausur erstellt wurde, wird die Klausur in der "
      + "Klausurbelege-Übersicht angezeigt und belegt werden.")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_4() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor klausurBelegen = page.getElementByName("klausur-belegen-a");
    page = klausurBelegen.click();
    HtmlAnchor klausurAnmelden = page.getElementByName("klausur-anmelden-a");
    page = klausurAnmelden.click();

    HtmlForm klausurAnmeldenform = page.getFormByName("klausurform");

    HtmlInput veranstaltungsName = klausurAnmeldenform.getInputByName("veranstaltungsName");
    veranstaltungsName.setValueAttribute("ProPra 2");
    HtmlInput veranstaltungsId = klausurAnmeldenform.getInputByName("veranstaltungsId");
    veranstaltungsId.setValueAttribute("1234");
    HtmlCheckBoxInput praesenz = klausurAnmeldenform.getInputByName("praesenz");
    praesenz.setChecked(true);
    HtmlInput urlaubsDatum = klausurAnmeldenform.getInputByName("klausurdatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = klausurAnmeldenform.getInputByName("klausurstart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = klausurAnmeldenform.getInputByName("klausurende");
    urlaubsEnde.setValueAttribute("11:00");

    HtmlSubmitInput button = klausurAnmeldenform.getInputByName("klausurSpeichern");
    page = button.click();
    HtmlForm klausurBelegenFrom = page.getElementByName("klausur_anmelden");
    HtmlSelect klasurSelect = klausurBelegenFrom.getSelectByName("veranstaltungsId");
    klasurSelect.setSelectedIndex(1);
    page = klausurBelegenFrom.getInputByName("klausuranmeldung_abschicken").click();

    HtmlTable urlaubsTabelle = page.getElementByName("klausur_table");
    HtmlTableCell veranstaltungsNameZelle = urlaubsTabelle.getCellAt(1, 0);
    HtmlTableCell klausurZeitraumZelle = urlaubsTabelle.getCellAt(1, 1);
    HtmlTableCell freistellungsZeitraumZelle = urlaubsTabelle.getCellAt(1, 2);
    assertThat(veranstaltungsNameZelle.getTextContent()).contains("ProPra 2");
    assertThat(klausurZeitraumZelle.getTextContent()).contains(
        "Datum: 2022-03-10 Startzeit: 10:00 Enduhrzeit: 11:00");
    assertThat(freistellungsZeitraumZelle.getTextContent()).contains(
        "Datum: 2022-03-10 Startzeit: 09:30 Enduhrzeit: 13:00");
  }


  @Test
  @DisplayName("Nachdem eine Klausur erstellt wurde, wird die Klausur in der "
      + "Klausurbelege-Übersicht angezeigt, kann belegt werden und kann storniert werden.")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_5() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor klausurBelegen = page.getElementByName("klausur-belegen-a");
    page = klausurBelegen.click();
    HtmlAnchor klausurAnmelden = page.getElementByName("klausur-anmelden-a");
    page = klausurAnmelden.click();

    HtmlForm klausurAnmeldenform = page.getFormByName("klausurform");

    HtmlInput veranstaltungsName = klausurAnmeldenform.getInputByName("veranstaltungsName");
    veranstaltungsName.setValueAttribute("ProPra 2");
    HtmlInput veranstaltungsId = klausurAnmeldenform.getInputByName("veranstaltungsId");
    veranstaltungsId.setValueAttribute("1234");
    HtmlCheckBoxInput praesenz = klausurAnmeldenform.getInputByName("praesenz");
    praesenz.setChecked(true);
    HtmlInput urlaubsDatum = klausurAnmeldenform.getInputByName("klausurdatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = klausurAnmeldenform.getInputByName("klausurstart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = klausurAnmeldenform.getInputByName("klausurende");
    urlaubsEnde.setValueAttribute("11:00");

    HtmlSubmitInput button = klausurAnmeldenform.getInputByName("klausurSpeichern");
    page = button.click();
    HtmlForm klausurBelegenFrom = page.getElementByName("klausur_anmelden");
    HtmlSelect klasurSelect = klausurBelegenFrom.getSelectByName("veranstaltungsId");
    klasurSelect.setSelectedIndex(1);
    page = klausurBelegenFrom.getInputByName("klausuranmeldung_abschicken").click();

    HtmlTable klausurTable = page.getElementByName("klausur_table");
    HtmlTableCell klausurStornoZelle = klausurTable.getCellAt(1, 3);
    HtmlForm stornoForm = (HtmlForm) klausurStornoZelle.getFirstElementChild();
    page = stornoForm.getInputByName("storniere 1234").click();

    assertThat(page.getElementByName("keineKlausur").getTextContent())
        .contains("angemeldet");
  }

}
