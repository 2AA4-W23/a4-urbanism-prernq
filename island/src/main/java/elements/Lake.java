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

public class Lake {
    Colour colour = new Colour();
    Seed seed = new Seed();
    public List<Polygon> assignLakes(List<Integer> outsideCircle, List<Integer> insideCircle, int num_lakes){
        
        List<Structs.Polygon> lakePolys = new ArrayList<>();
        
        for (int j = 0; j < islandGen.inPolygons.size(); j++){
            lakePolys.add(islandGen.inPolygons.get(j));
        }
        
        for(int i=0; i < num_lakes; i++){


            int randomindex = 0;
            Polygon p = lakePolys.get(seed.rand.nextInt(lakePolys.size()));
            Boolean can_be_lake = false;
            
            while(!can_be_lake){
                randomindex = seed.rand.nextInt((lakePolys.size()));
                p = lakePolys.get(randomindex);
                List<Structs.Property> properties = p.getPropertiesList();
                for (Structs.Property prop: properties){
                    if ((prop.getKey()).equals("biome") == true) {
                        if(!((prop.getValue()).equals("lake")) && !((prop.getValue()).equals("ocean")) && !((prop.getValue()).equals("aquifer"))) {
                                can_be_lake = true;    
                        }
                    }
                }
            }

            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if (((prop.getKey()).equals("biome") == false) || ((prop.getKey()).equals("rgb_color") == false)){
                    newProp.add(prop);
                }
            }
            if (can_be_lake == true){
                Property addLake = Property.newBuilder().setKey("biome").setValue("lake").build();
                Property addColour = colour.addColour("lake");
                newProp.add(addLake);
                newProp.add(addColour);
                lakePolys.set(randomindex, Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
            }
            else{
                lakePolys.set(randomindex, p);
            }

            
        }
        
        return lakePolys;
    }
}
