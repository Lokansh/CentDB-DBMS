package loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class EventLogger {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String EVENT_LOG_FILE_PATH = LOGS_DIRECTORY + "eventLog.txt";

    public EventLogger() {
        init();
    }

    private void init() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
    }


    /*
    String query_type, String database_name,
                           String table_name, String column_affected, String row_affected,
                           String constraint

     */
    public static void eventLogData(String executionTime, Instant dbState) {
        File generalLog = new File(EVENT_LOG_FILE_PATH);
        boolean generalLogExists = generalLog.exists();

        try (FileWriter writer = new FileWriter(generalLog, true)) {

            if (!generalLogExists) {
                writer.append("DB State | Execution Time\n");
            }
            String queryLog = executionTime + " | " + dbState;
            writer.append(queryLog).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void log(String executionTime, String dbState) {
        eventLogData(executionTime, dbState);
    }
}