package de.clsky.orderservice.testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresDB extends PostgreSQLContainer<PostgresDB> {
    public PostgresDB() {
        super("postgres:14");
    }
}
