package de.hhu.propra.chicken.repository;

import com.gargoylesoftware.htmlunit.WebClient;

public interface WebPageContentProvider {

  public String getWebPageContent(String veranstaltungsId);


}
