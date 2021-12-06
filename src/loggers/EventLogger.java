package loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventLogger {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String EVENT_LOG_FILE_PATH = LOGS_DIRECTORY + "eventLog.txt";

    static {
        init();
    }

    public EventLogger() {
        init();
    }

    private static void init() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdir();
        }
    }

    public static void eventLogData(String executionTime) {
        File generalLog = new File(EVENT_LOG_FILE_PATH);
        boolean generalLogExists = generalLog.exists();

        try (FileWriter writer = new FileWriter(generalLog, true)) {

            if (!generalLogExists) {
                writer.append("DB State | Execution Time\n");
            }
            String queryLog = executionTime;
            writer.append(queryLog).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}