/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
import java.util.List;

public interface Graph<V> {
    public int getSize();
    public List<V> getVertices();
    public V getVertex(int index);
    public int getIndex(V v);
    public List<Integer> getNeighbors(int index);
    public int getDegree(int v);
    public void printEdges();
    public void clear();
    public boolean addVertex(V vertex);
    public boolean addEdge(int u, int v);
    public boolean addEdge(Edge e);
    public boolean remove(V v);
    public boolean remove(int u, int v);
    public AbstractGraph<V>.SearchTree dfs(int v);
    public AbstractGraph<V>.SearchTree bfs(int v);
}