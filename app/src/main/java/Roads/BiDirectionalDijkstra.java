package Roads;

import java.util.PriorityQueue;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;


public class BiDirectionalDijkstra {
    private PriorityQueue<Vertex2> forwardPq;
    private PriorityQueue<Vertex2> backwardPq;
    private Vertex2[] graph;
    private int touchNode;
    private int currentEstimate;



    public BiDirectionalDijkstra(Vertex2[] graph) {
        forwardPq = new PriorityQueue<>(new Comparators.PQFComparator());   
        backwardPq = new PriorityQueue<>(new Comparators.PQBComparator());   
        this.graph = graph;
        this.touchNode = -1;
        this.currentEstimate = Integer.MAX_VALUE;
    }
    public void search(int source, int target){
        graph[source].forwardDist = 0;
        graph[target].backwardDist = 0;
        forwardPq.add(graph[source]);
        backwardPq.add(graph[target]);
        while (!forwardPq.isEmpty() && !backwardPq.isEmpty()) {
            Vertex2 u = forwardPq.remove(), v = backwardPq.remove();
            if (u.forwardDist + v.backwardDist >= currentEstimate) {
                break;
            }
            u.forwardVisted = true;
            forwardRelax(u);
            v.backwardVisited = true;
            backwardRelax(v);
        }
    }
    public void search2(int source, int target){
        graph[source].forwardDist = 0;
        graph[target].backwardDist = 0;
        forwardPq.add(graph[source]);
        backwardPq.add(graph[target]);
        while (!forwardPq.isEmpty() && !backwardPq.isEmpty()) {
            Vertex2 u = forwardPq.peek(), v = backwardPq.peek();
            if (u.forwardDist + v.backwardDist >= currentEstimate) {
                break;
            }
            if(forwardPq.size() > backwardPq.size()){   // Conditional or unconditional
                forwardPq.remove();
                u.forwardVisted = true;
                forwardRelax(u);
            }
            else{
                backwardPq.remove();
                v.backwardVisited = true;
                backwardRelax(v);
            }
        }
    }
    private void forwardRelax(Vertex2 vertex) {
        for (Edge e : vertex.outEdges){
            int v = e.innode, w = e.outnode;
            if(graph[w].forwardVisted) continue;   // ignore visisted nodes
            if(graph[w].forwardDist > graph[v].forwardDist + e.weight) {
                graph[w].forwardDist = graph[v].forwardDist + e.weight;
                graph[w].edgeTo = e;
                forwardPq.add(graph[w]);
                if(graph[w].backwardVisited){
                    int l = graph[w].forwardDist + graph[w].backwardDist;
                    if(currentEstimate > l){
                        currentEstimate = l;
                        touchNode = w;
                    }
                }
            }
        }
    }
    private void backwardRelax(Vertex2 vertex) {
        for (Edge e : vertex.inEdges){
            int v = e.innode, w = e.outnode;
            if(graph[v].backwardVisited) continue;   // ignore visisted nodes
            if(graph[v].backwardDist > graph[w].backwardDist + e.weight) {
                graph[v].backwardDist = graph[w].backwardDist + e.weight;
                graph[v].edgeFrom = e;
                backwardPq.add(graph[v]);
                if(graph[v].forwardVisted){
                    int l = graph[v].forwardDist + graph[v].backwardDist;
                    if(currentEstimate > l){
                        currentEstimate = l;
                        touchNode = v;
                    }
                }
            }
        }
    }
    public void trace(int source, int target){
        System.out.print(source + " to " + target + " ("+ currentEstimate +")   ");
        for (Edge e : pathToForward(touchNode)) {
            System.out.print(e + "   ");
        }
        for (Edge e : pathToBackward(touchNode)) {
            System.out.print(e + "   ");
        }
    }
    public Stack<Edge> pathToForward(int v) {
        Stack<Edge> path = new Stack<Edge>();
        for (Edge e = graph[v].edgeTo; e != null; e = graph[e.innode].edgeTo) {
            path.push(e);
        }
        return path;
    }

    public Queue<Edge> pathToBackward(int v) {
        Queue<Edge> path = new Queue<Edge>();
        for (Edge e = graph[v].edgeFrom; e != null; e = graph[e.outnode].edgeFrom) {
            path.enqueue(e);
        }
        return path;
    }

    public static void compute(Vertex2[] graph){
        BiDirectionalDijkstra bdj = new BiDirectionalDijkstra(graph);
        bdj.search(1, graph.length-1);
        // bdj.trace(0, graph.length-1);
    }
    public static void compute2(Vertex2[] graph){
        BiDirectionalDijkstra bdj = new BiDirectionalDijkstra(graph);
        bdj.search2(1, graph.length-1);
        // bdj.trace(0, graph.length-1);
    }
}
