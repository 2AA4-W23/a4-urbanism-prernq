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
    private Shape isleShape = new Shape();
    private Ocean ocean = new Ocean();
    private Beach beach = new Beach();
    private Biome biomes = new Biome();
    private River river = new River();
    private AltimetricProfile altimetric = new AltimetricProfile();
    private Lake lake = new Lake();
    private Elevation elevation = new Elevation();
    private Temperature temperature = new Temperature();
    private Humidity humidity = new Humidity();
    private Aquifer aquifer = new Aquifer();
    private SoilAbsorption soilAbsorption = new SoilAbsorption();
    private Shape.Shapes geoShape;
    public static Seed seed;

    public void init(Mesh aMesh){
        inVertices = new ArrayList<>();
        inCentroids = new ArrayList<>();
        inSegments = new ArrayList<>();
        inPolygons = new ArrayList<>();
        inMesh = aMesh;
        seed = new Seed();

        //make arrays of all values from input mesh aMesh
        extractCentandVert();
        extractSegments();
        extractPolygons();

        //initialize all classes from elements package
        isleShape.Shape();
    }

    private void extractCentandVert(){

        Property vertThickness = Property.newBuilder().setKey("thickness").setValue(String.valueOf(0)).build();
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
                inCentroids.add(Vertex.newBuilder().mergeFrom(v).clearProperties().addProperties(vertThickness).build());
            }
            else{
                //System.out.println("I am a vertex");
                inVertices.add(Vertex.newBuilder().mergeFrom(v).clearProperties().addProperties(vertThickness).build());

            }
        }
    }

    private void extractSegments(){
        Property segThickness = Property.newBuilder().setKey("thickness").setValue(String.valueOf(1)).build();

        for (Segment s : inMesh.getSegmentsList()){
            inSegments.add(Segment.newBuilder().mergeFrom(s).clearProperties().addProperties(segThickness).build());
        }
    }

    private void extractPolygons(){
        for (Polygon p : inMesh.getPolygonsList()){
            inPolygons.add(Polygon.newBuilder().mergeFrom(p).build());
        }
    }

    public void updatePolys(List<Polygon> newPolys){

        List<Polygon> original = new ArrayList<>();
        for (Polygon p : inPolygons){
            original.add(Polygon.newBuilder().mergeFrom(p).build());
        }

        inPolygons.clear();
        inPolygons = newPolys;

    }

    public void updateSegments(List<Segment> newSegments){

        List<Segment> original_segments = new ArrayList<>();
        for (Segment s : inSegments){
            original_segments.add(Segment.newBuilder().mergeFrom(s).build());
        }

        inSegments.clear();
        inSegments = newSegments;

    }

    public Mesh generate(Mesh aMesh, String mode){
        init(aMesh);

        //Choosing the island shape. This'll eventually be chosen based on a command line but for now just change it to the necessary enum value.
        geoShape = Shape.Shapes.CIRCLE;

        //for mode "lagoon" aka MVP
        if (mode.equals("lagoon")){

            if (geoShape == Shape.Shapes.CIRCLE) {
                //must be between -10 and 25
                int seaLevelTemp = 5;
                //get list of centroid that are inside the radius of the circle
                List<Integer> outsideCircle = isleShape.circle(200);
                List<Integer> insideCircle = isleShape.circle(50);
                List<Integer> centerCircle = isleShape.circle(10);

                List<Polygon> oceanAdded = ocean.assignOceanforCircle(outsideCircle);
                updatePolys(oceanAdded);

                //assign elevation based on alitmetric profile
                List<Polygon> altimetricAdded = altimetric.volcano(insideCircle, outsideCircle, centerCircle);
                updatePolys(altimetricAdded);

                //assigning elevation to polygons
                List<Polygon> elevationAdded = elevation.assignElevation();
                updatePolys(elevationAdded);

                //assigning temperature to polygons
                List<Polygon> temperatureAdded = temperature.assignTemperature(seaLevelTemp); //change back to sealevel temp
                updatePolys(temperatureAdded);

                //assign land biome values
                List<Polygon> biomesAdded = biomes.assignBiomeforCircle(outsideCircle, insideCircle);
                updatePolys(biomesAdded);

                //assigning lakes to polygons
                List<Polygon> lakeAdded = lake.assignLakes(outsideCircle, insideCircle, 3); // needs an input for number of lakes
                updatePolys(lakeAdded);

                //assigning aquifers to polygons
                List<Polygon> aquiferAdded = aquifer.assignAquifer();
                updatePolys(aquiferAdded);

                List<Polygon> humidityAdded = humidity.assignHumidity("canada");
                updatePolys(humidityAdded);

                //assigning soil profiles to polygons
                List<Polygon> soilAbsorptionAdded = soilAbsorption.assignSoilAbsorptionforCircle(outsideCircle);
                updatePolys(soilAbsorptionAdded);

                //assign land biome values
                List<Polygon> biomesAdded2 = biomes.assignBiomeforCircle(outsideCircle, insideCircle);
                updatePolys(biomesAdded2);

                List<Polygon> beachAdded = beach.assignBeachforCircle(outsideCircle);
                updatePolys(beachAdded);

                //assigning lakes to polygons
               // List<Polygon> lakeAdded = lake.assignLakes(outsideCircle, insideCircle, 3); // needs an input for number of lakes
                //updatePolys(lakeAdded);

                //List<Segment> riverAdded = river.assignRiverSegments(outsideCircle, insideCircle);
                //updateSegments(riverAdded);
            }
        }
        else if (mode.equals("normal")) {
            //more complex colours and biomes to be added in F23

            int seaLevelTemp = 0;
            seed.applySeed();


            //assigning elevation to polygons
            List<Polygon> elevationAdded = elevation.assignElevation();
            updatePolys(elevationAdded);

            //assigning temperature to polygons
            List<Polygon> temperatureAdded = temperature.assignTemperature(seaLevelTemp);
            updatePolys(temperatureAdded);

            //assigning aquifers to polygons
            List<Polygon> aquiferAdded = aquifer.assignAquifer();
            updatePolys(aquiferAdded);
/*
            for (Polygon p: inPolygons){
                for (Property prop: p.getPropertiesList()){
                    if (prop.getKey().equals("elevation")){
                        System.out.println(p.getCentroidIdx()+" "+prop.getValue());
                    }
                }
            }

 */

        }

        //return Mesh.newBuilder().addAllSegments(inSegments).addAllPolygons(inPolygons).build();
        return Mesh.newBuilder().addAllVertices(inVertices).addAllVertices(inCentroids).addAllSegments(inSegments).addAllPolygons(inPolygons).build();
    }


}
