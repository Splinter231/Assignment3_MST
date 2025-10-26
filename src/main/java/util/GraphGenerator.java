package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphGenerator {

    static class Edge {
        String from;
        String to;
        int weight;

        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    static class Graph {
        int id;
        String category;
        List<String> nodes;
        List<Edge> edges;

        Graph(int id, String category, List<String> nodes, List<Edge> edges) {
            this.id = id;
            this.category = category;
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    static class GraphCollection {
        List<Graph> graphs = new ArrayList<>();
    }

    public static void main(String[] args) {
        GraphCollection collection = new GraphCollection();
        int idCounter = 1;

        idCounter = generateGraphs(collection, "small", 5, 5, 30, idCounter);
        idCounter = generateGraphs(collection, "medium", 10, 50, 300, idCounter);
        idCounter = generateGraphs(collection, "large", 10, 500, 1000, idCounter);
        generateGraphs(collection, "xlarge", 5, 1000, 3000, idCounter);

        saveToJson(collection, "src/main/resources/data/assign_3_input.json");
    }

    private static int generateGraphs(GraphCollection collection, String category, int count, int minV, int maxV, int startId) {
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int vertices = random.nextInt(maxV - minV + 1) + minV;
            List<String> nodes = new ArrayList<>();
            for (int v = 1; v <= vertices; v++) {
                nodes.add("N" + v);
            }

            List<Edge> edges = new ArrayList<>();
            Set<String> existing = new HashSet<>();
            for (int v = 1; v < vertices; v++) {
                String a = "N" + v;
                String b = "N" + (v + 1);
                int w = random.nextInt(100) + 1;
                edges.add(new Edge(a, b, w));
                existing.add(a + "-" + b);
                existing.add(b + "-" + a);
            }

            int extraEdges = (int) (vertices * switch (category) {
                case "small" -> 0.5;
                case "medium" -> 1.0;
                case "large" -> 1.5;
                default -> 2.0;
            });

            while (edges.size() < vertices - 1 + extraEdges) {
                String a = nodes.get(random.nextInt(vertices));
                String b = nodes.get(random.nextInt(vertices));
                if (!a.equals(b) && !existing.contains(a + "-" + b) && !existing.contains(b + "-" + a)) {
                    int w = random.nextInt(100) + 1;
                    edges.add(new Edge(a, b, w));
                    existing.add(a + "-" + b);
                    existing.add(b + "-" + a);
                }
            }

            collection.graphs.add(new Graph(startId++, category, nodes, edges));
        }

        return startId;
    }

    private static void saveToJson(GraphCollection collection, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(collection, writer);
            System.out.println("Graph data successfully saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}
