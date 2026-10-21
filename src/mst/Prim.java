package mst;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    private static class Candidate {
        int vertex;
        int parent;
        int weight;

        Candidate(int vertex, int parent, int weight) {
            this.vertex = vertex;
            this.parent = parent;
            this.weight = weight;
        }
    }

    public static MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Prim requires an undirected graph.");
        }

        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        PriorityQueue<Candidate> queue =
                new PriorityQueue<>(Comparator.comparingInt(candidate -> candidate.weight));
        List<Edge> chosen = new ArrayList<>();
        long totalWeight = 0;
        long operations = 0;

        queue.add(new Candidate(0, -1, 0));
        while (!queue.isEmpty()) {
            Candidate current = queue.poll();
            operations++;
            if (visited[current.vertex]) {
                continue;
            }
            visited[current.vertex] = true;
            if (current.parent != -1) {
                chosen.add(new Edge(current.parent, current.vertex, current.weight));
                totalWeight += current.weight;
            }
            for (Edge edge : graph.getNeighbors(current.vertex)) {
                operations++;
                if (!visited[edge.to]) {
                    queue.add(new Candidate(edge.to, current.vertex, edge.weight));
                }
            }
        }

        boolean connected = chosen.size() == n - 1;
        return new MSTResult(chosen, totalWeight, connected, operations);
    }
}
