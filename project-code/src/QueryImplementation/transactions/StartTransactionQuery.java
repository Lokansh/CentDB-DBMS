package QueryImplementation.transactions;

import QueryImplementation.ExceptionHandler;
import loggers.EventLogger;
import services.DatabaseService;
import services.TableService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StartTransactionQuery {

    public void startTransaction() throws ExceptionHandler {

        Instant instant = Instant.now();

        if (DatabaseService.CURRENT_DATABASE_PATH == null) {
            System.out.println("No database selected");
            String eventMessage = "No database selected" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
            // return;
        }
        String selectedDatabase = new File(DatabaseService.CURRENT_DATABASE_PATH).getName();
        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + selectedDatabase;
        String tempDatabasePath = DatabaseService.getTempDatabaseFolderPath() + selectedDatabase;

        File tempDatabase = new File(tempDatabasePath);

//        TODO: REPLACE WITH DELETE DATABASE;
        deleteDatabase_REMOVEME(tempDatabase);

        if (tempDatabase.mkdirs()) {
            File realDatabase = new File(realDatabasePath);
            File[] potentialTables = realDatabase.listFiles();
            List<File> tables = null;
            if (potentialTables != null) {
                tables = Arrays.stream(potentialTables).filter(TableService::isTable).collect(Collectors.toList());
            }

            if (tables == null) {
                System.out.println("Database is empty. Transaction can't start");
                String eventMessage = "Database is empty. Transaction can't start" + " | " +
                        "Time of Execution: " + instant + "ms";
                EventLogger.eventLogData(eventMessage, instant);
                throw new ExceptionHandler(eventMessage);
                //return;
            }

            for (File table : tables) {
                Path sourcePath = Paths.get(realDatabasePath + "/" + table.getName());
                Path destPath = Paths.get(tempDatabasePath + "/" + table.getName());
                try {
                    Files.copy(sourcePath, destPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    new RollbackQuery().rollbackTransaction();
                    return;
                }
            }
            DatabaseService.CURRENT_DATABASE_PATH = tempDatabasePath;
            DatabaseService.isTransactionRunning = true;
            System.out.println("Transaction active for database " + selectedDatabase);
            String eventMessage = "Transaction active for database " + selectedDatabase + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
        } else {
            System.out.println("Could not start transaction. Please try again");
            String eventMessage = "Could not start transaction. Please try again" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }
    }

    private static void deleteDatabase_REMOVEME(File database) {
        if (database.isDirectory()) {
            File[] files = database.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
        database.delete();
    }

}
