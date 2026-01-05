DROP TABLE IF EXISTS robots;
DROP TABLE IF EXISTS hubs;

CREATE TABLE hubs
(
    hub_id    SERIAL PRIMARY KEY NOT NULL,
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address   TEXT NOT NULL
);

CREATE TABLE robots
(
    robot_id   SERIAL PRIMARY KEY NOT NULL,
    available  BOOLEAN DEFAULT TRUE NOT NULL,
    battery    INTEGER NOT NULL,
    hub_id     INTEGER NOT NULL,
    latitude   DOUBLE PRECISION NOT NULL,
    longitude  DOUBLE PRECISION NOT NULL,
    max_weight DOUBLE PRECISION NOT NULL,
    speed      INTEGER NOT NULL,
    price      NUMERIC NOT NULL,
    robot_type TEXT NOT NULL, -- "robot" / "drone"
    CONSTRAINT fk_hub FOREIGN KEY (hub_id) REFERENCES hubs (hub_id) ON DELETE CASCADE
);

INSERT INTO hubs (latitude, longitude, address)
VALUES (40.7128, -74.0060, 'New York Hub'),
       (37.7749, -122.4194, 'San Francisco Hub'),
       (34.0522, -118.2437, 'Los Angeles Hub');

INSERT INTO robots (available, battery, hub_id, latitude, longitude, max_weight, speed, price, robot_type)
VALUES (TRUE, 100, 1, 40.7128, -74.0060, 50.0, 10, 15.0, 'robot'),
       (TRUE, 100, 1, 40.7128, -74.0060, 20.0, 15, 12.0, 'drone'),
       (TRUE, 90, 2, 37.7749, -122.4194, 60.0, 12, 18.0, 'robot'),
       (TRUE, 95, 2, 37.7749, -122.4194, 25.0, 20, 14.0, 'drone'),
       (FALSE, 80, 3, 34.0522, -118.2437, 55.0, 11, 16.0, 'robot'),
       (TRUE, 100, 3, 34.0522, -118.2437, 30.0, 18, 13.0, 'drone');
