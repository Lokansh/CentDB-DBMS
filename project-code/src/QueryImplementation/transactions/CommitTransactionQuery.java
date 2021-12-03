package QueryImplementation.transactions;

import QueryImplementation.ExceptionHandler;
import loggers.EventLogger;
import services.DatabaseService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class CommitTransactionQuery {

    public void commitTransaction() throws ExceptionHandler {

        Instant instant = Instant.now();

        if (!DatabaseService.isTransactionRunning) {
            System.out.println("No transaction running. Please start a transaction first");
            String eventMessage = "No transaction running" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }

        String selectedDatabase = new File(DatabaseService.CURRENT_DATABASE_PATH).getName();
        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + selectedDatabase;
        String tempDatabasePath = DatabaseService.getTempDatabaseFolderPath() + selectedDatabase;

        File tempDatabase = new File(tempDatabasePath);
        boolean tempDatabaseExists = tempDatabase.isDirectory();

        if (!tempDatabaseExists) {
            System.out.println("Database " + tempDatabase + " does not exists!");
            String eventMessage = "Temporary Database " + tempDatabase + " does not exists!" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }

        File realDatabase = new File(realDatabasePath);
        boolean realDatabaseExists = realDatabase.isDirectory();
        if (!realDatabaseExists) {
            System.out.println("Database does not exists");
            String eventMessage = "Real Database " + realDatabase + " does not exists!" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }

        File[] allTables = realDatabase.listFiles();
        if (allTables == null) {
            System.out.println("Database is empty!");
            String eventMessage = "Database " + realDatabase + " is empty!" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }
        for (File table : allTables) {
            boolean isTableDeleted = table.delete();
            if (!isTableDeleted) {
                System.out.println("Failed to delete tables of the database ");
                String eventMessage = "Failed to delete tables of the database " + realDatabase + " | " +
                        "Time of Execution: " + instant + "ms";
                EventLogger.eventLogData(eventMessage, instant);
                throw new ExceptionHandler(eventMessage);
            }
        }
        boolean isRealDatabaseDeleted = realDatabase.delete();
        if (!isRealDatabaseDeleted) {
            System.out.println("Database " + realDatabase + " deletion error!");
            String eventMessage = "Database " + realDatabase + " deletion error!" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }
        if (realDatabase.mkdir()) {
            File[] tempTables = tempDatabase.listFiles();
            if (tempTables == null) {
                System.out.println("Database is empty");
                String eventMessage = "Temporary database " + tempDatabase + " is empty" + " | " +
                        "Time of Execution: " + instant + "ms";
                EventLogger.eventLogData(eventMessage, instant);
                throw new ExceptionHandler(eventMessage);
            }
            for (File table : tempTables) {
                Path sourcePath = Paths.get(tempDatabasePath + "/" + table.getName());
                Path destPath = Paths.get(realDatabasePath + "/" + table.getName());
                try {
                    Files.copy(sourcePath, destPath);
                } catch (Exception e) {
                    throw new ExceptionHandler(e.getMessage());
                }
            }
//            TODO: REPLACE WITH DROP DATABASE
            for (File table : tempTables) {
                boolean isTableDeleted = table.delete();
                if (!isTableDeleted) {
                    System.out.println("Failed to delete tables of the database " + tempDatabase + " Please try again!");
                    String eventMessage = "Failed to delete tables of the database " + tempDatabase + " | " +
                            "Time of Execution: " + instant + "ms";
                    EventLogger.eventLogData(eventMessage, instant);
                    throw new ExceptionHandler(eventMessage);
                }
            }
            boolean isTempDatabaseDeleted = tempDatabase.delete();
            if (!isTempDatabaseDeleted) {
                System.out.println("Database " + tempDatabase + " deletion error! ");
                String eventMessage = "Database " + tempDatabase + " deletion error!" + " | " +
                        "Time of Execution: " + instant + "ms";
                EventLogger.eventLogData(eventMessage, instant);
                throw new ExceptionHandler(eventMessage);
            }
            DatabaseService.CURRENT_DATABASE_PATH = realDatabasePath;
            DatabaseService.isTransactionRunning = false;
            System.out.println("Transaction committed!");
            String eventMessage = "Commit transaction on " + selectedDatabase + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
        } else {
            System.out.println("Failed to commit");
            String eventMessage = "Commit Failed" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage, instant);
            throw new ExceptionHandler(eventMessage);
        }
    }
}