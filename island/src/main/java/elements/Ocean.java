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

    private Colour colour;

    public List<Polygon> assignOceanforCircle(List<Integer> insideCents){

        List<Polygon> oceanPolys = new ArrayList<>();

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

            //check if the polygon already has the property key "biome"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if (((prop.getKey()).equals("biome") == false) || ((prop.getKey()).equals("rgb_color") == false)){
                    newProp.add(prop);
                }
            }

            //assign the property "ocean" and colour to polygons
            if (ocean == true){
                Property addOcean = Property.newBuilder().setKey("biome").setValue("ocean").build();
                Property addColour = colour.addColour("ocean");
                newProp.add(addOcean);
                newProp.add(addColour);
                oceanPolys.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else{
                oceanPolys.add(p);
            }
        }

        return oceanPolys;
    }



}
