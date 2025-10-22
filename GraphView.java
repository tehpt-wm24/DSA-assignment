/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphView extends Pane{
    private Graph<? extends Displayable> graph;
    
    public GraphView(Graph<? extends Displayable> graph) {
        this.graph = graph;
        paintGraph();
    }
    
    private void paintGraph() {
        // Draw vertices
        java.util.List<? extends Displayable> vertices = graph.getVertices();
        for(int i = 0; i < graph.getSize(); i++) {
            int x = vertices.get(i).getX();
            int y = vertices.get(i).getY();
            String name = vertices.get(i).getName();
            
            // Different colors for users and movies
            Circle circle = new Circle(x, y, 16);
            if(name.startsWith("User: ")) {
                circle.setFill(javafx.scene.paint.Color.LIGHTGREEN);
            } else {
                circle.setFill(javafx.scene.paint.Color.LIGHTBLUE);
            }
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            
            getChildren().add(circle);
            getChildren().add(new Text(x - 20, y - 20, name.replace("User: ", "").replace("Movie: ", "")));
        }
        
        // Draw edges for pairs of vertices
        for(int i = 0; i < graph.getSize(); i++) {
            java.util.List<Integer> neighbors = graph.getNeighbors(i);
            int x1 = graph.getVertex(i).getX();
            int y1 = graph.getVertex(i).getY();
            for(int v : neighbors) {
                int x2 = graph.getVertex(v).getX();
                int y2 = graph.getVertex(v).getY();
                
                // Draw an edge for (i, v)
                Line line = new Line(x1, y1, x2, y2);
                
                // Different line styles for different relationships
                String name1 = graph.getVertex(i).getName();
                String name2 = graph.getVertex(v).getName();
                
                if(name1.startsWith("User: ") && name2.startsWith("User: ")) {
                    line.setStroke(javafx.scene.paint.Color.RED); // User-User connection
                    line.getStrokeDashArray().addAll(10.0, 5.0); // 🔹 Dashed line for friendships
                } else if(name1.startsWith("Movie: ") && name2.startsWith("Movie: ")) {
                    line.setStroke(javafx.scene.paint.Color.BLUE); // Movie-Movie connection
                    line.getStrokeDashArray().addAll(5.0, 5.0); // Dashed line for movie similarities
                } else {
                    line.setStroke(javafx.scene.paint.Color.GRAY); // User-Movie connection
                }

                
                getChildren().add(line);
            }
        }
    }
    
    public void updateGraph(Graph<? extends Displayable> newGraph) {
        this.graph = newGraph;
        getChildren().clear();
        paintGraph();
    }
}