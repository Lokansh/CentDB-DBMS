package QueryImplementation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQuery {
    final String dataStoragePath = "database_storage/";

    // update table db1.tb1 set name="updated" where id=1
    public void updateQuery(String Query, String globalPath){
        constants_QI constant = new constants_QI();
        String line;
        String filterColumn = null;
        String filterValue = null;
        String updateColumn = null;
        String updateValue = null;
        try {
            System.out.println("Executing update function..");
            if (globalPath == null || globalPath.isEmpty() || globalPath.isBlank()) {
                // database not defined

                // extract path
                Pattern dbTBPattern = Pattern.compile(constant.UPD_QUERY_DBTB_NAME, Pattern.DOTALL);
                Matcher dbTBMatcher = dbTBPattern.matcher(Query);

                // extract set clause
                Pattern setPattern = Pattern.compile(constant.SET_CLAUSE, Pattern.DOTALL);
                Matcher setMatcher = setPattern.matcher(Query);
                if (setMatcher.find()){
                    updateColumn = setMatcher.group(0).trim().split(constant.EQUAL_OPR)[0];
                    updateValue = setMatcher.group(0).trim().split(constant.EQUAL_OPR)[1].replaceAll("\"","");
                }

                // extract where clause
                Pattern wherePattern = Pattern.compile(constant.WHERE_CLAUSE, Pattern.DOTALL);
                Matcher whereMatcher = wherePattern.matcher(Query);
                if (whereMatcher.find()){
                    filterColumn = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[0];
                    filterValue = whereMatcher.group(0).trim().split(constant.EQUAL_OPR)[1]
                            .replaceAll("\"","").replaceAll("'","")
                            .replace(";","");
                }

                if (dbTBMatcher.find()) {
                    // extract database name and table name
                    String extractedStr = dbTBMatcher.group(0).trim();
                    String databaseName = extractedStr.split(constant.DOT_OPR)[0];
                    String tableName = dbTBMatcher.group(0).trim().split(constant.DOT_OPR)[1];
                    String updatePAth = dataStoragePath + databaseName + "/" + tableName;
                    String tempPath = dataStoragePath + databaseName + "/" + tableName+"_temp";

                    int indexFilterCol = 0;
                    int updateColIndex = 0;
                    int rowCounter = 0;

                    File file = new File(updatePAth);
                    // if file path exists
                    BufferedReader br = new BufferedReader(new FileReader(updatePAth));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tempPath));
                    if (file.exists()) {
                        while (((line = br.readLine()) != null)) {
                            if (rowCounter == 0) {
                                ArrayList<String> header = new ArrayList<>(Arrays.asList(line.split(constant.COMMA_OPR)));
                                indexFilterCol = header.indexOf(filterColumn);
                                updateColIndex = header.indexOf(updateColumn);
                                bw.append(line);
                                bw.newLine();
                                rowCounter++;
                            } else {
                                // row data's
                                rowCounter++;
                                String extractedValueOfRow = line.split(constant.COMMA_OPR)[indexFilterCol];
                                if (extractedValueOfRow.equals(filterValue)){               // check if filter matches
                                    // this is the row that is to be updated
                                    ArrayList<String> namesList = new ArrayList<>(Arrays.asList(line.split(constant.COMMA_OPR)));
                                    String oldValue = namesList.get(updateColIndex);        // for loggers
                                    namesList.set(updateColIndex, updateValue);             // update the value
                                    String listString = String.join(constant.COMMA_OPR, namesList);
                                    bw.append(listString);
                                    bw.newLine();
                                }
                                else {
                                    bw.append(line);
                                    bw.newLine();
                                }
                            }
                        } // end of while

                        bw.close();
                        br.close();

                    } else {
                        System.out.println("Path does not exists.");
                    }

                    // delete the actual data file and renaming the temp file to actual file
                    file.delete();
                    File newfile = new File(tempPath);
                    newfile.renameTo(file);
                    System.out.println("Updated successfully.");

                } // end of if for path matcher - db and tb
            } // end of if - for no database specified
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

    }

}

