package services;

import java.io.File;

public class TableService {
    public static boolean isTable(File table) {
        return !table.getName().contains("schema");
    }
}
