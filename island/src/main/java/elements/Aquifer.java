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

public class Aquifer {

    public List<Structs.Polygon> assignAquifer() {

        Random rand = new Random();

        //will later be determined by a command line argument. int = 10 for now.
        int numAquifers = 25;
        ArrayList<Integer> randomAquifers = new ArrayList<>();

        List<Polygon> PolysWithAquifer = new ArrayList<>();

        //get the indices of the random aquifers.
        for (int i = 0; i < numAquifers; i++) {
            int randomPoly = rand.nextInt(((islandGen.inPolygons.size()) - 0) + 1) + 0;
            randomAquifers.add(randomPoly);
        }

        //assign property "true" or "false" for aquifer.
        for (int i = 0; i < islandGen.inPolygons.size(); i++) {

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if the polygon already has the property key "aquifer"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop : properties) {
                if (!(prop.getKey()).equals("aquifer")) {
                    newProp.add(prop);
                }
            }

            if (randomAquifers.contains(i)) {
                Property addAquifer = Property.newBuilder().setKey("aquifer").setValue("true").build();
                newProp.add(addAquifer);
            }
            else {
                Property addAquifer = Property.newBuilder().setKey("aquifer").setValue("false").build();
                newProp.add(addAquifer);
            }
            PolysWithAquifer.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
        }

        return PolysWithAquifer;

    }
}
