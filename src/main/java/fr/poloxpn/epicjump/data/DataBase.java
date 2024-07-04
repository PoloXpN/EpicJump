package fr.poloxpn.epicjump.data;

import fr.poloxpn.epicjump.EpicJump;

public class DataBase {

    //TODO: Implement the database connection

        private final String host;
        private final String database;
        private final String username;
        private final String password;
        private final int port;
        private final EpicJump plugin;

        public DataBase(String host, String database, String username, String password, int port, EpicJump plugin) {
            this.host = host;
            this.database = database;
            this.username = username;
            this.password = password;
            this.port = port;
            this.plugin = plugin;
        }

        public String getHost() {
            return host;
        }

        public String getDatabase() {
            return database;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public int getPort() {
            return port;
        }

}
