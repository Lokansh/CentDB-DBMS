import QueryImplementation.QueryOperations;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // accept user input
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println(userArgument);
        QueryOperations obj = new QueryOperations();
        obj.createDatabase(userArgument);

    }
}
