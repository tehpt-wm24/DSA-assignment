/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

public class MovieVertex implements Displayable {
    private String name;
    private int x;
    private int y;

    // Constructor with coordinates
    public MovieVertex(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Optional: constructor without coordinates (default position)
    public MovieVertex(String name) {
        this(name, 100, 100); // Default position
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    // Setters in case you want to move nodes later
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return name + " (" + x + "," + y + ")";
    }
}

