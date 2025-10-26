package mst;

import graph.Edge;
import graph.Graph;
import util.JsonReader;
import util.JsonWriter;
import java.util.*;

public class MSTBenchmark {

    public static void main(String[] args) {
        String inputPath = "src/main/resources/data/assign_3_input.json";
        String outputPath = "src/main/resources/data/assign_3_output.json";

        JsonReader.GraphList graphList = JsonReader.readGraphs(inputPath);
        if (graphList == null || graphList.graphs == null) {
            System.err.println("No graphs found in input file.");
            return;
        }

        List<JsonWriter.MSTResult> results = new ArrayList<>();

        for (JsonReader.Graph gData : graphList.graphs) {
            System.out.println("▶ Running MST for Graph ID: " + gData.id + " (" + gData.category + ")");

            List<String> cleanNodes = new ArrayList<>(gData.nodes);
            List<Edge> cleanEdges = new ArrayList<>();
            for (JsonReader.Edge e : gData.edges) {
                cleanEdges.add(new Edge(e.from, e.to, e.weight));
            }

            Graph graph = new Graph(cleanNodes, cleanEdges);

            if (graph.vertexCount() == 0 || graph.edgeCount() == 0) {
                System.out.println("Skipping graph " + gData.id + " (empty or disconnected)");
                continue;
            }

            Prim prim = new Prim();
            prim.run(graph);

            Kruskal kruskal = new Kruskal();
            kruskal.run(graph);

            if (prim.getTotalCost() != kruskal.getTotalCost()) {
                System.out.printf("Graph %d: Cost mismatch (Prim=%d, Kruskal=%d)%n",
                        gData.id, prim.getTotalCost(), kruskal.getTotalCost());
            } else {
                System.out.printf("Graph %d: Costs match (MST=%d)%n", gData.id, prim.getTotalCost());
            }

            JsonWriter.MSTResult result = new JsonWriter.MSTResult();
            result.graph_id = gData.id;
            result.input_stats = new JsonWriter.InputStats(graph.vertexCount(), graph.edgeCount());

            result.prim = new JsonWriter.AlgoResult(
                    convertEdges(prim.getMstEdges()),
                    prim.getTotalCost(),
                    prim.getOperationCount(),
                    prim.getExecutionTimeMs()
            );

            result.kruskal = new JsonWriter.AlgoResult(
                    convertEdges(kruskal.getMstEdges()),
                    kruskal.getTotalCost(),
                    kruskal.getOperationCount(),
                    kruskal.getExecutionTimeMs()
            );

            results.add(result);
        }

        JsonWriter.writeResults(results, outputPath);
        util.CSVExporter.writeCSV(results, "src/main/resources/data/mst_metrics.csv");
        System.out.println("All graphs processed and results written to " + outputPath);
    }

    private static List<JsonWriter.MSTEdge> convertEdges(List<Edge> edges) {
        List<JsonWriter.MSTEdge> list = new ArrayList<>();
        for (Edge e : edges) {
            list.add(new JsonWriter.MSTEdge(e.getFrom(), e.getTo(), e.getWeight()));
        }
        return list;
    }
}
