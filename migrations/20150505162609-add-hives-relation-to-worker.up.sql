ALTER TABLE workers
  ADD COLUMN hive_uuid uuid REFERENCES hives (uuid),
  ADD COLUMN worker_id_string VARCHAR(1024);

CREATE UNIQUE INDEX unique_workers_worker_id_string ON workers (worker_id_string);
