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

public class Humidity {
    public List<Structs.Polygon> assignHumidity(String location) {
        
        List<Polygon> PolysWithHumidity = new ArrayList<>();
        //assign property for humidity.
        for (int i = 0; i < islandGen.inPolygons.size(); i++) {

            List<Polygon> p_first_degree_neighbours = new ArrayList<>();
            List<Polygon> p_second_degree_neighbours = new ArrayList<>();
            List<Polygon> p_third_degree_neighbours = new ArrayList<>();
            List<Polygon> p_fourth_degree_neighbours = new ArrayList<>();
            
            Boolean is_land = false;
            
            //change how much to multiply by based on location biome etc canada vs japan
            //first degree neighbours add humidity tiles*3
            //second degree neighbours add humidity of tiles*2
            //third degree neighbours add humidity of tiles*1
            //fourth degree neighbours add humidity of tiles*0.5
            int num_of_1dwater = 0;
            int num_of_2dwater = 0;
            int num_of_3dwater = 0;
            int num_of_4dwater = 0;
            
            double humidity = 0;
            
            Structs.Polygon p = islandGen.inPolygons.get(i);
            List<Integer> p_neighbours_idxs = p.getNeighborIdxsList();

            //creates a list of first degree neighbour polygons
            for(int neighbour_idx : p_neighbours_idxs){
                Polygon neighbour_poly = islandGen.inPolygons.get(neighbour_idx);
                p_first_degree_neighbours.add(neighbour_poly);
            }

            //creates list of second degree neighbours
            for(Polygon neighbour: p_first_degree_neighbours){
                List<Integer> neighbour_neighbour_idxs = neighbour.getNeighborIdxsList();
                for(int idx : neighbour_neighbour_idxs){
                    Polygon neighbour_poly = islandGen.inPolygons.get(idx);
                    // if not existing neighbour of main poly and not already a first-degree neighbour
                    if(!p_first_degree_neighbours.contains(neighbour_poly) && !p_second_degree_neighbours.contains(neighbour_poly) && neighbour_poly != p){
                        p_second_degree_neighbours.add(neighbour_poly);
                    }
                }
            }

             //creates list of third degree neighbours
             for(Polygon neighbour: p_second_degree_neighbours){
                List<Integer> neighbour_neighbour_idxs = neighbour.getNeighborIdxsList();
                for(int idx : neighbour_neighbour_idxs){
                    Polygon neighbour_poly = islandGen.inPolygons.get(idx);
                    // if not existing neighbour of main poly and not already a first-degree neighbour
                    if(!p_third_degree_neighbours.contains(neighbour_poly) && !p_first_degree_neighbours.contains(neighbour_poly) && !p_second_degree_neighbours.contains(neighbour_poly) && neighbour_poly != p){
                        p_third_degree_neighbours.add(neighbour_poly);
                    }
                }
            }

            //creates list of fourth degree neighbours
            for(Polygon neighbour: p_third_degree_neighbours){
                List<Integer> neighbour_neighbour_idxs = neighbour.getNeighborIdxsList();
                for(int idx : neighbour_neighbour_idxs){
                    Polygon neighbour_poly = islandGen.inPolygons.get(idx);
                    // if not existing neighbour of main poly and not already a first-degree neighbour
                    if(!p_fourth_degree_neighbours.contains(neighbour_poly) && !p_third_degree_neighbours.contains(neighbour_poly) && !p_first_degree_neighbours.contains(neighbour_poly) && !p_second_degree_neighbours.contains(neighbour_poly) && neighbour_poly != p){
                        p_fourth_degree_neighbours.add(neighbour_poly);
                    }
                }
            }

            //checks if current polygon is land
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if ((prop.getKey()).equals("biome") == true){
                    if(((prop.getValue()).equals("forest")) || ((prop.getValue()).equals("taiga")) || ((prop.getValue()).equals("tempdforest")) || ((prop.getValue()).equals("temprforest")) || ((prop.getValue()).equals("savanna")) || ((prop.getValue()).equals("troprforest")) || ((prop.getValue()).equals("desert")) || ((prop.getValue()).equals("subdesert")) || ((prop.getValue()).equals("tundra"))) {
                        is_land = true;              
                    }
                }
            }

            //checks if neighbouring polygons are water
            for(Polygon neighbour: p_first_degree_neighbours){
                List<Structs.Property> neighbour_properties = neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            num_of_1dwater += 1;              
                        }
                    }else if((prop.getKey()).equals("aquifer") == true){
                        num_of_1dwater += 1;
                    }
                }
            }

            //checks if second degree neighbouring polygons are water
            for(Polygon second_neighbour: p_second_degree_neighbours){
                List<Structs.Property> neighbour2_properties = second_neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour2_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            num_of_2dwater += 1;              
                        }
                    }else if((prop.getKey()).equals("aquifer") == true){
                        num_of_2dwater += 1;
                    }
                }
            }

             //checks if third degree neighbouring polygons are water
             for(Polygon third_neighbour: p_third_degree_neighbours){
                List<Structs.Property> neighbour3_properties = third_neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour3_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            num_of_3dwater += 1;              
                        }
                    }else if((prop.getKey()).equals("aquifer") == true){
                        num_of_3dwater += 1;
                    }
                }
            }

            //checks if fourth degree neighbouring polygons are water
            for(Polygon fourth_neighbour: p_fourth_degree_neighbours){
                List<Structs.Property> neighbour4_properties = fourth_neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour4_properties){
                    if ((prop.getKey()).equals("biome") == true){
                        if((prop.getValue()).equals("ocean") || (prop.getValue()).equals("lake")){
                            num_of_4dwater += 1;              
                        }
                    }else if((prop.getKey()).equals("aquifer") == true){
                        num_of_4dwater += 1;
                    }
                }
            }

            //calculate humidity of polygon
            humidity = (num_of_1dwater*2 + num_of_2dwater*1 + num_of_3dwater*0.5 + num_of_4dwater*0.2);
            int humidity_int =  (int)humidity;

            //check if the polygon already has the property key "humidity"
            for (Structs.Property prop : properties) {
                if (!(prop.getKey()).equals("humidity")) {
                    newProp.add(prop);
                }
            }

            //assign the property "humidity" to polygons
            if (is_land == true){
                Property addHumidity = Property.newBuilder().setKey("humidity").setValue(String.valueOf(humidity_int)).build();
                newProp.add(addHumidity);
                PolysWithHumidity.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else{
                PolysWithHumidity.add(p);
            }

        }

        return PolysWithHumidity;

    }
}
