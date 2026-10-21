package experiments;

import graph.Graph;
import java.util.Random;

public class GraphGenerator {
    public static Graph connectedUndirected(int vertices, int edges, long seed) {
        int maximum = vertices * (vertices - 1) / 2;
        if (vertices < 1 || edges < vertices - 1 || edges > maximum) {
            throw new IllegalArgumentException("For a connected simple graph, edges must be V-1..V*(V-1)/2.");
        }
        Graph graph = new Graph(vertices, false);
        Random random = new Random(seed);
        boolean[][] used = new boolean[vertices][vertices];

        for (int vertex = 1; vertex < vertices; vertex++) {
            int parent = random.nextInt(vertex);
            int weight = 1 + random.nextInt(100);
            graph.addEdge(parent, vertex, weight);
            used[parent][vertex] = true;
            used[vertex][parent] = true;
        }

        int currentEdges = vertices - 1;
        while (currentEdges < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);
            if (from != to && !used[from][to]) {
                int weight = 1 + random.nextInt(100);
                graph.addEdge(from, to, weight);
                used[from][to] = true;
                used[to][from] = true;
                currentEdges++;
            }
        }
        return graph;
    }

    public static Graph directedRandom(int vertices, int edges, long seed, boolean allowCycles) {
        int maximum = vertices * (vertices - 1);
        if (edges < 0 || edges > maximum) {
            throw new IllegalArgumentException("Invalid directed edge count.");
        }
        Graph graph = new Graph(vertices, true);
        Random random = new Random(seed);
        boolean[][] used = new boolean[vertices][vertices];
        int count = 0;
        while (count < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);
            if (from == to || used[from][to]) {
                continue;
            }
            if (!allowCycles && from >= to) {
                continue;
            }
            graph.addEdge(from, to, 1 + random.nextInt(100));
            used[from][to] = true;
            count++;
        }
        return graph;
    }
}
