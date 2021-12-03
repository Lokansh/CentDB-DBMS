package QueryImplementation.transactions;

import services.DatabaseService;

import java.io.File;

public class RollbackQuery {

    public void rollbackTransaction() {

        if (!DatabaseService.isTransactionRunning) {
            System.out.println("No transaction running. Please start a transaction first");
            return;
        }

        String selectedDatabase = new File(DatabaseService.CURRENT_DATABASE_PATH).getName();
        String tempDatabasePath = DatabaseService.getTempDatabaseFolderPath() + selectedDatabase;
        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + selectedDatabase;

        File tempDatabase = new File(tempDatabasePath);
        boolean dbExists = tempDatabase.isDirectory();
        if (!dbExists) {
            System.out.println("Database " + tempDatabase + " does not exist!");
            return;
        }
        File[] dbTables = tempDatabase.listFiles();
        if (dbTables == null) {
            System.out.println("Database " + tempDatabase + " failed to delete!");
            return;
        }
        for (File table : dbTables) {
            boolean tableDelete = table.delete();
            if (!tableDelete) {
                System.out.println("Failed to delete tables of the database " + tempDatabase + " Please try again!");
            }
        }
        boolean dbDelete = tempDatabase.delete();
        if (dbDelete) {
            DatabaseService.CURRENT_DATABASE_PATH = realDatabasePath;
            DatabaseService.isTransactionRunning = false;
            System.out.println("Transaction rollback successful!");
        } else {
            System.out.println("Error: Database " + tempDatabase + " deletion error!");
        }
    }

}
