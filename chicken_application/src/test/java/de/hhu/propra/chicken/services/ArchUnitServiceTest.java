package de.hhu.propra.chicken.services;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;

@AnalyzeClasses(packages = "de.hhu.propra.chicken.services", importOptions =
    ImportOption.DoNotIncludeTests.class)
public class ArchUnitServiceTest {

  @ArchTest
  ArchRule noDeprecatedClasses = classes()
      .should()
      .notBeAnnotatedWith(Deprecated.class);

  @ArchTest
  ArchRule noDeprecatedMethods = ArchRuleDefinition.methods()
      .should()
      .notBeAnnotatedWith(Deprecated.class);

  @ArchTest
  ArchRule noFieldInjection = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  ArchRule noFieldsAreNotPrivate = ArchRuleDefinition.fields()
      .that()
      .areDeclaredInClassesThat()
      .resideInAPackage("..")
      .and()
      .areNotStatic()
      .should()
      .bePrivate();

}
