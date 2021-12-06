package QueryImplementation;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryExecutor {

    public static String globalDBDirectoryPath = "";    //Static global database directory address

    private final Scanner scanner;

    public QueryExecutor(Scanner scanner) {
        this.scanner = scanner;
    }

    public void queryExecute(){
        try {
            // accept user input
            String userArgument = null;
            System.out.println("Enter Query-------");
            userArgument = scanner.nextLine();
            userArgument = userArgument.trim();
            System.out.println("Input query is:" + userArgument);

            QueryOperations obj = new QueryOperations();
            constants_QI cons = new constants_QI();

            // Pattern Matcher for CREATE DATABASE
            Pattern createDBPattern = Pattern.compile(cons.CREATE_DB, Pattern.DOTALL);
            Matcher createDBMatcher = createDBPattern.matcher(userArgument);
            if (createDBMatcher.find()) {
                System.out.println(createDBMatcher.group(0).trim());
                obj.createDatabase(userArgument);
            }

            // Pattern Matcher for USE
            Pattern usePattern = Pattern.compile(cons.USE_DB, Pattern.DOTALL);
            Matcher useMatcher = usePattern.matcher(userArgument);
            if (useMatcher.find()) {
                System.out.println(useMatcher.group(0).trim());
                UseQuery useQueryObj = new UseQuery();
                globalDBDirectoryPath = useQueryObj.useDatabase(userArgument);
                System.out.println("globalDBDirectoryPath->" + globalDBDirectoryPath);
            }

            // Pattern Matcher for CREATE TABLE
            Pattern createTBPattern = Pattern.compile(cons.CREATE_TB, Pattern.DOTALL);
            Matcher createTBmatcher = createTBPattern.matcher(userArgument);
            if (createTBmatcher.find()) {
                System.out.println(createTBmatcher.group(0).trim());
                //obj.createSchema("create table table1 (id int(10),name varchar(25));",globalDBDirectoryPath);
                obj.createSchema(userArgument, globalDBDirectoryPath);
            }

            // Pattern Matcher for INSERT INTO TABLE
            Pattern insertPattern = Pattern.compile(cons.INSERT_TB, Pattern.DOTALL);
            Matcher insertMatcher = insertPattern.matcher(userArgument);
            if (insertMatcher.find()) {
                System.out.println(insertMatcher.group(0).trim());
                
                InsertQuery insertQueryObj = new InsertQuery();
                insertQueryObj.insertQuery(userArgument, globalDBDirectoryPath);
            }

            // Pattern Matcher for SELECT
            Pattern selectPattern = Pattern.compile(cons.SELECT_TB, Pattern.DOTALL);
            Matcher selectMatcher = selectPattern.matcher(userArgument);
            if (selectMatcher.find()) {
                System.out.println(selectMatcher.group(0).trim());
                obj.selectTableQuery(userArgument);
            }

            // Pattern Matcher for UPDATE
            Pattern updatePattern = Pattern.compile(cons.UPDATE_TB, Pattern.DOTALL);
            Matcher updateMatcher = updatePattern.matcher(userArgument);
            if (updateMatcher.find()) {
                System.out.println(updateMatcher.group(0).trim());
                UpdateQuery updateObj = new UpdateQuery();
                updateObj.updateQuery(userArgument, globalDBDirectoryPath);
            }

            // Pattern Matcher for DELETE
            Pattern deletePattern = Pattern.compile(cons.DELETE_ROW, Pattern.DOTALL);
            Matcher deleteMatcher = deletePattern.matcher(userArgument);
            if (deleteMatcher.find()) {
                System.out.println(deleteMatcher.group(0).trim());
            }

            // Pattern Matcher for DROP
            Pattern dropPattern = Pattern.compile(cons.DROP_TB, Pattern.DOTALL);
            Matcher dropMatcher = dropPattern.matcher(userArgument);
            if (dropMatcher.find()) {
                System.out.println(dropMatcher.group(0).trim());
            }
        } catch (Exception e) {e.getStackTrace();}
    }
}
