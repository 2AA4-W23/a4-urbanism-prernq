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
        else if (biome == "forest") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("0,204,0").build();
        }
        else if (biome == "beach") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("255,229,204").build();
        }
        else if (biome == "taiga") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("5,102,33").build();
        }
        else if (biome == "tempdforest") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("46,177,83").build();
        }
        else if (biome == "temprforest") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("6,250,162").build();
        }
        else if (biome == "savanna") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("155,224,35").build();
        }
        else if (biome == "troprforest") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("8,249,54").build();
        }
        else if (biome == "desert") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("250,219,7").build();
        }
        else if (biome == "subdesert") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("249,148,24").build();
        }
        else if (biome == "tundra") {
            colour = Property.newBuilder().setKey("rgb_color").setValue("87,234,249").build();
        }

        return colour;
    }
}
