package de.hhu.propra.chicken.repository;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class WebPageContentProviderImpl implements WebPageContentProvider {

  @Override
  public String getWebPageContent(String veranstaltungsId) {
    try (WebClient webClient = new WebClient()) {
      final HtmlPage page1 = webClient.getPage(
          "https://lsf.hhu.de/qisserver/rds?state=verpublish&status=init&vmfile=no"
              + "&publishid=" + veranstaltungsId.toString()
              + "&moduleCall=webInfo&publishConfFile=webInfo&publishSubDir=veranstaltung");
      String htmlPageString = page1.getWebResponse().getContentAsString();
      return htmlPageString;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;

  }
}
