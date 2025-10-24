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

            int edgeCount = (int) (vertices * switch (category) {
                case "small" -> 1.5;
                case "medium" -> 2.0;
                case "large" -> 2.5;
                default -> 3.0;
            });

            Set<String> edgeSet = new HashSet<>();
            List<Edge> edges = new ArrayList<>();

            while (edges.size() < edgeCount) {
                String a = nodes.get(random.nextInt(vertices));
                String b = nodes.get(random.nextInt(vertices));
                if (!a.equals(b)) {
                    String edgeKey = a + "-" + b;
                    String reverseKey = b + "-" + a;
                    if (!edgeSet.contains(edgeKey) && !edgeSet.contains(reverseKey)) {
                        edgeSet.add(edgeKey);
                        edges.add(new Edge(a, b, random.nextInt(100) + 1));
                    }
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
