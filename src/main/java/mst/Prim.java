package mst;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Prim {
    private int operationCount;
    private double executionTimeMs;
    private int totalCost;
    private final List<Edge> mstEdges = new ArrayList<>();

    public void run(Graph graph) {
        long start = System.nanoTime();
        operationCount = 0;
        totalCost = 0;
        mstEdges.clear();

        Map<String, Boolean> visited = new HashMap<>();
        for (String v : graph.getVertices()) visited.put(v, false);

        String startVertex = graph.getVertices().get(0);
        visited.put(startVertex, true);

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        pq.addAll(graph.getAdjacencyList().get(startVertex));
        operationCount += graph.getAdjacencyList().get(startVertex).size();

        while (!pq.isEmpty() && mstEdges.size() < graph.vertexCount() - 1) {
            Edge edge = pq.poll();
            operationCount++;
            if (edge == null) continue;

            String next = !visited.get(edge.getFrom()) ? edge.getFrom() : edge.getTo();
            if (visited.get(next)) continue;

            mstEdges.add(edge);
            totalCost += edge.getWeight();
            visited.put(next, true);

            for (Edge e : graph.getAdjacencyList().get(next)) {
                String neighbor = e.getFrom().equals(next) ? e.getTo() : e.getFrom();
                if (!visited.get(neighbor)) {
                    pq.add(e);
                    operationCount++;
                }
            }
        }
        long end = System.nanoTime();
        executionTimeMs = (end - start) / 1_000_000.0;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

}
