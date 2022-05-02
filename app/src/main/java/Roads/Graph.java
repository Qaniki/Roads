package Roads;

import java.util.Random;

public class Graph {
    Vertex2[] vertices;

    /**
     * Construct an empty graph with size V
     */
    public Graph(int V){
        this.vertices = new Vertex2[V];
        for(int i=0;i<V;i++)
            vertices[i] = new Vertex2(i);
    }
    /**
     * Construct graph using an array of vertices
     */
    public Graph(Vertex2[] vertices){
        this.vertices = vertices;
    }
    /**
     * Returns a graph with size V and E where the edges are random.
     */
    public Graph(int V, int E) {
        this.vertices = new Vertex2[V];
        for(int i=0;i<V;i++)
            vertices[i] = new Vertex2(i);
        Random rand = new Random();
        for (int j = 0; j < E; j++) {
            int v = rand.nextInt(V);
            int w = rand.nextInt(V);
            int weight = rand.nextInt(100) + 1;
            vertices[v].outEdges.add(new Edge(v, w, weight));
            vertices[w].inEdges.add(new Edge(v, w, weight));
        }
    }
    /**
     * Returns a deep copy of the graph.
     */
    public Graph copy() {
        Vertex2[] newVertices = new Vertex2[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            newVertices[i] = vertices[i].copy();
        return new Graph(newVertices);
    }

    /**
     * Returns an array of vertices.
     */
    public Vertex2[] getVertices(){
        return vertices;
    }
}
