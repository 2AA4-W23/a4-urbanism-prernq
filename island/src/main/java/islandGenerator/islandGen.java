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

    public Mesh generate(Mesh aMesh, String mode){
        init(aMesh);

        //get list of centroids inside a given radius (the polygons that make up the island)
        //List<Integer> insideCents = shape.circle(200);

/*
        for (int i=0; i<insideCents.size(); i++){
            System.out.println(insideCents.get(i) + ": "+inCentroids.get(insideCents.get(i)).getX()+","+inCentroids.get(insideCents.get(i)).getY());
        }


 */

        /*
        System.out.println("My mkode is"+mode);
        //for mode "lagoon"
        if (mode.equals("lagoon")){
            System.out.println("hey ;)");
            List<Integer> insideCents = shape.circle(200);

            //assign biome property
            for (Polygon p: inPolygons){

                //check if the polygon already has the property key "biome"
                Boolean biomeProp = false;
                List<Property> properties = p.getPropertiesList();
                for (Property prop: properties){
                    if (prop.getKey() == "biome"){
                        biomeProp = true;
                    }
                }

                //check if polygon should be considered ocean (not a centroid inside the island radius)
                Boolean ocean = true;
                for (int insideIdx: insideCents){
                    if (insideIdx == p.getCentroidIdx()){
                        ocean = false;
                    }
                }

                //assign the property "ocean" to polygons
                if (biomeProp == true){
                    if (ocean == true){
                        //p.newBuilderForType().get

                    }
                }
                else{
                    if (ocean == true){
                        //p.newBuilderForType().
                        Property addProp = Property.newBuilder().setKey("biome").setValue("ocean").build();
                        Polygon.newBuilder(p).addProperties(addProp).build();
                    }
                }




            }




        }

         */




        return Mesh.newBuilder().addAllVertices(inVertices).addAllVertices(inCentroids).addAllSegments(inSegments).addAllPolygons(inPolygons).build();
    }


}
