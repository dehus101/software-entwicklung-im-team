package de.hhu.propra.chicken.web.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.web.AuthenticationTemplates;
import de.hhu.propra.chicken.web.configuration.MethodSecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@Import(MethodSecurityConfiguration.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ChickenService chickenService;

  @Test
  @DisplayName("redirectet zu github login ohne anmeldung")
  void test1() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/tutor/logs");

    mockMvc.perform(getRequest)
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("Tutor darf auf Tutor logs zugreifen")
  void test2() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/tutor/logs")
        .session(AuthenticationTemplates.tutorSession());

    mockMvc.perform(getRequest)
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Tutor darf nicht auf Organisator logs zugreifen")
  void test3() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/organisator/logs")
        .session(AuthenticationTemplates.tutorSession());

    mockMvc.perform(getRequest)
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Student darf nicht auf Tutoren logs zugreifen")
  void test4() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/tutor/logs")
        .session(AuthenticationTemplates.studentSession());

    mockMvc.perform(getRequest)
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Student darf nicht auf Organisator logs zugreifen")
  void test5() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/organisator/logs")
        .session(AuthenticationTemplates.studentSession());

    mockMvc.perform(getRequest)
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Organisator darf auf Organisator logs zugreifen")
  void test6() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/organisator/logs")
        .session(AuthenticationTemplates.organisatorSession());

    mockMvc.perform(getRequest)
        .andExpect(status().isOk());
  }
}
