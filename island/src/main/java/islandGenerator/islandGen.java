package islandGenerator;

import java.util.List;

import static java.lang.Double.compare;
import static java.lang.Math.abs;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import elements.*;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

//Use the class for generating the vertices, segments and polygons of the island.
public class islandGen {

    public static List<Vertex> inVertices;
    public static List<Vertex> inCentroids;
    public static List<Segment> inSegments;
    public static List<Polygon> inPolygons;
    private Mesh inMesh;
    private Shape shape = new Shape();
    private Ocean ocean = new Ocean();


    public void init(Mesh aMesh){
        inVertices = new ArrayList<>();
        inCentroids = new ArrayList<>();
        inSegments = new ArrayList<>();
        inPolygons = new ArrayList<>();
        inMesh = aMesh;

        //make arrays of all values from input mesh aMesh
        extractCentandVert();
        extractSegments();
        extractPolygons();

        //initialize all classes from elements package
        shape.Shape();


    }

    private void extractCentandVert(){
        String val = null;

        for (Vertex v : this.inMesh.getVerticesList()){
            List<Property> properties = v.getPropertiesList();

            for (Property p : properties){
                if (p.getKey().equals("centroid")){
                    val = p.getValue();
                    //System.out.println("We have a centroid on our hand folks: "+val);
                }
            }

            if (val.equals("yes")){
                //System.out.println("I am a centroid!");
                this.inCentroids.add(Vertex.newBuilder().mergeFrom(v).build());
            }
            else{
                //System.out.println("I am a vertex");
                this.inVertices.add(Vertex.newBuilder().mergeFrom(v).build());

            }
        }
    }
    private void extractSegments(){
        for (Segment s : inMesh.getSegmentsList()){
            this.inSegments.add(Segment.newBuilder().mergeFrom(s).build());
        }
    }
    private void extractPolygons(){
        for (Polygon p : inMesh.getPolygonsList()){
            this.inPolygons.add(Polygon.newBuilder().mergeFrom(p).build());
        }
    }

    public void updatePolys(List<Polygon> newPolys){

        List<Polygon> original = new ArrayList<>();
        for (Polygon p : this.inPolygons){
            original.add(Polygon.newBuilder().mergeFrom(p).build());
        }

        this.inPolygons.clear();
        this.inPolygons = newPolys;

    }

    public Mesh generate(Mesh aMesh, String mode){
        init(aMesh);

/*
        //testing to make sure ocean class does not duplicate biome property

        Property prop = Property.newBuilder().setKey("biome").setValue("unassigned").build();
        Property test = Property.newBuilder().setKey("test").setValue("test").build();
        List<Polygon> unassigned = new ArrayList<>();
        for (Polygon p : inPolygons){
            unassigned.add(Polygon.newBuilder(p).addProperties(prop).addProperties(test).build());
        }
        updatePolys(unassigned);

 */

        //for mode "lagoon"
        if (mode.equals("lagoon")){
            //get list of centroid that are inside the radius of the circle
            List<Integer> insideCents = shape.circle(200);

            //get updated list of polygons with property key "biome" given value "ocean"
            List<Polygon> oceanAdded = ocean.assignOcean(insideCents);
            updatePolys(oceanAdded);

        }


        return Mesh.newBuilder().addAllVertices(inVertices).addAllVertices(inCentroids).addAllSegments(inSegments).addAllPolygons(inPolygons).build();
    }


}
