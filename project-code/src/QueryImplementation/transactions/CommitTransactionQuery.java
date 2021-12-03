package QueryImplementation.transactions;

import java.io.File;
import services.DatabaseService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommitTransactionQuery {

    public void commitTransaction() {

        String selectedDatabase = new File(DatabaseService.CURRENT_DATABASE_PATH).getName();
        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + selectedDatabase;
        String tempDatabasePath = DatabaseService.getTempDatabaseFolderPath() + selectedDatabase;

        File tempDatabase = new File(tempDatabasePath);
      //  DatabaseService.CURRENT_DATABASE_PATH = tempDatabase;

        final String inMemoryDatabasePath = DatabaseService.CURRENT_DATABASE_PATH ;//+ "/" + this.useDatabaseName + "/";
        final File inMemoryDatabase = new File(inMemoryDatabasePath);
        final boolean isInMemoryDatabaseExists = inMemoryDatabase.isDirectory();

        if (!isInMemoryDatabaseExists) {
            System.out.println("Error: Database " + inMemoryDatabase + " does not exists!");

        }
        final String inServerDatabasePath = DatabaseService.CURRENT_DATABASE_PATH;// + "/" + this.useDatabaseName + "/";
        final File inServerDatabase = new File(inServerDatabasePath);
        final boolean isInServerDatabaseExists = inServerDatabase.isDirectory();
        if (!isInServerDatabaseExists) {
            System.out.println("Error: Database does not exists");
        }

        final File[] allTables = inServerDatabase.listFiles();
        if (allTables == null) {
            System.out.println("Error: Database failed to delete!");
        }
        for (final File table : allTables) {
            final boolean isTableDeleted = table.delete();
            if (!isTableDeleted) {
                System.out.println("Error: Failed to delete tables of the database ");
            }
        }
        final boolean isInServerDatabaseDeleted = inServerDatabase.delete();
        if (!isInServerDatabaseDeleted) {
            System.out.println("Error: Database " + inServerDatabase + " deletion error!");
        }
        if (inServerDatabase.mkdir()) {
            final File[] inMemoryDatabaseTables = inMemoryDatabase.listFiles();
            if (inMemoryDatabaseTables == null) {
                System.out.println("Error: Something went wrong. Transaction execution failed!");
            }
            for (final File table : inMemoryDatabaseTables) {
                final Path src = Paths.get(inMemoryDatabasePath + table.getName());
                final Path dest = Paths.get(inServerDatabasePath + table.getName());
                try {
                    Files.copy(src, dest);
                } catch (final IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
            for (final File table : inMemoryDatabaseTables) {
                final boolean isTableDeleted = table.delete();
                if (!isTableDeleted) {
                    System.out.println("Error: Failed to delete tables of the database " + inMemoryDatabase + " Please try again!");
                }
            }
            final boolean isInMemoryDatabaseDeleted = inMemoryDatabase.delete();
            if (!isInMemoryDatabaseDeleted) {
                System.out.println("Error: Database " + inMemoryDatabase + " deletion error! ");
            }
            System.out.println("Transaction committed!");

        } else {
            System.out.println("Error: Something went wrong. Transaction execution failed!");
        }
    }
}