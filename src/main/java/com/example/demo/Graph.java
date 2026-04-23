package com.example.demo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class Graph {
    // 邻接表：key = 单词（小写），value = Map<邻居单词, 权重>
    private Map<String, Map<String, Integer>> adjList;
    // 节点集合（与邻接表的key一致）
    private Set<String> vertices;
    private Random rand;

    public Graph() {
        adjList = new HashMap<>();
        vertices = new HashSet<>();
        rand = new Random();
    }

    // 添加节点
    public void addVertex(String word) {
        word = word.toLowerCase();
        if (!vertices.contains(word)) {
            vertices.add(word);
            adjList.put(word, new HashMap<>());
        }
    }

    // 添加边（若已存在则权重+1）
    public void addEdge(String from, String to) {
        from = from.toLowerCase();
        to = to.toLowerCase();
        addVertex(from);
        addVertex(to);
        Map<String, Integer> edges = adjList.get(from);
        edges.put(to, edges.getOrDefault(to, 0) + 1);
    }

    // 获取某节点的所有出边
    public Map<String, Integer> getOutEdges(String word) {
        word = word.toLowerCase();
        return adjList.getOrDefault(word, Collections.emptyMap());
    }

    // 获取权重
    public int getWeight(String from, String to) {
        from = from.toLowerCase();
        to = to.toLowerCase();
        return adjList.getOrDefault(from, Collections.emptyMap())
                .getOrDefault(to, 0);
    }

    // 检查节点是否存在
    public boolean containsVertex(String word) {
        return vertices.contains(word.toLowerCase());
    }

    // 获取所有节点
    public Set<String> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    // 获取所有边（用于展示或图形生成）
    public Map<String, Map<String, Integer>> getAdjList() {
        return Collections.unmodifiableMap(adjList);
    }

    // --------------------------- 功能实现 ---------------------------

    // 功能2：展示有向图（CLI方式）
    public void showDirectedGraph() {
        for (String from : vertices) {
            System.out.print(from + " -> ");
            Map<String, Integer> edges = adjList.get(from);
            if (edges.isEmpty()) {
                System.out.println("(no out edges)");
            } else {
                List<String> edgeStrs = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : edges.entrySet()) {
                    edgeStrs.add(entry.getKey() + "(" + entry.getValue() + ")");
                }
                System.out.println(String.join(", ", edgeStrs));
            }
        }
    }

    // 可选：导出为DOT文件并生成图片（需安装Graphviz）
    public void exportToDot(String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println("digraph G {");
            out.println("  node [shape=box];");
            for (String from : vertices) {
                Map<String, Integer> edges = adjList.get(from);
                for (Map.Entry<String, Integer> entry : edges.entrySet()) {
                    out.printf("  \"%s\" -> \"%s\" [label=\"%d\"];%n",
                            from, entry.getKey(), entry.getValue());
                }
            }
            out.println("}");
        }
        // 调用Graphviz生成图片
        Runtime.getRuntime().exec("dot -Tpng " + filename + " -o " + filename + ".png");
    }

    // 功能3：查询桥接词
    public String queryBridgeWords(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        if (!containsVertex(word1) && !containsVertex(word2)) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
        } else if (!containsVertex(word1)) {
            return "No \"" + word1 + "\" in the graph!";
        } else if (!containsVertex(word2)) {
            return "No \"" + word2 + "\" in the graph!";
        }

        List<String> bridges = new ArrayList<>();
        Map<String, Integer> fromEdges = adjList.get(word1);
        for (String candidate : fromEdges.keySet()) {
            if (adjList.get(candidate).containsKey(word2)) {
                bridges.add(candidate);
            }
        }

        if (bridges.isEmpty()) {
            return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("The bridge words from \"").append(word1).append("\" to \"").append(word2).append("\" ");
            if (bridges.size() == 1) {
                sb.append("is: \"").append(bridges.get(0)).append("\"");
            } else {
                sb.append("are: ");
                for (int i = 0; i < bridges.size(); i++) {
                    if (i > 0) {
                        if (i == bridges.size() - 1) {
                            sb.append(" and ");
                        } else {
                            sb.append(", ");
                        }
                    }
                    sb.append("\"").append(bridges.get(i)).append("\"");
                }
            }
            return sb.toString();
        }
    }

    // 功能4：根据bridge word生成新文本
    public String generateNewText(String inputText) {
        List<String> words = Utils.processInputText(inputText);
        if (words.isEmpty()) return "";

        List<String> result = new ArrayList<>();
        result.add(words.get(0));

        for (int i = 0; i < words.size() - 1; i++) {
            String current = words.get(i);
            String next = words.get(i + 1);
            List<String> bridges = getBridgeWordsList(current, next);
            if (!bridges.isEmpty()) {
                String bridge = bridges.get(rand.nextInt(bridges.size()));
                result.add(bridge);
            }
            result.add(next);
        }

        return String.join(" ", result);
    }

    // 辅助方法：返回桥接词列表
    private List<String> getBridgeWordsList(String word1, String word2) {
        List<String> bridges = new ArrayList<>();
        if (!containsVertex(word1) || !containsVertex(word2)) {
            return bridges;
        }
        Map<String, Integer> fromEdges = adjList.get(word1);
        for (String candidate : fromEdges.keySet()) {
            if (adjList.get(candidate).containsKey(word2)) {
                bridges.add(candidate);
            }
        }
        return bridges;
    }

    // 功能5：计算最短路径
    public String calcShortestPath(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        if (!containsVertex(word1) || !containsVertex(word2)) {
            return "No \"" + word1 + "\" or \"" + word2 + "\" in the graph!";
        }

        // Dijkstra
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String v : vertices) {
            dist.put(v, Integer.MAX_VALUE);
        }
        dist.put(word1, 0);
        pq.offer(word1);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(word2)) break;
            if (dist.get(u) == Integer.MAX_VALUE) continue;

            Map<String, Integer> edges = adjList.get(u);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String v = edge.getKey();
                int w = edge.getValue();
                int newDist = dist.get(u) + w;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.offer(v);
                }
            }
        }

        if (!prev.containsKey(word2) && !word1.equals(word2)) {
            return "No path from \"" + word1 + "\" to \"" + word2 + "\"!";
        }

        // 回溯路径
        List<String> path = new LinkedList<>();
        String step = word2;
        while (step != null) {
            path.add(0, step);
            step = prev.get(step);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Shortest path length: ").append(dist.get(word2));
        sb.append("\nPath: ").append(String.join(" -> ", path));
        return sb.toString();
    }

    // 功能6：计算PageRank
    public double calPageRank(String word) {
    word = word.toLowerCase();
    if (!containsVertex(word)) return 0.0;

    int N = vertices.size();
    double d = 0.85;
    double epsilon = 1e-8;

    Map<String, Double> pr = new HashMap<>();
    Map<String, Double> newPR = new HashMap<>();

    // 初始化 PR 值
    double init = 1.0 / N;
    for (String v : vertices) {
        pr.put(v, init);
    }

    boolean converged;
    do {
        // 1. 基础值 (1-d)/N
        double base = (1 - d) / N;
        for (String v : vertices) {
            newPR.put(v, base);
        }

        // 2. 处理悬挂节点（出度为0的节点）：它们的 PR 值将均分给所有节点
        double sinkSum = 0.0;
        for (String u : vertices) {
            if (adjList.get(u).isEmpty()) {
                sinkSum += pr.get(u) / N;
            }
        }

        // 3. 正向传播：对于每个节点 u，将其 PR 值按出度分配给邻居
        for (String u : vertices) {
            Map<String, Integer> outEdges = adjList.get(u);
            int outDegree = outEdges.size();
            if (outDegree > 0) {
                double contribution = d * pr.get(u) / outDegree;
                for (String v : outEdges.keySet()) {
                    newPR.put(v, newPR.get(v) + contribution);
                }
            }
        }

        // 4. 加上悬挂节点贡献
        for (String v : vertices) {
            newPR.put(v, newPR.get(v) + d * sinkSum);
        }

        // 5. 检查收敛,b2
        converged = true;
        for (String v : vertices) {
            if (Math.abs(newPR.get(v) - pr.get(v)) > epsilon) {
                converged = false;
                break;
            }
        }

        // 6. 交换
        Map<String, Double> temp = pr;
        pr = newPR;
        newPR = temp;
    } while (!converged);

    return pr.get(word);
}

    // 功能7：随机游走
    public String randomWalk() {
        if (vertices.isEmpty()) return "";

        // 随机选择起点
        List<String> vertexList = new ArrayList<>(vertices);
        String current = vertexList.get(rand.nextInt(vertexList.size()));
        List<String> visitedNodes = new ArrayList<>();
        visitedNodes.add(current);
        Set<String> visitedEdges = new HashSet<>();

        while (true) {
            Map<String, Integer> outEdges = adjList.get(current);
            if (outEdges.isEmpty()) {
                break; // 无出边，停止
            }
            // 随机选择一条出边
            List<String> neighbors = new ArrayList<>(outEdges.keySet());
            String next = neighbors.get(rand.nextInt(neighbors.size()));
            String edgeKey = current + "->" + next;
            if (visitedEdges.contains(edgeKey)) {
                break; // 遇到重复边，停止
            }
            visitedEdges.add(edgeKey);
            visitedNodes.add(next);
            current = next;
        }

        return String.join(" ", visitedNodes);
    }
}