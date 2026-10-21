package experiments;

import connectivity.ConnectivityAlgorithms;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;
import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;

public class Benchmark {
    private static final int REPEATS = 5;

    public static void main(String[] args) throws Exception {
        new File("results/tables").mkdirs();
        new File("results/plots").mkdirs();
        try (PrintWriter out = new PrintWriter("results/tables/benchmark.csv")) {
            out.println("algorithm,vertices,edges,average_time_ns,operations,notes");
            int[] sizes = {10, 100, 1000};
            int[] edgeCounts = {30, 500, 5000};
            for (int i = 0; i < sizes.length; i++) {
                int vertices = sizes[i];
                int edges = edgeCounts[i];
                Graph undirected = GraphGenerator.connectedUndirected(vertices, edges, 20261021L + vertices);
                Graph directed = GraphGenerator.directedRandom(vertices, edges, 3000L + vertices, true);
                Graph dag = GraphGenerator.directedRandom(vertices, edges, 6000L + vertices, false);

                long kruskalTime = 0, primTime = 0, dijkstraTime = 0;
                long kruskalOps = 0, primOps = 0, dijkstraOps = 0;
                long sccTime = 0, topoTime = 0, topoCycleTime = 0;
                for (int repeat = 0; repeat < REPEATS; repeat++) {
                    long start = System.nanoTime();
                    var kruskal = Kruskal.findMST(undirected);
                    kruskalTime += System.nanoTime() - start;
                    kruskalOps = kruskal.getOperations();

                    start = System.nanoTime();
                    var prim = Prim.findMST(undirected);
                    primTime += System.nanoTime() - start;
                    primOps = prim.getOperations();

                    start = System.nanoTime();
                    var dijkstra = Dijkstra.shortestPaths(undirected, 0);
                    dijkstraTime += System.nanoTime() - start;
                    dijkstraOps = dijkstra.operations;

                    start = System.nanoTime();
                    ConnectivityAlgorithms.kosarajuSCC(directed);
                    sccTime += System.nanoTime() - start;

                    start = System.nanoTime();
                    ConnectivityAlgorithms.topologicalSort(dag);
                    topoTime += System.nanoTime() - start;

                    start = System.nanoTime();
                    ConnectivityAlgorithms.topologicalSort(directed);
                    topoCycleTime += System.nanoTime() - start;
                }

                write(out, "Kruskal", vertices, edges, kruskalTime / REPEATS, kruskalOps, "connected undirected");
                write(out, "Prim", vertices, edges, primTime / REPEATS, primOps, "connected undirected");
                write(out, "Dijkstra", vertices, edges, dijkstraTime / REPEATS, dijkstraOps, "non-negative weights");
                write(out, "KosarajuSCC", vertices, edges, sccTime / REPEATS, -1, "directed graph; cycles allowed");
                write(out, "TopologicalDAG", vertices, edges, topoTime / REPEATS, -1, "DAG; edges go from lower to higher vertex");
                write(out, "TopologicalCyclicInput", vertices, edges, topoCycleTime / REPEATS, -1,
                        "cycle presence is not guaranteed for every generated graph");
                System.out.printf(Locale.US, "Finished V=%d, E=%d%n", vertices, edges);
            }
        }
        System.out.println("Benchmark saved to results/tables/benchmark.csv");
    }

    private static void write(PrintWriter out, String algorithm, int vertices, int edges,
                              long averageTime, long operations, String notes) {
        out.printf(Locale.US, "%s,%d,%d,%d,%d,%s%n",
                algorithm, vertices, edges, averageTime, operations, notes);
    }
}
