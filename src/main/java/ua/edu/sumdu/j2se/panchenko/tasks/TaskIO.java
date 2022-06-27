package ua.edu.sumdu.j2se.panchenko.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * The class that contains methods for writing and reading task lists in various formats.
 */
public class TaskIO {
    /**
     * The method writes tasks from the list to the stream in binary format.
     */
    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(out)) {
            objectOutputStream.writeInt(tasks.size());
            for (Task task : tasks) {
                objectOutputStream.writeInt(task.getTitle().length());
                objectOutputStream.writeUTF(task.getTitle());
                objectOutputStream.writeInt(task.isActive() ? 1 : 0);
                objectOutputStream.writeInt(task.getRepeatInterval());
                if (task.isRepeated()) {
                    objectOutputStream.writeLong(task.getStartTime().toEpochSecond(ZoneOffset.UTC));
                    objectOutputStream.writeLong(task.getEndTime().toEpochSecond(ZoneOffset.UTC));
                } else {
                    objectOutputStream.writeLong(task.getTime().toEpochSecond(ZoneOffset.UTC));
                }
            }
        }
    }

    /**
     * The method reads tasks from the stream into this task list.
     */
    public static void read(AbstractTaskList tasks, InputStream in) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(in)) {
            int size = objectInputStream.readInt();
            for (int i = 0; i < size; i++) {
                int lengthOfTitle = objectInputStream.readInt();
                String title = objectInputStream.readUTF();
                boolean active = objectInputStream.readInt() == 1;
                int interval = objectInputStream.readInt();
                Task task;
                if (interval != 0) {
                    LocalDateTime start = LocalDateTime
                            .ofEpochSecond(objectInputStream.readLong(), 0, ZoneOffset.UTC);
                    LocalDateTime end = LocalDateTime
                            .ofEpochSecond(objectInputStream.readLong(), 0, ZoneOffset.UTC);
                    task = new Task(title, start, end, interval);
                } else {
                    LocalDateTime time = LocalDateTime
                            .ofEpochSecond(objectInputStream.readLong(), 0, ZoneOffset.UTC);
                    task = new Task(title, time);
                }
                task.setActive(active);
                tasks.add(task);
            }
        }
    }

    /**
     * The method writes tasks from the list to a file.
     */
    public static void writeBinary(AbstractTaskList tasks, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            oos.writeObject(tasks);
        }
    }

    /**
     * The method reads tasks from a file into a task list.
     */
    public static void readBinary(AbstractTaskList tasks, File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            AbstractTaskList list = (AbstractTaskList) ois.readObject();
        }
    }

    /**
     * The method writes tasks from a list to a stream in JSON format.
     */
    public static void write(AbstractTaskList tasks, Writer out) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = gsonBuilder.create();
        out.write(gson.toJson(tasks.getStream().toArray()));
        out.flush();
    }

    /**
     * The method reads tasks from the stream into a list.
     */
    public static void read(AbstractTaskList tasks, Reader in) throws IOException {
        try (BufferedReader reader = new BufferedReader(in)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            Gson gson = gsonBuilder.create();
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuilder.append(str);
            }
            JsonArray array = new Gson().fromJson(stringBuilder.toString(), JsonArray.class);
            array.forEach(task -> tasks.add(gson.fromJson(task, Task.class)));
        }
    }

    /**
     * The method writes tasks to a file in JSON format.
     */
    public static void writeText(AbstractTaskList tasks, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            write(tasks, fileWriter);
        }
    }

    /**
     * The method reads tasks from a file.
     */
    public static void readText(AbstractTaskList tasks, File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        }
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime time) throws IOException {
            if (time == null) {
                jsonWriter.nullValue();
            } else {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                jsonWriter.value(time.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                return null;
            } else {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                return LocalDateTime.parse(jsonReader.nextString(), formatter);
            }
        }
    }
}
