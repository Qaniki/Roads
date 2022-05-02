package Roads;

import java.util.ArrayList;
import java.util.List;

public class Vertex2 {
    int id;
    List<Edge> inEdges;          // Using a node oriented approach over an edge one.
    List<Edge> outEdges;

    // int x,y;                        // 2D Coord for node        
    
    int edgeDifference;             // Potential added shortcuts(#inedges * #outedges) - #inedges - #outedges
    int deletedNeighbors;           // Number of contracted Neighbors. 
    int importance;                 // importance = edgeDifference + deletedNeighbors. Contract node with least importance first.

    boolean contracted;             

    int contractOrder;

    boolean forwardVisted;
    boolean backwardVisited;

    int forwardDist;
    int backwardDist;
    Edge edgeTo;
    Edge edgeFrom;

    public Vertex2(int id){
        this.id=id;
        this.inEdges = new ArrayList<Edge>();
        this.outEdges = new ArrayList<Edge>();
        this.forwardDist = Integer.MAX_VALUE;
        this.backwardDist = Integer.MAX_VALUE;
    }

    public Vertex2 copy() {
        Vertex2 newVertex = new Vertex2(this.id);
        newVertex.inEdges = this.inEdges;
        newVertex.inEdges = this.outEdges;
        newVertex.edgeDifference = this.edgeDifference;
        newVertex.deletedNeighbors = this.deletedNeighbors;
        newVertex.importance = this.importance;
        newVertex.contracted = this.contracted;
        newVertex.contractOrder = this.contractOrder;
        return newVertex;
    }

}
