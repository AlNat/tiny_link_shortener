CREATE SCHEMA IF NOT EXISTS tiny_link_shortener;

------------------------------------

-- Global sequence for creating shortlink based on id

CREATE SEQUENCE IF NOT EXISTS tiny_link_shortener.short_link_generator_id
    AS BIGINT START WITH 100000 NO MAXVALUE;

------------------------------------

CREATE TABLE IF NOT EXISTS tiny_link_shortener.link_status
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50),
    description VARCHAR(255)
);

INSERT INTO tiny_link_shortener.link_status (id, name, description)
VALUES (0, 'CREATED', 'Link created')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.link_status (id, name, description)
VALUES (1, 'DELETED', 'Link deleted')
ON CONFLICT DO NOTHING;

---------------------------------
-- Table of short links itself --
---------------------------------

CREATE TABLE IF NOT EXISTS tiny_link_shortener.link
(
    id              SERIAL8 PRIMARY KEY,
    created         TIMESTAMP     NOT NULL,
    status          INT4          NOT NULL REFERENCES tiny_link_shortener.link_status,
    original_link   VARCHAR(2048) NOT NULL,
    short_link      VARCHAR(64)   NOT NULL, -- due to 64+ symbols -- is not short link at all
    available_from  TIMESTAMP,
    available_to    TIMESTAMP,
    max_visit_count INT4
);

CREATE INDEX IF NOT EXISTS link_original_link_idx ON tiny_link_shortener.link (original_link);
CREATE UNIQUE INDEX IF NOT EXISTS link_short_link_idx ON tiny_link_shortener.link (short_link);
CREATE INDEX IF NOT EXISTS link_created_idx ON tiny_link_shortener.link (created);


------------------------------------


CREATE TABLE IF NOT EXISTS tiny_link_shortener.visit_status
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50),
    description VARCHAR(255)
);

INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (0, 'CREATED', 'Visit successful')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (1, 'NOT_FOUND', 'Link not found')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (2, 'DELETED', 'Link deleted')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (3, 'NOT_AVAILABLE', 'Link not available yet')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (4, 'EXPIRED', 'Link expired')
ON CONFLICT DO NOTHING;
INSERT INTO tiny_link_shortener.visit_status (id, name, description)
VALUES (5, 'TOO_MUCH_REQUEST', 'Link expires all visits')
ON CONFLICT DO NOTHING;


------------------------------------
-- Table of visits to short links --
------------------------------------

CREATE TABLE IF NOT EXISTS tiny_link_shortener.visit
(
    id         SERIAL8 PRIMARY KEY,
    link_id    INT8      NOT NULL REFERENCES tiny_link_shortener.link (id),
    created    TIMESTAMP NOT NULL,
    status     INT4      NOT NULL REFERENCES tiny_link_shortener.visit_status,
    ip_address INET, -- postgres specific IP address storage
    user_agent VARCHAR(255),
    headers    text
);

CREATE INDEX IF NOT EXISTS visit_link_id_idx ON tiny_link_shortener.visit (link_id);
CREATE INDEX IF NOT EXISTS visit_created_idx ON tiny_link_shortener.visit (created);
CREATE INDEX IF NOT EXISTS visit_status_idx ON tiny_link_shortener.visit (status);
CREATE INDEX IF NOT EXISTS visit_ip_address_idx ON tiny_link_shortener.visit USING gist (ip_address inet_ops);
