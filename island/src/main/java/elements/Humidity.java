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
    public List<Structs.Polygon> assignHumidity() {

        List<Polygon> PolysWithHumidity = new ArrayList<>();

        //assign property for humidity.
        for (int i = 0; i < islandGen.inPolygons.size(); i++) {

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if the polygon already has the property key "humudity"
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop : properties) {
                if (!(prop.getKey()).equals("humidity")) {
                    newProp.add(prop);
                }
            }

            Property addHumidity = Property.newBuilder().setKey("humidity").setValue(String.valueOf(0)).build();
            newProp.add(addHumidity);

            PolysWithHumidity.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
        }

        return PolysWithHumidity;

    }
}
