package loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralLogger {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String GENERAL_LOG_FILE_PATH = LOGS_DIRECTORY + "generalLog.txt";

    static {
        init();
    }

    private static void init() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdir();
        }
    }

    public static void logGeneralData(String executionTime, String dbState) {
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
}
