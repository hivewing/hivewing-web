DROP TABLE IF EXISTS users;

CREATE TABLE users (
  uuid uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
  encrypted_password VARCHAR(64),
  updated_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  email VARCHAR(255) NOT NULL
);

CREATE TRIGGER set_updated_at_users
  BEFORE UPDATE ON users FOR EACH ROW
  EXECUTE PROCEDURE set_updated_at_column();

CREATE UNIQUE INDEX unique_users_email ON users (email);
