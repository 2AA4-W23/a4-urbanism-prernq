package pathGenerator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class City {
    int numOfCities;
    Mesh aMesh;
    Random random = new Random();

    //create city object
    public City(int numOfCities, Mesh mesh){
        this.numOfCities = numOfCities;
        this.aMesh = mesh;

    }

    //initialize capital and city colours
    Property capital = Property.newBuilder().setKey("rgb_color").setValue("100,100,100").build();
    Property city = Property.newBuilder().setKey("rgb_color").setValue("75,75,75").build();

    //creates duplicate mesh to update
    public Mesh.Builder duplicate(){
       
        //adds all properties to duplicate
        Mesh.Builder duplicate = Mesh.newBuilder();
        duplicate.addAllVertices(aMesh.getVerticesList());
        duplicate.addAllPolygons(aMesh.getPolygonsList());
        duplicate.addAllSegments(aMesh.getSegmentsList());
        
        return duplicate;
    }

    public void createCity(){
    
        Mesh.Builder duplicate = duplicate();
        int numPolys = aMesh.getPolygonsCount();

        for(int i = 0; i <= (numOfCities - 1 );){

            Polygon p = aMesh.getPolygons(random.nextInt(numPolys));
            List<Property> properties = p.getPropertiesList();

            for (Property prop: properties){
                //biome is not water
                if ((prop.getKey()).equals("biome") == true) {
                    if(!((prop.getValue()).equals("lake")) && !((prop.getValue()).equals("ocean")) && !((prop.getValue()).equals("aquifer"))) {
                            i++; //updates for loop
                    }
                }
            }
            
            //updates the city
            Vertex.Builder new_city = Vertex.newBuilder(aMesh.getVertices(p.getCentroidIdx()));
            new_city.addProperties(city);
            duplicate.setVertices(p.getCentroidIdx(), new_city);
        
        }
        
        this.aMesh = duplicate.build();
          
    }

    public List<Polygon> createCapital(){

        Mesh.Builder duplicate = duplicate();
        int numPolys = aMesh.getPolygonsCount();
        Polygon poly = aMesh.getPolygons(random.nextInt(numPolys));
        Boolean can_be_capital = false;
        List<Property> properties = poly.getPropertiesList();

        //loops until capital city found 
        while(!can_be_capital){
            for (Property prop: properties){
                //biome is not water
                if ((prop.getKey()).equals("biome") == true) {
                    if(!((prop.getValue()).equals("lake")) && !((prop.getValue()).equals("ocean")) && !((prop.getValue()).equals("aquifer"))) {
                        can_be_capital = true;
                    }
                }
            }
        }

        Vertex.Builder cap = Vertex.newBuilder(aMesh.getVertices(poly.getCentroidIdx()));
        cap.addProperties(capital);
        duplicate.setVertices(poly.getCentroidIdx(), cap);
        this.aMesh = duplicate.build();

        createCity();

        return aMesh.getPolygonsList();

    }

 }



