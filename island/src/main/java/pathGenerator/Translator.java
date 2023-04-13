package pathGenerator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Graph;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

//converts the mesh into a graph
public class Translator {
    private Mesh aMesh;

    public Translator(Mesh mesh){
        this.aMesh = mesh;
    }

    public Graph translate(){
        Graph graph = new Graph();

        for(Polygon p: aMesh.getPolygonsList()){
            graph.addNode(p.getCentroidIdx());
        }

        for(Polygon p: aMesh.getPolygonsList()){
            
            for(int index: p.getNeighborIdxsList()){
                Polygon p_neighbor = aMesh.getPolygons(index);
                graph.addEdge(graph.getNode(p.getCentroidIdx()), graph.getNode(p_neighbor.getCentroidIdx()));
                
            }
        }

        return graph;
 
    }
}
