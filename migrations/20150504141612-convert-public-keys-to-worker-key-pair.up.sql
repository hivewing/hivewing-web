DROP TRIGGER IF EXISTS set_updated_at_worker_public_keys ON worker_public_keys;
DROP INDEX IF EXISTS unique_worker_public_keys;
DROP INDEX IF EXISTS unique_worker_public_keys_and_worker;
DROP TABLE IF EXISTS worker_public_keys;
DROP TABLE IF EXISTS worker_key_pairs;

CREATE TABLE worker_key_pairs (
  uuid uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  worker_uuid uuid REFERENCES workers (uuid) NOT NULL,
  public_key TEXT NOT NULL,
  private_key TEXT NOT NULL
);

CREATE TRIGGER set_updated_at_worker_key_pairs
  BEFORE UPDATE ON worker_key_pairs FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_worker_key_pairs ON worker_key_pairs (worker_uuid);
