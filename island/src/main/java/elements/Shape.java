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

public class Shape {
    private int height;
    private int width;
    private double[] centre;

    public enum Shapes {
        CIRCLE, OVAL, RANDOM
    }


    public void Shape(){
        System.out.println("Constructor working");
        height = 500;
        width = 500;
        centre = new double[2];
        setCentre();
    }

    public void setCentre(){

        this.centre[0] = width/2;
        this.centre[1] = height/2;
    }

    public List<Integer> circle(double radius){        //returns int list of centroid indices within given radius of a circle centered at centre of mesh

        List<Integer> insideCents = new ArrayList<>();

        for (int i = 0; i < islandGen.inCentroids.size(); i++){
            Vertex c = islandGen.inCentroids.get(i);
            double x = c.getX() - centre[0];
            double y = c.getY() - centre[1];

            double calc = Math.sqrt((Math.abs(x)*Math.abs(x)) + (Math.abs(y)*Math.abs(y)));

            //System.out.println(i+": "+c.getX()+" "+c.getY()+"      "+calc);

            if (compare(radius, calc) >= 0){
                insideCents.add(i);
            }
        }
        return insideCents;
    }
}
