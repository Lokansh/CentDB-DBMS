package loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class QueryLogger {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String QUERY_LOG_FILE_PATH = LOGS_DIRECTORY + "queryLog.txt";

    static {
        init();
    }

    private static void init() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdir();
        }
    }

    public static void logQueryData(String queryType, String user, String database, String table, String query, String status, Instant timestamp) {
        File queryLogFile = new File(QUERY_LOG_FILE_PATH);
        boolean queryLogExists = queryLogFile.exists();

        try (FileWriter writer = new FileWriter(queryLogFile, true)) {

            if (!queryLogExists) {
                writer.append("Query Type | User | Database | Table | Query | Status | Timestamp\n");
            }
            String queryLog = queryType + " | " + user + " | " + database + " | " + table + " | " + query + " | " + status + " | " + timestamp;
            writer.append(queryLog).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}