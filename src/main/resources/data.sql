INSERT INTO car_type (id, name, total_stock) VALUES
  ('1', 'Sedan', 10),
  ('2', 'SUV', 5),
  ('3', 'Van', 3);

INSERT INTO reservation (id, customer_name, start_datetime, end_datetime, status, car_type_id) VALUES
  ('a1111111-1111-1111-1111-111111111111', 'John Doe', '2025-07-01T10:00:00', '2025-07-03T10:00:00', 'RESERVED', '1'),
  ('a2222222-2222-2222-2222-222222222222', 'Alice Smith', '2025-07-02T15:00:00', '2025-07-05T15:00:00', 'RESERVED', '2');
