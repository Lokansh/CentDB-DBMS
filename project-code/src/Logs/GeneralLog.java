package Logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralLog {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String GENERAL_LOG_FILE_PATH = LOGS_DIRECTORY + "generalLog.txt";

    public GeneralLog() {
        init();
    }

    private void init() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
    }

    private void logData(String executionTime, String dbState) {
        File generalLog = new File(GENERAL_LOG_FILE_PATH);
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
        logData(executionTime, dbState);
    }
}
// DB state is the number of tables and records in the database at any instant