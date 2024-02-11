package ru.gb.student.repo;

import java.io.IOException;

public interface RepoInterface<T> {
    void save(T typeToSave) throws IOException;

    T load() throws IOException;
}
