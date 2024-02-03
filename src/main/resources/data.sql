DROP TABLE if EXISTS message;
DROP TABLE if EXISTS account;
CREATE TABLE account (
    account_id INT PRIMARY KEY auto_increment,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);
CREATE TABLE message (
    message_id INT PRIMARY KEY auto_increment,
    posted_by INT,
    message_text VARCHAR(255),
    time_posted_epoch bigint,
    FOREIGN KEY (posted_by) REFERENCES account(account_id)
);

-- Starting test values with ids of 9999 to avoid test issues
INSERT INTO account VALUES (9999, 'testuser1', 'password');
INSERT INTO account VALUES (9998, 'testuser2', 'password');
INSERT INTO account VALUES (9997, 'testuser3', 'password');
INSERT INTO account VALUES (9996, 'testuser4', 'password');

INSERT INTO message VALUES (9999, 9999,'test message 1',1669947792);
INSERT INTO message VALUES (9997, 9997,'test message 2',1669947792);
INSERT INTO message VALUES (9996, 9996,'test message 3',1669947792);

