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
        double max_x = 0.0;
        double max_y = 0.0;
        for (Vertex v: islandGen.inVertices) {
            if (compare(max_x, v.getX()) < 0){
                max_x = v.getX();
            }
            if (compare(max_y, v.getY()) < 0){
                max_y = v.getY();
            }
        }

        double centreX = max_x/2;
        double centreY = max_y/2;

        //this.centre.equals([centreX, centreY]);
        this.centre[0] = centreX;
        this.centre[1] = centreY;
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
