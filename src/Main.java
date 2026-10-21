import connectivity.ConnectivityAlgorithms;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

public class Main {
    public static void main(String[] args) {
        Graph roads = new Graph(5, false);
        roads.addEdge(0, 1, 4);
        roads.addEdge(0, 2, 2);
        roads.addEdge(1, 2, 1);
        roads.addEdge(1, 3, 5);
        roads.addEdge(2, 3, 8);
        roads.addEdge(3, 4, 3);
        roads.addEdge(2, 4, 10);

        System.out.println("=== Minimum Spanning Tree ===");
        System.out.println("Kruskal: " + Kruskal.findMST(roads));
        System.out.println("Prim:    " + Prim.findMST(roads));

        Graph services = new Graph(5, true);
        services.addEdge(0, 1, 1);
        services.addEdge(0, 2, 1);
        services.addEdge(1, 3, 1);
        services.addEdge(2, 3, 1);
        services.addEdge(3, 4, 1);

        System.out.println("\\n=== Topological Ordering ===");
        ConnectivityAlgorithms.TopologicalResult topological =
                ConnectivityAlgorithms.topologicalSort(services);
        System.out.println(topological.isDag ? topological.order : "Cycle detected");

        Graph network = new Graph(5, true);
        network.addEdge(0, 1, 4);
        network.addEdge(0, 2, 2);
        network.addEdge(2, 1, 1);
        network.addEdge(1, 3, 5);
        network.addEdge(2, 3, 8);
        network.addEdge(3, 4, 3);

        System.out.println("\\n=== Dijkstra from vertex 0 ===");
        Dijkstra.Result shortest = Dijkstra.shortestPaths(network, 0);
        for (int vertex = 0; vertex < network.getVertexCount(); vertex++) {
            System.out.println("To " + vertex + ": distance=" + shortest.distanceToString(vertex)
                    + ", path=" + shortest.getPathTo(vertex));
        }

        Graph sccGraph = new Graph(5, true);
        sccGraph.addEdge(0, 1, 1);
        sccGraph.addEdge(1, 0, 1);
        sccGraph.addEdge(1, 2, 1);
        sccGraph.addEdge(2, 3, 1);
        sccGraph.addEdge(3, 2, 1);
        System.out.println("\\n=== Strongly Connected Components ===");
        System.out.println(ConnectivityAlgorithms.kosarajuSCC(sccGraph));
    }
}
