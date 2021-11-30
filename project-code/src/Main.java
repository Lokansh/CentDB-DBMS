import QueryImplementation.InsertQuery;
import QueryImplementation.QueryOperations;
import QueryImplementation.UseQuery;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static String globalDBDirectoryPath = "";//Static global database directory address

    public static void main(String[] args) throws IOException {
        // accept user input
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Query-------");
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);

        QueryOperations obj = new QueryOperations();
        UseQuery useQueryObj = new UseQuery();
        globalDBDirectoryPath = useQueryObj.useDatabase(userArgument);
        System.out.println("globalDBDirectoryPath->" + globalDBDirectoryPath);
        obj.createSchema("create table table1 (id int(10),name varchar(25));",globalDBDirectoryPath);
        // obj.selectTableQuery(userArgument);


        //obj.createDatabase(userArgument);
        globalDBDirectoryPath = useQueryObj.useDatabase(userArgument);
        System.out.println("globalDBDirectoryPath->" + globalDBDirectoryPath);

        System.out.println("Enter Insert query-------");
        String userArgument2 = s.nextLine();

        InsertQuery insertQueryObj = new InsertQuery();
        insertQueryObj.insertQuery(userArgument2,globalDBDirectoryPath);

        //obj.selectTableQuery(userArgument);
        

    }
}