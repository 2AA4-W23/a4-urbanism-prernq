package ca.mcmaster.cas.se2aa4.a4.pathfinder.graph;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Graph {
    
    //initialize variables
    private List<Node> node_list;
    private List<Edge> edge_list;
    private Map<Node, List<Node>> a_list;

    //create graph object
    public Graph(){
        this.node_list = new ArrayList<>();
        this.edge_list = new ArrayList<>();
        this.a_list =  new HashMap<>();
    }

    //returns list of nodes
    public List<Node> getNodeList(){
        return node_list;
    }

    //returns a node at an index
    public Node getNode(int index){
        for (Node n: a_list.keySet()) {
            if (n.getnodeIndex() == index){
                return n;
            }
        }
        return null;
    }

    //returns list of edges
    public List<Edge> getEdgeList(){
        return edge_list;
    }

    //returns edge at an index
    public Edge getEdge(int index){
        return edge_list.get(index);
    }

    //adds a node to node list at a specific index
    public void addNode(int nodeIdx) {
        Node new_node = getNode(nodeIdx);
        if (new_node == null) {
            new_node = new Node(nodeIdx);
            a_list.putIfAbsent(new_node, new ArrayList<>());
            node_list.add(new_node);
        }
    }

    //adds node to list with a new node specified
    public void addNode(Node new_node){
        if(getNode(new_node.getnodeIndex()) == null){
            a_list.putIfAbsent(new_node, new ArrayList<>());
            node_list.add(new_node);
        }
    }

    //adds a new edge if it does not already exist
    public void addEdge(Node start_node, Node end_node) {
        
        //check if edge exists already
        if (a_list.get(start_node).contains(end_node)){
            return; //breaks if edge found
        }
        else if(a_list.get(start_node).contains(end_node)){
            return; //breaks if edge found
        }
        
        //otherwise create and add the edge
        Edge new_edge = new Edge(start_node, end_node);
        a_list.get(start_node).add(end_node);
        a_list.get(start_node).add(end_node);
        edge_list.add(new_edge);
    }



}
