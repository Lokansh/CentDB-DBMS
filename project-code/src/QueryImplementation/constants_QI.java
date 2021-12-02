package QueryImplementation;

public class constants_QI {
    final String a="abc";
    final String UPD_QUERY_DBTB_NAME = "(?<=update table\\s).*(?=\\sset)";
    final String SET_CLAUSE = "(?<=set\\s).*(?=\\swhere)";
    final String WHERE_CLAUSE = "(?<=where\\s).*";
    final String EQUAL_OPR = "=";
    final String DOT_OPR = "\\.";
    final String COMMA_OPR = ",";

}
