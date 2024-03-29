package elements;

import java.util.List;

import static java.lang.Double.compare;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

import islandGenerator.islandGen;

public class Elevation {
    List<Polygon> PolysWithElevation = new ArrayList<>();
    List<Vertex> VertsWithElevation = new ArrayList<>();
    Properties properties = new Properties();
    Seed seed = new Seed();


    public List<Polygon> assignElevation() {

        int numRandomHighPoints = 10;
        int numRandomLowPoints = 10;
        ArrayList<Integer> randomHP = new ArrayList<>();
        ArrayList<Integer> randomLP = new ArrayList<>();

        //get the indices of the random high points
        for (int i = 0; i < numRandomHighPoints; i++) {
            int randomPoly = seed.rand.nextInt(((islandGen.inPolygons.size()) - 0) + 1) + 0;
            randomHP.add(randomPoly);
        }

        //get the indices of the random low points (making sure to not choose any points that are already in the high point array).
        for (int i = 0; i < numRandomLowPoints; i++) {
            boolean chosen = true;
            while (chosen == true) {
                int randomPoly = seed.rand.nextInt(((islandGen.inPolygons.size()) - 0) + 1) + 0;
                if ((randomHP.contains(randomPoly)) == false) {
                    randomLP.add(randomPoly);
                    chosen = false;
                }
            }
        }

        //assign elevation property
        for (int i = 0; i < islandGen.inPolygons.size(); i++) {

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if the polygon already has the property key "elevation"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop : properties) {
                if (!(prop.getKey()).equals("elevation")) {
                    newProp.add(prop);
                }
            }

            boolean oceanORbeachfound = false;
            for (Property prop : p.getPropertiesList()) {
                if (prop.getKey() == "biome") {
                    if ((prop.getValue() == "ocean") || (prop.getValue() == "lake")) {
                        Property addElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(0)).build();
                        newProp.add(addElevation);
                        oceanORbeachfound = true;
                    }
                    else if ((prop.getValue() == "beach")) {
                        Property addElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(1)).build();
                        newProp.add(addElevation);
                        oceanORbeachfound = true;
                    }
                }
            }

            if (oceanORbeachfound == false) {
                if (randomHP.contains(i)) {
                    Property addElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(5)).build();
                    newProp.add(addElevation);
                }
                else if (randomLP.contains(i)) {
                    Property addElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(-5)).build();
                    newProp.add(addElevation);
                }
                else {
                    int randElevation = seed.rand.nextInt(10)-4;
                    Property addElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(randElevation)).build();
                    newProp.add(addElevation);
                }
            }
            PolysWithElevation.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
        }

        return PolysWithElevation;

    }

    public List<Vertex> AssignElevationVert(){

        for (int i = 0; i < islandGen.inVertices.size(); i++){
            int avgElevation = 0;
            int count = 0;
            int elevation = 0;
            Vertex v = islandGen.inVertices.get(i);


            for (Polygon p: PolysWithElevation){
                List<Integer> segIdxs = p.getSegmentIdxsList();

                for (int segIdx: segIdxs){
                    Segment s = islandGen.inSegments.get(segIdx);

                    if ((s.getV1Idx() == i) || (s.getV2Idx() == i)){
                        for (Property prop: p.getPropertiesList()){
                            if (prop.getKey().equals("elevation")){
                                elevation = Integer.valueOf(prop.getValue());
                            }
                        }

                        avgElevation += elevation;
                        count ++;
                        break;
                    }
                }
            }

            if (!(count == 0)){
                avgElevation = avgElevation/count;
            }

            //System.out.println(i+" "+avgElevation);
            Vertex updatedVert = properties.addPropertyV(v, "elevation", String.valueOf(avgElevation));
            VertsWithElevation.add(updatedVert);
        }
        return VertsWithElevation;
    }
}
