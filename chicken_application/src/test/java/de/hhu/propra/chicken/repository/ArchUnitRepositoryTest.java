package de.hhu.propra.chicken.repository;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;

@AnalyzeClasses(packagesOf = HeutigesDatumRepository.class, importOptions =
    ImportOption.DoNotIncludeTests.class)
public class ArchUnitRepositoryTest {

  @ArchTest
  ArchRule repositoriesEnthaltenNamen = classes()
      .that()
      .resideInAPackage("de.hhu.propra.chicken.repositories")
      .should()
      .haveSimpleNameContaining("Repository")
      .andShould()
      .bePublic();

}
