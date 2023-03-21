package elements;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Double.compare;
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

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class SoilAbsorption {
    public List<Polygon> assignSoilAbsorptionforCircle(List<Integer> insideCents){
        List<Polygon> polysWithAbsorption = new ArrayList<>();

        for (int i = 0; i < islandGen.inPolygons.size(); i++){
            Structs.Polygon p = islandGen.inPolygons.get(i);
        }

        return polysWithAbsorption;
    }
}
