CREATE TABLE IF NOT EXISTS players
(
    id INT NOT NULL AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS names
(
    id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    player_id INT NOT NULL,
    PRIMARY KEY (id),
    INDEX fk_names_players_idx (player_id ASC) VISIBLE,
    CONSTRAINT fk_names_players
        FOREIGN KEY (player_id)
        REFERENCES players (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);