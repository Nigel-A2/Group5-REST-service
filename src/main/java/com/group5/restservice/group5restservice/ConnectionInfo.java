package com.group5.restservice.group5restservice;

/**
 * Stores connection information
 * */
class ConnectionInfo {
    private final String protocol, host, port, database, username, password;

    // Constructor
    ConnectionInfo(String protocol, String host, String port, String database, String username, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets database protocol for the connection
     * @return The database protocol
     * */
    String getProtocol() {
        return protocol;
    }

    /**
     * Gets the database server host
     * @return The host name
     * */
    String getHost() {
        return host;
    }

    /**
     * Get the database server port
     * @return The database server port
     * */
    String getPort() {
        return port;
    }

    /**
     * Gets the name of the database to connect to
     * @return The database name
     * */
    String getDatabase() {
        return database;
    }

    /**
     * Gets the username for this connection
     * @return The database user name
     * */
    String getUsername() {
        return username;
    }

    /**
     * Gets the password for this connection
     * @return The database user's password
     * */
    String getPassword() {
        return password;
    }

    /**
     * Get the compiled JDBC connection string
     * @return the connection string to use
     * */
    String getConnectionString() {
        return String.format(
                "jdbc:%s://%s:%s/%s",
                getProtocol(), getHost(), getPort(), getDatabase()
        );
    }
}
