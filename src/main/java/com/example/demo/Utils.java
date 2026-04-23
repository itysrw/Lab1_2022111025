package com.example.demo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

        // 私有构造函数，防止实例化
    private Utils() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }

    // 从文件读取并返回单词列表（按顺序）
    public static List<String> readWordsFromFile(String filePath) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 将行内所有非字母字符替换为空格，然后按空格分割
                String processed = line.replaceAll("[^a-zA-Z]", " ").toLowerCase();
                String[] tokens = processed.split("\\s+");
                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        words.add(token);
                    }
                }
            }
        }
        return words;
    }

    // 从文件构建图
    public static Graph buildGraphFromFile(String filePath) throws IOException {
        List<String> words = readWordsFromFile(filePath);
        Graph graph = new Graph();
        for (int i = 0; i < words.size() - 1; i++) {
            String from = words.get(i);
            String to = words.get(i + 1);
            graph.addEdge(from, to);
        }
        return graph;
    }

    // 将用户输入文本转换为单词列表（同样处理标点）
    public static List<String> processInputText(String text) {
        String processed = text.replaceAll("[^a-zA-Z]", " ").toLowerCase();
        String[] tokens = processed.split("\\s+");
        List<String> words = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }
}