/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>();
    protected List<List<Edge>> neighbors = new ArrayList<>();
    
    protected AbstractGraph() {
    }
    
    protected AbstractGraph(V[] vertices, int[][] edges) {
        for(V vertex : vertices) {
            addVertex(vertex);
        }
        createAdjacencyLists(edges, vertices.length);
    }
    
    protected AbstractGraph(List<V> vertices, List<Edge> edges) {
        for(V vertex : vertices) {
            addVertex(vertex);
        }
        createAdjacencyLists(edges, vertices.size());
    }
    
    protected AbstractGraph(List<Edge> edges, int numberOfVertices) {
        for(int i = 0; i < numberOfVertices; i++) {
            addVertex(null);
        }
        createAdjacencyLists(edges, numberOfVertices);
    }
    
    protected AbstractGraph(int[][] edges, int numberOfVertices) {
        for(int i = 0; i < numberOfVertices; i++) {
            addVertex(null);
        }
        createAdjacencyLists(edges, numberOfVertices);
    }
    
    private void createAdjacencyLists(int[][] edges, int numberOfVertices) {
        for(int[] edge : edges) {
            addEdge(edge[0], edge[1]);
        }
    }
    
    private void createAdjacencyLists(List<Edge> edges, int numberOfVertices) {
        for(Edge edge : edges) {
            addEdge(edge.u, edge.v);
        }
    }
    
    @Override
    public int getSize() {
        return vertices.size();
    }
    
    @Override
    public List<V> getVertices() {
        return vertices;
    }
    
    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }
    
    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }
    
    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for(Edge e : neighbors.get(index)) {
            result.add(e.v);
        }
        return result;
    }
    
    @Override
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }
    
    @Override
    public void printEdges() {
        for(int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for(Edge e : neighbors.get(u)) {
                System.out.print("(" + getVertex(e.u) + ", " + getVertex(e.v) + ") ");
            }
            System.out.println();
        }
    }
    
    @Override
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }
    
    @Override
    public boolean addVertex(V vertex) {
        if(!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addEdge(Edge e) {
    if (e.u < 0 || e.u > getSize() - 1) {
        throw new IllegalArgumentException("No such index: " + e.u);
    }
    if (e.v < 0 || e.v > getSize() - 1) {
        throw new IllegalArgumentException("No such index: " + e.v);
    } 
    if (!neighbors.get(e.u).contains(e)) {
        neighbors.get(e.u).add(e);
        return true;
    } else {
        return false;
    }
}
    @Override
    public boolean addEdge(int u, int v) {
        return addEdge(new Edge(u, v));
    }
    
    @Override
    public boolean remove(V v) {
        int index = getIndex(v);
        if(index >= 0) {
            vertices.remove(index);
            neighbors.remove(index);
            
            // Remove all edges pointing to this vertex
            for(List<Edge> edgeList : neighbors) {
                for(int i = edgeList.size() - 1; i >= 0; i--) {
                    if(edgeList.get(i).v == index) {
                        edgeList.remove(i);
                    }
                }
            }
            
            // Update indices in all edges
            for(List<Edge> edgeList : neighbors) {
                for(Edge edge : edgeList) {
                    if(edge.u > index) edge.u--;
                    if(edge.v > index) edge.v--;
                }
            }
            return true;
        }   
        return false;
    }
    
    @Override
    public boolean remove(int u, int v) {
        if(u < 0 || u > getSize() - 1 || v < 0 || v > getSize() - 1) {
            return false;
        }
        
        List<Edge> edges = neighbors.get(u);
        for(int i = edges.size() - 1; i >= 0; i--) {
            if(edges.get(i).v == v) {
                edges.remove(i);
                break;
            }
        }
        
        edges = neighbors.get(v);
        for(int i = edges.size() - 1; i >= 0; i--) {
            if(edges.get(i).v == u) {
                edges.remove(i);
                break;
            }
        }
        return true;
    }
    
    // DFS implementation
    @Override
    public AbstractGraph<V>.SearchTree dfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for(int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        
        boolean[] isVisited = new boolean[vertices.size()];
        dfs(v, parent, searchOrder, isVisited);
        
        return new SearchTree(v, parent, searchOrder);
    }
    
    private void dfs(int v, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
        searchOrder.add(v);
        isVisited[v] = true;
        
        for(int neighbor : getNeighbors(v)) {
            if(!isVisited[neighbor]) {
                parent[neighbor] = v;
                dfs(neighbor, parent, searchOrder, isVisited);
            }
        }
    }
    
    // BFS implementation
    @Override
    public AbstractGraph<V>.SearchTree bfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for(int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        
        java.util.LinkedList<Integer> queue = new java.util.LinkedList<>();
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(v);
        isVisited[v] = true;
        
        while(!queue.isEmpty()) {
            int u = queue.poll();
            searchOrder.add(u);
            for(int w : getNeighbors(u)) {
                if(!isVisited[w]) {
                    queue.offer(w);
                    parent[w] = u;
                    isVisited[w] = true;
                }
            }
        }
        return new SearchTree(v, parent, searchOrder);
    }
    
    public class SearchTree {
        private int root;
        private int[] parent;
        private List<Integer> searchOrder;
        
        public SearchTree(int root, int[] parent, List<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }
        
        public int getRoot() {
            return root;
        }
        
        public int getParent(int v) {
            return parent[v];
        }
        
        public List<Integer> getSearchOrder() {
            return searchOrder;
        }
        
        public int getNumberOfVerticesFound() {
            return searchOrder.size();
        }
        
        public List<V> getPath(int index) {
            ArrayList<V> path = new ArrayList<>();
            do {
                path.add(vertices.get(index));
                index = parent[index];
            } while(index != -1);
            return path;
        }
        
        public void printPath(int index) {
            List<V> path = getPath(index);
            System.out.print("A path from " + vertices.get(root) + " to " + vertices.get(index) + ": ");
            for(int i = path.size() - 1; i >= 0; i--) {
                System.out.print(path.get(i) + " ");
            }
            System.out.println();
        }
        
        public void printTree() {
            System.out.println("Root is: " + vertices.get(root));
            System.out.print("Edges: ");
            for(int i = 0; i < parent.length; i++) {
                if(parent[i] != -1) {
                    System.out.print("(" + vertices.get(parent[i]) + ", " + vertices.get(i) + ") ");
                }
            }
            System.out.println();
        }
    }
}