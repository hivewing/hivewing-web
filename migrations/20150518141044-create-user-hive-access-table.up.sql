DROP TABLE IF EXISTS hive_access;

CREATE TABLE hive_access (
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  user_uuid uuid REFERENCES users NOT NULL,
  hive_uuid uuid REFERENCES hives NOT NULL
);

CREATE TRIGGER set_updated_at_hive_access
  BEFORE UPDATE ON hive_access FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_hive_user_access ON hive_access (user_uuid, hive_uuid);
