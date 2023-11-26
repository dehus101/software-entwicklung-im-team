package de.hhu.propra.chicken.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VeranstaltungIdTest {

  WebPageContentProvider provider = mock(WebPageContentProvider.class);

  @Test
  @DisplayName("Existierende Veranstaltung-id gibt true zurück.")
  void test_1() throws IOException {
    when(provider.getWebPageContent("223554"))
        .thenReturn(LsfPage.THEORETISCHE_INFORMATIK_PAGE);

    VeranstaltungsIdRepositoryImpl veranstaltungsId = new VeranstaltungsIdRepositoryImpl(provider);
    assertThat(veranstaltungsId.webCheck("223554")).isTrue();
  }

  @Test
  @DisplayName("Nicht existierende Veranstaltung-id gibt false zurück.")
  void test_2() throws IOException {
    when(provider.getWebPageContent("219999"))
        .thenReturn(LsfPage.FALSCHE_PAGE);

    VeranstaltungsIdRepositoryImpl veranstaltungsId = new VeranstaltungsIdRepositoryImpl(provider);
    assertThat(veranstaltungsId.webCheck("219999")).isFalse();
  }


}
