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

public class Ocean {

    Colour colour = new Colour();
    List<Polygon> oceanPolys = new ArrayList<>();
    Properties properties = new Properties();

    public List<Polygon> assignOceanforCircle(List<Integer> insideCents){

        Property oceanColour = colour.addColour("ocean");

        //assign biome property
        for (int i = 0; i < islandGen.inPolygons.size(); i++){

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if polygon should be considered ocean (not a centroid inside the island radius)
            Boolean ocean = true;
            for (int insideIdx: insideCents){
                if (insideIdx == p.getCentroidIdx()){
                    ocean = false;
                }
            }

            //assign the property "ocean" and colour to polygons
            if (ocean == true){
                p = properties.addPropertyP(p, "biome", "ocean");
                p = properties.addPropertyP(p, oceanColour.getKey(), oceanColour.getValue());
            }
            oceanPolys.add(p);
        }

        System.out.println(oceanPolys.size());

        return oceanPolys;
    }
}
