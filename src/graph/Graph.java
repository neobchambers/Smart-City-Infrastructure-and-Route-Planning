package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private final int vertices;
    private final boolean directed;
    private final List<Edge> edges;
    private final List<List<Edge>> adjacency;

    public Graph(int vertices, boolean directed) {
        if (vertices < 1) {
            throw new IllegalArgumentException("A graph must have at least one vertex.");
        }
        this.vertices = vertices;
        this.directed = directed;
        this.edges = new ArrayList<>();
        this.adjacency = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public int getVertexCount() {
        return vertices;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Edge> getNeighbors(int vertex) {
        checkVertex(vertex);
        return Collections.unmodifiableList(adjacency.get(vertex));
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);
        if (weight < 0 && !directed) {
        }
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacency.get(from).add(edge);
        if (!directed) {
            adjacency.get(to).add(new Edge(to, from, weight));
        }
    }

    public void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException("Vertex must be between 0 and " + (vertices - 1));
        }
    }
}
