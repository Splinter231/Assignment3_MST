package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CSVExporter {

    public static void writeCSV(List<JsonWriter.MSTResult> results, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Graph_ID,Category,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time_ms,Kruskal_Time_ms,Prim_Operations,Kruskal_Operations\n");

            for (JsonWriter.MSTResult r : results) {
                String category = categorize(r.input_stats.vertices);

                String line = String.format(Locale.US,
                        "%d,%s,%d,%d,%d,%d,%.4f,%.4f,%d,%d\n",
                        r.graph_id,
                        category,
                        r.input_stats.vertices,
                        r.input_stats.edges,
                        r.prim.total_cost,
                        r.kruskal.total_cost,
                        r.prim.execution_time_ms,
                        r.kruskal.execution_time_ms,
                        r.prim.operations_count,
                        r.kruskal.operations_count
                );

                writer.write(line);
            }

            System.out.println("Metrics successfully exported to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    private static String categorize(int vertices) {
        if (vertices <= 30) return "small";
        else if (vertices <= 300) return "medium";
        else if (vertices <= 1000) return "large";
        else return "xlarge";
    }
}
