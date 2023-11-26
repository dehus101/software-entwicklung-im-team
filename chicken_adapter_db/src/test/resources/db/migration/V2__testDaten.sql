INSERT INTO student_dto (id, githubhandle)
VALUES (1, 'dehus101');
INSERT INTO student_dto (id, githubhandle)
VALUES (2, 'fnellen');
INSERT INTO student_dto (id, githubhandle)
VALUES (3, 'ernaz100');
INSERT INTO student_dto (id, githubhandle)
VALUES (4, 'pifis102');
INSERT INTO student_dto (id, githubhandle)
VALUES (5, 'TeeJaey');

INSERT INTO urlaub_zeitraum_dto(student_dto, datum, start_uhrzeit, end_uhrzeit)
VALUES (1, '2022-03-07', '09:30:00', '10:30:00');

INSERT INTO klausur_dto(id, veranstaltungs_id, veranstaltungs_name, klausurdatum, klausurstart_uhrzeit,
                        klausurend_uhrzeit, freistellungdatum, freistellungstart_uhrzeit,
                        freistellungend_uhrzeit, praesenz)
VALUES (1, '215783', 'Professionelle Softwareentwicklung im Team', '2022-03-07', '09:30:00', '10:30:00', '2022-03-07',
        '09:30:00', '10:30:00', TRUE);