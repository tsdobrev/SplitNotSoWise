package bg.sofia.uni.fmi.mjt.splitwise.database;

import bg.sofia.uni.fmi.mjt.splitwise.user.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UsersDatabase {
    private static final Path storagePlace = Path.of("registeredUsers.txt");
    private static final Gson GSON = new Gson();

    public UsersDatabase() {
    }

    // Saving format - json

    public void updateDatabase(Map<String, User> users) {
        try (var writer = new FileWriter(storagePlace.toFile(), false)) {
            GSON.toJson(users, writer);
        } catch (IOException e) {
            throw new UncheckedIOException("An error occurred while writing users to the database", e);
        }
    }

    public Map<String, User> getSavedUsers() {
        try (var reader = Files.newBufferedReader(storagePlace)) {
            Type type = new TypeToken<Map<String, User>>() {
            }.getType();
            return GSON.fromJson(reader, type);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
