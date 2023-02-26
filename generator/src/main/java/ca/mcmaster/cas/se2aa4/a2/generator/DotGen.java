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

        /*
        //usable segment generator but creates repeating segments
        for (int x = 0; x < vertices.size() - 1; x += 4) {
            //following line only works for squares
            Vertex v = vertices.get(x);
            System.out.println(v);
            if (v.getX() <= width && v.getY() <= height) {
                System.out.println("X: "+v.getX()+"\tY: "+v.getY());
                segments.add(Segment.newBuilder().setV1Idx(x).setV2Idx(x + 1).build());
                segments.add(Segment.newBuilder().setV1Idx(x).setV2Idx(x + 2).build());
                segments.add(Segment.newBuilder().setV1Idx(x + 1).setV2Idx(x + 3).build());
                segments.add(Segment.newBuilder().setV1Idx(x + 2).setV2Idx(x + 3).build());
            }
        }

         */




        // Distribute colors and thicknesses randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithProperties = new ArrayList<>();

        Random bag = new Random();
        int vertThicknessNumber = bag.nextInt(11 - 3) + 3;

        for (Vertex v : vertices) {
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
        }

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

        /* old version doesnt work with no repeating segments
        for (int x = 0; x < segments.size() - 1; x += 4) {
            polygons.add(Polygon.newBuilder().addSegmentIdxs(x).addSegmentIdxs(x + 1).addSegmentIdxs(x + 2).addSegmentIdxs(x + 3).build());
        }


*/
        /*
        //testing output for vertices, segments, and polygons
        System.out.println("Vertices:");
        for (int i = 0; i < vertices.size(); i++){
            Vertex v = vertices.get(i);
            System.out.println("Vertex"+i+":\t X: "+v.getX()+"\tY: "+v.getY());
        }
        int c = 0;
        for (Segment s : segments){
            System.out.println("Segment"+c+": V1:"+s.getV1Idx()+"\tV2: "+s.getV2Idx());
            c++;
        }
        for(Polygon p: polygons){
            System.out.println(p.getCentroidIdx() + ": "+p.getSegmentIdxsList());
        }

         */


        //Adding neighbouring polygons as references
        ArrayList<Polygon> polygonsWithNeighbours = new ArrayList<>();

        for (Polygon p : polygons){
            List<Integer> pSegs = p.getSegmentIdxsList();
            ArrayList<Integer> neighbours = new ArrayList<>();
            //System.out.println("\n\npsegs: "+pSegs);


            for (int i = 0; i < pSegs.size(); i++){
                for (Polygon ref: polygons) {
                    List<Integer> refSegs = ref.getSegmentIdxsList();
                    //System.out.println("refsegs "+refSegs);

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
                centroids.add(Vertex.newBuilder().setX((double) x_coord_avg).setY((double) y_coord_avg).build());
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

        Irregular libJTS = new Irregular();
        ArrayList<ArrayList<ArrayList<Double>>> voronoiPoly = libJTS.voronoiDiagram();
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Segment> segments = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();



        for (int i = 0; i < voronoiPoly.size(); i++){
            for (int j = 0; j < voronoiPoly.get(i).size(); j++){
                //System.out.println("Vertices");
                boolean contains = false;
                Double x = voronoiPoly.get(i).get(j).get(0);
                Double y = voronoiPoly.get(i).get(j).get(1);

                /*
                if (compare(x,0.0) < 0){
                    x = 0.0;
                }
                else if (compare(x,(double) width) > 0){
                    x = (double) width;
                }
                else {
                    System.out.println("no change");
                }
                if (compare(y,0.0) < 0){
                    y = 0.0;
                }
                else if (compare(y,(double) height) > 0){
                    y = (double) height;
                }
                else {
                    System.out.println("no change");
                }
                */

                if (vertices.size() == 0){
                    vertices.add(Vertex.newBuilder().setX(x).setY(y).build());
                    contains = true;
                }
                else{
                    for (Vertex v : vertices){
                        if ((compare(x,v.getX()) == 0) && (compare(y,v.getY()) == 0)){

                            //System.out.println("already exists");
                        }
                    }
                    if (contains == false) {
                        //System.out.println("Adding new");
                        vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                    }
                }
                //System.out.println(vertices.size());
            }
        }

        for(int x = 0; x < vertices.size(); x++){
            Vertex v = vertices.get(x);
            double x_coord = v.getX();
            double y_coord = v.getY();
            if(x_coord > width || y_coord > height){
                if(x_coord > width){
                    x_coord = (double) width;
                }
                if(y_coord > height){
                    y_coord = (double) height;
                }
                vertices.set(x, Vertex.newBuilder().setX((double) x_coord).setY((double) y_coord).build());
            }

        }

        //list of segments
        for (int i = 0; i < voronoiPoly.size(); i++){
            System.out.println("Starting");
            System.out.println(voronoiPoly.size());
            System.out.println("\n\nPolygon "+i);
            ArrayList<Integer> segIdxs = new ArrayList<Integer>(voronoiPoly.get(i).size());

            for (int j = 0; j < (voronoiPoly.get(i).size() - 1); j++){

                Double x1 = voronoiPoly.get(i).get(j).get(0);
                Double y1 = voronoiPoly.get(i).get(j).get(1);
                Double x2 = voronoiPoly.get(i).get(j+1).get(0);
                Double y2 = voronoiPoly.get(i).get(j+1).get(1);


                int v1Idx = 0;
                int v2Idx = 0;

                int segIdx = 0;


                System.out.println("SEGMENT");
                for (int a = 0; a < vertices.size(); a++){
                    Vertex vert = vertices.get(a);
                    Double vertX = vert.getX();
                    Double vertY = vert.getY();

                    if ((compare((double) vertX, (double) x1) == 0) && (compare((double) vertY, (double) y1) == 0)){
                        v1Idx = a;
                        //System.out.println("\n\nvert: ("+ vert.getX()+","+vert.getY()+")");
                        //System.out.println("v1: "+x1+","+y1);
                        //System.out.println("working v1");
                    }
                    if ((compare(vertX,x2) == 0) && (compare(vertY,y2) == 0)){
                        v2Idx = a;
                        //System.out.println("\n\nvert: ("+ vert.getX()+","+vert.getY()+")");
                        //System.out.println("v2: "+x2+","+y2);
                        //System.out.println("working v2");
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
                            //System.out.println("segidx" +segIdx);
                            segIdxs.add(segIdx);
                            //System.out.println("contains");
                        }
                    }
                    if (contains == false) {

                        //vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                        segIdx = segments.size();
                        //System.out.println("segidx" +segIdx);
                        segIdxs.add(segIdx);
                        segments.add(Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build());

                    }
                }

                //trying to crop segments
                /* 
                for (Segment s : segments){
                    int sv1idx = s.getV1Idx();
                    int sv2idx = s.getV2Idx();
                    Vertex sv1 = vertices.get(sv1idx);
                    Vertex sv2 = vertices.get(sv2idx);

                    double sv1_x = sv1.getX();
                    double sv1_y = sv1.getY();
                    double sv2_x = sv2.getX();
                    double sv2_y = sv2.getY();
                    
                    if(sv1_x > width){
                        sv1_x = width;
                        vertices.set(sv1idx, Vertex.newBuilder().setX((double) sv1_x).setY((double) sv1_y).build());
                    }
                    if(sv1_y > height){
                        sv1_y = height;
                        vertices.set(sv1idx, Vertex.newBuilder().setX((double) sv1_x).setY((double) sv1_y).build());
                    }
                    if(sv2_x > width){
                        sv2_x = width;
                        vertices.set(sv2idx, Vertex.newBuilder().setX((double) sv2_x).setY((double) sv2_y).build());
                    }
                    if(sv2_y > height){
                        sv2_y = height;
                        vertices.set(sv2idx, Vertex.newBuilder().setX((double) sv2_x).setY((double) sv2_y).build());
                    }
                }
                */

                //add polygons segments
                //polygons.add(Polygon.newBuilder(polygons.get(i)).addSegmentIdxs(segIdx).build());

                //System.out.println("ENDING");
                //System.out.println(segments.size());
                for (Segment s : segments){
                    System.out.println(s.getV1Idx() +" "+ s.getV2Idx());
                }
            }
            //System.out.println("stil ending");
            polygons.add(Polygon.newBuilder().setCentroidIdx(i).addAllSegmentIdxs(segIdxs).build());
        }

/*
        //list of segments
        for (int i = 0; i < voronoiPoly.size(); i++){
            System.out.println("Segments");
            for (int j = 0; j < voronoiPoly.get(i).size(); j++) {

                Double x1 = 0.0;
                Double y1 = 0.0;
                Double x2 = 0.0;
                Double y2 = 0.0;

                int v1 = 0;
                int v2 = 0;

                boolean contains = true;
                while (contains == true) {
                    contains = false;

                    x1 = voronoiPoly.get(i).get(j).get(0);
                    y1 = voronoiPoly.get(i).get(j).get(1);
                    x1 = voronoiPoly.get(i).get(j+1).get(0);
                    y1 = voronoiPoly.get(i).get(j+1).get(1);

                    int vIdx = 0;
                    for (Vertex v : vertices){
                        if ((compare(x1,v.getX()) == 0) && (compare(y1,v.getY()) == 0)){
                            v1 = vIdx;
                        }
                        if ((compare(x2,v.getX()) == 0) && (compare(y2,v.getY()) == 0)){
                            v2 = vIdx;
                        }
                    }

                    for (Segment s : segments) {
                        if ((s.getV1Idx() == v1) && (s.getV2Idx() == v2)) {
                            contains = true;
                        }
                    }
                }
                segments.add(Segment.newBuilder().setV1Idx(v1).setV2Idx(v2).build());



            }
        }

 */


        //list of polygons
        //polygons.add(Polygon.newBuilder().setCentroidIdx(0).build());


        /*
        //create random points
        Random bag = new Random();
        ArrayList<Vertex> points = new ArrayList<>();
        int number = 40;
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
        }

 */
        Random bag = new Random();
        ArrayList<Vertex> points = new ArrayList<>();

        //Distribute points colours and thickness (red, 3).
        ArrayList<Vertex> pointsWithProperties = new ArrayList<>();
        int pointThicknessNumber = 3;
        for(Vertex p: points) {
            Property color = Property.newBuilder().setKey("rgb_color").setValue("255,0,0,").build();
            String pointThicknessValue = String.valueOf(pointThicknessNumber);
            Property pointThickness = Property.newBuilder().setKey("thickness").setValue(pointThicknessValue).build();
            Vertex withProperties = Vertex.newBuilder(p).addProperties(color).addProperties(pointThickness).build();
            pointsWithProperties.add(withProperties);
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

            //int segThicknessNumber = bag.nextInt(5);
            String segThicknessValue = String.valueOf(segThicknessNumber);
            Property segThickness = Property.newBuilder().setKey("thickness").setValue(segThicknessValue).build();

            //add colors and thickness values to segments
            Segment withProperties = Segment.newBuilder(s).addProperties(color).addProperties(segThickness).build();
            segmentsWithProperties.add(withProperties);
        }

        System.out.println("Vertices: "+vertices.size());
        for (Vertex v: vertices){
            System.out.println("Vertex: " +v.getX()+","+v.getY());
        }

        System.out.println("segments");
        for (Segment s : segments){
            System.out.println("Segment: "+s.getV1Idx()+"("+vertices.get(s.getV1Idx()).getX()+","+vertices.get(s.getV1Idx()).getY()+")\t,"+s.getV2Idx()+"("+vertices.get(s.getV2Idx()).getX()+","+vertices.get(s.getV2Idx()).getY()+")");
        }

        System.out.println("Polygons");
        for (Polygon p: polygons){
            System.out.println(p.getCentroidIdx()+": "+p.getSegmentIdxsList());
        }

        return Mesh.newBuilder().addAllVertices(verticesWithProperties).addAllSegments(segmentsWithProperties).build();
    }
}
