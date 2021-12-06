
INSERT INTO PlanRecord(id, planName, authorName, date)
VALUES (111, 'None', 'alice', '2012-12-03');

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (1, 'pushups', 10, 1, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (2, 'squats', 20, 1, 111);

INSERT INTO PlanRecord(id, planName, authorName, date)
VALUES (20, 'None', 'alice', '2012-12-04');

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (3, 'pushups', 3, 1, 20);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (4, 'squats', 4, 1, 20);

INSERT INTO PlanRecord(id, planName, authorName, date)
VALUES (3, 'Chest plan', 'alice', '2012-12-05');

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (5, 'pushups', 25, 1, 3);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (6, 'benchpress', 10, 1, 3);
