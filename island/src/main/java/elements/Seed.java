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
    public static Random rand;
    public static long seed;
    public static Shape.Shapes shape;
    public static AltimetricProfile.Profiles altProfile;
    public static int riverNum;
    public int[] riverStart;




    public static ArrayList<Integer> randomHP;
    public static ArrayList<Integer> randomLP;

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


    public void getRands(){
        //random shape
        Shape.Shapes[] shapesList = Shape.Shapes.values();
        System.out.println(shapesList.length);
        shape = shapesList[rand.nextInt(shapesList.length)];

        System.out.println(shape);

        //random altimetric profile
        AltimetricProfile.Profiles[] altList = AltimetricProfile.Profiles.values();
        altProfile = altList[rand.nextInt(altList.length)];

        System.out.println(altProfile);


        //random rivers
        riverNum = rand.nextInt(11);
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


    }


    public ArrayList<Integer> getRandHigh(){
        return randomHP;
    }
    public ArrayList<Integer> getRandLow(){
        return randomLP;
    }



    public void randElevation(int numHigh, int numLow){
        ArrayList<Integer> randomHP = new ArrayList<>();
        ArrayList<Integer> randomLP = new ArrayList<>();

        //add random high point polygon indices to list
        for (int i = 0; i < numHigh; i++) {
            boolean chosen = true;
            while (chosen == true){
                int randInt = rand.nextInt(((islandGen.inPolygons.size()) - 0) + 1) + 0;

                if (randomHP == null){
                    randomHP.add(randInt);
                    chosen = false;
                }
                else if (((randomHP.contains(randInt)) == false)) {
                    randomHP.add(randInt);
                    chosen = false;
                }
            }
        }

        //add random low point polygon indices to list
        for (int i = 0; i < numLow; i++) {
            boolean chosen = true;
            while (chosen == true){
                int randInt = rand.nextInt(((islandGen.inPolygons.size()) - 0) + 1) + 0;

                if (randomLP == null){
                    if (randomHP == null){
                        randomLP.add(randInt);
                        chosen = false;
                    }
                    else{
                        if (randomHP.contains(randInt) == false){
                            randomLP.add(randInt);
                            chosen = false;
                        }
                    }
                }
                else{
                    if (randomHP == null){
                        if ((randomLP.contains(randInt)) == false){
                            randomLP.add(randInt);
                            chosen = false;
                        }
                    }
                    else{
                        if (((randomLP.contains(randInt)) == false) && ((randomHP.contains(randInt)) == false)){
                            randomLP.add(randInt);
                            chosen = false;
                        }
                    }
                }
            }
        }
    }
    public void setAllRand(int numHigh, int numLow){
        randElevation(numHigh, numLow);
    }
}
