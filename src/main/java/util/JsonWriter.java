package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {

    public static class MSTEdge {
        public String from;
        public String to;
        public int weight;

        public MSTEdge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static class MSTResult {
        public int graph_id;
        public InputStats input_stats;
        public AlgoResult prim;
        public AlgoResult kruskal;
    }

    public static class InputStats {
        public int vertices;
        public int edges;

        public InputStats(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
    }

    public static class AlgoResult {
        public List<MSTEdge> mst_edges;
        public int total_cost;
        public int operations_count;
        public double execution_time_ms;

        public AlgoResult(List<MSTEdge> mst_edges, int total_cost, int operations_count, double execution_time_ms) {
            this.mst_edges = mst_edges;
            this.total_cost = total_cost;
            this.operations_count = operations_count;
            this.execution_time_ms = execution_time_ms;
        }
    }

    public static void writeResults(List<MSTResult> results, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(results, writer);
            System.out.println("Results successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
