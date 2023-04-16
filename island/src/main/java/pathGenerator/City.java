package pathGenerator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import elements.Properties;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import islandGenerator.islandGen;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class City {

    int numOfCities;
    Mesh aMesh;
    Random random = new Random();

    //create city object
    public City(int numOfCities){
        this.numOfCities = numOfCities;

    }


    public List<Vertex> assignCities(){
    
        int numPolys = islandGen.inPolygons.size();
        List<Structs.Polygon> cityPolys = new ArrayList<>();
        List<Structs.Vertex> cityVertices = new ArrayList<>();
        Boolean can_be_capital = false;

        for (int j = 0; j < islandGen.inPolygons.size(); j++){
            cityPolys.add(islandGen.inPolygons.get(j));
        }

        for(int k = 0; k < islandGen.inCentroids.size(); k++){
            cityVertices.add(islandGen.inCentroids.get(k));
        }

        //creates capital
        int capIdx = random.nextInt((numPolys));
        Polygon cap = cityPolys.get(capIdx);
        while(!can_be_capital){
            capIdx = random.nextInt((numPolys));
            cap = cityPolys.get(capIdx);
            List<Property> cap_properties = cap.getPropertiesList();
            
            for (Property prop: cap_properties){
                //biome is not water
                if ((prop.getKey()).equals("biome") == true) {
                    if(!((prop.getValue()).equals("lake")) && !((prop.getValue()).equals("ocean")) && !((prop.getValue()).equals("aquifer")) && !((prop.getValue().equals("city")))) {
                            can_be_capital = true;
                    }
                }
            }

        }
        List <Property> capitalProps = new ArrayList<>();
        int capCentIdx = cap.getCentroidIdx();
        
        //for vertex
        Property capitalColour = Property.newBuilder().setKey("rgb_color").setValue("255, 120, 0").build();
        Property cityThickness = Property.newBuilder().setKey("thickness").setValue("4").build();
        capitalProps.add(capitalColour);
        capitalProps.add(cityThickness);
        //for poly
        Property isCity = Property.newBuilder().setKey("city").setValue("yes").build();
        
        Vertex cap_v = cityVertices.get(capCentIdx);


        cityVertices.set(capCentIdx, Vertex.newBuilder(cap_v).clearProperties().addAllProperties(capitalProps).build());
        cityPolys.set(capIdx, Polygon.newBuilder(cap).addProperties(isCity).build());
        

        for(int i = 0; i <= (numOfCities - 2 );){

            int randomidx = random.nextInt(numPolys);
            Polygon p = cityPolys.get(randomidx);
            List<Property> properties = p.getPropertiesList();

            for (Property prop: properties){
                //biome is not water
                if ((prop.getKey()).equals("biome") == true) {
                    if(!((prop.getValue()).equals("lake")) && !((prop.getValue()).equals("ocean")) && !((prop.getValue()).equals("aquifer")) && !((prop.getValue().equals("city")))) {
                            i++; //updates for loop
                    }
                }
            }
            
            //updates the city
            List<Structs.Property> newProp = new ArrayList<>();
            int cityCentIdx = p.getCentroidIdx();
            Property cityColour = Property.newBuilder().setKey("rgb_color").setValue("255,75,0").build();
            //Property isCity = Property.newBuilder().setKey("city").setValue("true").build();
            Vertex v = cityVertices.get(cityCentIdx);

            newProp.add(cityColour);
            newProp.add(cityThickness);

            cityVertices.set(cityCentIdx, Vertex.newBuilder(v).clearProperties().addAllProperties(newProp).build());
            cityPolys.set(randomidx, Polygon.newBuilder(p).addProperties(isCity).build());

        
        }
        return cityVertices;
          
    }


 }



