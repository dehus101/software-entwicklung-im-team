package de.hhu.propra.chicken.web.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.chicken.web.annotations.OrganisatorRoute;
import de.hhu.propra.chicken.web.annotations.StudentRoute;
import de.hhu.propra.chicken.web.annotations.TutorRoute;
import de.hhu.propra.chicken.web.controller.student.StudentController;

@AnalyzeClasses(
    packagesOf = StudentController.class,
    importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnitTest {


  @ArchTest
  static final ArchRule rule1 = classes().that().resideInAPackage("..organisator..")
      .should()
      .beAnnotatedWith(OrganisatorRoute.class);

  @ArchTest
  static final ArchRule rule2 =
      classes().that().resideInAPackage("..student..").should()
          .beAnnotatedWith(StudentRoute.class);

  @ArchTest
  static final ArchRule rule5 = noClasses().that().resideOutsideOfPackage("..tutor..")
      .should()
      .beAnnotatedWith(TutorRoute.class);

  @ArchTest
  static final ArchRule rule3 =
      classes().that().resideInAPackage("..tutor..").should()
          .notBeAnnotatedWith(TutorRoute.class);

  @ArchTest
  static final ArchRule rule4 = classes().that().resideInAPackage("..organisator..")
      .should()
      .notBeAnnotatedWith(TutorRoute.class);


}
