package QueryImplementation;

import java.util.Arrays;
import java.util.List;

public class constants_QI {
    final String a="abc";
    final String UPD_QUERY_DBTB_NAME = "(?<=update table\\s).*(?=\\sset)";
    final String SET_CLAUSE = "(?<=set\\s).*(?=\\swhere)";
    final String WHERE_CLAUSE = "(?<=where\\s).*";
    final String EQUAL_OPR = "=";
    final String DOT_OPR = "\\.";
    final String COMMA_OPR = ",";
    final List<String> dataType= Arrays.asList("INT","VARCHAR","BOOLEAN","BIGINT");
    //final String datatypes = [""],

}
