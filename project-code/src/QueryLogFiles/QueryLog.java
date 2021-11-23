package QueryLogFiles;

import java.io.*;
import java.util.Date;

public class QueryLog {

    public void createQueryLog(String folder, String query_type, String database_name,
                               String table_name, String column_affected, String row_affected,
                               String constraint) throws IOException {
        String query_log="";
        File f = new File(folder+database_name+"\\"+database_name+"_logs.txt");
        if(f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(folder+database_name+"\\"+database_name+"_logs.txt"));
            if (br.readLine() == null) {
                query_log = "Query type|Database Name|Table Name|Column Affected|Row Affected|Condition|TimeStamp\n";
            }
        }
        else{
            query_log = "Query type|Database Name|Table Name|Column Affected|Row Affected|Condition|TimeStamp\n";
        }
        query_log = query_log+query_type+"|"+database_name+"|"+table_name+"|"+column_affected+"|"
                +row_affected+"|"+constraint+"|"+(new Date());
        FileWriter writer = new FileWriter(folder+database_name+"\\"+database_name+"_logs.txt",true);
        writer.write(query_log+"\n");
        writer.close();
    }
}