CREATE SEQUENCE translation_seq START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE translation_value_seq START WITH 1000 INCREMENT BY 1;

CREATE TABLE translations
(
    id              BIGINT       NOT NULL,
    translation_key VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
CREATE INDEX translation_key_idx ON translations (translation_key);

CREATE TABLE translation_values
(
    id             BIGINT       NOT NULL,
    translation_id BIGINT       NOT NULL,
    language_code  VARCHAR(255) NOT NULL,
    value          VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX translation_language_idx ON translation_values (language_code);

ALTER TABLE translation_values
    ADD CONSTRAINT translation_values_translation_id FOREIGN KEY (translation_id) REFERENCES translations;
