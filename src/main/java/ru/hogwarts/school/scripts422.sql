CREATE TABLE person (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    has_driver_license BOOLEAN DEFAULT 'false'
);
CREATE TABLE car (
    id INTEGER PRIMARY KEY,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    coast INTEGER NOT NULL
);
CREATE TABLE person_car (
    person_id INTEGER REFERENCES person (id),
    car_id INTEGER REFERENCES car (id)
);