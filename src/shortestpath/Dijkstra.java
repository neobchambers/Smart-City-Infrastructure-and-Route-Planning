package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public static class Result {
        public final long[] distances;
        public final int[] previous;
        public final long operations;

        Result(long[] distances, int[] previous, long operations) {
            this.distances = distances;
            this.previous = previous;
            this.operations = operations;
        }

        public List<Integer> getPathTo(int target) {
            if (target < 0 || target >= distances.length) {
                throw new IllegalArgumentException("Invalid target vertex.");
            }
            if (distances[target] == Long.MAX_VALUE) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            int current = target;
            while (current != -1) {
                path.add(current);
                current = previous[current];
            }
            Collections.reverse(path);
            return path;
        }

        public String distanceToString(int vertex) {
            return distances[vertex] == Long.MAX_VALUE ? "unreachable" : Long.toString(distances[vertex]);
        }
    }

    private static class State {
        int vertex;
        long distance;

        State(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static Result shortestPaths(Graph graph, int source) {
        graph.checkVertex(source);
        int n = graph.getVertexCount();
        for (Edge edge : graph.getEdges()) {
            if (edge.weight < 0) {
                throw new IllegalArgumentException("Dijkstra cannot process negative edge weights.");
            }
        }

        long[] distance = new long[n];
        int[] previous = new int[n];
        Arrays.fill(distance, Long.MAX_VALUE);
        Arrays.fill(previous, -1);
        distance[source] = 0;

        PriorityQueue<State> queue =
                new PriorityQueue<>(Comparator.comparingLong(state -> state.distance));
        queue.add(new State(source, 0));
        long operations = 0;

        while (!queue.isEmpty()) {
            State current = queue.poll();
            operations++;
            if (current.distance != distance[current.vertex]) {
                continue;
            }
            for (Edge edge : graph.getNeighbors(current.vertex)) {
                operations++;
                if (distance[current.vertex] != Long.MAX_VALUE
                        && distance[current.vertex] + edge.weight < distance[edge.to]) {
                    distance[edge.to] = distance[current.vertex] + edge.weight;
                    previous[edge.to] = current.vertex;
                    queue.add(new State(edge.to, distance[edge.to]));
                }
            }
        }
        return new Result(distance, previous, operations);
    }
}
