package pathGenerator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Graph;
import islandGenerator.islandGen;

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
        List<Polygon> polys = new ArrayList<>();

        for (int j = 0; j < islandGen.inPolygons.size(); j++){
            polys.add(islandGen.inPolygons.get(j));
        }

        for(Polygon p: polys){
            graph.addNode(p.getCentroidIdx());
        }

        for(Polygon p: polys){
            List<Integer> p_neighbour_idxs = p.getNeighborIdxsList();
            for(int index: p_neighbour_idxs){
                Polygon p_neighbor = polys.get(index);
                graph.addEdge(graph.getNode(p.getCentroidIdx()), graph.getNode(p_neighbor.getCentroidIdx()));
                
            }
        }

        return graph;
 
    }
}
