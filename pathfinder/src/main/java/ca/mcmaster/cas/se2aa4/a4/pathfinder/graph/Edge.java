package ca.mcmaster.cas.se2aa4.a4.pathfinder.graph;

import java.util.HashMap;
import java.util.Map;

public class Edge {

    //initialize edge variables
    private int weight;
    private Node start;
    private Node end;
    private Map<String, String> properties;

    //create edge object
    public Edge(Node start, Node end) {
        this.weight = 0;
        this.start = start;
        this.end = end;
        this.properties = new HashMap<>();
        start.addEdgePath(this);
        end.addEdgePath(this);
    }

    //returns weight of edge
    public int getWeight() {
        return weight;
    }

    //sets weight of edge
    public void setWeight(int weight){
        this.weight = weight;
    }

    //returns edge starting node
    public Node getStartNode() {
        return start;
    }

    //returns edge ending node
    public Node getEndNode() {
        return end;
    }

    // adds property to edge
    public void addProperty(String key, String prop){
        properties.put(key, prop);
    }

    //returns value of a property
    public String getProperty(String key){
        return properties.get(key);
    }
}
