package dsu;

public class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;
    private int components;

    public DisjointSetUnion(int size){
        if(size < 1){
            throw new IllegalArgumentException("DSU size must be positive.");
        }
        parent = new int[size];
        rank = new int[size];
        components = size;
        for (int i = 0; i < size;i++){
            parent[i] = i;
        }
    }
    public int find(int value) {
        check(value);
        if(parent[value] != value){
            parent[value] = find(parent[value]);
        }
        return parent[value];
    }

    public boolean union(int first, int second) {
        int rootFirst = find(first);
        int rootSecond = find(second);
        if (rootFirst == rootSecond) {
            return false;
        }
        if (rank[rootFirst] < rank[rootSecond]){
            parent[rootFirst] = rootSecond;
        } else if (rank[rootFirst] > rank[rootSecond]) {
            parent[rootSecond] = rootFirst;
        }
        else {
            parent[rootSecond] = rootFirst;
            rank[rootFirst]++;
        }
        components--;
        return true;
    }
    public int getComponentsCount(){
        return components;
    }

    private void check(int value) {
        if(value < 0 || value >= parent.length){
            throw new IllegalArgumentException("Invalid DSU element: " + value);
        }
    }
}
