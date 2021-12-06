
INSERT INTO PlanRecord(id, planName, authorName, date)
VALUES (111, 'planName', 'authorName', '2012-02-01');

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (1, 'pushups', 1, 0, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (2, 'pushups', 2, 0, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (3, 'pushups', 3, 0, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (4, 'pushups', 4, 0, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (5, 'pushups', 20, 0, 111);

INSERT INTO ExerciseRecord(id, exerciseName, amount, amountType, planrecord_id)
VALUES (6, 'pushups', 5, 0, 111);
