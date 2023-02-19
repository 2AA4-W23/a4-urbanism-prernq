package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class DotGen {

    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;
    private ArrayList<Double[]> vertex_coords = new ArrayList<Double[]>();

    public Mesh generate() {
        Set<Vertex> vertices = new HashSet<>();
        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                Double[] coordinates = {(double)x,(double) y};
                vertex_coords.add(coordinates);
                System.out.println(coordinates.toString());
                //vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build());
                //vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build());
                //vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build());
            }
        }
        /*
        Segment[] segments = new Segment[100];
        for(int x = 0 ; x < vertex_coords.size(); x++){
            Double[] coordinate_set = vertex_coords.get(x);
            double x_coord = coordinate_set[0];
            double y_coord = coordinate_set[1]
            Segment.newBuilder().
            segments.add(Segment.newBuilder().setV1Idx(vertices[x]).setV2Idx((int) y_coord).build());
        }
        */

        // Create all the edges
        ArrayList<Segment> segments = new ArrayList<>();
        for (int x = 0; x < vertices.size() - 1; x++) {
            segments.add(Segment.newBuilder().setV1Idx(x).setV2Idx(x + 1).build());
        }

        //System.out.println("fThis is here.");
        //System.out.print(vertex_coords.toString().toString());


        // Distribute colors randomly. Vertices are immutable, need to enrich them
        Set<Vertex> verticesWithColors = new HashSet<>();
        Random bag = new Random();
        for(Vertex v: vertices){
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = Vertex.newBuilder(v).addProperties(color).build();
            verticesWithColors.add(colored);
        }
        //return Mesh.newBuilder().addAllVertices(verticesWithColors).build();
        
        // Average colour segments
        ArrayList<Segment> segmentsWithColors = new ArrayList<>();
        for (Segment s : segments) {
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            int RedAverage = red / 2;
            int GreenAverage = green / 2;
            int BlueAverage = blue / 2;
            String colorCode = RedAverage + "," + GreenAverage + "," + BlueAverage;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Segment colored = Segment.newBuilder(s).addProperties(color).build();
            segmentsWithColors.add(colored);
        }
        return Mesh.newBuilder().addAllVertices(verticesWithColors).addAllSegments(segmentsWithColors).build();
    }
}
