package analytics;

import QueryImplementation.constants_QI;
import services.DatabaseService;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analytics {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String QUERY_LOG_FILE_PATH = LOGS_DIRECTORY + "queryLog.txt";

    private final Scanner scanner;



    public Analytics(Scanner scanner) {
        this.scanner = scanner;
    }

    public void countQueries(String databaseName) {

        HashMap<String, Integer> countQueriesByDatabase = new HashMap<>();

        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + databaseName;

        File dir = new File(realDatabasePath);
        if(!dir.isDirectory()){
            System.out.println("Failure : Database does not exists");
            return;
        }

        if(!logExists()){
            System.out.println("Failure : No data exists");
            return;
        }

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(QUERY_LOG_FILE_PATH))) {
            bufferedReader.readLine();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String user = tokens[1].trim();
                String dbName = tokens[2].trim();
                if (databaseName.equalsIgnoreCase(dbName)) {
                    if (countQueriesByDatabase.containsKey(user)) {
                        countQueriesByDatabase.put(user, countQueriesByDatabase.get(user) + 1);
                    } else {
                        countQueriesByDatabase.put(user, 1);
                    }
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
        countQueriesByDatabase.forEach((user, queryCount) -> {
                System.out.println("user " + user + " submitted " + queryCount + " queries on " + databaseName);
            });

    }



    public void countSuccessUpdateQueries(String databaseName) {

        HashMap<String, Integer> countQueriesByDatabase = new HashMap<>();

        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + databaseName;

        File dir = new File(realDatabasePath);
        if(!dir.isDirectory()){
            System.out.println("Failure : Database does not exists");
            return;
        }

        if(!logExists()){
            System.out.println("Failure : No data exists");
            return;
        }

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(QUERY_LOG_FILE_PATH))) {
            bufferedReader.readLine();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String user = tokens[1].trim();
                String dbName = tokens[2].trim();
                if (databaseName.equalsIgnoreCase(dbName)) {
                    if (countQueriesByDatabase.containsKey(user)) {
                        countQueriesByDatabase.put(user, countQueriesByDatabase.get(user) + 1);
                    } else {
                        countQueriesByDatabase.put(user, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        countQueriesByDatabase.forEach((user, queryCount) -> {
            System.out.println("user " + user + " submitted " + queryCount + " queries on " + databaseName);
        });

    }


    private boolean logExists() {
        File logsDir = new File(QUERY_LOG_FILE_PATH);
           return logsDir.exists();
    }


    public void performAnalytics(){
   // String userArgument = null;
    System.out.println("Enter Query-------");
    String userArgument = scanner.nextLine();
    userArgument = userArgument.trim();
    System.out.println("Input query is:" + userArgument);

    constants_QI cons = new constants_QI();

    // Pattern Matcher for CREATE DATABASE
    Pattern createDBPattern = Pattern.compile(cons.ANALYTICS_COUNT_QUERIES, Pattern.CASE_INSENSITIVE);
    Matcher createDBMatcher = createDBPattern.matcher(userArgument);
    if (createDBMatcher.find()) {
        String databaseName = (createDBMatcher.group(1).trim());
        System.out.println(databaseName);
        countQueries(databaseName);
    }
    }
}