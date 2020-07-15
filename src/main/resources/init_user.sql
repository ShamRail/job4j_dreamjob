CREATE TABLE IF NOT EXISTS _user(
    id SERIAL PRIMARY KEY,
    name VARCHAR(511),
    email VARCHAR(255),
    password VARCHAR(255)
);