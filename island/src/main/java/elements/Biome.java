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

public class Biome {

    private Colour colour;
    //dont know if we actually need this enum...
    public enum Biomes {
        BEACH, OCEAN, LAKE, TAIGA, TEMPERATE_DECIDUOUS_FOREST,
        TEMPERATE_RAIN_FOREST, TROPICAL_SEASONAL_FOREST,
        TROPICAL_RAIN_FOREST, SUBTROPICAL_DESERT, GRASSLANDS,
        SAVANNA, TUNDRA
    }

    //this function gets called when the island is the circle shape.
    public List<Polygon> assignforCircle(List<Integer> outsideCircle, List<Integer> insideCircle) {

        List<Polygon> PolysWithBiome = new ArrayList<>();

        //assign biome property
        for (int i = 0; i < islandGen.inPolygons.size(); i++){

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if polygon should be considered lake
            Boolean lake = false;
            for (int insideIdx: insideCircle){
                if (insideIdx == p.getCentroidIdx()){
                    lake = true;
                }
            }

            Boolean forest = false;

            //check if the polygon already has the property key "biome"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if ((prop.getKey()).equals("biome") == false){
                    newProp.add(prop);
                }
            }

            //assign the correct biome property for the circle island (forest or lake) and colour to polygons using the Colour class.
            if (lake == true){
                Property addLake = Property.newBuilder().setKey("biome").setValue("lake").build();
                Property addColour = colour.addColour("lake");
                newProp.add(addLake);
                newProp.add(addColour);
                PolysWithBiome.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else if (forest == true) {

            }
            else{
                PolysWithBiome.add(p);
            }
        }

        return PolysWithBiome;

    }

}
