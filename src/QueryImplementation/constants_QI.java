package QueryImplementation;

import java.util.Arrays;
import java.util.List;

public class constants_QI {
    final String UPD_QUERY_DBTB_NAME = "(?<=update)(.*)(?=set)";
    final String SET_CLAUSE = "(?<=set\\s).*(?=\\swhere)";
    final String WHERE_CLAUSE = "(?<=where\\s).*";
    final String EQUAL_OPR = "=";
    final String DOT_OPR = "\\.";
    final String COMMA_OPR = ",";
    final String CREATE_DB = "^create database.*;$";
    final String USE_DB = "^use database.*;$";
    final String CREATE_TB = "^create table.*;$";
    final String INSERT_TB = "^insert into .*;$";
    final String SELECT_TB = "^select.*;$";
    final String UPDATE_TB = "^update .*;$";
    final String DELETE_ROW = "^delete.*;$";
    final String DROP_TB = "^drop table.*;$";
    final String EXTRACT_DB_FROM_GLOBALPATH = "(?<=/)(.*)";
    //final String DEL_QUERY = "(?<=delete\\s).*";
    //final List<String> dataType = Arrays.asList("INT", "VARCHAR", "BOOLEAN", "BIGINT");
    final String START_TRANSACTION = "\\s*start transaction\\s*;$";
    final String ROLLBACK_TRANSACTION = "\\s*rollback\\s*;$";
    final String COMMIT_TRANSACTION = "\\s*commit\\s*;$";
    public final String ANALYTICS_COUNT_QUERIES = "\\s*count\\s*queries\\s*([a-zA-Z0-9_]+)\\s*;";
    public final String ANALYTICS_UPDATE_QUERIES = "\\s*count\\s*update\\s*([a-zA-Z0-9_]+)\\s*;";
    public final String ARCHIVE_DUMP = "Archive\\";
    public final String SQLDUMP_PATH = "database_export_dump\\";
}
