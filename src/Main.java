import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Graph graph = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    buildGraphFromFile();
                    break;
                case "2":
                    showGraph();
                    break;
                case "3":
                    queryBridgeWords();
                    break;
                case "4":
                    generateNewText();
                    break;
                case "5":
                    calcShortestPath();
                    break;
                case "6":
                    calPageRank();
                    break;
                case "7":
                    randomWalk();
                    break;
                case "8":
                    System.out.println("再见！");
                    return;
                default:
                    System.out.println("无效选项，请输入1-8。");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== 图菜单 ===");
        System.out.println("1. 从文件构建图");
        System.out.println("2. 显示有向图");
        System.out.println("3. 查询桥接词");
        System.out.println("4. 生成新文本");
        System.out.println("5. 计算最短路径");
        System.out.println("6. 计算 PageRank");
        System.out.println("7. 随机游走");
        System.out.println("8. 退出");
        System.out.print("请选择你想进行的功能: ");
    }

    private static void buildGraphFromFile() {
        System.out.print("请输入文件路径: ");
        String path = scanner.nextLine().trim();
        try {
            graph = Utils.buildGraphFromFile(path);
            System.out.println("图构建成功。");
        } catch (IOException e) {
            System.out.println("读取文件出错: " + e.getMessage());
        }
    }

    private static void showGraph() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        graph.showDirectedGraph();
    }

    private static void queryBridgeWords() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        System.out.print("请输入第一个单词: ");
        String w1 = scanner.nextLine().trim();
        System.out.print("请输入第二个单词: ");
        String w2 = scanner.nextLine().trim();
        System.out.println(graph.queryBridgeWords(w1, w2));
    }

    private static void generateNewText() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        System.out.print("请输入一行文本: ");
        String input = scanner.nextLine().trim();
        String result = graph.generateNewText(input);
        System.out.println("生成的新文本: " + result);
    }

    private static void calcShortestPath() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        System.out.print("请输入第一个单词: ");
        String w1 = scanner.nextLine().trim();
        System.out.print("请输入第二个单词（留空则计算单源最短路径）: ");
        String w2 = scanner.nextLine().trim();
        if (w2.isEmpty()) {
            // 可选功能：单源最短路径（可自行扩展）
            System.out.println("单源最短路径功能尚未实现。");
        } else {
            System.out.println(graph.calcShortestPath(w1, w2));
        }
    }

    private static void calPageRank() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        System.out.print("请输入单词: ");
        String word = scanner.nextLine().trim();
        double pr = graph.calPageRank(word);
        System.out.printf("单词 \"%s\" 的 PageRank 值为 %.6f%n", word, pr);
    }

    private static void randomWalk() {
        if (graph == null) {
            System.out.println("尚未构建图，请先构建。");
            return;
        }
        String path = graph.randomWalk();
        System.out.println("随机游走路径: " + path);
        // 写入文件
        try (java.io.PrintWriter out = new java.io.PrintWriter("random_walk.txt")) {
            out.println(path);
            System.out.println("路径已保存至 random_walk.txt");
        } catch (IOException e) {
            System.out.println("保存文件失败: " + e.getMessage());
        }
    }
}