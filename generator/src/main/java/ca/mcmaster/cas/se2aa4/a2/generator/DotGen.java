package ca.mcmaster.cas.se2aa4.a2.generator;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import static java.lang.Double.compare;
import static java.lang.Math.abs;


import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import JTS.Irregular;



public class DotGen {

    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;
    public String mode;
    private ArrayList<Double[]> vertex_coords = new ArrayList<Double[]>();
    private ArrayList<Double[]> centroid_coords = new ArrayList<Double[]>();

    public DotGen(String arg1) {
        mode = arg1;
    }

    public Mesh generategrid() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        // Create all the vertices
        for (int x = 0; x <= width; x += square_size) {
            for (int y = 0; y <= height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());

                Double[] coordinates = {(double) x, (double) y};
                vertex_coords.add(coordinates);
            }
        }

        // Create all the segments
        ArrayList<Segment> segments = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++){
            Vertex v1 = vertices.get(i);
            Double v1XCoord = v1.getX();
            Double v1YCoord = v1.getY();

            for (int j = 0; j < vertices.size(); j++){
                Vertex v2 = vertices.get(j);
                Double v2XCoord = v2.getX();
                Double v2YCoord = v2.getY();

                if (compare(v2XCoord,v1XCoord) == 0){
                    Double diff = v2YCoord - v1YCoord;
                    if (diff == square_size){
                        segments.add(Segment.newBuilder().setV1Idx(i).setV2Idx(j).build());
                    }
                }
                if (compare(v1YCoord,v2YCoord) == 0){
                    Double diff = v2XCoord - v1XCoord;
                    if (diff == square_size){
                        segments.add(Segment.newBuilder().setV1Idx(i).setV2Idx(j).build());
                    }
                }
            }
        }

        // Distribute colors and thicknesses randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithProperties = new ArrayList<>();

        Random bag = new Random();
        int vertThicknessNumber = bag.nextInt(11 - 3) + 3;

        for (Vertex v : vertices) {
            int blue;
            int green;
            int red;
            if (mode == "debug") {
                red = 0;
                green = 0;
                blue = 0;
            } else {
                red = bag.nextInt(255);
                green = bag.nextInt(255);
                blue = bag.nextInt(255);
            }

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

            int RedAverage;
            int GreenAverage;
            int BlueAverage;

            if (mode == "debug") {
                RedAverage = 0;
                GreenAverage = 0;
                BlueAverage = 0;
            } else {
                int vertex1Idx = s.getV1Idx();
                int vertex2Idx = s.getV2Idx();
                Vertex vertex1 = verticesWithProperties.get(vertex1Idx);
                Vertex vertex2 = verticesWithProperties.get(vertex2Idx);

                List<Property> properties_v1 = vertex1.getPropertiesList();
                List<Property> properties_v2 = vertex2.getPropertiesList();
                String color_code_v1 = null;
                String color_code_v2 = null;

                for (Property p : properties_v1) {
                    if (p.getKey().equals("rgb_color")) {
                        color_code_v1 = p.getValue();
                    }
                }
                for (Property p : properties_v2) {
                    if (p.getKey().equals("rgb_color")) {
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

                RedAverage = (red_v1 + red_v2) / 2;
                GreenAverage = (green_v1 + green_v2) / 2;
                BlueAverage = (blue_v1 + blue_v2) / 2;

            }

            String colorCode = RedAverage + "," + GreenAverage + "," + BlueAverage;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();

            //Determine thicknesses of segments randomly
            String segThicknessValue = String.valueOf(segThicknessNumber);
            Property segThickness = Property.newBuilder().setKey("thickness").setValue(segThicknessValue).build();

            //add colors and thickness values to segments
            Segment withProperties = Segment.newBuilder(s).addProperties(color).addProperties(segThickness).build();
            segmentsWithProperties.add(withProperties);
        }

        //Create List of Polygons for grid
        //Since the segments are added by the order of vertices, which are added by creating 4 at a time, the segments are pre ordered for each polygon
        ArrayList<Polygon> polygons = new ArrayList<>();

        int centIdx = 0;
        for (int i = 0; i < segments.size(); i+=2){
            Segment s1 = segments.get(i);
            int s1v1 = s1.getV1Idx();
            int s1v2 = s1.getV2Idx();
            for (int j = 0; j < segments.size(); j++){
                Segment s2 = segments.get(j);
                int s2v1 = s2.getV1Idx();
                int s2v2 = s2.getV2Idx();

                if ((s1v2 == s2v1) && (i != j)){
                    for (int k = 0; k < segments.size(); k++){
                        Segment s3 = segments.get(k);
                        int s3v1 = s3.getV1Idx();
                        int s3v2 = s3.getV2Idx();

                        if ((s2v2 == s3v2) && (i != k) && (j != k)){
                            for (int l = 0; l < segments.size(); l++){
                                Segment s4 = segments.get(l);
                                int s4v1 = s4.getV1Idx();
                                int s4v2 = s4.getV2Idx();

                                if ((s3v1 == s4v2) && (s4v1 == s1v1) && (i != l) && (j != l) && (k != l)){
                                    polygons.add(Polygon.newBuilder().setCentroidIdx(centIdx).addSegmentIdxs(i).addSegmentIdxs(j).addSegmentIdxs(k).addSegmentIdxs(l).build());
                                    centIdx++;
                                }
                            }
                        }
                    }
                }
            }
        }

        //Adding neighbouring polygons as references
        ArrayList<Polygon> polygonsWithNeighbours = new ArrayList<>();

        for (Polygon p : polygons){
            List<Integer> pSegs = p.getSegmentIdxsList();
            ArrayList<Integer> neighbours = new ArrayList<>();

            for (int i = 0; i < pSegs.size(); i++){
                for (Polygon ref: polygons) {
                    List<Integer> refSegs = ref.getSegmentIdxsList();

                    for (int j = 0; j < refSegs.size(); j++) {
                        if ((refSegs.get(j) == pSegs.get(i)) && (ref.getCentroidIdx() != p.getCentroidIdx())) {

                            neighbours.add(ref.getCentroidIdx());

                        }
                    }
                }
            }
            Polygon withNeighbours = Polygon.newBuilder(p).addAllNeighborIdxs(neighbours).build();
            polygonsWithNeighbours.add(withNeighbours);
        }

        // Create Centroids and list
        ArrayList<Vertex> centroids = new ArrayList<>();
        for (Polygon p : polygons) {
            List<Integer> polygon_segments = p.getSegmentIdxsList();
            int number_of_segments = polygon_segments.size();
            double x_coord_total = 0.0;
            double y_coord_total = 0.0;
            double x_coord_avg = 0.0;
            double y_coord_avg = 0.0;

            for (int x = 0; x < number_of_segments; x++) {
                Segment s = segments.get((polygon_segments.get(x)));
                int vertex1Idx = s.getV1Idx();
                int vertex2Idx = s.getV2Idx();
                Vertex vertex1 = vertices.get(vertex1Idx);
                Vertex vertex2 = vertices.get(vertex2Idx);
                x_coord_total += vertex1.getX();
                y_coord_total += vertex1.getY();
                x_coord_total += vertex2.getX();
                y_coord_total += vertex2.getY();
            }
            x_coord_avg = x_coord_total / (number_of_segments * 2);
            y_coord_avg = y_coord_total / (number_of_segments * 2);
            if ((x_coord_avg <= width) && (y_coord_avg <= height)) {
                Double[] coordinates = {x_coord_avg, y_coord_avg};
                centroid_coords.add(coordinates);
            }
        }

        //Distribute centroid colours and thickness
        ArrayList<Vertex> centroidsWithProperties = new ArrayList<>();
        int centThicknessNumber = 3;
        for (Vertex c : centroids) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("255,0,0,").build();
            String centThicknessValue = String.valueOf(centThicknessNumber);
            Property centThickness = Property.newBuilder().setKey("thickness").setValue(centThicknessValue).build();
            Vertex withProperties = Vertex.newBuilder(c).addProperties(color).addProperties(centThickness).build();
            centroidsWithProperties.add(withProperties);
        }

        return Mesh.newBuilder().addAllVertices(verticesWithProperties).addAllSegments(segmentsWithProperties).addAllPolygons(polygonsWithNeighbours).addAllVertices(centroidsWithProperties).build();
    }

    public Mesh generateirregular() {

        //create random points
        Random bag = new Random();
        ArrayList<Vertex> points = new ArrayList<>();
        int number = 120;
        for (int i = 0; i < number; i++) {
            int x = 0;
            int y = 0;
            boolean contains = true;
            while (contains == true) {
                contains = false;
                x = bag.nextInt((width + 1) - 0) + 0;
                y = bag.nextInt((height + 1) - 0) + 0;
                for (Vertex p : points) {
                    if ((p.getX() == x) && (p.getY() == y)) {
                        contains = true;
                    }
                }
            }
            points.add(Vertex.newBuilder().setX(x).setY(y).build());
            Double[] coordinates = {(double) x, (double) y};
            centroid_coords.add(coordinates);
        }

        //initializing Irregular grid class
        Irregular libJTS = new Irregular();
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Segment> segments = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();
        int relax = 5;


        libJTS.setCentroids(centroid_coords);
        libJTS.voronoiDiagram();
        libJTS.resetCentroids();

        for (int i = 0; i < relax; i++){
            libJTS.voronoiDiagram();
            libJTS.resetCentroids();
        }

        ArrayList<ArrayList<ArrayList<Double>>> voronoiPoly = libJTS.getPolygonCoords();
        ArrayList<Double[]> voronoiCentroids = libJTS.getCentroids();

        for (int i = 0; i < voronoiPoly.size(); i++){
            for (int j = 0; j < voronoiPoly.get(i).size(); j++){
                boolean contains = false;
                Double x = voronoiPoly.get(i).get(j).get(0);
                Double y = voronoiPoly.get(i).get(j).get(1);

                if (vertices.size() == 0){
                    vertices.add(Vertex.newBuilder().setX(x).setY(y).build());
                    contains = true;
                }
                else{
                    if (contains == false) {
                        vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                    }
                }
            }
        }

        //list of segments
        for (int i = 0; i < voronoiPoly.size(); i++){
            ArrayList<Integer> segIdxs = new ArrayList<Integer>(voronoiPoly.get(i).size());

            for (int j = 0; j < (voronoiPoly.get(i).size() - 1); j++){

                Double x1 = voronoiPoly.get(i).get(j).get(0);
                Double y1 = voronoiPoly.get(i).get(j).get(1);
                Double x2 = voronoiPoly.get(i).get(j+1).get(0);
                Double y2 = voronoiPoly.get(i).get(j+1).get(1);

                int v1Idx = 0;
                int v2Idx = 0;

                int segIdx = 0;

                //System.out.println("SEGMENT");
                for (int a = 0; a < vertices.size(); a++){
                    Vertex vert = vertices.get(a);
                    Double vertX = vert.getX();
                    Double vertY = vert.getY();

                    if ((compare((double) vertX, (double) x1) == 0) && (compare((double) vertY, (double) y1) == 0)){
                        v1Idx = a;
                    }
                    if ((compare(vertX,x2) == 0) && (compare(vertY,y2) == 0)){
                        v2Idx = a;
                    }
                }

                boolean contains = false;
                if (segments.size() == 0){
                    segments.add(Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build());
                    segIdx = 0;
                    segIdxs.add(segIdx);
                }
                else{
                    for (int k = 0; k < segments.size(); k++){
                        Segment seg = segments.get(k);
                        if (((seg.getV1Idx() == v1Idx) && (seg.getV2Idx() == v2Idx)) || ((seg.getV2Idx() == v1Idx) && (seg.getV1Idx()) == v2Idx)){
                            contains = true;
                            segIdx = k;
                            segIdxs.add(segIdx);
                        }
                    }
                    if (contains == false) {
                        segIdx = segments.size();
                        segIdxs.add(segIdx);
                        segments.add(Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build());

                    }
                }
            }
            polygons.add(Polygon.newBuilder().setCentroidIdx(i).addAllSegmentIdxs(segIdxs).build());
        }

        ArrayList<Vertex> centroids = new ArrayList<>();
        for (Double[] c : voronoiCentroids){
            centroids.add(Vertex.newBuilder().setX(c[0]).setY(c[1]).build());
        }

        //Distribute points colours and thickness (red, 3).
        ArrayList<Vertex> centroidsWithProperties = new ArrayList<>();
        int centroidThicknessNumber = 3;
        for(Vertex c: centroids) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("255,0,0,").build();
            String centroidThicknessValue = String.valueOf(centroidThicknessNumber);
            Property centroidThickness = Property.newBuilder().setKey("thickness").setValue(centroidThicknessValue).build();
            Vertex withProperties = Vertex.newBuilder(c).addProperties(color).addProperties(centroidThickness).build();
            centroidsWithProperties.add(withProperties);
        }

        //Distribute points colours and thickness (red, 3).
        ArrayList<Vertex> verticesWithProperties = new ArrayList<>();
        int vertexThicknessNumber = 3;
        for(Vertex v: vertices) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("0,0,0,").build();
            String vertexThicknessValue = String.valueOf(vertexThicknessNumber);
            Property vertexThickness = Property.newBuilder().setKey("thickness").setValue(vertexThicknessValue).build();
            Vertex withProperties = Vertex.newBuilder(v).addProperties(color).addProperties(vertexThickness).build();
            verticesWithProperties.add(withProperties);
        }

        ArrayList<Segment> segmentsWithProperties = new ArrayList<>();
        int segmentThicknessNumber = 3;
        for(Segment s: segments) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("0,0,0,").build();
            String segmentThicknessValue = String.valueOf(segmentThicknessNumber);
            Property segmentThickness = Property.newBuilder().setKey("thickness").setValue(segmentThicknessValue).build();
            Segment withProperties = Segment.newBuilder(s).addProperties(color).addProperties(segmentThickness).build();
            segmentsWithProperties.add(withProperties);
        }

        //set polygon properties
        ArrayList<Polygon> polygonsWithProperties = new ArrayList<>();
        int polygonThicknessNumber = 3;
        for(Polygon p: polygons) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("255,0,0,").build();
            String polygonThicknessValue = String.valueOf(polygonThicknessNumber);
            Property polygonThickness = Property.newBuilder().setKey("thickness").setValue(polygonThicknessValue).build();
            Polygon withProperties = Polygon.newBuilder(p).addProperties(color).addProperties(polygonThickness).build();
            polygonsWithProperties.add(withProperties);
        }

        return Mesh.newBuilder().addAllVertices(verticesWithProperties).addAllVertices(centroidsWithProperties).addAllSegments(segmentsWithProperties).addAllPolygons(polygonsWithProperties).build();
    }
}
