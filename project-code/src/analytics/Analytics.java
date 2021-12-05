package analytics;

import QueryImplementation.constants_QI;
import services.DatabaseService;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analytics {
    private static final String LOGS_DIRECTORY = "logs/";
    private static final String QUERY_LOG_FILE_PATH = LOGS_DIRECTORY + "queryLog.txt";

    private static final String ANALYTICS_FOLDER_PATH = "analytics/";
    private static final String ANALYTICS_COUNT_QUERIES_FILE_PATH = ANALYTICS_FOLDER_PATH + "analytics_queries.txt";
    private static final String ANALYTICS_UPDATES_FILE_PATH = ANALYTICS_FOLDER_PATH + "analytics_updates.txt";

    private final Scanner scanner;

    public Analytics(Scanner scanner) {
        this.scanner = scanner;
    }

    public void countQueries(String databaseName) {

        HashMap<String, Integer> countQueriesByDatabase = new HashMap<>();

        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + databaseName;

        File dir = new File(realDatabasePath);
        if (!dir.isDirectory()) {
            System.out.println("Failure : Database does not exists");
            return;
        }

        if (!logExists()) {
            System.out.println("Failure : No data exists");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(QUERY_LOG_FILE_PATH))) {
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
            return;
        }

        StringBuilder builder = new StringBuilder();

        countQueriesByDatabase.forEach((user, queryCount) -> {
            String log = "user " + user + " submitted " + queryCount + " queries on " + databaseName;
            System.out.println(log);
            builder.append(log).append("\n");
        });

        try (FileWriter fileWriter = new FileWriter(ANALYTICS_COUNT_QUERIES_FILE_PATH, true)) {
            fileWriter.append(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void countSuccessUpdateQueries(String databaseName) {

        HashMap<String, Integer> countSuccessfulUpdates = new HashMap<>();

        String realDatabasePath = DatabaseService.getRootDatabaseFolderPath() + databaseName;

        File dir = new File(realDatabasePath);
        if (!dir.isDirectory()) {
            System.out.println("Failure : Database does not exists");
            return;
        }

        if (!logExists()) {
            System.out.println("Failure : No data exists");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(QUERY_LOG_FILE_PATH))) {
            bufferedReader.readLine();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String dbName = tokens[2].trim();
                String status = tokens[5].trim();
                String table = tokens[3].trim();
                String queryType = tokens[0].trim();


                if (databaseName.equalsIgnoreCase(dbName) && queryType.equalsIgnoreCase("update") && status.equalsIgnoreCase("success")) {
                    if (countSuccessfulUpdates.containsKey(table)) {
                        countSuccessfulUpdates.put(table, countSuccessfulUpdates.get(table) + 1);
                    } else {
                        countSuccessfulUpdates.put(table, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();

        countSuccessfulUpdates.forEach((table, queryCount) -> {
            String log = "Total " + queryCount + " Update operations are performed on " + table;
            System.out.println("Total " + queryCount + " Update operations are performed on " + table);
            builder.append(log).append("\n");
        });

        try (FileWriter fileWriter = new FileWriter(ANALYTICS_UPDATES_FILE_PATH, true)) {
            fileWriter.append(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean logExists() {
        File logsDir = new File(QUERY_LOG_FILE_PATH);
        return logsDir.exists();
    }


    public void performAnalytics() {
        System.out.println("Enter Query-------");
        String userArgument = scanner.nextLine();
        userArgument = userArgument.trim();
        System.out.println("Input query is:" + userArgument);

        constants_QI cons = new constants_QI();

        File analyticsFolder = new File(ANALYTICS_FOLDER_PATH);
        if (!analyticsFolder.exists()) {
            boolean analyticsFolderCreated = analyticsFolder.mkdir();
            if (!analyticsFolderCreated) {
                System.out.println("Analytics folder could not be created");
                return;
            }
        }

        Pattern countQueriesPattern = Pattern.compile(cons.ANALYTICS_COUNT_QUERIES, Pattern.CASE_INSENSITIVE);
        Matcher countQueriesMatcher = countQueriesPattern.matcher(userArgument);
        if (countQueriesMatcher.find()) {
            String databaseName = (countQueriesMatcher.group(1).trim());
            countQueries(databaseName);
        }

        Pattern countUpdateQueriesMatcherPattern = Pattern.compile(cons.ANALYTICS_UPDATE_QUERIES, Pattern.CASE_INSENSITIVE);
        Matcher countUpdateQueriesMatcher = countUpdateQueriesMatcherPattern.matcher(userArgument);
        if (countUpdateQueriesMatcher.find()) {
            String databaseName = (countUpdateQueriesMatcher.group(1).trim());
            countSuccessUpdateQueries(databaseName);
        }
    }
}