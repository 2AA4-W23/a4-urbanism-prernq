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

        //assign biome property
        for (int i = 0; i < islandGen.inPolygons.size(); i++){

            Structs.Polygon p = islandGen.inPolygons.get(i);
            List<Integer> p_neighbours_idxs = p.getNeighborIdxsList();
            List<Polygon> p_neighbours = new ArrayList<>();
            List<Polygon> p_neighbours_of_neighbours = new ArrayList<>();
            Boolean is_land = false;
            Boolean it_touches_water = false;
            Boolean neighbour_touches_water = false;
            Boolean neighbour2_touches_water = false;
            int absorption;

            //creates list of neighbour polygons from neighbour index list
            for(int neighbour_idx : p_neighbours_idxs){
                Polygon neighbour_poly = islandGen.inPolygons.get(neighbour_idx);
                p_neighbours.add(neighbour_poly);
            }

            //creates neighbour index list of neighbours
            for(Polygon neighbour: p_neighbours){
                List<Integer> neighbour_neighbour_idxs = neighbour.getNeighborIdxsList();
                for(int idx : neighbour_neighbour_idxs){
                    Polygon neighbour_poly = islandGen.inPolygons.get(idx);
                    // if not existing neighbour of main poly and not already a first-degree neighbour
                    if(!p_neighbours.contains(neighbour_poly) && !p_neighbours_of_neighbours.contains(neighbour_poly) && neighbour_poly != p){
                        p_neighbours_of_neighbours.add(neighbour_poly);
                    }
                }
            }


            
            
            //checks if current polygon is land by checking if its a forest
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if ((prop.getKey()).equals("biome") == true){
                    if((prop.getValue()).equals("forest")|| (prop.getValue()).equals("beach")){
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
                            it_touches_water = true;              
                        }
                    }
                }
            }

            //checks if second degree neighbouring polygons are ocean or lake
            for(Polygon second_neighbour: p_neighbours_of_neighbours){
                List<Structs.Property> neighbour2_properties = second_neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour2_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            neighbour_touches_water = true;              
                        }
                    }
                }
            }


            //sets soil absorption profile
            /*
            for (int insideIdx: insideCents){
                if (insideIdx == p.getCentroidIdx() && is_land && it_touches_water){
                    absorption = 1;
                }else if(insideIdx == p.getCentroidIdx() && is_land && neighbour_touches_water){
                    absorption = 2;
                }
            }
            */

            //check if the polygon already has the property key "biome"
            for (Structs.Property prop: properties){
                if (((prop.getKey()).equals("soil_absorption") == false) || ((prop.getKey()).equals("rgb_color") == false)){
                    newProp.add(prop);
                }
            }
            //assign the property "ocean" and colour to polygons
            if (it_touches_water){
                Property addSoilAbsorption = Property.newBuilder().setKey("soil_absorption").setValue("1").build();
                //Property addSoilColour = colour.addColour("profile1");
                newProp.add(addSoilAbsorption);
                //newProp.add(addSoilColour);
                polysWithAbsorption.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }else if (neighbour_touches_water){
                Property addSoilAbsorption = Property.newBuilder().setKey("soil_absorption").setValue("2").build();
                //Property addSoilColour = colour.addColour("profile2");
                newProp.add(addSoilAbsorption);
                //newProp.add(addSoilColour);
                polysWithAbsorption.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else{
                Property addSoilAbsorption = Property.newBuilder().setKey("soil_absorption").setValue("0").build();
                //Property addSoilColour = colour.addColour("profile0");
                newProp.add(addSoilAbsorption);
                //newProp.add(addSoilColour);
                polysWithAbsorption.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
        }

        return polysWithAbsorption;
    }
}
