import QueryImplementation.InsertQuery;
import QueryImplementation.QueryOperations;
import QueryImplementation.UseQuery;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static String globalDBDirectoryPath = "";    //Static global database directory address

    public static void main(String[] args) throws IOException {
        // accept user input
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Query-------");
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);

        QueryOperations obj = new QueryOperations();

        // Pattern Matcher for CREATE DATABASE
        Pattern createDBPattern = Pattern.compile("^create database.*$", Pattern.DOTALL);
        Matcher createDBMatcher = createDBPattern.matcher(userArgument);
        if (createDBMatcher.find()) {
            System.out.println(createDBMatcher.group(0).trim());
            obj.createDatabase(userArgument);
        }

        // Pattern Matcher for USE
        Pattern usePattern = Pattern.compile("^use database.*$", Pattern.DOTALL);
        Matcher useMatcher = usePattern.matcher(userArgument);
        if (useMatcher.find()) {
            System.out.println(useMatcher.group(0).trim());
            UseQuery useQueryObj = new UseQuery();
            globalDBDirectoryPath = useQueryObj.useDatabase(userArgument);
            System.out.println("globalDBDirectoryPath->" + globalDBDirectoryPath);
        }

        // Pattern Matcher for CREATE TABLE
        Pattern createTBPattern = Pattern.compile("^create table.*$", Pattern.DOTALL);
        Matcher createTBmatcher = createTBPattern.matcher(userArgument);
        if (createTBmatcher.find()) {
            System.out.println(createTBmatcher.group(0).trim());
            //obj.createSchema("create table table1 (id int(10),name varchar(25));",globalDBDirectoryPath);
            obj.createSchema(userArgument, globalDBDirectoryPath);
        }

        // Pattern Matcher for INSERT INTO TABLE
        Pattern insertPattern = Pattern.compile("^insert into table.*$", Pattern.DOTALL);
        Matcher insertMatcher = insertPattern.matcher(userArgument);
        if (insertMatcher.find()) {
            System.out.println(insertMatcher.group(0).trim());
            String userArgument2 = s.nextLine();

            InsertQuery insertQueryObj = new InsertQuery();
            insertQueryObj.insertQuery(userArgument2,globalDBDirectoryPath);
        }

        // Pattern Matcher for SELECT
        Pattern selectPattern = Pattern.compile("^select.*$", Pattern.DOTALL);
        Matcher selectMatcher = selectPattern.matcher(userArgument);
        if (selectMatcher.find()) {
            System.out.println(selectMatcher.group(0).trim());
            obj.selectTableQuery(userArgument);
        }

        // Pattern Matcher for UPDATE
        Pattern updatePattern = Pattern.compile("^use.*$", Pattern.DOTALL);
        Matcher updateMatcher = updatePattern.matcher(userArgument);
        if (updateMatcher.find()) {
            System.out.println(updateMatcher.group(0).trim());
        }

        // Pattern Matcher for DELETE
        Pattern deletePattern = Pattern.compile("^delete.*$", Pattern.DOTALL);
        Matcher deleteMatcher = deletePattern.matcher(userArgument);
        if (deleteMatcher.find()) {
            System.out.println(deleteMatcher.group(0).trim());
        }

        // Pattern Matcher for DROP
        Pattern dropPattern = Pattern.compile("^drop table.*$", Pattern.DOTALL);
        Matcher dropMatcher = dropPattern.matcher(userArgument);
        if (dropMatcher.find()) {
            System.out.println(dropMatcher.group(0).trim());
        }
    }
}