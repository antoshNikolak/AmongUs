package Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static String getFileAsString(String fileName){
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream!= null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                }
            }else{
                System.out.println("Input null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
