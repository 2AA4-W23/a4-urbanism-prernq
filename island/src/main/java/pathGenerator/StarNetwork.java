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

        for(Polygon p: aMesh.getPolygonsList()){
            if(aMesh.getVertices(p.getCentroidIdx()).getPropertiesCount() == 1){
                city_node_idxs.add(p.getCentroidIdx());
            }
        }
    }

    public List<Polygon> createPath(){

        Property path_color = Structs.Property.newBuilder().setKey("rgb_color").setValue("0,255,255").build();

        Pathfinder find_path = new ShortestPath(graph);
        Mesh.Builder duplicate = Structs.Mesh.newBuilder();
        
        duplicate.addAllPolygons(aMesh.getPolygonsList());
        duplicate.addAllVertices(aMesh.getVerticesList());
        duplicate.addAllSegments(aMesh.getSegmentsList());

        List<Node> path_nodes = find_path.path_finder(graph.getNode(city_node_idxs.get(0)), graph.getNode(city_node_idxs.get(1)));
        
        for(Node n: path_nodes){
            
            System.out.println(n.getnodeIndex());
            Vertex.Builder new_city = Vertex.newBuilder(aMesh.getVertices(n.getnodeIndex()));
            new_city.addProperties(path_color);
            duplicate.setVertices(n.getnodeIndex(), new_city);
        }

        Mesh new_mesh = duplicate.build();

        return new_mesh.getPolygonsList();

    }
}
