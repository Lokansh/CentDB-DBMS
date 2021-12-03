package QueryImplementation.transactions;

import services.DatabaseService;

import java.io.File;

public class RollbackQuery {

    private void  rollbackTransaction() {

        final String realDB = DatabaseService.CURRENT_DATABASE_PATH;// + this.useDatabaseName;
        final File tempDB = new File(realDB);
        final boolean dbExists = tempDB.isDirectory();
        if (!dbExists) {
            System.out.println("Error: Database " + tempDB + " does not exists!");
        }
        final File[] dbTables = tempDB.listFiles();
        if (dbTables == null) {
            System.out.println("Error: Database " + tempDB + " failed to delete!" );
        }
        for (final File table : dbTables) {
            final boolean tableDelete = table.delete();
            if (!tableDelete) {
                System.out.println("Error: Failed to delete tables of the database " + tempDB + " Please try again!" );
            }
        }
        final boolean dbDelete= tempDB.delete();
        if (dbDelete) {
            System.out.println("Transaction rollback!" );
        } else {
            System.out.println("Error: Database " + tempDB + " deletion error!");
        }
    }

}
