package de.hhu.propra.chicken.services;

import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

public class ZeitraumDtoTemplate {


  static final ZeitraumDto ZEITRAUM_03_07_0930_1030 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 7),
      LocalTime.of(9, 30),
      LocalTime.of(10, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_07_1230_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 7),
      LocalTime.of(12, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_07_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 7),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_08_0930_1030 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 8),
      LocalTime.of(9, 30),
      LocalTime.of(10, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_08_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 8),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_0945 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(9, 45),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_1215 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(12, 15),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1000_1100 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(10, 0),
      LocalTime.of(11, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1030_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(10, 30),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1030_1145 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(10, 30),
      LocalTime.of(11, 45),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1100_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 0),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1145_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 45),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1145_1215 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 45),
      LocalTime.of(12, 15),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1300_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(13, 0),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1230_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(12, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(11, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_1030_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(10, 30),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_0930_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(9, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_1030_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(10, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_1230_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(12, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));


  static final ZeitraumDto ZEITRAUM_03_14_0930_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(9, 30),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_14_1100_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 14),
      LocalTime.of(11, 0),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1030_1130 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 30),
      LocalTime.of(11, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1100_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(11, 0),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1145_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(11, 45),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1015_1100 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 15),
      LocalTime.of(11, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1000_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 0),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1000_1030 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 0),
      LocalTime.of(10, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1130_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(11, 30),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1045_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 45),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_15_1000_1100 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 15),
      LocalTime.of(10, 0),
      LocalTime.of(11, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1100_1200 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(11, 0),
      LocalTime.of(12, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1200_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(12, 0),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1000_1145 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(10, 0),
      LocalTime.of(11, 45),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_0930_1000 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(9, 30),
      LocalTime.of(10, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1000_1100 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(10, 0),
      LocalTime.of(11, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1000_1300 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(10, 0),
      LocalTime.of(13, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1200_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(12, 0),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1300_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(13, 0),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_0930_1300 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(9, 30),
      LocalTime.of(13, 0),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_08_0930_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 8),
      LocalTime.of(9, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_07_0930_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 7),
      LocalTime.of(9, 30),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_0930_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(9, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1100_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(11, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_24_1130_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 24),
      LocalTime.of(11, 30),
      LocalTime.of(13, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));

  static final ZeitraumDto ZEITRAUM_03_09_1000_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 3, 9),
      LocalTime.of(10, 0),
      LocalTime.of(12, 30),
      LocalDate.of(2022, 3, 7),
      LocalDate.of(2022, 3, 25));
}
