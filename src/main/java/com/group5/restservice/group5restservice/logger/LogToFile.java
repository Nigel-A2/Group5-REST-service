package com.group5.restservice.group5restservice.logger;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.Instant;

/**
* Just for debugging help - easy logging to a file
* @author Nate Penner
* */
public class LogToFile {
    public static void logToFile(String data, String file) {
        data = MessageFormat.format("[{0}] - {1}\n", Date.from(Instant.now()).toString(), data);
        try {
            FileOutputStream output = new FileOutputStream(file, true);
            output.write(data.getBytes(StandardCharsets.UTF_8));
            output.close();
        } catch (Exception e) {}
    }
    public static void logToFile(String data) {
        logToFile(data, "log1.txt");
    }
}
