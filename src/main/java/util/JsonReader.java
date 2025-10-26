package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonReader {

    public static class Edge {
        public String from;
        public String to;
        public int weight;
    }

    public static class Graph {
        public int id;
        public String category;
        public List<String> nodes;
        public List<Edge> edges;
    }

    public static class GraphList {
        public List<Graph> graphs;
    }

    public static GraphList readGraphs(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<GraphList>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}