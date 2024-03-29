package ca.mcmaster.cas.se2aa4.a4.pathfinder.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

    //initialize node variables
    private List <Edge> edgePath; 
    private Map <String, String> properties;
    private int nodeIndex; //identifies the node

    //create node object
    public Node (int nodeIndex) {
        this.nodeIndex = nodeIndex;
        this.edgePath = new ArrayList<>();
        this.properties = new HashMap<>();
    }

    //returns current edge path
    public List<Edge> getEdgePath (){
        return edgePath;
    }

    //adds an edge to edge path
    public void addEdgePath (Edge edge){
        edgePath.add(edge);
    }


    // adds property to properties list
    public void addProperty (String key, String value){
        properties.put(key, value);
    }

    //returns value of property 
    public String getProperty (String key){
        return properties.get(key);
    }

    //returns the node number
    public int getnodeIndex () {
        return nodeIndex;
    }

}
