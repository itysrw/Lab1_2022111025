package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BridgeWordsBlackBoxTest {
    private Graph graphWithOneBridge;
    private Graph graphWithTwoBridges;
    private Graph graphWithNoBridge;
    private Graph emptyGraph;

    @BeforeEach
    void setUp() {
        graphWithOneBridge = new Graph();
        graphWithOneBridge.addEdge("cat", "tiger");
        graphWithOneBridge.addEdge("tiger", "dog");

        graphWithTwoBridges = new Graph();
        graphWithTwoBridges.addEdge("cat", "tiger");
        graphWithTwoBridges.addEdge("tiger", "dog");
        graphWithTwoBridges.addEdge("cat", "lion");
        graphWithTwoBridges.addEdge("lion", "dog");

        graphWithNoBridge = new Graph();
        graphWithNoBridge.addEdge("cat", "tiger");
        graphWithNoBridge.addEdge("cat", "lion");
        graphWithNoBridge.addVertex("dog");

        emptyGraph = new Graph();
        emptyGraph.addVertex("cat");
        emptyGraph.addVertex("dog");
    }

    @Test
    void testOneBridgeWord() {
        String result = graphWithOneBridge.queryBridgeWords("cat", "dog");
        assertEquals("The bridge words from \"cat\" to \"dog\" is: \"tiger\"", result);
    }

    @Test
void testMultipleBridgeWords() {
    String result = graphWithTwoBridges.queryBridgeWords("cat", "dog");
    boolean ok = result.equals("The bridge words from \"cat\" to \"dog\" are: \"tiger\" and \"lion\"") ||
                 result.equals("The bridge words from \"cat\" to \"dog\" are: \"lion\" and \"tiger\"");
    assertTrue(ok);
}
    @Test
    void testNoBridgeWord() {
        String result = graphWithNoBridge.queryBridgeWords("cat", "dog");
        assertEquals("No bridge words from \"cat\" to \"dog\"!", result);
    }

    @Test
    void testWord2NotExist() {
        String result = graphWithOneBridge.queryBridgeWords("cat", "nonexist");
        assertEquals("No \"nonexist\" in the graph!", result);
    }

    @Test
    void testWord1NotExist() {
        String result = graphWithOneBridge.queryBridgeWords("nonexist", "dog");
        assertEquals("No \"nonexist\" in the graph!", result);
    }

    @Test
    void testBothNotExist() {
        String result = emptyGraph.queryBridgeWords("aaa", "bbb");
        assertEquals("No \"aaa\" and \"bbb\" in the graph!", result);
    }
}