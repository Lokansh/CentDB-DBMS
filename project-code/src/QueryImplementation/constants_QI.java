package QueryImplementation;

public class constants_QI {
    final String a = "abc";
    final String UPD_QUERY_DBTB_NAME = "(?<=update table\\s).*(?=\\sset)";
    final String SET_CLAUSE = "(?<=set\\s).*(?=\\swhere)";
    final String WHERE_CLAUSE = "(?<=where\\s).*";
    final String EQUAL_OPR = "=";
    final String DOT_OPR = "\\.";
    final String COMMA_OPR = ",";
    final String CREATE_DB = "^create database.*$";
    final String USE_DB = "^use database.*$";
    final String CREATE_TB = "^create table.*$";
    final String INSERT_TB = "^insert into .*$";
    final String SELECT_TB = "^select.*$";
    final String UPDATE_TB = "^update table.*$";
    final String DELETE_ROW = "^delete.*$";
    final String DROP_TB = "^drop table.*$";
    final String START_TRANSACTION = "\\s*start transaction\\s*;$";
    final String ROLLBACK_TRANSACTION = "\\s*rollback\\s*;$";

}
