/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
import java.util.List;

public class UnweightedGraph<V> extends AbstractGraph<V> {
    public UnweightedGraph() {
    }
    
    public UnweightedGraph(V[] vertices, int[][] edges) {
        super(vertices, edges);
    }
    
    public UnweightedGraph(List<V> vertices, List<Edge> edges) {
        super(vertices, edges);
    }
    
    public UnweightedGraph(List<Edge> edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }
    
    public UnweightedGraph(int[][] edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }
}