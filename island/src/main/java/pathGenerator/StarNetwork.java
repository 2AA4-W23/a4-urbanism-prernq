package pathGenerator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Node;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Graph;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway.ShortestPath;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway.Pathfinder;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import islandGenerator.islandGen;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public class StarNetwork {

    Graph graph;
    Mesh aMesh;
    List<Integer> city_node_idxs;
    

    public StarNetwork(Mesh mesh){
        this.aMesh = mesh;
        this.graph = new Translator(mesh).translate();
        this.city_node_idxs = new ArrayList<>();
        cityFinder();
        
    }

    public void cityFinder(){
        List<Polygon> polys = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();

        for (int j = 0; j < islandGen.inPolygons.size(); j++){
            polys.add(islandGen.inPolygons.get(j));
        }

        for(int i = 0; i < islandGen.inVertices.size(); i++){
            vertices.add(islandGen.inVertices.get(i));
        }

        for(Polygon p: polys){
            int cent_idx = p.getCentroidIdx();
            Vertex cent = vertices.get(cent_idx);

            if((cent.getPropertiesList()).size() == 1){
                city_node_idxs.add(p.getCentroidIdx());
            }
        }
    }

    public List<Segment> createPath(){
        List<Segment> segs = new ArrayList<>();
        List<Polygon> polys = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();

        for (int j = 0; j < islandGen.inSegments.size(); j++){
            segs.add(islandGen.inSegments.get(j));
        }

        for (int j = 0; j < islandGen.inPolygons.size(); j++){
            polys.add(islandGen.inPolygons.get(j));
        }

        for(int i = 0; i < islandGen.inVertices.size(); i++){
            vertices.add(islandGen.inVertices.get(i));
        }

        //create path properties
        Property path_color = Property.newBuilder().setKey("rgb_color").setValue("255,0,0").build();
        Property path_thickness = Property.newBuilder().setKey("thickness").setValue("3").build();

        //finds shortest path
        Pathfinder find_path = new ShortestPath(graph);
        List<Node> path_nodes = find_path.path_finder(graph.getNode(city_node_idxs.get(0)), graph.getNode(city_node_idxs.get(1)));
        
        for(Node n: path_nodes){
            //seg.add(Segment.newBuilder().setV1Idx(i).setV2Idx(j).build())
            
            int n_idx = n.getnodeIndex();
            Vertex vertexNode = vertices.get(n_idx);
            vertices.set(n_idx, Vertex.newBuilder(vertexNode).addProperties(path_color).build());
            vertices.set(n_idx, Vertex.newBuilder(vertexNode).addProperties(path_thickness).build());
        }
        return segs;

    }
}
