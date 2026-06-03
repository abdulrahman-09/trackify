CREATE TABLE tasks (
                       id               BINARY(16)    NOT NULL,
                       title            VARCHAR(255)  NOT NULL,
                       description      VARCHAR(1000),
                       due_date         DATETIME(6),
                       creation_date    DATETIME(6)   NOT NULL,
                       last_update_date DATETIME(6)   NOT NULL,
                       priority         VARCHAR(50)   NOT NULL,
                       status           VARCHAR(50)   NOT NULL DEFAULT 'OPEN',
                       task_list_id     BINARY(16)    NOT NULL,

                       CONSTRAINT pk_tasks         PRIMARY KEY (id),
                       CONSTRAINT fk_tasks_tl      FOREIGN KEY (task_list_id)
                           REFERENCES task_lists(id)
                           ON DELETE CASCADE
);