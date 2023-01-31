CREATE TABLE tiny_link_shortener.user(
    id SERIAL4 PRIMARY KEY,
    name varchar(256) NOT NULL,
    key varchar(64),
    role int4 NOT NULL,
    is_active boolean NOT NULL default true
);

-- TODO comments
-- TODO undex to key

-- TODO table for roles description

-- Modify links table

ALTER TABLE tiny_link_shortener.link
    ADD column user_id int4 REFERENCES tiny_link_shortener.user(id);

comment on column tiny_link_shortener.link.user_id is 'User who creat the link';

-- Recommends do it concurently outsiide
CREATE INDEX IF NOT EXISTS link_user_id_idx ON tiny_link_shortener.link (user_id);
