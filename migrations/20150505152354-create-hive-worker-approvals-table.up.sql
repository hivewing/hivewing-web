DROP TABLE IF EXISTS hive_worker_approvals;

CREATE TABLE hive_worker_approvals (
  approval BOOLEAN,
  hive_uuid uuid REFERENCES hives (uuid) NOT NULL,
  worker_id_string VARCHAR(2048) NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TRIGGER set_updated_at_hive_worker_approvals
  BEFORE UPDATE ON hive_worker_approvals FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_hive_worker_approval_hive_worker_id ON hive_worker_approvals (hive_uuid, worker_id_string);
