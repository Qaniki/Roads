package Roads;

import java.util.PriorityQueue;

import edu.princeton.cs.algs4.Stack;




public class Dijkstra {
    private PriorityQueue<Vertex2> pq;
    private Vertex2[] graph;

    public Dijkstra(Vertex2[] graph) {
        pq = new PriorityQueue<>(new Comparators.PQFComparator());      // Forward queue
        this.graph = graph;
    }
    public void search(int s, int t){
        graph[s].forwardDist = 0;
        pq.add(graph[s]);
        while(!pq.isEmpty() && pq.peek()!=graph[t]){  // stop when next node is target
            Vertex2 v = pq.remove();
            v.forwardVisted = true;
            relaxEdges(v);
        }
    }
    private void relaxEdges(Vertex2 vertex){
        for (Edge e : vertex.outEdges){
            int v = e.innode, w = e.outnode;
            if(graph[w].forwardVisted) continue;   // ignore visisted nodes
            if(graph[w].forwardDist > graph[v].forwardDist + e.weight) {
                graph[w].forwardDist = graph[v].forwardDist + e.weight;
                graph[w].edgeTo = e;
                // pq.remove(graph[w]);
                pq.add(graph[w]);
            }
        }
    }
    public void trace(int s, int t){
        System.out.print(s + " to " + t + " ("+ graph[t].forwardDist+")   ");
        for (Edge e : pathTo(t)) {
            System.out.print(e + "   ");
        }
    }

    public Stack<Edge> pathTo(int v) {
        Stack<Edge> path = new Stack<Edge>();
        for (Edge e = graph[v].edgeTo; e != null; e = graph[e.innode].edgeTo) {
            path.push(e);
        }
        return path;
    }

    public static void compute(Vertex2[] graph){
        Dijkstra dj = new Dijkstra(graph);
        dj.search(1, graph.length-1);
        // dj.trace(0, graph.length-1);
    }
}
