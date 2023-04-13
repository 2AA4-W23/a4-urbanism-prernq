package ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Node;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Edge;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Graph;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Comparator;

//Algorithm ShortestPath(start, end, w)
public class ShortestPath implements Pathfinder {
    
    //initialize variables
    private Graph graph;
    Map<Node, Node> path_nodes = new HashMap<>();
    Map<Node, Double> distance_map = new HashMap<>();
    List<Node> shortestPath = new ArrayList<>();
    
    //create graph
    public ShortestPath(Graph graph) {
        this.graph = graph;
    }
    
    @Override
    public List<Node> path_finder(Node start, Node end) {

        //a priority queue of nodes that have not been iterated already
        PriorityQueue<Node> nodes_notchecked = new PriorityQueue<>(Comparator.comparingDouble(distance_map::get));
        //set of nodes iterated through already
        Set<Node> nodes_checked = new HashSet<>();
        
        //give temporary distances to each node
        for (Node n :graph.getNodeList()){
            if (n.equals(start)) {
                distance_map.put(n, 0.00); //sets the distance for the start node to 0
            } else {
                distance_map.put(n, Double.MAX_VALUE); //sets the distance for the node to the max value
            }
            
            nodes_notchecked.offer(n); //adds the node to unchecked nodes
        }
        
        // iterates through each node until there are none left
        while ((nodes_notchecked.isEmpty()) == false) {
            Node currentNode = nodes_notchecked.poll(); // priority queue node
            
            // shortest path found when the current node is the last node
            if (currentNode.equals(end)) {
                Node n = end;
                
                //as long as the node exists
                while (n != null) {
                    
                    //goes backwards to ad each node into the shortest path
                    shortestPath.add(0, n);
                    n = path_nodes.get(n);
                }
                
                //returns the shortest path found and ends
                return shortestPath; 
            }

            //otherwise continues and adds current node to list of nodes already checked
            nodes_checked.add(currentNode);
            
            //checks the distance of the edges connecting the node to offer to the nodes not checked yet
            for (Edge e: currentNode.getEdgePath()) {
                
                Node n = e.getEndNode();
                //creates temporary distance for current node
                double temp_dist = (distance_map.get(currentNode)+e.getWeight());
                
                //if the temporary distance is less than the distance the node currently has...
                if (temp_dist < distance_map.get(n)) {
                    //...adds the temporary distance and the node into the distance map + adds to path
                    distance_map.put(n, temp_dist);
                    path_nodes.put(n, currentNode);
                    
                    //removes the node from unchecked nodes and offers it back
                    nodes_notchecked.remove(n);
                    nodes_notchecked.offer(n);
                }
            }
        }
        
        return null;

    }
}
