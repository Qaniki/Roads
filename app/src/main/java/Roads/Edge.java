package Roads;

public class Edge {
    boolean shortcut;
    int weight;
    int middlenode;
    int innode;
    int outnode;
    Edge inedge;
    Edge outedge;
    // Maybe make seperate edge classese for in and out edges.
    public Edge(int innode, int outnode, int weight){
        this.innode = innode;
        this.outnode = outnode;
        this.weight = weight;
    }
    public Edge(int innode, int outnode, int weight, int middlenode, Edge inedge, Edge outedge){
        this.innode = innode;
        this.outnode = outnode;
        this.shortcut = true;
        this.middlenode = middlenode;
        this.inedge = inedge;
        this.outedge = outedge;
        this.weight = weight;
    }

    public String toString(){
        return innode  + "->" + outnode + "   " + weight + "  ";
    }
}
