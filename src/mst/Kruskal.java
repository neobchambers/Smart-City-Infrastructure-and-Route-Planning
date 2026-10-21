package mst;

import dsu.DisjointSetUnion;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {
    public static MSTResult findMST(Graph graph){
        if(graph.isDirected()){
            throw new IllegalArgumentException("Kruskal requires an undirected path.");
        }
        List<Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort(Comparator.comparingInt(edge -> edge.weight));
        DisjointSetUnion dsu = new DisjointSetUnion(graph.getVertexCount());
        List<Edge> chosen = new ArrayList<>();
        long totalWeight = 0;
        long operations = 0;

        for (Edge edge : sorted){
            operations++;
            if(dsu.union(edge.from, edge.to)){
                chosen.add(edge);
                totalWeight += edge.weight;
                if (chosen.size() == graph.getVertexCount() - 1) {
                    break;
                }
            }
        }

        boolean connected = chosen.size() == graph.getVertexCount() - 1;
        return new MSTResult(chosen, totalWeight, connected, operations);
    }
}
