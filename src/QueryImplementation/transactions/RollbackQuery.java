package QueryImplementation.transactions;

import exceptions.ExceptionHandler;
import loggers.EventLogger;
import services.DatabaseService;

import java.io.File;
import java.time.Instant;

public class RollbackQuery {

    public void rollbackTransaction() throws ExceptionHandler {

        Instant instant = Instant.now();

        if (!DatabaseService.isTransactionRunning) {
            System.out.println("No transaction running. Please start a transaction first");
            String eventMessage = "No transaction running. Please start a transaction first" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage);
            throw new ExceptionHandler(eventMessage);
        }

        String selectedDatabase = new File(DatabaseService.CURRENT_DATABASE_PATH).getName();
        String tempDatabasePath = DatabaseService.getTempDatabaseFolderPath() + selectedDatabase;
        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + selectedDatabase;

        File tempDatabase = new File(tempDatabasePath);
        boolean dbExists = tempDatabase.isDirectory();
        if (!dbExists) {
            System.out.println("Database " + tempDatabase + "does not exist");
            String eventMessage = "Database " + tempDatabase + "does not exist" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage);
            throw new ExceptionHandler(eventMessage);
        }
        File[] dbTables = tempDatabase.listFiles();
        if (dbTables == null) {
            System.out.println("Failed to delete database :" + tempDatabase + " failed to delete");
            String eventMessage = "Failed to delete database :" + tempDatabase + " failed to delete" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage);
            throw new ExceptionHandler(eventMessage);
        }
        for (File table : dbTables) {
            boolean tableDelete = table.delete();
            if (!tableDelete) {
                System.out.println("Failed to delete tables of the database :" + tempDatabase);
                String eventMessage = "Failed to delete tables of the database :" + tempDatabase + " | " +
                        "Time of Execution: " + instant + "ms";
                EventLogger.eventLogData(eventMessage);
                throw new ExceptionHandler(eventMessage);
            }
        }
        boolean dbDelete = tempDatabase.delete();
        if (dbDelete) {
            DatabaseService.CURRENT_DATABASE_PATH = realDatabasePath;
            DatabaseService.isTransactionRunning = false;
            System.out.println("Transaction rollback is successful");
            String eventMessage = "Transaction rollback is successful" + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage);
        } else {
            System.out.println("There is error while deleting database :" + tempDatabase);
            String eventMessage = "There is error while deleting database :" + tempDatabase + " | " +
                    "Time of Execution: " + instant + "ms";
            EventLogger.eventLogData(eventMessage);
            throw new ExceptionHandler(eventMessage);
        }
    }

}
