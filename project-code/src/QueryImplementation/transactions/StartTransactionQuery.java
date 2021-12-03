package QueryImplementation.transactions;

import services.DatabaseService;

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

    }
}
