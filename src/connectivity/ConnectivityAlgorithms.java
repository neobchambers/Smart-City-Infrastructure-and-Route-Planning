package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class ConnectivityAlgorithms {
    public static class TopologicalResult {
        public final List<Integer> order;
        public final boolean isDag;

        public TopologicalResult(List<Integer> order, boolean isDag) {
            this.order = order;
            this.isDag = isDag;
        }
    }

    public static TopologicalResult topologicalSort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Topological sorting requires a directed graph.");
        }
        int n = graph.getVertexCount();
        int[] indegree = new int[n];
        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.addLast(i);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.removeFirst();
            order.add(vertex);
            for (Edge edge : graph.getNeighbors(vertex)) {
                indegree[edge.to]--;
                if (indegree[edge.to] == 0) {
                    queue.addLast(edge.to);
                }
            }
        }
        return new TopologicalResult(order, order.size() == n);
    }

    public static List<List<Integer>> kosarajuSCC(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("SCC requires a directed graph.");
        }
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        List<Integer> finishOrder = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsFinish(graph, i, visited, finishOrder);
            }
        }

        List<List<Integer>> reverse = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            reverse.add(new ArrayList<>());
        }
        for (Edge edge : graph.getEdges()) {
            reverse.get(edge.to).add(edge.from);
        }

        java.util.Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();
        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            int vertex = finishOrder.get(i);
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                dfsReverse(vertex, reverse, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private static void dfsFinish(Graph graph, int vertex, boolean[] visited, List<Integer> order) {
        visited[vertex] = true;
        for (Edge edge : graph.getNeighbors(vertex)) {
            if (!visited[edge.to]) {
                dfsFinish(graph, edge.to, visited, order);
            }
        }
        order.add(vertex);
    }

    private static void dfsReverse(int vertex, List<List<Integer>> reverse,
                                   boolean[] visited, List<Integer> component) {
        visited[vertex] = true;
        component.add(vertex);
        for (int next : reverse.get(vertex)) {
            if (!visited[next]) {
                dfsReverse(next, reverse, visited, component);
            }
        }
    }
}
