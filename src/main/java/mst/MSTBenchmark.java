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
            List<Edge> edges = new ArrayList<>();
            for (JsonReader.Edge e : gData.edges) {
                edges.add(new Edge(e.from, e.to, e.weight));
            }

            Graph graph = new Graph(gData.nodes, edges);

            Prim prim = new Prim();
            prim.run(graph);

            Kruskal kruskal = new Kruskal();
            kruskal.run(graph);

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

            System.out.printf("Graph %d done: Prim=%.2fms, Kruskal=%.2fms%n",
                    gData.id, prim.getExecutionTimeMs(), kruskal.getExecutionTimeMs());
        }

        JsonWriter.writeResults(results, outputPath);
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
