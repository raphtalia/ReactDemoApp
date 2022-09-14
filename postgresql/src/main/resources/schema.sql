CREATE SCHEMA IF NOT EXISTS galadriel;

CREATE TABLE IF NOT EXISTS galadriel.users (
    user_id serial PRIMARY KEY,
    username VARCHAR(31) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS galadriel.refresh_tokens (
    token VARCHAR(255) PRIMARY KEY,
    user_id INT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES galadriel.users (user_id)
);

-- Refresh token expiration
CREATE FUNCTION galadriel.refresh_token_ttl() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM galadriel.refresh_tokens WHERE timestamp < NOW() - INTERVAL '10 SECOND';
    RETURN NEW;
END;
$$;

CREATE TRIGGER refresh_token_ttl_trigger
    AFTER INSERT ON galadriel.refresh_tokens
    EXECUTE PROCEDURE galadriel.refresh_token_ttl();