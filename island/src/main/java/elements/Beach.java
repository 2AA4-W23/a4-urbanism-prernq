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

public class Beach {
    Colour colour = new Colour();

    public List<Polygon> assignBeachforCircle(List<Integer> insideCents){

        List<Polygon> beachPolys = new ArrayList<>();

        //assign biome property
        for (int i = 0; i < islandGen.inPolygons.size(); i++){

            Structs.Polygon p = islandGen.inPolygons.get(i);
            List<Integer> p_neighbours_idxs = p.getNeighborIdxsList();
            List<Polygon> p_neighbours = new ArrayList<>();
            Boolean beach = false;
            Boolean is_land = false;
            Boolean touches_water = false;

            //creates list of neighbour polygons from neighbour index list
            for(int neighbour_idx : p_neighbours_idxs){
                Polygon neighbour_poly = islandGen.inPolygons.get(neighbour_idx);
                p_neighbours.add(neighbour_poly);
            }
            
            //checks if current polygon is land by checking if its a forest
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if ((prop.getKey()).equals("biome") == true){
                    if((prop.getValue()).equals("forest")){
                        is_land = true;              
                    }
                }
            }

            //checks if neighbouring polygons are ocean or lake
            for(Polygon neighbour: p_neighbours){
                List<Structs.Property> neighbour_properties = neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            touches_water = true;              
                        }
                    }
                }
            }

            //check if polygon should be considered beach
            //if polygon apart of outter circle and is land and touches water
            for (int insideIdx: insideCents){
                if (insideIdx == p.getCentroidIdx() && is_land && touches_water){
                    beach = true;
                }
            }

            //check if the polygon already has the property key "biome"
            for (Structs.Property prop: properties){
                if (((prop.getKey()).equals("biome") == false) || ((prop.getKey()).equals("rgb_color") == false)){
                    newProp.add(prop);
                }
            }
            //assign the property "ocean" and colour to polygons
            if (beach == true){
                Property addBeach = Property.newBuilder().setKey("biome").setValue("beach").build();
                Property addBeachColour = colour.addColour("beach");
                newProp.add(addBeach);
                newProp.add(addBeachColour);
                beachPolys.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else{
                beachPolys.add(p);
            }
        }

        return beachPolys;
    }
}
