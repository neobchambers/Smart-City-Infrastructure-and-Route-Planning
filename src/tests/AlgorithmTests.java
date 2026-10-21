package tests;

import connectivity.ConnectivityAlgorithms;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmTests {
    private static int passed = 0;

    public static void main(String[] args) {
        testMstSameWeight();
        testDisconnectedMst();
        testSingleVertex();
        testTopologicalDag();
        testCycleDetection();
        testSccPartition();
        testDijkstraAndUnreachable();
        testDijkstraRejectsNegativeWeights();
        System.out.println("Checks passed: " + passed);
    }

    private static void testMstSameWeight() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 4);
        graph.addEdge(0, 2, 2);
        check(Kruskal.findMST(graph).isConnected()
                        && Kruskal.findMST(graph).getTotalWeight() == Prim.findMST(graph).getTotalWeight(),
                "Kruskal and Prim agree on MST weight");
    }

    private static void testDisconnectedMst() {
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 5);
        check(!Kruskal.findMST(graph).isConnected() && !Prim.findMST(graph).isConnected(),
                "Disconnected graph is detected");
    }

    private static void testSingleVertex() {
        Graph graph = new Graph(1, false);
        check(Kruskal.findMST(graph).isConnected() && Kruskal.findMST(graph).getTotalWeight() == 0
                && Prim.findMST(graph).isConnected(), "Single-vertex MST");
    }

    private static void testTopologicalDag() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        ConnectivityAlgorithms.TopologicalResult result = ConnectivityAlgorithms.topologicalSort(graph);
        check(result.isDag && validTopologicalOrder(graph, result.order), "Valid topological order");
    }

    private static void testCycleDetection() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        check(!ConnectivityAlgorithms.topologicalSort(graph).isDag, "Directed cycle detected");
    }

    private static void testSccPartition() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);
        List<List<Integer>> components = ConnectivityAlgorithms.kosarajuSCC(graph);
        Set<Integer> all = new HashSet<>();
        for (List<Integer> component : components) {
            for (int vertex : component) {
                check(all.add(vertex), "Each vertex appears in one SCC only");
            }
        }
        check(all.size() == 4 && components.size() == 2, "SCCs partition all vertices");
    }

    private static void testDijkstraAndUnreachable() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 3);
        Dijkstra.Result result = Dijkstra.shortestPaths(graph, 0);
        check(result.distances[2] == 7 && result.distances[3] == Long.MAX_VALUE
                        && result.getPathTo(2).equals(java.util.Arrays.asList(0, 1, 2)),
                "Dijkstra distance, path and unreachable vertex");
    }

    private static void testDijkstraRejectsNegativeWeights() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, -1);
        boolean rejected = false;
        try {
            Dijkstra.shortestPaths(graph, 0);
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }
        check(rejected, "Dijkstra rejects negative weights");
    }

    private static boolean validTopologicalOrder(Graph graph, List<Integer> order) {
        int[] position = new int[graph.getVertexCount()];
        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }
        for (graph.Edge edge : graph.getEdges()) {
            if (position[edge.from] >= position[edge.to]) {
                return false;
            }
        }
        return true;
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError("FAILED: " + description);
        }
        passed++;
        System.out.println("PASS: " + description);
    }
}
