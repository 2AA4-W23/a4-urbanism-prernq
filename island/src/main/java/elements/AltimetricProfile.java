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

public class AltimetricProfile {
    public Seed seed = new Seed();
    public enum Profiles {
        FLAT, VOLCANO, RANDOM
    }
    public List<Polygon> random(){
        List<Polygon> randomPolys = new ArrayList<>();
        return randomPolys;
    }
    public List<Polygon> volcano (List<Integer> outsideCircle, List<Integer> insideCircle, List<Integer> middleCircle){
        List<Polygon> volcanoPolys = new ArrayList<>();
        List<Polygon> starting_polys = new ArrayList<>();

        for(int j =0; j <islandGen.inPolygons.size(); j++){
            Structs.Polygon poly = islandGen.inPolygons.get(j);
            for(int idx: middleCircle){
                if(poly.getCentroidIdx() == idx){
                    starting_polys.add(poly);
                }
            }
        }

        Polygon p = starting_polys.get(seed.rand.nextInt(1, starting_polys.size()));

        List<Polygon> p_first_degree_neighbours = new ArrayList<>();
        List<Polygon> p_second_degree_neighbours = new ArrayList<>();
        List<Polygon> p_third_degree_neighbours = new ArrayList<>();
        List<Polygon> p_fourth_degree_neighbours = new ArrayList<>();
        
        Boolean is_land = false;
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
        /*
        List<Structs.Property> properties = p.getPropertiesList();
        List<Structs.Property> newProp = new ArrayList<>();
        for (Structs.Property prop: properties){
            if ((prop.getKey()).equals("biome") == true){
                if(((prop.getValue()).equals("forest")) || ((prop.getValue()).equals("taiga")) || ((prop.getValue()).equals("tempdforest")) || ((prop.getValue()).equals("temprforest")) || ((prop.getValue()).equals("savanna")) || ((prop.getValue()).equals("troprforest")) || ((prop.getValue()).equals("desert")) || ((prop.getValue()).equals("subdesert")) || ((prop.getValue()).equals("tundra"))) {
                    is_land = true;              
                }
            }
        }
        */

        
        for(Polygon tile: islandGen.inPolygons){
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            //check if the polygon already has the property key "elevation"
            for (Structs.Property prop : properties) {
                if (!(prop.getKey()).equals("elevation")) {
                    newProp.add(prop);
                }
            }

            Property addVolcanoElevation = Property.newBuilder().setKey("elevation").setValue(String.valueOf(5)).build();
            newProp.add(addVolcanoElevation);
            volcanoPolys.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());

        }
    

        return volcanoPolys;
    }
}
