package de.hhu.propra.chicken.aggregates;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;
import de.hhu.propra.chicken.domain.stereotypes.AggregateRoot;
import de.hhu.propra.chicken.domain.stereotypes.EntityObject;
import de.hhu.propra.chicken.domain.stereotypes.ValueObject;

@AnalyzeClasses(packages = "de.hhu.propra.chicken.domain.aggregates", importOptions =
    ImportOption.DoNotIncludeTests.class)
public class ArchUnitTests {

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

  @ArchTest
  ArchRule aggregateSindPublic = ArchRuleDefinition.classes()
      .that()
      .areAnnotatedWith(AggregateRoot.class)
      .should()
      .bePublic()
      .andShould()
      .beAnnotatedWith(EntityObject.class);

  @ArchTest
  ArchRule valueObjekteSindPackagePrivate = ArchRuleDefinition.classes()
      .that()
      .arePackagePrivate()
      .should()
      .beAnnotatedWith(ValueObject.class);

  @ArchTest
  ArchRule valueObjekteSindNichtPublic = ArchRuleDefinition.noClasses()
      .that()
      .resideOutsideOfPackage("..dto..")
      .and()
      .arePublic()
      .should()
      .beAnnotatedWith(ValueObject.class);

}
