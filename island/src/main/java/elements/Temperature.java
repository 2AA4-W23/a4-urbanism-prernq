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

public class Temperature {

    public List<Polygon> assignTemperature(int sealeveltemp) {

        List<Polygon> PolysWithTemperature = new ArrayList<>();
        int elevation = 0;

        //assign temperature property based on elevation and sea level temp
        for (int i = 0; i < islandGen.inPolygons.size(); i++) {

            Structs.Polygon p = islandGen.inPolygons.get(i);

            //check if the polygon already has the property key "temperature" and get elevation value.
            List<Structs.Property> properties = p.getPropertiesList();
            List<Structs.Property> newProp = new ArrayList<>();
            for (Structs.Property prop: properties){
                if (!(prop.getKey()).equals("temperature")){
                    newProp.add(prop);
                }
                if ((prop.getKey()).equals("elevation")) {
                    elevation = Integer.parseInt(prop.getValue());
                }
            }

            int polyTemp = sealeveltemp + elevation;

            Property addTemperature = Property.newBuilder().setKey("temperature").setValue(String.valueOf(polyTemp)).build();
            newProp.add(addTemperature);
            PolysWithTemperature.add(Polygon.newBuilder(p).clearProperties().addAllProperties(newProp).build());
        }

        return PolysWithTemperature;

    }


}
