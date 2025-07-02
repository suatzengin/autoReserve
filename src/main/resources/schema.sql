CREATE TABLE car_type (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    total_stock INT NOT NULL
);

CREATE TABLE reservation (
    id VARCHAR(36) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    car_type_id VARCHAR(36)
);
