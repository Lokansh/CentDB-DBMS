import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CentDB {
    void writeback(String output_filename, String writebackStr, String writebackType){
        /*
        This method writes back the encoded string when encode method is called.
        And writes back the decoded string when decode method is called.
         */
        try {
            FileWriter myWriter = new FileWriter(output_filename);
            myWriter.write(writebackStr);
            myWriter.close();
            System.out.println(writebackType + " file writeback successful at below location. "+ output_filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
