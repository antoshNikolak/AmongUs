package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Objects;

public class FileUtils {
    public static String getFileAsString(String fileName){
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream!= null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line).append("\n");
                }
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        result.append(line).append("\n");
//                    }
//                }
            } else{
                throw new Exception("input stream null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
