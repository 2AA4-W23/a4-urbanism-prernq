package elements;

import islandGenerator.islandGen;

import java.util.ArrayList;
import java.util.Random;

public class Seed {
    public static Random rand;
    public static long seed;
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
