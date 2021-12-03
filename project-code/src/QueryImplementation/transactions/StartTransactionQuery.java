package QueryImplementation.transactions;

import services.DatabaseService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartTransactionQuery {

    public void startTransaction() {
        if (DatabaseService.CURRENT_DATABASE_PATH == null) {
            System.out.println("No database selected");
            return;
        }

        String realDatabase = DatabaseService.CURRENT_DATABASE_PATH;
        String databaseName = realDatabase.substring(realDatabase.lastIndexOf("/") + 1);
        String tempDatabase = DatabaseService.TEMP_DATABASE_FOLDER_PATH + databaseName;

        DatabaseService.CURRENT_DATABASE_PATH = tempDatabase;

        final File tempDB = new File(tempDatabase);
        if (tempDB.mkdir()) {
            final File realDB = new File(realDatabase);
            final File[] realDBTables = realDB.listFiles();
            if (realDBTables == null) {
                System.out.println("Tables doesn't exists. Transaction can't start.");
            }
            for (final File table : realDBTables) {
                final Path src = Paths.get(realDatabase + table.getName());
                final Path dest = Paths.get(tempDatabase + table.getName());
                try {
                    Files.copy(src, dest);
                } catch (final IOException e) {
                    System.out.println(e);
                }
            }
            System.out.printf("Transaction started for database");
        } else {
            System.out.println("Transaction execution failed");
        }
    }

}
