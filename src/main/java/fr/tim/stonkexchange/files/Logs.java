package fr.tim.stonkexchange.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logs {

    private File file;
    private final List<String> logs;

    public Logs() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        initLogsFile(dtf.format(now));

        logs = new ArrayList<>();
    }

    public void log(String log) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        logs.add("["+ dtf.format(now) + "] " + log);
        write();
    }

    private void write() {
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            for (String log : logs) pw.println(log);

            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLogsFile(String date) {

        File file = new File(FileManager.LOG_PATH + date + ".log");

        int count = 1;

        while (file.exists()) {
            file = new File(FileManager.LOG_PATH + date + "-" + count + ".txt");
            count++;
        }
        this.file = file;

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
