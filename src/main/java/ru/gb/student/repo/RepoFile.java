package ru.gb.student.repo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RepoFile implements RepoInterface<String>{
    private final File logFile = new File("src/main/java/ru/gb/student/repo/log.txt");

    @Override
    public void save(String stringToSave) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
            fos.write(stringToSave.getBytes());
            fos.write("\n".getBytes());
        } catch (IOException e) {
            throw (new IOException(e.getMessage()));
        }
    }

    @Override
    public String load() throws IOException {
        String string;
        try (FileInputStream fis = new FileInputStream(logFile)) {
            string = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw (new IOException(e.getMessage()));
        }
        return string;
    }
}
