package elements;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Double.compare;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import islandGenerator.islandGen;


public class Properties {


    public Polygon addPropertyP(Polygon poly, String key, String value){
        Polygon updatedPoly;
        boolean contains = false;


        List<Property> updatedProperties = new ArrayList<>();

        for (Property prop: poly.getPropertiesList()){
            if (prop.getKey().equals(key)){
                updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
                contains = true;
            }
            else{
                updatedProperties.add(Property.newBuilder().setKey(prop.getKey()).setValue(prop.getValue()).build());
            }
        }

        if (contains == false){
            updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
        }

        updatedPoly = Polygon.newBuilder(poly).clearProperties().addAllProperties(updatedProperties).build();


        return updatedPoly;
    }

    public Segment addPropertyS(Segment seg, String key, String value){
        Segment updatedSeg;
        boolean contains = false;

        List<Property> updatedProperties = new ArrayList<>();

        for (Property prop: seg.getPropertiesList()){
            if (prop.getKey().equals(key)){
                updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
                contains = true;
            }
            else{
                updatedProperties.add(Property.newBuilder().setKey(prop.getKey()).setValue(prop.getValue()).build());
            }
        }

        if (contains == false){
            updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
        }

        updatedSeg = Segment.newBuilder(seg).clearProperties().addAllProperties(updatedProperties).build();


        return updatedSeg;
    }

    public Vertex addPropertyV(Vertex vert, String key, String value){
        Vertex updatedVert;
        boolean contains = false;


            List<Property> updatedProperties = new ArrayList<>();

            for (Property prop: vert.getPropertiesList()){
                if (prop.getKey().equals(key)){
                    updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
                    contains = true;
                }
                else{
                    updatedProperties.add(Property.newBuilder().setKey(prop.getKey()).setValue(prop.getValue()).build());
                }
            }

            if (contains == false){
                updatedProperties.add(Property.newBuilder().setKey(key).setValue(value).build());
            }

            updatedVert = Vertex.newBuilder(vert).clearProperties().addAllProperties(updatedProperties).build();


        return updatedVert;
    }
}
