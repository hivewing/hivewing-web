DROP TABLE IF EXISTS access_tokens;

CREATE TABLE access_tokens (
  name VARCHAR(255) NOT NULL,
  token VARCHAR(88) NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  user_uuid uuid REFERENCES users NOT NULL
);

CREATE TRIGGER set_updated_at_access_tokens
  BEFORE UPDATE ON access_tokens FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_access_tokens ON access_tokens (token);
CREATE UNIQUE INDEX unique_user_uuid_and_name ON access_tokens (user_uuid, name);
CREATE INDEX access_tokens_by_user_uuid ON access_tokens (user_uuid);
