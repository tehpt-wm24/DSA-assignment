/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

/**
 *
 * @author pingting
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.util.*;

public class MovieRecommendationSystem extends Application {

    private Graph<MovieVertex> graph;
    private List<MovieVertex> vertices;
    private GraphView graphView;
    private TextArea outputArea;
    private TextField userField, addMovieField, watchedMovieField, movie1Field, movie2Field;
    private ComboBox<String> watchedUserComboBox;
    private ComboBox<String> recommendUserComboBox;
    private ComboBox<String> friendUser1;
    private ComboBox<String> friendUser2;
    private Spinner<Integer> depthSpinner;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Recommendation System");
        printAsciiTitle();

        try {

            initializeUI();

            initializeSampleData();

            BorderPane mainLayout = createMainLayout();

            ScrollPane scrollableRoot = new ScrollPane(mainLayout);
            scrollableRoot.setFitToWidth(true);
            scrollableRoot.setFitToHeight(true);

            Scene scene = new Scene(scrollableRoot, 900, 700);
            primaryStage.setScene(scene);
            primaryStage.show();

            displayInitialRecommendations();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error during startup: " + e.getMessage());
        }
    }

    private void printAsciiTitle() {
        System.out.println("\n ____| _)  |             ____|            _)               ");
        System.out.println(" |      |  |  __ `__ \\   |    |   |   __|  |   _ \\   __ \\  ");
        System.out.println(" __|    |  |  |   |   |  __|  |   | \\__ \\  |  (   |  |   | ");
        System.out.println("_|     _| _| _|  _|  _| _|   \\__,_| ____/ _| \\___/  _|  _| \n");
    }

    private void initializeUI() {
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(150);
        outputArea.setStyle("-fx-font-family: monospace;");

        userField = new TextField();
        userField.setPromptText("Enter username");

        addMovieField = new TextField();
        addMovieField.setPromptText("Enter movie name");

        watchedMovieField = new TextField();
        watchedMovieField.setPromptText("Movie name");

        movie1Field = new TextField();
        movie1Field.setPromptText("First movie");

        movie2Field = new TextField();
        movie2Field.setPromptText("Second movie");

        watchedUserComboBox = new ComboBox<>();
        watchedUserComboBox.setPromptText("Select user");

        recommendUserComboBox = new ComboBox<>();
        recommendUserComboBox.setPromptText("Select user");

        friendUser1 = new ComboBox<>();
        friendUser1.setPromptText("Select User 1");

        friendUser2 = new ComboBox<>();
        friendUser2.setPromptText("Select User 2");

        depthSpinner = new Spinner<>(1, 5, 2);
        depthSpinner.setEditable(true);
    }

    private BorderPane createMainLayout() {
        Text asciiTitle = new Text();
        asciiTitle.setText("\n ____| _)  |             ____|            _)               "
                + "\n |      |  |  __ `__ \\   |    |   |   __|  |   _ \\   __ \\  "
                + "\n __|    |  |  |   |   |  __|  |   | \\__ \\  |  (   |  |   | "
                + "\n_|     _| _| _|  _|  _| _|   \\__,_| ____/ _| \\___/  _|  _| \n");
        asciiTitle.setFont(Font.font("Monospaced", 12));
        asciiTitle.setStyle("-fx-fill: #2c3e50; -fx-font-weight: bold;"); // Very dark blue

        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Movie Recommendation System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        titleBox.getChildren().addAll(asciiTitle, titleLabel);

        // Create control panel
        GridPane controlPanel = createControlPanel();

        // Create graph view
        graphView = new GraphView(graph);
        graphView.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7;");
        graphView.setPrefSize(1200, 800);
        graphView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        graphView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Create scroll pane
        ScrollPane graphScrollPane = new ScrollPane(graphView);
        graphScrollPane.setPannable(true);
        graphScrollPane.setFitToWidth(false);
        graphScrollPane.setFitToHeight(false);
        graphScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        graphScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleBox);
        mainLayout.setCenter(graphScrollPane);
        mainLayout.setBottom(outputArea);
        mainLayout.setRight(controlPanel);

        return mainLayout;
    }

    private GridPane createControlPanel() {
        GridPane controlPanel = new GridPane();
        controlPanel.setPadding(new Insets(10));
        controlPanel.setHgap(10);
        controlPanel.setVgap(10);
        controlPanel.setStyle("-fx-background-color: #dfe6e9;");

        controlPanel.add(new Label("Add User:"), 0, 0);
        controlPanel.add(userField, 1, 0);
        Button addUserBtn = createButton("Add User", "#27ae60");
        Button deleteUserBtn = createButton("Delete User", "#c0392b");
        controlPanel.add(addUserBtn, 2, 0);
        controlPanel.add(deleteUserBtn, 3, 0);

        controlPanel.add(new Label("Add Movie:"), 0, 1);
        controlPanel.add(addMovieField, 1, 1);
        Button addMovieBtn = createButton("Add Movie", "#2980b9");
        Button deleteMovieBtn = createButton("Delete Movie", "#c0392b");
        controlPanel.add(addMovieBtn, 2, 1);
        controlPanel.add(deleteMovieBtn, 3, 1);

        controlPanel.add(new Label("Record Watched:"), 0, 2);
        controlPanel.add(watchedUserComboBox, 1, 2);
        controlPanel.add(new Label("Movie:"), 2, 2);
        controlPanel.add(watchedMovieField, 3, 2);
        Button addWatchedBtn = createButton("Record", "#f39c12");
        Button removeWatchedBtn = createButton("Remove Watch", "#d35400");
        controlPanel.add(addWatchedBtn, 4, 2);
        controlPanel.add(removeWatchedBtn, 5, 2);

        controlPanel.add(new Label("Add Similarity:"), 0, 3);
        controlPanel.add(movie1Field, 1, 3);
        controlPanel.add(new Label("Movie 2:"), 2, 3);
        controlPanel.add(movie2Field, 3, 3);
        Button addSimilarityBtn = createButton("Add Similarity", "#8e44ad");
        Button removeSimilarityBtn = createButton("Remove Similarity", "#9b59b6");
        controlPanel.add(addSimilarityBtn, 4, 3);
        controlPanel.add(removeSimilarityBtn, 5, 3);

        controlPanel.add(new Label("Get Recommendations:"), 0, 4);
        controlPanel.add(recommendUserComboBox, 1, 4);
        controlPanel.add(new Label("Depth:"), 2, 4);
        controlPanel.add(depthSpinner, 3, 4);
        Button recommendBtn = createButton("Get Recommendations", "#e74c3c");
        controlPanel.add(recommendBtn, 4, 4);

        controlPanel.add(new Label("Friendship:"), 0, 5);
        controlPanel.add(friendUser1, 1, 5);
        controlPanel.add(friendUser2, 2, 5);
        Button addFriendBtn = createButton("Add Friend", "#16a085");
        Button removeFriendBtn = createButton("Remove Friend", "#e67e22");
        controlPanel.add(addFriendBtn, 3, 5);
        controlPanel.add(removeFriendBtn, 4, 5);

        Button displayGraphBtn = createButton("Display Graph", "#34495e");
        controlPanel.add(displayGraphBtn, 0, 6, 5, 1);

        wireButtonActions(
                addUserBtn, deleteUserBtn,
                addMovieBtn, deleteMovieBtn,
                addWatchedBtn, removeWatchedBtn,
                addSimilarityBtn, removeSimilarityBtn,
                recommendBtn, addFriendBtn,
                removeFriendBtn, displayGraphBtn
        );

        return controlPanel;
    }

    private void wireButtonActions(
            Button addUserBtn, Button deleteUserBtn,
            Button addMovieBtn, Button deleteMovieBtn,
            Button addWatchedBtn, Button removeWatchedBtn,
            Button addSimilarityBtn, Button removeSimilarityBtn,
            Button recommendBtn, Button addFriendBtn,
            Button removeFriendBtn, Button displayGraphBtn
    ) {

        addUserBtn.setOnAction(e -> addUser());
        deleteUserBtn.setOnAction(e -> deleteUser());

        addMovieBtn.setOnAction(e -> addMovie());
        deleteMovieBtn.setOnAction(e -> deleteMovie());

        addWatchedBtn.setOnAction(e -> recordWatched());
        removeWatchedBtn.setOnAction(e -> removeWatched());

        addSimilarityBtn.setOnAction(e -> addSimilarity());
        removeSimilarityBtn.setOnAction(e -> removeSimilarity());

        recommendBtn.setOnAction(e -> getRecommendations());

        addFriendBtn.setOnAction(e -> addFriend());
        removeFriendBtn.setOnAction(e -> removeFriend());

        displayGraphBtn.setOnAction(e -> displayGraph());
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void initializeSampleData() {
        // Create vertices (users and movies)
        vertices = new ArrayList<>();

        // Add users (only Ping Ting, Chiew Chin, and Chun Wan)
        vertices.add(new MovieVertex("User: Ping Ting", 250, 100));
        vertices.add(new MovieVertex("User: Chiew Chin", 200, 200));
        vertices.add(new MovieVertex("User: Chun Wan", 250, 300));

        // Add movies
        vertices.add(new MovieVertex("Movie: Titanic", 400, 100));
        vertices.add(new MovieVertex("Movie: Joker", 450, 200));
        vertices.add(new MovieVertex("Movie: Godzilla x Kong", 400, 300));
        vertices.add(new MovieVertex("Movie: Fast & Furious", 450, 400));

        // Create edges (user watched movie relationships)
        List<Edge> edges = new ArrayList<>();

        // Ping Ting's watched movies
        edges.add(new Edge(0, 3));
        edges.add(new Edge(0, 4));
        edges.add(new Edge(0, 5));
        edges.add(new Edge(0, 6));

        // Chiew Chin's watched movies
        edges.add(new Edge(1, 3));
        edges.add(new Edge(1, 6));

        // Chun Wan's watched movies
        edges.add(new Edge(2, 3));
        edges.add(new Edge(2, 5));
        edges.add(new Edge(2, 6));

        // Movie similarities
        edges.add(new Edge(3, 4));  // Titanic -> Joker
        edges.add(new Edge(3, 6));  // Titanic -> Fast & Furious
        edges.add(new Edge(4, 5));  // Joker -> Godzilla x Kong

        graph = new UnweightedGraph<>(vertices, edges);
        updateUserComboBox();
    }

    private int countEdges() {
        int count = 0;
        for (int i = 0; i < graph.getSize(); i++) {
            count += graph.getNeighbors(i).size();
        }
        return count / 2; // Each edge is counted twice in an undirected graph
    }

    private void displayInitialRecommendations() {
        outputArea.setText("╔══════════════════════════════════════════════════╗\n");
        outputArea.appendText("║           MOVIE RECOMMENDATION SYSTEM            ║\n");
        outputArea.appendText("╚══════════════════════════════════════════════════╝\n\n");
        outputArea.appendText("📊 Sample data loaded successfully!\n");
        outputArea.appendText("🎬 Graph displayed with " + graph.getSize() + " vertices\n");
        outputArea.appendText("🔗 Total connections: " + countEdges() + " edges\n\n");
        outputArea.appendText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        outputArea.appendText("\t\t\t   💡 INITIAL RECOMMENDATIONS\n");
        outputArea.appendText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        // Display recommendations for each user
        for (int i = 0; i < 3; i++) {
            String userName = vertices.get(i).getName().replace("User: ", "");
            List<Integer> watchedMovies = graph.getNeighbors(i);
            Map<Integer, Integer> recommendationDepths = getRecommendationsWithDepth(i, 2);
            List<Integer> recommendations = new ArrayList<>(recommendationDepths.keySet());

            outputArea.appendText("USER: " + userName.toUpperCase() + "\n");
            outputArea.appendText("   Watched: ");

            if (watchedMovies.isEmpty()) {
                outputArea.appendText("No movies yet\n");
            } else {
                for (int j = 0; j < watchedMovies.size(); j++) {
                    if (j > 0) {
                        outputArea.appendText(", ");
                    }
                    outputArea.appendText(vertices.get(watchedMovies.get(j)).getName().replace("Movie: ", ""));
                }
                outputArea.appendText("\n");
            }

            outputArea.appendText("   Recommendations for " + userName + ":\n");
            if (recommendations.isEmpty()) {
                outputArea.appendText("   No recommendations found\n");
            } else {
                for (int index : recommendations) {
                    outputArea.appendText("   • " + vertices.get(index).getName().replace("Movie: ", "") + "\n");
                }
            }
            outputArea.appendText("\n");
        }

        outputArea.appendText("💡 Tip: Use the controls on the right to add users, movies, and get personalized recommendations!\n");
    }

    private void addUser() {
        String username = userField.getText().trim();
        if (username.isEmpty()) {
            outputArea.setText("❌ Error: Username cannot be empty.");
            return;
        }

        String userKey = "User: " + username;

        for (MovieVertex v : vertices) {
            if (v.getName().equals(userKey)) {
                outputArea.setText("⚠ User '" + username + "' already exists.");
                return;
            }
        }

        long userCount = vertices.stream()
                .filter(v -> v.getName().startsWith("User: "))
                .count();

        int x = 100;
        int y = 140 + (int) (userCount * 80);

        MovieVertex newUser = new MovieVertex(userKey, x, y);
        vertices.add(newUser);

        // Rebuild graph
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                if (i < neighbor) {
                    edges.add(new Edge(i, neighbor));
                }
            }
        }

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);
        updateUserComboBox();

        // Auto-select new user in both dropdowns
        watchedUserComboBox.setValue(username);
        recommendUserComboBox.setValue(username);

        outputArea.setText("✅ SUCCESS: User '" + username + "' added successfully!\n\n");
        outputArea.appendText("👤 New user profile created\n");
        outputArea.appendText("📊 Total users now: " + countUsers() + "\n");
        outputArea.appendText("🎯 User can now receive movie recommendations");

        userField.clear();
    }

    private void deleteUser() {
        String username = userField.getText().trim();
        if (username.isEmpty()) {
            outputArea.setText("❌ Error: Username cannot be empty.");
            return;
        }

        String userKey = "User: " + username;
        int userIndex = -1;

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals(userKey)) {
                userIndex = i;
                break;
            }
        }

        if (userIndex == -1) {
            outputArea.setText("⚠ User '" + username + "' not found.");
            return;
        }

        // Remove vertex
        vertices.remove(userIndex);

        graph = new UnweightedGraph<>(vertices, rebuildEdgesAfterRemoval(userIndex));
        graphView.updateGraph(graph);
        updateUserComboBox();

        outputArea.setText("🗑 User '" + username + "' deleted successfully.");
        userField.clear();
    }

    private void addMovie() {
        String movieName = addMovieField.getText().trim();
        if (movieName.isEmpty()) {
            outputArea.setText("❌ Error: Movie name cannot be empty.");
            return;
        }

        String movieKey = "Movie: " + movieName;

        for (MovieVertex vertex : vertices) {
            if (vertex.getName().equals(movieKey)) {
                outputArea.setText("⚠ Movie '" + movieName + "' already exists!");
                addMovieField.clear();
                return;
            }
        }

        MovieVertex newMovie = new MovieVertex(movieKey, 400 + vertices.size() * 20, 100 + (vertices.size() % 5) * 80);
        vertices.add(newMovie);

        // Create a new graph with the updated vertices
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                edges.add(new Edge(i, neighbor));
            }
        }

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.setText("✅ SUCCESS: Movie '" + movieName + "' added successfully!\n\n");
        outputArea.appendText("🎬 New movie available for recommendations\n");
        outputArea.appendText("📊 Total movies now: " + countMovies() + "\n");
        outputArea.appendText("💡 Users can now watch and rate this movie");

        addMovieField.clear();
    }

    private void deleteMovie() {
        String movieName = addMovieField.getText().trim();
        if (movieName.isEmpty()) {
            outputArea.setText("❌ Error: Movie name cannot be empty.");
            return;
        }

        String movieKey = "Movie: " + movieName;
        int movieIndex = -1;

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals(movieKey)) {
                movieIndex = i;
                break;
            }
        }

        if (movieIndex == -1) {
            outputArea.setText("⚠ Movie '" + movieName + "' not found.");
            return;
        }

        // Remove vertex
        vertices.remove(movieIndex);

        graph = new UnweightedGraph<>(vertices, rebuildEdgesAfterRemoval(movieIndex));
        graphView.updateGraph(graph);

        outputArea.setText("🗑 Movie '" + movieName + "' deleted successfully.");
        addMovieField.clear();
    }

    private void addFriend() {
        String u1 = friendUser1.getValue();
        String u2 = friendUser2.getValue();

        if (u1 == null || u2 == null) {
            outputArea.setText("❌ Error: Please select both users.");
            return;
        }
        if (u1.equals(u2)) {
            outputArea.setText("⚠ Cannot add a user as their own friend.");
            return;
        }

        int u1Index = -1, u2Index = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals("User: " + u1)) {
                u1Index = i;
            }
            if (vertices.get(i).getName().equals("User: " + u2)) {
                u2Index = i;
            }
        }

        if (u1Index == -1 || u2Index == -1) {
            outputArea.setText("⚠ One or both users not found.");
            return;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                edges.add(new Edge(i, neighbor));
            }
        }
        edges.add(new Edge(u1Index, u2Index));

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.setText("🤝 Friendship added between " + u1 + " and " + u2);
        friendUser1.setValue(null);
        friendUser2.setValue(null);
    }

    private void removeFriend() {
        String u1 = friendUser1.getValue();
        String u2 = friendUser2.getValue();

        if (u1 == null || u2 == null) {
            outputArea.setText("❌ Error: Please select both users.");
            return;
        }
        if (u1.equals(u2)) {
            outputArea.setText("⚠ Cannot remove friendship with self.");
            return;
        }

        int u1Index = -1, u2Index = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals("User: " + u1)) {
                u1Index = i;
            }
            if (vertices.get(i).getName().equals("User: " + u2)) {
                u2Index = i;
            }
        }

        if (u1Index == -1 || u2Index == -1) {
            outputArea.setText("⚠ One or both users not found.");
            return;
        }

        boolean friendshipExists = false;
        for (int neighbor : graph.getNeighbors(u1Index)) {
            if (neighbor == u2Index) {
                friendshipExists = true;
                break;
            }
        }

        if (!friendshipExists) {
            outputArea.setText("❌ Error: No friendship exists between " + u1 + " and " + u2 + ".");
            return;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                if (!((i == u1Index && neighbor == u2Index) || (i == u2Index && neighbor == u1Index))) {
                    edges.add(new Edge(i, neighbor));
                }
            }
        }

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.setText("🚫 Friendship removed between " + u1 + " and " + u2);
        friendUser1.setValue(null);
        friendUser2.setValue(null);
    }

    private List<Edge> rebuildEdgesAfterRemoval(int removedIndex) {
        List<Edge> newEdges = new ArrayList<>();

        // Create index mapping
        Map<Integer, Integer> indexMap = new HashMap<>();
        int newIndex = 0;

        for (int oldIndex = 0; oldIndex < graph.getSize(); oldIndex++) {
            if (oldIndex != removedIndex) {
                indexMap.put(oldIndex, newIndex);
                newIndex++;
            }
        }

        // Rebuild edges with corrected indices
        for (int oldI = 0; oldI < graph.getSize(); oldI++) {
            if (oldI == removedIndex) {
                continue;
            }

            for (int oldNeighbor : graph.getNeighbors(oldI)) {
                if (oldNeighbor == removedIndex) {
                    continue;
                }

                // Only add each edge once (i < neighbor)
                if (oldI < oldNeighbor) {
                    Integer correctedI = indexMap.get(oldI);
                    Integer correctedNeighbor = indexMap.get(oldNeighbor);

                    if (correctedI != null && correctedNeighbor != null) {
                        newEdges.add(new Edge(correctedI, correctedNeighbor));
                    }
                }
            }
        }

        return newEdges;
    }

    private void recordWatched() {
        String username = watchedUserComboBox.getValue();
        if (username == null) {
            outputArea.setText("❌ Error: Please select a user first.");
            return;
        }

        String movieName = watchedMovieField.getText().trim();
        if (movieName.isEmpty()) {
            outputArea.setText("❌ Error: Please enter a movie name.");
            return;
        }

        // Find the movie vertex
        int movieIndex = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals("Movie: " + movieName)) {
                movieIndex = i;
                break;
            }
        }

        if (movieIndex == -1) {
            outputArea.setText("❌ Error: Movie not found. Please add it first.");
            return;
        }

        // Find the user vertex
        int userIndex = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals("User: " + username)) {
                userIndex = i;
                break;
            }
        }

        // Add the edge
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                edges.add(new Edge(i, neighbor));
            }
        }
        edges.add(new Edge(userIndex, movieIndex));

        graph = new UnweightedGraph<>(vertices, edges);
        graph.addEdge(userIndex, movieIndex);
        graphView.updateGraph(graph);

        outputArea.setText("Recorded: " + username + " watched " + movieName);
        watchedMovieField.clear();
    }

    private void removeWatched() {
        String user = watchedUserComboBox.getValue();
        String movie = watchedMovieField.getText().trim();

        if (user == null || movie.isEmpty()) {
            outputArea.appendText("❌ Please select a user and enter a movie to remove.\n");
            return;
        }

        String fullUser = "User: " + user;
        String fullMovie = "Movie: " + movie;

        int userIndex = -1, movieIndex = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals(fullUser)) {
                userIndex = i;
            }
            if (vertices.get(i).getName().equals(fullMovie)) {
                movieIndex = i;
            }
        }

        if (userIndex == -1 || movieIndex == -1) {
            outputArea.appendText("⚠ One or both nodes not found.\n");
            return;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                if (!(i == userIndex && neighbor == movieIndex)
                        && !(i == movieIndex && neighbor == userIndex)) {
                    edges.add(new Edge(i, neighbor));
                }
            }
        }

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.appendText("🗑 Removed watched record: " + user + " → " + movie + "\n");
        watchedMovieField.clear();
    }

    private void addSimilarity() {
        String movie1 = movie1Field.getText().trim();
        String movie2 = movie2Field.getText().trim();

        if (movie1.isEmpty() || movie2.isEmpty()) {
            outputArea.setText("❌ Error: Please enter both movie names.");
            return;
        }

        // Find the movie vertices
        int movie1Index = -1, movie2Index = -1;
        for (int i = 0; i < vertices.size(); i++) {
            String name = vertices.get(i).getName();
            if (name.equals("Movie: " + movie1)) {
                movie1Index = i;
            }
            if (name.equals("Movie: " + movie2)) {
                movie2Index = i;
            }
        }

        if (movie1Index == -1 || movie2Index == -1) {
            outputArea.setText("❌ Error: One or both movies not found. Please add them first.");
            return;
        }

        // Add the edge
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                edges.add(new Edge(i, neighbor));
            }
        }
        edges.add(new Edge(movie1Index, movie2Index));

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.setText("Added similarity between " + movie1 + " and " + movie2);
        movie1Field.clear();
        movie2Field.clear();
    }

    private void removeSimilarity() {
        String movie1 = movie1Field.getText().trim();
        String movie2 = movie2Field.getText().trim();

        if (movie1.isEmpty() || movie2.isEmpty()) {
            outputArea.appendText("❌ Please enter both movies to remove similarity.\n");
            return;
        }

        String fullMovie1 = "Movie: " + movie1;
        String fullMovie2 = "Movie: " + movie2;

        int m1Index = -1, m2Index = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals(fullMovie1)) {
                m1Index = i;
            }
            if (vertices.get(i).getName().equals(fullMovie2)) {
                m2Index = i;
            }
        }

        if (m1Index == -1 || m2Index == -1) {
            outputArea.appendText("⚠ One or both movies not found.\n");
            return;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                if (!(i == m1Index && neighbor == m2Index)
                        && !(i == m2Index && neighbor == m1Index)) {
                    edges.add(new Edge(i, neighbor));
                }
            }
        }

        graph = new UnweightedGraph<>(vertices, edges);
        graphView.updateGraph(graph);

        outputArea.appendText("🗑 Removed similarity between " + movie1 + " and " + movie2 + "\n");
        movie1Field.clear();
        movie2Field.clear();
    }

    private void getRecommendations() {
        String username = recommendUserComboBox.getValue();
        if (username == null) {
            outputArea.setText("❌ Error: Please select a user first.");
            return;
        }

        // Find the user vertex
        int userIndex = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals("User: " + username)) {
                userIndex = i;
                break;
            }
        }

        int depth = depthSpinner.getValue();

        // 🔹 Get recommendations using BFS on movie similarities
        Map<Integer, Integer> recommendationDepths = getRecommendationsWithDepth(userIndex, depth);

        // Get movies already watched
        List<Integer> watchedMovies = new ArrayList<>();
        for (int neighbor : graph.getNeighbors(userIndex)) {
            if (vertices.get(neighbor).getName().startsWith("Movie: ")) {
                watchedMovies.add(neighbor);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════╗\n");
        sb.append("║               RECOMMENDATION RESULTS             ║\n");
        sb.append("╚══════════════════════════════════════════════════╝\n\n");

        sb.append("👤 User: ").append(username).append("\n");
        sb.append("🔍 Search depth: ").append(depth).append("\n\n");

        sb.append("🎬 Currently watching:\n");
        if (watchedMovies.isEmpty()) {
            sb.append("   • No movies watched yet\n");
        } else {
            for (int index : watchedMovies) {
                sb.append("   • ").append(vertices.get(index).getName().replace("Movie: ", "")).append("\n");
            }
        }
        sb.append("\n");

        sb.append("💫 Recommended movies:\n");

        if (recommendationDepths.isEmpty()) {
            sb.append("   • No recommendations found\n");
            sb.append("   💡 Try increasing the search depth or adding more movie similarities\n");
        } else {
            for (int index : recommendationDepths.keySet()) {
                String movieName = vertices.get(index).getName().replace("Movie: ", "");
                int d = recommendationDepths.get(index);
                sb.append("   • ").append(movieName).append(" (found at depth ").append(d).append(")\n");
            }
        }

        sb.append("\n📈 Found ").append(recommendationDepths.size()).append(" recommendations total\n");

        outputArea.setText(sb.toString());
    }

    private void displayGraph() {
        graphView.updateGraph(graph);

        StringBuilder graphInfo = new StringBuilder();
        graphInfo.append("╔══════════════════════════════════════════════════╗\n");
        graphInfo.append("║                  GRAPH OVERVIEW                  ║\n");
        graphInfo.append("╚══════════════════════════════════════════════════╝\n\n");

        graphInfo.append("📊 Graph Statistics:\n");
        graphInfo.append("   • Total vertices: ").append(graph.getSize()).append("\n");
        graphInfo.append("   • Total edges: ").append(countEdges()).append("\n");
        graphInfo.append("   • Users: ").append(countUsers()).append("\n");
        graphInfo.append("   • Movies: ").append(countMovies()).append("\n\n");

        graphInfo.append("👥 Users in system:\n");
        for (MovieVertex vertex : vertices) {
            if (vertex.getName().startsWith("User: ")) {
                graphInfo.append("   • ").append(vertex.getName().replace("User: ", "")).append("\n");
            }
        }

        graphInfo.append("\n🎬 Movies in system:\n");
        for (MovieVertex vertex : vertices) {
            if (vertex.getName().startsWith("Movie: ")) {
                graphInfo.append("   • ").append(vertex.getName().replace("Movie: ", "")).append("\n");
            }
        }

        outputArea.setText(graphInfo.toString());
    }

    private int countUsers() {
        int count = 0;
        for (MovieVertex vertex : vertices) {
            if (vertex.getName().startsWith("User: ")) {
                count++;
            }
        }
        return count;
    }

    private int countMovies() {
        int count = 0;
        for (MovieVertex vertex : vertices) {
            if (vertex.getName().startsWith("Movie: ")) {
                count++;
            }
        }
        return count;
    }

    // BFS-based recommendation algorithm
    private Map<Integer, Integer> getRecommendationsWithDepth(int userIndex, int maxDepth) {
        Map<Integer, Integer> recommendationDepths = new HashMap<>();

        if (userIndex < 0 || userIndex >= vertices.size() || maxDepth < 1) {
            return recommendationDepths;
        }

        // Step 1: find all movies this user has already watched
        Set<Integer> watchedMovies = new HashSet<>();
        for (int neighbor : graph.getNeighbors(userIndex)) {
            if (vertices.get(neighbor).getName().startsWith("Movie: ")) {
                watchedMovies.add(neighbor);
            }
        }

        // Step 2: BFS starting from watched movies, but only through Movie–Movie edges
        Queue<int[]> queue = new LinkedList<>(); // {movieIndex, depth}
        Set<Integer> visited = new HashSet<>();

        for (int movie : watchedMovies) {
            queue.add(new int[]{movie, 0});
            visited.add(movie);
        }

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int movieIndex = current[0];
            int depth = current[1];

            if (depth >= maxDepth) {
                continue;
            }

            for (int neighbor : graph.getNeighbors(movieIndex)) {
                String name = vertices.get(neighbor).getName();

                // Only follow Movie ↔ Movie edges
                if (name.startsWith("Movie: ") && !watchedMovies.contains(neighbor)) {
                    if (!recommendationDepths.containsKey(neighbor)) {
                        recommendationDepths.put(neighbor, depth + 1);
                        queue.add(new int[]{neighbor, depth + 1});
                    }
                }
            }
        }
        return recommendationDepths;
    }

    private void updateUserComboBox() {
        watchedUserComboBox.getItems().clear();
        recommendUserComboBox.getItems().clear();
        friendUser1.getItems().clear();
        friendUser2.getItems().clear();

        watchedUserComboBox.setPromptText("Select User");
        recommendUserComboBox.setPromptText("Select User");
        friendUser1.setPromptText("Select User 1");
        friendUser2.setPromptText("Select User 2");

        for (Displayable v : vertices) {
            String name = v.getName();
            if (name.startsWith("User: ")) {
                String userName = name.replace("User: ", "");
                watchedUserComboBox.getItems().add(userName);
                recommendUserComboBox.getItems().add(userName);
                friendUser1.getItems().add(userName);
                friendUser2.getItems().add(userName);
            }
        }
    }

    // Helper Method
    private void showSuccessMessage(String title, String message) {
        outputArea.setText("✅ " + title + "\n\n" + message);
    }

    private void showErrorMessage(String message) {
        outputArea.setText("❌ Error: " + message);
    }

    private void showInfoMessage(String title, String message) {
        outputArea.setText("💡 " + title + "\n\n" + message);
    }

    // Vertex class implementing Displayable
    static class MovieVertex implements Displayable {

        private String name;
        private int x, y;

        public MovieVertex(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}