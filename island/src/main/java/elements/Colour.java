package elements;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
public class Colour {

    public Property addColour(String biome) {
        Property colour = null;

        if (biome == "lake") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("0,128,255").build();
        }
        else if (biome == "ocean") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("0,0,204").build();
        }

        return colour;
    }
}
