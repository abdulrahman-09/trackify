CREATE TABLE task_lists (
                            id               BINARY(16)    NOT NULL,
                            title            VARCHAR(255)  NOT NULL,
                            description      VARCHAR(1000),
                            user_id          BINARY(16)    NOT NULL,
                            creation_date    DATETIME(6)   NOT NULL,
                            last_update_date DATETIME(6)   NOT NULL,

                            CONSTRAINT pk_task_lists  PRIMARY KEY (id),
                            CONSTRAINT fk_tl_user     FOREIGN KEY (user_id)
                                REFERENCES users(user_id)
                                ON DELETE CASCADE
);