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

    Colour colour = new Colour();

    //dont know if we actually need this enum...
    public enum Biomes {
        BEACH, OCEAN, LAKE, TAIGA, TEMPERATE_DECIDUOUS_FOREST,
        TEMPERATE_RAIN_FOREST, TROPICAL_SEASONAL_FOREST,
        TROPICAL_RAIN_FOREST, SUBTROPICAL_DESERT, GRASSLANDS,
        SAVANNA, TUNDRA
    }

    //this function gets called when the island is the circle shape.
    public List<Polygon> assignBiomeforCircle(List<Integer> outsideCircle, List<Integer> insideCircle) {

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

            //checks if polygon should be considered land
            // if centroid inside outtercircle but not inside inner circle
            Boolean land = false;
            for (int outsideIdx: outsideCircle){
                if(outsideIdx == p.getCentroidIdx() && lake == false){
                    land = true;
                }
            }

            //check if the polygon already has the property key "biome"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if ((prop.getKey()).equals("biome") == false){
                    newProp.add(prop);
                }
            }

            //assign the correct biome property for the circle island (land or lake) and colour to polygons using the Colour class.
            if (lake == true){
                Property addLake = Property.newBuilder().setKey("biome").setValue("lake").build();
                Property addColour = colour.addColour("lake");
                newProp.add(addLake);
                newProp.add(addColour);
                PolysWithBiome.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else if (land == true) {
                int temp = 1000;
                int humid = 1000;
                for (Property prop: newProp) {
                    if ((prop.getKey()).equals("temperature")) {
                        temp = Integer.parseInt(prop.getValue());
                    }
                    else if ((prop.getKey()).equals("humidity")) {
                        humid = Integer.parseInt(prop.getValue());
                    }
                }


                Property addLand;
                Property addLandColour;

                //assign taiga biome
                if (((temp >= -4)&&(temp <= 4)) && ((humid >= 3)&&(humid <= 20))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("taiga").build();
                    addLandColour = colour.addColour("taiga");
                }
                //assign temperate deciduous forest
                else if (((temp >= 4)&&(temp <= 20)) && ((humid >= 10)&&(humid <= 20))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("tempdforest").build();
                    addLandColour = colour.addColour("tempdforest");
                }
                //assign temperate rain forest
                else if (((temp >= 4)&&(temp <= 20)) && ((humid >= 20)&&(humid <= 30))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("temprforest").build();
                    addLandColour = colour.addColour("temprforest");
                }
                //assign savanna
                else if (((temp >= 20)&&(temp <= 30)) && ((humid >= 3)&&(humid <= 24))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("savanna").build();
                    addLandColour = colour.addColour("savanna");
                }
                //assign tropical rain forest
                else if (((temp >= 20)&&(temp <= 30)) && ((humid >= 24)&&(humid <= 45))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("troprforest").build();
                    addLandColour = colour.addColour("troprforest");
                }
                //assign desert
                else if (((temp >= 4)&&(temp <= 20)) && ((humid >= 3)&&(humid <= 10))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("desert").build();
                    addLandColour = colour.addColour("desert");
                }
                //assign subtropical desert
                else if (((temp >= -4)&&(temp <= 30)) && ((humid >= 0)&&(humid <= 3))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("subdesert").build();
                    addLandColour = colour.addColour("subdesert");
                }
                //assign tundra
                else if (((temp >= -15)&&(temp <= -4)) && ((humid >= 0)&&(humid <= 10))) {
                    addLand = Property.newBuilder().setKey("biome").setValue("tundra").build();
                    addLandColour = colour.addColour("tundra");
                }
                //assign regular forest
                else {
                    addLand = Property.newBuilder().setKey("biome").setValue("forest").build();
                    addLandColour = colour.addColour("forest");
                }

                newProp.add(addLand);
                newProp.add(addLandColour);
                PolysWithBiome.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());

            }
            else{
                PolysWithBiome.add(p);
            }
        }

        return PolysWithBiome;

    }

}
