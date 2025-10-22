/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
public class Edge {
    public int u;
    public int v;
    
    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }
    
    public boolean equals(Object o) {
        return u == ((Edge) o).u && v == ((Edge) o).v;
    }
    
    public String toString() {
        return "(" + u + ", " + v + ")";
    }
}