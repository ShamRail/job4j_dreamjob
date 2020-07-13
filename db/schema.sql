CREATE TABLE IF NOT EXISTS post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT
);

CREATE TABLE IF NOT EXISTS candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   memo TEXT
);