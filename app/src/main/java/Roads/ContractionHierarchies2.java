package Roads;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import edu.princeton.cs.algs4.*;

public class ContractionHierarchies2 {
    
    public ContractionHierarchies2(){

    }

    // public static void compute(Vertex2[] graph){
    //     Vertex2[] pgraph = PreProcess.process(graph);
    //     BidirectionalDijkstra bd = new BidirectionalDijkstra(pgraph);
        
    // }

    public static class PreProcess{
        Comparator<Vertex2> IPQcomp = new Comparators.ImportancePQComparator();
        // Comparator<Vertex2> PQcomp = new Comparators.PQComparator();
        PriorityQueue<Vertex2> IPQ = new PriorityQueue<>(IPQcomp);
        // PriorityQueue<Vertex2> PQ = new PriorityQueue<>(PQcomp);

        public static Graph process(Vertex2[] graph){
            PreProcess pp = new PreProcess();
            pp.computeInitialImportance(graph);
            return new Graph(pp.preprocess(graph));
        }

        // Compute initial importance for each node
        public void computeInitialImportance(Vertex2[] graph){
            for(int i = 0; i < graph.length; i++){
                computeImportance(graph[i]);
                IPQ.add(graph[i]);
            }
        }
        // Update importance during preprocess
        public void computeImportance(Vertex2 node){
                // Potential shortcuts - "removed" edges
                node.edgeDifference = node.inEdges.size()*node.outEdges.size() - node.inEdges.size() - node.outEdges.size();    
                // Deleted neighbors for more uniform contraction
                node.importance = node.edgeDifference + node.deletedNeighbors;
        }
        // Preprocess the graph
        public Vertex2[] preprocess(Vertex2[] graph){
            int contractNumber = 0;
            while(IPQ.size()!=0){
                Vertex2 node = IPQ.poll();
                // Lazy Updates cf. 3.2.1
                computeImportance(node);                        // Keeping node importance up to date
                if(IPQ.size()==0 || node.importance <= IPQ.peek().importance){
                    node.contractOrder = contractNumber; 
                    contractNumber++;
                    contractNode(graph,node);
                }
                else IPQ.add(node);
            }
            return graph;
        }
        public void contractNode(Vertex2[] graph, Vertex2 node){
            List<Edge> inEdges = node.inEdges;
            List<Edge> outEdges = node.outEdges;
            
            node.contracted = true;

            // finding max distance to restrict dijstra search around node
            int inMaxCost = 0;
            int outMaxCost = 0;

            for(int i = 0; i < inEdges.size();i++){
                if(!graph[inEdges.get(i).innode].contracted){
                    if(inEdges.get(i).weight>inMaxCost){
                        inMaxCost = inEdges.get(i).weight;
                    }
                }
            }
            for(int i = 0; i < outEdges.size();i++){
                if(!graph[outEdges.get(i).outnode].contracted){
                    if(outEdges.get(i).weight>outMaxCost){
                        outMaxCost = outEdges.get(i).weight;
                    }
                }
            }
    
            int maxCost = inMaxCost+outMaxCost; 				//total max distance.


            // Find shortest path from all innodes to all outnodes
            for(int i=0;i<inEdges.size();i++){
                int innode = inEdges.get(i).innode;
                if(!graph[innode].contracted){
                    int incost = inEdges.get(i).weight;
                
                    int[] dist = new int[graph.length];
                    for (int k = 0; k < graph.length; k++)
                        dist[k] = Integer.MAX_VALUE;
                    dijkstra(graph,innode,dist,maxCost); 	
                    // Shortcuts
                    for(int j=0;j<outEdges.size();j++){
                        int outnode = outEdges.get(j).outnode;
                        int outcost = outEdges.get(j).weight;

                        if(!graph[outnode].contracted){
                            if(dist[outnode]>incost+outcost){
                                Edge shortcut = new Edge(innode, outnode, incost+outcost, node.id, inEdges.get(i), outEdges.get(j));
                                graph[innode].outEdges.add(shortcut); // 3.5.1 2nd option
                                graph[outnode].inEdges.add(shortcut);
                            }
                        }
                    }
                }
            }
            updateNeighbors(graph,node);
        }
        public void dijkstra(Vertex2 [] graph, int source, int[] dist, int maxCost){
            // PriorityQueue<Vertex2> pq = new PriorityQueue<Vertex2>(graph.length,PQcomp);
            IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(graph.length);

            dist[source] = 0;
            pq.insert(source, dist[source]);
            // pq.add(graph[source]);
            while(pq.size()!=0){
                int node = pq.delMin();
                if(dist[node] > maxCost){
                    return;
                }
                relaxEdges(graph,node, dist,pq);
            }
        }
        public void relaxEdges(Vertex2[] graph,int node, int[] dist, IndexMinPQ<Integer> pq){
            List<Edge> outNodes = graph[node].outEdges;
            for(int i=0;i<outNodes.size();i++){
                int outNode = outNodes.get(i).outnode;
                int cost = outNodes.get(i).weight;
                if(!graph[outNode].contracted){
                    if(dist[outNode] > dist[node] + cost){
                        dist[outNode] = dist[node] + cost;
                        // pq.remove(graph[outNode]);          
                        // pq.add(graph[outNode]);
                        if(pq.contains(outNode)) pq.decreaseKey(outNode, dist[node] + cost);
                        else pq.insert(outNode, dist[node] + cost);
                    }
                }
            }
        }
        public void updateNeighbors(Vertex2[] graph, Vertex2 node){
            for(int i=0;i<node.inEdges.size();i++){
                int innode = node.inEdges.get(i).innode;
                if(!graph[innode].contracted)
                    graph[innode].deletedNeighbors++;
            }
            for(int i=0;i<node.outEdges.size();i++){
                int outnode =node.outEdges.get(i).outnode;
                if(!graph[outnode].contracted)
                    graph[outnode].deletedNeighbors++;
            }
        }

