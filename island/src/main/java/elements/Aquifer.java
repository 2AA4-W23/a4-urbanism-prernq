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

        List<Polygon> PolysWithAquifer = new ArrayList<>();

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

            Property addAquifer = Property.newBuilder().setKey("aquifer").setValue("false").build();
            newProp.add(addAquifer);
            PolysWithAquifer.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
        }

        return PolysWithAquifer;

    }
}
