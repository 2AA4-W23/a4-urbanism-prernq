package elements;

import islandGenerator.islandGen;

import java.util.ArrayList;
import java.util.Random;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

public class Seed {
    public Random rand = new Random();
    public long seed;
    public static Shape.Shapes shape;
    public AltimetricProfile.Profiles altProfile;
    public int[] riverStart;


    public void applySeed(){
        rand = new Random();
        seed = rand.nextLong();
        rand.setSeed(seed);
    }
    public void applySeed(long seed){
        rand = new Random();
        this.seed = seed;
        rand.setSeed(seed);
    }
    public long getSeed(){
        return seed;
    }

    public Random getRand(){
        return rand;
    }


    public Shape.Shapes getShape() {
        //random shape
        Shape.Shapes[] shapesList = Shape.Shapes.values();
        System.out.println(shapesList.length);
        shape = shapesList[rand.nextInt(shapesList.length)];

        System.out.println(shape);

        return shape;
    }

    public AltimetricProfile.Profiles getProfile() {
        //random altimetric profile
        AltimetricProfile.Profiles[] altList = AltimetricProfile.Profiles.values();
        altProfile = altList[rand.nextInt(altList.length)];

        System.out.println(altProfile);

        return altProfile;
    }

    public int[] getRiverStart(int riverNum) {    //random rivers

        riverStart = new int[riverNum];

        for (int i = 0; i < riverStart.length; i++){
            boolean approved = false;
            int rivVert;

            do{
                rivVert = rand.nextInt(islandGen.inVertices.size());

                if (!(islandGen.oceanVerts.contains(islandGen.inVertices.get(rivVert)))){
                    for (Property prop: islandGen.inVertices.get(rivVert).getPropertiesList()){
                        if (prop.getKey().equals("elevation")){
                            System.out.println(prop.getValue());
                            int elev = Integer.valueOf(prop.getValue());
                            if (elev > 0){
                                approved = true;
                            }
                            else{
                                System.out.println("not approved" +elev);
                            }
                        }
                    }
                }
            }
            while (approved == false);

            riverStart[i] = rivVert;
            System.out.println(rivVert);

        }

        for (int j = 0; j < riverStart.length; j++){
            System.out.println(riverStart[j]);
        }

        return riverStart;

    }

}
