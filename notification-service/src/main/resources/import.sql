INSERT INTO ScheduledRecord(id, email, exerciseName, hour, minute)
VALUES (nextval('hibernate_sequence'),
        'franta@nonexistent.com', 'Pushups', 18, 1);

INSERT INTO ScheduledRecord(id, email, exerciseName, hour, minute)
VALUES (nextval('hibernate_sequence'),
        'nonexistent.email@nonexistent.com', 'Pushups', 19, 2);

INSERT INTO ScheduledRecord(id, email, exerciseName, hour, minute)
VALUES (nextval('hibernate_sequence'),
        'someone@nonexistent.com', 'Running', 20, 3);

INSERT INTO ScheduledRecord(id, email, exerciseName, hour, minute)
VALUES (nextval('hibernate_sequence'),
        'franta@nonexistent.com', 'Walking', 21, 4);
