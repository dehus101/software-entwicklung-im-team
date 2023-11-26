package de.hhu.propra.chicken.repository;

import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import org.springframework.stereotype.Repository;

@Repository
public class VeranstaltungsIdRepositoryImpl implements VeranstaltungsIdRepository {

  private final WebPageContentProvider provider;

  public VeranstaltungsIdRepositoryImpl(WebPageContentProvider provider) {
    this.provider = provider;
  }


  @Override
  public boolean webCheck(String veranstaltungsId) {
    String webPageContent = provider.getWebPageContent(veranstaltungsId);
    return webPageContent.contains("Veranstaltungsart");
  }

}
