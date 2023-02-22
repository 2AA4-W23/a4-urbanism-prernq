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
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

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

        // Create all the segments
        ArrayList<Segment> segments = new ArrayList<>();
        for (int x = 0; x < vertices.size() - 1; x++) {
            segments.add(Segment.newBuilder().setV1Idx(x).setV2Idx(x + 1).build());
        }

        // Distribute colors and thicknesses randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithProperties = new ArrayList<>();
        int vertThicknessNumber = bag.nextInt(11 - 3) + 3;
        Random bag = new Random();
        for(Vertex v: vertices){
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);

            //int vertThicknessNumber = bag.nextInt(11);
            String colorCode = red + "," + green + "," + blue;
            String vertThicknessValue = String.valueOf(vertThicknessNumber);
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Property vertThickness = Property.newBuilder().setKey("thickness").setValue(vertThicknessValue).build();
            Vertex withProperties = Vertex.newBuilder(v).addProperties(color).addProperties(vertThickness).build();
            verticesWithProperties.add(withProperties);
        }
        
        // Determine colors of segments based on average of its vertices
        ArrayList<Segment> segmentsWithProperties = new ArrayList<>();
        int segThicknessNumber = bag.nextInt(5 - 1) + 1;
        for (Segment s : segments) {

            int vertex1Idx = s.getV1Idx();
            int vertex2Idx = s.getV2Idx();
            Vertex vertex1 = verticesWithProperties.get(vertex1Idx);
            Vertex vertex2 = verticesWithProperties.get(vertex2Idx);

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

            //Determine thicknesses of segments randomly

            //int segThicknessNumber = bag.nextInt(5);
            String segThicknessValue = String.valueOf(segThicknessNumber);
            Property segThickness = Property.newBuilder().setKey("thickness").setValue(segThicknessValue).build();

            //add colors and thickness values to segments
            Segment withProperties = Segment.newBuilder(s).addProperties(color).addProperties(segThickness).build();
            segmentsWithProperties.add(withProperties);
        }

        //Create List of Polygons for squares
        //Since the segments are added by the order of vertices, which are added by creating 4 at a time, the segments are pre ordered for each polygon
        ArrayList<Polygon> polygons = new ArrayList<>();
        for(int x = 0; x < segments.size(); x+= 4){
            polygons.add(Polygon.newBuilder().addSegmentIdxs(x).addSegmentIdxs(x+1).addSegmentIdxs(x+2).addSegmentIdxs(x+3).build());
        }

        //the wrong way of making polygons list
        /*
        ArrayList<ArrayList<Segment>> shapes = new ArrayList<>();
        ArrayList<Segment> one_polygon = new ArrayList<>();
        int count = 0;
        for(Segment s : segments){
            if(count < 4){
                one_polygon.add(s);
                count += 1;
                if(count == 4){
                    shapes.add(one_polygon);
                    one_polygon = null;
                }
            }
        }
        */


        /*
        // Initialize Polygons List, a list of segments that make-up a polygon --> not complete
        // Please do not remove this as it can help with irregular polygon generation
        ArrayList<Polygon> polygons_list = new ArrayList<>();
        for(Segment s : segments){
            ArrayList<Segment> polygon = new ArrayList<>(); 
            int seg1_vertex1Idx = s.getV1Idx();
            int seg1_vertex2Idx = s.getV2Idx();

            Vertex seg1_vertex1 = verticesWithColors.get(seg1_vertex1Idx);
            Vertex seg1_vertex2 = verticesWithColors.get(seg1_vertex2Idx);
            
            polygon.add(s);
            int prev1;
            int prev2;
            for(Segment e : segments){
                int seg2_vertex1Idx = e.getV1Idx();
                int seg2_vertex2Idx = e.getV2Idx();

                Vertex seg2_vertex1 = verticesWithColors.get(seg1_vertex1Idx);
                Vertex seg2_vertex2 = verticesWithColors.get(seg1_vertex2Idx);
                
                if(seg1_vertex1Idx == seg2_vertex1Idx && seg1_vertex2Idx != seg2_vertex2Idx){
                    //polygon.add(s);
                    polygon.add(e);
                }else if(seg1_vertex1Idx == seg2_vertex2Idx && seg1_vertex1Idx != seg2_vertex1Idx){
                    //polygon.add(s);
                    polygon.add(e);
                }else if(seg1_vertex2Idx == seg2_vertex1Idx && seg1_vertex2Idx != seg2_vertex2Idx){
                    //polygon.add(s);
                    polygon.add(e);
                }else if(seg1_vertex2Idx == seg2_vertex2Idx && seg1_vertex2Idx != seg2_vertex1Idx){
                    //polygon.add(s);
                    polygon.add(e);
                }

                prev1 = seg2_vertex1Idx;
                prev2 = seg2_vertex2Idx;

            }
            int count = 0;
            for(ArrayList<Segment> p : polygons_list){
                for(Segment seg : p){
                    if(seg == polygon(0)){

                    }
                }
            }
            polygons_list.add(polygon);

        }
        */

        //Error check polygons list
        /*
        System.out.println("\n\nError check polygon list!!\n\n");
        for(ArrayList<Segment> x : polygons_list){
            for(Segment y : x){
                System.out.println(y.toString());
            }
        }
        */


        // Create Centroids and list
        
        
        return Mesh.newBuilder().addAllVertices(verticesWithProperties).addAllSegments(segmentsWithProperties).addAllPolygons(polygons).build();
    }
}
