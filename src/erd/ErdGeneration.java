package erd;

import authentication.model.Session;
import services.DatabaseService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ErdGeneration {
    private static String erdPath = "erd/";

    public ErdGeneration(Scanner scanner) {
        this.scanner = scanner;
        init();
    }

    private void init() {
        File file = new File(erdPath);
        if(!file.exists()){
            file.mkdir();
        }
    }

    private final Scanner scanner;

    public Boolean erdGenerate(){
        List<File> allDatabases = new ArrayList<>();
        File file = new File(DatabaseService.getRootDatabaseFolderPath());
        File[] fileArray = file.listFiles();
        for(File fileDatabase : fileArray){
            if(fileDatabase.isDirectory()){
                allDatabases.add(fileDatabase);
            }
        }

        System.out.println("Pick a database to generate ERD for -");
        for(int i = 0; i < allDatabases.size(); i++){
            System.out.println(i+1 + " " + allDatabases.get(i).getName());
        }
        int choice = Integer.parseInt(scanner.nextLine());
        if(choice < 1 || choice > allDatabases.size()){
            System.out.println("Invalid choice selected.");
            return false;
        }
        File databaseName = allDatabases.get(choice-1);
        String schemaFilePath = DatabaseService.getRootDatabaseFolderPath() + databaseName.getName() + "/" +
                                databaseName.getName() + "_schema";
        File schema = new File(schemaFilePath);
        if(!schema.exists()){
            System.out.println("Schema does not exists");
            return false;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bReader = new BufferedReader(new FileReader(schema))){
            String tableName = null;
            String line = null;
            List<String> cardinalities = new ArrayList<>();

            while((line = bReader.readLine()) != null){
                if(line.trim().isEmpty()){
                    tableName = null;
                    builder.append(String.join("\n",cardinalities));
                    builder.append("\n");
                    if(!cardinalities.isEmpty()){
                        builder.append("\n");
                    }
                    cardinalities.clear();
                    continue;
                }
                if(line.charAt(0) == '[' && line.charAt(line.length()-1) == ']'){
                    tableName = line;
                    builder.append(tableName).append("\n");
                }
                else if(line.contains("FOREIGN KEY")){
                    line = line.substring(0,line.length() - 1);
                    builder.append(line).append("\n");
                    String inputName = line.replaceAll("FOREIGN KEY ","")
                                        .replaceAll("REFERENCES","(1) -> (N)");
                    cardinalities.add(inputName);
                }
                else{
                    line = line.substring(0,line.length() - 1);
                    builder.append(line).append("\n");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String fileName = erdPath + allDatabases.get(choice - 1).getName() + "_" + Session.getInstance().getUser().getName()
                            + "_" + System.currentTimeMillis();
        try(FileWriter fWriter = new FileWriter(fileName)){
            fWriter.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ERD generated successfully!");
        return true;
    }
}
