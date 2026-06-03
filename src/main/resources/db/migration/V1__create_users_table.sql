CREATE TABLE users (
                       user_id          BINARY(16)   NOT NULL,
                       first_name       VARCHAR(255) NOT NULL,
                       last_name        VARCHAR(255) NOT NULL,
                       email            VARCHAR(255) NOT NULL,
                       password         VARCHAR(255),
                       auth_provider    VARCHAR(50),
                       auth_provider_id VARCHAR(255),
                       created_at       DATETIME(6),
                       updated_at       DATETIME(6),

                       CONSTRAINT pk_users      PRIMARY KEY (user_id),
                       CONSTRAINT uk_users_email UNIQUE (email)
);