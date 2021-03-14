CREATE TABLE IF NOT EXISTS situation (
    id SERIAL PRIMARY KEY,
    parent_situations INTEGER,
    FOREIGN KEY (parent_situations) REFERENCES situation (id)
);

CREATE TABLE IF NOT EXISTS situation_questions (
    id SERIAL PRIMARY KEY,
    _key VARCHAR UNIQUE,
    _value VARCHAR,
    situation_id INTEGER,
    FOREIGN KEY (situation_id) REFERENCES situation(id)
);

CREATE TABLE IF NOT EXISTS reasoning (
    id SERIAL PRIMARY KEY,
    premise_situation_id INTEGER,
    result_situation_id INTEGER ,
    premise VARCHAR,
    result VARCHAR,
    FOREIGN KEY (premise_situation_id) REFERENCES situation(id),
    FOREIGN KEY (result_situation_id) REFERENCES situation(id)
);