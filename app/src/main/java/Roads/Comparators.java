package Roads;

import java.util.Comparator;

public class Comparators {
    public static class ImportancePQComparator implements Comparator<Vertex2>{
        public int compare(Vertex2 node1, Vertex2 node2){
            if(node1.importance > node2.importance){
                return 1;
            }
            if(node1.importance < node2.importance){
                return -1;
            }
            return 0;
        }
    }
    public static class PQFComparator implements Comparator<Vertex2>{
        public int compare(Vertex2 node1,Vertex2 node2){
            if(node1.forwardDist>node2.forwardDist){
                return 1;
            }
            if(node1.forwardDist<node2.forwardDist){
                return -1;
            }
            return 0;
        }
    }
    public static class PQBComparator implements Comparator<Vertex2>{
        public int compare(Vertex2 node1,Vertex2 node2){
            if(node1.backwardDist>node2.backwardDist){
                return 1;
            }
            if(node1.backwardDist<node2.backwardDist){
                return -1;
            }
            return 0;
        }
    }
}
