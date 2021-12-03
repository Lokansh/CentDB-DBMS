package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseService {

    private static final String ROOT_DATABASE_FOLDER_PATH = "database_storage/";
    private static final String TEMP_DATABASE_FOLDER_PATH = "temp_database/";

    public static boolean isTransactionRunning = false;
    public static String CURRENT_DATABASE_PATH = null;

    public List<Path> getAllDatabases() {
        List<Path> databases = null;
        Path path = Paths.get(ROOT_DATABASE_FOLDER_PATH);
        try {
            Stream<Path> list = Files.list(path);
            databases = list.filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databases;
    }

    public static String getRootDatabaseFolderPath() {
        return ROOT_DATABASE_FOLDER_PATH;
    }

    public static String getTempDatabaseFolderPath() {
        return TEMP_DATABASE_FOLDER_PATH;
    }
}
