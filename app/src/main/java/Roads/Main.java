package Roads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String filename = "src/main/resources/tCOL.txt";
        Vertex2[] graph;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String[] size = reader.readLine().split(" ");
            int V = Integer.valueOf(size[0]);
            graph = new Vertex2[V+1];
            for(int i=0;i<V+1;i++)
                graph[i] = new Vertex2(i);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                int v = Integer.valueOf(data[1]), w = Integer.valueOf(data[2]), weight = Integer.valueOf(data[3]);
                Edge e = new Edge(v,w,weight);
                graph[v].outEdges.add(e);
                graph[w].inEdges.add(e);
            }
            reader.close();
        } catch (IOException exn) {
              System.out.println("no file found");
              graph = new Vertex2[0];
          }
        // long seed = 26;
        // Random random = new Random(seed);
        // int V = random.nextInt(500, 1000);
        // int E = random.nextInt(0, V*(V-1));

        Graph pgraph = ContractionHierarchies2.PreProcess.process(graph);
        ContractionHierarchies2.BidirectionalDijkstra bd = new ContractionHierarchies2.BidirectionalDijkstra(pgraph.getVertices());
        int dist = bd.computeDist(1, 435666);
        // System.out.println(dist);
        // Dijkstra dj = new Dijkstra(graph);
        // dj.search(1, 435666);
        // dj.trace(1, 435666);
        // 1 to 435666 (7938506) //
        // BiDirectionalDijkstra bdj = new BiDirectionalDijkstra(graph);
        // bdj.search(1, 435666);
        // bdj.trace(1, 435666);

    }
}
