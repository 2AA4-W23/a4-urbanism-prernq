package main;

import elements.*;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import java.awt.Color;

import java.awt.*;
import java.util.List;

// This class is for any functions relating to editing or testing the properties of polygons.
public class Polygon {

    public void setColour() {
    }

    public Color getColour(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        /*if (val == null)
            return Colour.BLACK;*/
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    public void setAltProf() {
    }

    public AltimetricProfile getAltProf(List<Property> properties) {
        return new AltimetricProfile();
    }

    public void setAquifer() {
    }

    public Aquifer getAquifer(List<Property> properties) {
        return new Aquifer();
    }

    public void setBiome() {
    }

    public Biome getBiome(List<Property> properties) {
        return new Biome();
    }

    public void setElevation() {
    }

    public Elevation getElevation(List<Property> properties) {
        return new Elevation();
    }

    public void setHumidity() {
    }

    public Humidity getHumidity(List<Property> properties) {
        return new Humidity();
    }

    //whether one of the polygon's segments is a river
    public boolean nextToRiver() {
        return false;
    }

    public void setSoilAbsoprtion() {
    }

    public SoilAbsorption getSoilAbsorption(List<Property> properties) {
        return new SoilAbsorption();
    }

    public void setTemperature() {
    }

    public Temperature getTemperature(List<Property> properties) {
        return new Temperature();
    }
}
