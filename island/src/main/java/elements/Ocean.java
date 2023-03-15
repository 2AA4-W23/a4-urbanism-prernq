package elements;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

public class Ocean {

    public void assignOcean(){


        //List<Structs.Polygon> test = new ArrayList<>();

        /*
        Property x = Property.newBuilder().setKey("biome").setValue("unassigned").build();
        for (Polygon r : inPolygons){
            Polygon withProperties = Polygon.newBuilder(r).addProperties(x).build();
            test.add(withProperties);
        }

         */



/*
        System.out.println("My mode is"+mode);
        //for mode "lagoon"
        if (mode.equals("lagoon")){
            System.out.println("hey ;)");
            List<Integer> insideCents = shape.circle(200);

            //assign biome property
            for (int i = 0; i < inPolygons.size(); i++){
                Structs.Polygon p = inPolygons.get(i);

                //check if polygon should be considered ocean (not a centroid inside the island radius)
                Boolean ocean = true;
                for (int insideIdx: insideCents){
                    if (insideIdx == p.getCentroidIdx()){
                        ocean = false;
                    }
                }

                //check if the polygon already has the property key "biome"
                List<Structs.Property> oldProps = p.getPropertiesList();
                List<Structs.Property> newProps = new ArrayList<>();
                for (Structs.Property prop: oldProps){
                    if (prop.getKey() != "biome"){
                        newProps.add(prop);
                    }
                }


                //assign the property "ocean" to polygons
                if (ocean == true){
                    //new property list with updated "biome" for "ocean"
                    Structs.Property addProp = Structs.Property.newBuilder().setKey("biome").setValue("ocean").build();
                    newProps.add(addProp);

                    Structs.Polygon.newBuilder(p).clearProperties().addAllProperties(newProps).build();


                    //Polygon addPoly = Polygon.newBuilder().mergeFrom(p).addProperties(addProp).build();
                    //Polygon addPoly = Polygon.newBuilder(p).
                    //inPolygons.set(i, addPoly);
                }
            }
        }

 */




    }



}
