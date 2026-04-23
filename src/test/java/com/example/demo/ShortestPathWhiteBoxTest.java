package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShortestPathWhiteBoxTest {
    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
        graph.addEdge("cat", "dog");   // 直接边
        graph.addEdge("cat", "tiger");
        graph.addEdge("tiger", "dog");
    }

    @Test
    void testWordNotExist() {
        assertEquals("No \"nonexist\" or \"dog\" in the graph!", 
                     graph.calcShortestPath("nonexist", "dog"));
    }

    @Test
    void testSameWord() {
        assertEquals("Shortest path length: 0\nPath: cat", 
                     graph.calcShortestPath("cat", "cat"));
    }

    @Test
    void testDirectEdge() {
        assertEquals("Shortest path length: 1\nPath: cat -> dog", 
                     graph.calcShortestPath("cat", "dog"));
    }

    @Test
    void testIndirectPath() {
        Graph g = new Graph();
        g.addEdge("cat", "tiger");
        g.addEdge("tiger", "dog");
        assertEquals("Shortest path length: 2\nPath: cat -> tiger -> dog", 
                     g.calcShortestPath("cat", "dog"));
    }

    @Test
    void testNoPath() {
        Graph g = new Graph();
        g.addVertex("cat");
        g.addVertex("dog");
        assertEquals("No path from \"cat\" to \"dog\"!", 
                     g.calcShortestPath("cat", "dog"));
    }
}