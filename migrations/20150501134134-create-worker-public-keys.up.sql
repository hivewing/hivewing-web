DROP TABLE IF EXISTS worker_public_keys;

CREATE TABLE worker_public_keys (
  uuid uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  worker_uuid uuid REFERENCES workers (uuid) NOT NULL,
  key TEXT NOT NULL
);

CREATE TRIGGER set_updated_at_worker_public_keys
  BEFORE UPDATE ON worker_public_keys FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_worker_public_keys ON worker_public_keys (key);
CREATE UNIQUE INDEX unique_worker_public_keys_and_worker ON worker_public_keys (key, worker_uuid);
