package services;

import exceptions.ExceptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TableService {
    public static boolean isTable(File table) {
        return !table.getName().contains("schema");
    }

    public static String getPrimaryKey(String databasePath, String tableName) throws ExceptionHandler {
        File databaseFolder = new File(databasePath);
        String databaseName = databaseFolder.getName();
        String schemaFilePath = databasePath + "/" + databaseName + "_schema";
        File schema = new File(schemaFilePath);
        if (!schema.exists()) {
            throw new ExceptionHandler("Schema does not exists");
        }
        try (BufferedReader bReader = new BufferedReader(new FileReader(schema))) {
            String line = null;
            String table = null;
            while ((line = bReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    table = null;
                    continue;
                }
                if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
                    table = line.substring(1, line.length() - 1);
                }
                if (tableName.equalsIgnoreCase(table) && line.contains("PRIMARY KEY")) {
                    return line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