        // Preproccessing gives short cuts(stored in nodes) and node hierarchy(node ordering)
    }
    public static class BidirectionalDijkstra{
        Comparator<Vertex2> forwardC;
        Comparator<Vertex2> backwardC;
        PriorityQueue<Vertex2> forwardPQ;
        PriorityQueue<Vertex2> backwardPQ;
        Vertex2[] graph;
        Vertex2 touchNode;

        public static int compute(Vertex2[] graph){
            BidirectionalDijkstra bd = new BidirectionalDijkstra(graph);
            return  bd.computeDist(1, graph.length-1);
        }

        public BidirectionalDijkstra(Vertex2[] graph){
            this.forwardC = new Comparators.PQFComparator();
            this.backwardC = new Comparators.PQBComparator();
            this.graph = graph;
            this.touchNode =  null;
        }

        public int computeDist(int source, int target){

            graph[source].forwardDist = 0;
            graph[target].backwardDist = 0;
    
            forwardPQ = new PriorityQueue<Vertex2>(graph.length,forwardC);
            backwardPQ = new PriorityQueue<Vertex2>(graph.length,backwardC);
            
            forwardPQ.add(graph[source]);
            backwardPQ.add(graph[target]);
    
            int currentEstimate = Integer.MAX_VALUE;
            
            while(forwardPQ.size()!=0 || backwardPQ.size()!=0){
                if(forwardPQ.size()!=0){
                    Vertex2 node = forwardPQ.remove();
                    // Add earlier stopping criteria, ie. if the current path length to this node is longer than our best estimate
                    // do not relax, and thus not adding anything more from this node to PQ
                    // if(currentEstimate < node.forwardDist)
                    // Not working right now
                        forwardRelax(node.id);
                    if(node.backwardVisited && node.forwardDist + node.backwardDist < currentEstimate){
                        currentEstimate = node.forwardDist + node.backwardDist;
                        touchNode = node;
                    }
                }
                if(backwardPQ.size()!=0){
                    Vertex2 node = backwardPQ.remove();
                    // if(currentEstimate < node.backwardDist)
                        backwardRelax(node.id);
                    if(node.forwardVisted && node.forwardDist + node.backwardDist < currentEstimate){
                        currentEstimate = node.forwardDist + node.backwardDist;
                        touchNode = node;
                    }
                }
            }
            // trace(source, target, currentEstimate);
            return currentEstimate;
        }

        private void forwardRelax(int node){
            List<Edge> outNodes = graph[node].outEdges;
            graph[node].forwardVisted = true;
            for(int i=0;i<outNodes.size();i++){
                int outnode = outNodes.get(i).outnode;
                int cost = outNodes.get(i).weight;
                if(graph[node].contractOrder < graph[outnode].contractOrder){
                    if(graph[outnode].forwardDist > graph[node].forwardDist + cost){
                        graph[outnode].forwardDist = graph[node].forwardDist + cost;
                        graph[outnode].edgeTo = outNodes.get(i);
                        forwardPQ.remove(graph[outnode]);
                        forwardPQ.add(graph[outnode]);
                    }
                }
            }
        }
        private void backwardRelax(int node){
            List<Edge> inNodes = graph[node].inEdges;
            graph[node].backwardVisited = true;
            for(int i=0;i<inNodes.size();i++){
                int outnode = inNodes.get(i).innode;
                int cost = inNodes.get(i).weight;
                if(graph[node].contractOrder < graph[outnode].contractOrder){
                    if(graph[outnode].backwardDist > graph[node].backwardDist + cost){
                        graph[outnode].backwardDist = graph[node].backwardDist + cost;
                        graph[outnode].edgeFrom = inNodes.get(i);
                        backwardPQ.remove(graph[outnode]);
                        backwardPQ.add(graph[outnode]);
                    }
                }
            }
        }
        // below are methods used for tracing the path //
        public void trace(int source, int target, int currentEstimate){
            System.out.print(source + " to " + target + " ("+ currentEstimate +")   ");
            for (Edge e : pathToForward(touchNode)) {
                System.out.print(e + "   ");
            }
            for (Edge e : pathToBackward(touchNode)) {
                System.out.print(e + "   ");
            }
        }
        public Stack<Edge> pathToForward(Vertex2 v) {
            Stack<Edge> path = new Stack<Edge>();
            for (Edge e = v.edgeTo; e != null; e = graph[e.innode].edgeTo) {
                recPush(path, e);
            }
            return path;
        }
        public void recPush(Stack<Edge> path, Edge e){
            if(e.shortcut){
                recPush(path, e.outedge);
                recPush(path, e.inedge);
            }
            else path.push(e);
        }
        public Queue<Edge> pathToBackward(Vertex2 v) {
            Queue<Edge> path = new Queue<Edge>();
            for (Edge e = v.edgeFrom; e != null; e = graph[e.outnode].edgeFrom) {
                recEnqueue(path, e);
            }
            return path;
        }
        public void recEnqueue(Queue<Edge> path, Edge e){
            if(e.shortcut){
                recEnqueue(path, e.inedge);
                recEnqueue(path, e.outedge);
            }
            else path.enqueue(e);
        }        
    }
}    

 

