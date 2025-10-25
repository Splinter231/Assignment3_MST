package graph;

import java.util.*;

public class Graph {
    private final List<String> vertices;
    private final List<Edge> edges;
    private final Map<String, List<Edge>> adjacencyList;

    public Graph(List<String> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.adjacencyList = new HashMap<>();
        buildAdjacencyList();
    }

    private void buildAdjacencyList() {
        for (String v : vertices) {
            adjacencyList.put(v, new ArrayList<>());
        }
        for (Edge e : edges) {
            adjacencyList.get(e.getFrom()).add(e);
            adjacencyList.get(e.getTo()).add(e);
        }
    }

    public List<String> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    public void addEdge(String from, String to, int weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(edge);
        adjacencyList.computeIfAbsent(to, k -> new ArrayList<>()).add(edge);
    }
}
