package RSMS.common.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileService {

    public static void writeTo(String fileName, String data, boolean doNotOverwrite) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, doNotOverwrite));
            writer.append(data + "\n");
            writer.close();
        } catch (IOException err) {
            System.out.println("Failed to write: " + data + " : " + err);
        }
    }

    public static void writeTo(String fileName, String data) {
            boolean doNotOverwrite = true;
            FileService.writeTo(fileName, data, doNotOverwrite);
    }
}
