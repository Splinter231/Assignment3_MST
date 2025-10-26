package mst;

import graph.Edge;
import graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MSTTest {

    private Graph createSampleGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "B", 1));
        edges.add(new Edge("A", "C", 4));
        edges.add(new Edge("B", "C", 2));
        edges.add(new Edge("B", "D", 5));
        edges.add(new Edge("C", "D", 3));
        return new Graph(vertices, edges);
    }

    private boolean isConnected(Graph graph, List<Edge> mstEdges) {
        Map<String, List<String>> adj = new HashMap<>();
        for (String v : graph.getVertices()) adj.put(v, new ArrayList<>());
        for (Edge e : mstEdges) {
            adj.get(e.getFrom()).add(e.getTo());
            adj.get(e.getTo()).add(e.getFrom());
        }
        Set<String> visited = new HashSet<>();
        dfs(graph.getVertices().get(0), adj, visited);
        return visited.size() == graph.vertexCount();
    }

    private void dfs(String v, Map<String, List<String>> adj, Set<String> visited) {
        visited.add(v);
        for (String n : adj.get(v)) {
            if (!visited.contains(n)) dfs(n, adj, visited);
        }
    }

    private boolean hasCycle(Graph graph, List<Edge> mstEdges) {
        Map<String, String> parent = new HashMap<>();
        for (String v : graph.getVertices()) parent.put(v, v);
        for (Edge e : mstEdges) {
            String root1 = find(parent, e.getFrom());
            String root2 = find(parent, e.getTo());
            if (root1.equals(root2)) return true;
            parent.put(root2, root1);
        }
        return false;
    }
    @Test
    void testDisconnectedGraphHandledGracefully() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "B", 1)); // C и D изолированы
        Graph graph = new Graph(vertices, edges);

        Prim prim = new Prim();
        prim.run(graph);

        Kruskal kruskal = new Kruskal();
        kruskal.run(graph);

        assertTrue(prim.getTotalCost() >= 0);
        assertTrue(kruskal.getTotalCost() >= 0);

        boolean primConnected = prim.getMstEdges().size() == graph.vertexCount() - 1;
        boolean kruskalConnected = kruskal.getMstEdges().size() == graph.vertexCount() - 1;

        assertFalse(primConnected && kruskalConnected, "Disconnected graph should not have full MST");
        System.out.println("Disconnected graph handled correctly");
    }
    @Test
    void testReproducibility() {
        Graph graph = createSampleGraph();

        Prim prim1 = new Prim();
        Prim prim2 = new Prim();
        prim1.run(graph);
        prim2.run(graph);

        Kruskal kr1 = new Kruskal();
        Kruskal kr2 = new Kruskal();
        kr1.run(graph);
        kr2.run(graph);

        assertEquals(prim1.getTotalCost(), prim2.getTotalCost(), "Prim not reproducible");
        assertEquals(kr1.getTotalCost(), kr2.getTotalCost(), "Kruskal not reproducible");
        System.out.println("Results are reproducible for the same dataset");
    }


    private String find(Map<String, String> parent, String v) {
        if (!parent.get(v).equals(v)) parent.put(v, find(parent, parent.get(v)));
        return parent.get(v);
    }

    @Test
    void testPrimAndKruskalCorrectness() {
        Graph graph = createSampleGraph();

        Prim prim = new Prim();
        prim.run(graph);

        Kruskal kruskal = new Kruskal();
        kruskal.run(graph);

        assertEquals(prim.getTotalCost(), kruskal.getTotalCost(), "MST total cost mismatch");
        assertEquals(graph.vertexCount() - 1, prim.getMstEdges().size(), "Prim MST edge count invalid");
        assertEquals(graph.vertexCount() - 1, kruskal.getMstEdges().size(), "Kruskal MST edge count invalid");
        assertFalse(hasCycle(graph, prim.getMstEdges()), "Prim MST has a cycle");
        assertFalse(hasCycle(graph, kruskal.getMstEdges()), "Kruskal MST has a cycle");
        assertTrue(isConnected(graph, prim.getMstEdges()), "Prim MST not connected");
        assertTrue(isConnected(graph, kruskal.getMstEdges()), "Kruskal MST not connected");
        assertTrue(prim.getOperationCount() >= 0, "Prim operation count negative");
        assertTrue(kruskal.getOperationCount() >= 0, "Kruskal operation count negative");
        assertTrue(prim.getExecutionTimeMs() >= 0, "Prim execution time negative");
        assertTrue(kruskal.getExecutionTimeMs() >= 0, "Kruskal execution time negative");

        System.out.println("✅ Prim cost: " + prim.getTotalCost() +
                " | Kruskal cost: " + kruskal.getTotalCost() +
                " | Edges: " + prim.getMstEdges().size());
    }
}
