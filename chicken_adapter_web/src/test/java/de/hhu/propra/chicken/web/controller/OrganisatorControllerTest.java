package de.hhu.propra.chicken.web.controller;

import static de.hhu.propra.chicken.web.StudentTemplate.DENNIS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.chicken.services.ChickenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OrganisatorControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ChickenService chickenService;

  @Test
  @DisplayName("Tutor kann Tutorlogs sehen")
  void test_01() throws Exception {

    MockHttpServletRequestBuilder getRequest =
        get("/organisator/logs").flashAttr("handle", DENNIS.getGithubHandle());

    mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();
  }
}
