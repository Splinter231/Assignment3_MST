package mst;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Kruskal {
    private int operationCount;
    private double executionTimeMs;
    private int totalCost;
    private final List<Edge> mstEdges = new ArrayList<>();

    private static class DisjointSet {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();

        public void makeSet(List<String> vertices) {
            for (String v : vertices) {
                parent.put(v, v);
                rank.put(v, 0);
            }
        }

        public String find(String v) {
            if (!parent.get(v).equals(v)) {
                parent.put(v, find(parent.get(v))); // path compression
            }
            return parent.get(v);
        }

        public void union(String v1, String v2) {
            String root1 = find(v1);
            String root2 = find(v2);
            if (root1.equals(root2)) return;

            int rank1 = rank.get(root1);
            int rank2 = rank.get(root2);

            if (rank1 < rank2) {
                parent.put(root1, root2);
            } else if (rank1 > rank2) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank1 + 1);
            }
        }
    }

    public void run(Graph graph) {
        long start = System.nanoTime();
        operationCount = 0;
        totalCost = 0;
        mstEdges.clear();

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingInt(Edge::getWeight));
        operationCount += edges.size();

        DisjointSet ds = new DisjointSet();
        ds.makeSet(graph.getVertices());

        for (Edge edge : edges) {
            operationCount++;
            String root1 = ds.find(edge.getFrom());
            String root2 = ds.find(edge.getTo());

            // добавляем ребро, если вершины ещё не связаны
            if (!root1.equals(root2)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                ds.union(edge.getFrom(), edge.getTo()); // правильный union
            }

            if (mstEdges.size() == graph.vertexCount() - 1) break;
        }

        long end = System.nanoTime();
        executionTimeMs = (end - start) / 1_000_000.0;
    }

    public List<Edge> getMstEdges() { return mstEdges; }
    public int getTotalCost() { return totalCost; }
    public int getOperationCount() { return operationCount; }
    public double getExecutionTimeMs() { return executionTimeMs; }
}
