package de.hhu.propra.chicken.services;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.chicken.services.fehler.KlausurException;

@AnalyzeClasses(packagesOf = KlausurException.class, importOptions =
    ImportOption.DoNotIncludeTests.class)
public class ArchUnitExceptionTest {

  @ArchTest
  ArchRule exceptionsName = classes()
      .that()
      .resideInAPackage("de.hhu.propra.chicken.services.fehler")
      .should()
      .haveSimpleNameContaining("Exception");


}
