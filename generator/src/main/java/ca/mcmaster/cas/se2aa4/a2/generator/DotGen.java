package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import java.util.List;

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
        ArrayList<Vertex> vertices = new ArrayList<>();
        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build());

                Double[] coordinates = {(double)x,(double) y};
                vertex_coords.add(coordinates);
            }
        }

        // Create segments
        ArrayList<Segment> segments = new ArrayList<>();
        for (int x = 0; x < vertices.size() - 1; x++) {
            segments.add(Segment.newBuilder().setV1Idx(x).setV2Idx(x + 1).build());
        }

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithColors = new ArrayList<>();
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
        
        // Average colour segments
        ArrayList<Segment> segmentsWithColors = new ArrayList<>();
        for (Segment s : segments) {

            int vertex1Idx = s.getV1Idx();
            int vertex2Idx = s.getV2Idx();
            Vertex vertex1 = verticesWithColors.get(vertex1Idx);
            Vertex vertex2 = verticesWithColors.get(vertex2Idx);

            List<Property> properties_v1 = vertex1.getPropertiesList();
            List<Property> properties_v2 = vertex2.getPropertiesList();
            String color_code_v1 = null;
            String color_code_v2 = null;
            
            for(Property p: properties_v1){
                if(p.getKey().equals("rgb_color")){
                    color_code_v1 = p.getValue();
                }
            }
            for(Property p: properties_v2){
                if(p.getKey().equals("rgb_color")){
                    color_code_v2 = p.getValue();
                }
            }

            
            //String colour_code_v1 = vertex1.getProperties(vertex1Idx).getValue();
            //String colour_code_v2 = vertex2.getProperties(vertex2Idx).getValue();
            String[] colors_v1 = color_code_v1.split(",");
            String[] colors_v2 = color_code_v2.split(",");
            
            int red_v1 = Integer.parseInt(colors_v1[0]);
            int green_v1 = Integer.parseInt(colors_v1[1]);
            int blue_v1 = Integer.parseInt(colors_v1[2]);
            int red_v2 = Integer.parseInt(colors_v2[0]);
            int green_v2 = Integer.parseInt(colors_v2[1]);
            int blue_v2 = Integer.parseInt(colors_v2[2]);

            int RedAverage = (red_v1 + red_v2) / 2;
            int GreenAverage = (green_v1 + green_v2) / 2;
            int BlueAverage = (blue_v1 + blue_v2) / 2;
            String colorCode = RedAverage + "," + GreenAverage + "," + BlueAverage;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Segment colored = Segment.newBuilder(s).addProperties(color).build();
            segmentsWithColors.add(colored);
        }
        
        return Mesh.newBuilder().addAllVertices(verticesWithColors).addAllSegments(segmentsWithColors).build();
    }
}
