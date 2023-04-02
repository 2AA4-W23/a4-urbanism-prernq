package elements;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Double.compare;
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

import ca.mcmaster.cas.se2aa4.a2.io.Structs;


public class River {
    private Colour colour = new Colour();
    private Seed seed = new Seed();
    private Properties properties = new Properties();

    private List<Vertex> currVerts = new ArrayList<>();
    private List<Vertex> nextVerts = new ArrayList<>();
    private List<Segment> segmentsWithRivers = new ArrayList<>();
    private Property addColour = colour.addColour("lake");

    public void checkAndAdd(Vertex curr, Vertex next, int segIdx){
        int currElev = -5;
        int nextElev = -5;
        for (Property pCurr: curr.getPropertiesList()){
            if (pCurr.getKey().equals("elevation")){
                currElev = Integer.valueOf(pCurr.getValue());

            }
        }
        for (Property pNext: next.getPropertiesList()){
            if (pNext.getKey().equals("elevation")){
                nextElev = Integer.valueOf(pNext.getValue());
            }
        }

        if (islandGen.oceanSegs.contains(islandGen.inSegments.get(segIdx))){
            System.out.println("you reached the ocean");
        }
        else if (currElev > nextElev){
            System.out.println("segment added");
            nextVerts.add(next);

            Segment newSeg = properties.addPropertyS(segmentsWithRivers.get(segIdx), "river", "true");
            newSeg = properties.addPropertyS(newSeg, "thickness", "4");
            newSeg = properties.addPropertyS(newSeg, addColour.getKey(), addColour.getValue());

            segmentsWithRivers.remove(segIdx);
            segmentsWithRivers.add(segIdx, newSeg);
        }
        else{
            System.out.println("Im not going anywhere");
        }



    }

    public List<Segment> assignRiverSegments(List<Integer> outsideCircle, List<Integer> insideCircle, int num_of_rivers, int[] riverStart){

        //rivers work by getting the list of all segments and checking the elevation associated with the first polygon they connect
        //gets a random segment to start with and generates vertexes associated with it
        //uses the list of polygons and gets the first polygon the segment is associated with to check if the river is on land
        //if the polygon is land, it will add this segment to the list of river segments or it should just change the properties, color and thickness, associated with the segment
        //it checks the neighboring polygons to find the lowest polygon that shares the segment vertexes to continue the river
        //if there is no polygon to continue to, makes the current polygon a lake, if it reaches a body of water should stop.



        /*
        List<Integer> river_segments = new ArrayList<>();
        List<Segment> river_segments_with_properties = new ArrayList<>();


        int river_start_idx = 0;
        river_start_idx = seed.rand.nextInt((islandGen.inSegments.size()));
        Structs.Segment river_start_segment = islandGen.inSegments.get(river_start_idx);

        
        int river_start_v1_idx = river_start_segment.getV1Idx();
        int river_start_v2_idx = river_start_segment.getV2Idx();
        Structs.Vertex river_start_v1 = islandGen.inVertices.get(river_start_v1_idx);
        Structs.Vertex river_start_v2 = islandGen.inVertices.get(river_start_v2_idx);

         */




        for(Segment s: islandGen.inSegments){
            //List<Structs.Property> props = s.getPropertiesList();

            /*
            List<Structs.Property> newProp = new ArrayList<>();
            //check if the polygon already has the property key "biome"
            for (Structs.Property prop: properties){
                if (((prop.getKey()).equals("river") == false) || ((prop.getKey()).equals("rgb_color") == false)){
                    newProp.add(prop);
                }
            }

             */
            //assign the property "river" to false for all segments
            //Property addRiver = Property.newBuilder().setKey("river").setValue("false").build();


            //Property addThickness = Property.newBuilder().setKey("Thickness").setValue("0").build();

            /*
            newProp.add(addRiver);
            newProp.add(addColour);
            newProp.add(addThickness);
            segmentsWithRivers.add(Segment.newBuilder(s).clearProperties().addAllProperties(newProp).build());
             */
            if (!(islandGen.oceanSegs.contains(s))) {
                s = properties.addPropertyS(s, addColour.getKey(), "0,0,0");
                s = properties.addPropertyS(s, "river", "false");
                s = properties.addPropertyS(s, "thickness", "1");
            }

            segmentsWithRivers.add(s);
        }

        for (int i = 0; i < riverStart.length; i++){
            System.out.println("NEW VERTEX");
            System.out.println(riverStart[i]);

            Vertex start = islandGen.inVertices.get(riverStart[i]);
            //Property vertColour = Property.newBuilder().setKey("rgb_color").setValue("0,0,255").build();
            start = properties.addPropertyV(start, "rgb_color", "255,0,0");
            start = properties.addPropertyV(start, "thickness", "7");
            islandGen.inVertices.remove(riverStart[i]);
            islandGen.inVertices.add(riverStart[i], start);

            currVerts.clear();
            nextVerts.clear();

            currVerts.add(start);

            do{
                System.out.println("going through currVerts "+currVerts.size());

                for (Vertex curr: currVerts) {
                    int segIdx = 0;

                    for (Segment s : islandGen.inSegments) {
                        Vertex next;
                        Vertex v1 = islandGen.inVertices.get(s.getV1Idx());

                        Vertex v2 = islandGen.inVertices.get(s.getV2Idx());


                        if ((Double.compare(curr.getX(), v1.getX()) == 0) && (Double.compare(curr.getY(), v1.getY()) == 0)) {
                            System.out.println("match to v1");
                            next = v2;
                            checkAndAdd(curr, next, segIdx);
                        } else if ((Double.compare(curr.getX(), v2.getX()) == 0) && (Double.compare(curr.getY(), v2.getY()) == 0)) {
                            next = v1;
                            System.out.println("match to v2");
                            checkAndAdd(curr, next, segIdx);
                        }

                        segIdx++;
                    }
                }

                currVerts.clear();
                currVerts.addAll(nextVerts);
                nextVerts.clear();

                System.out.println("at end of currVerts "+currVerts.size());

            }while (!(currVerts.isEmpty()));
        }

        return segmentsWithRivers;



        /*

        //this doesnt work needs to be changed
        for (int i = 0; i < islandGen.inPolygons.size(); i++){

            Structs.Polygon p = islandGen.inPolygons.get(i);
            List<Integer> polygon_segment_idxs = p.getSegmentIdxsList();
            List<Integer> p_neighbours_idxs = p.getNeighborIdxsList();
            List<Polygon> p_neighbours = new ArrayList<>();
            Boolean is_land = false;
            Boolean is_lake =  false;
            Boolean it_touches_water = false;
            Boolean start_river = false;

            //check if polygon should be considered lake
            for (int insideIdx: insideCircle){
                if (insideIdx == p.getCentroidIdx()){
                    is_lake = true;
                }
            }

            //checks if polygon should be considered land
            // if centroid inside outtercircle but not inside inner circle
            for (int outsideIdx: outsideCircle){
                if(outsideIdx == p.getCentroidIdx() && is_lake == false){
                    is_land = true;
                }
            }

            for(int seg: polygon_segment_idxs){
                if(seg == river_start_idx){
                    if(is_land){
                        start_river = true;
                        river_segments.add(seg);

                    }
                }
            }

            Boolean shares_segment = false;
            //creates list of neighbour polygons from neighbour index list
            for(int neighbour_idx : p_neighbours_idxs){
                Polygon neighbour_poly = islandGen.inPolygons.get(neighbour_idx);
                List<Integer> neighbour_poly_segments = neighbour_poly.getSegmentIdxsList();
                for(int segIdx: neighbour_poly_segments){
                    if(segIdx == river_start_idx){
                        shares_segment = true;
                    }
                }
                if(shares_segment){
                    p_neighbours.add(neighbour_poly);
                }
            }
            
            
            Polygon lowest_neighbour = p;
            int neighbour_elevation_int=100;
            int lowest_neighbour_elevation_int=100;
            //checks if neighbouring polygons are ocean or lake
            for(Polygon neighbour: p_neighbours){
                List<Structs.Property> neighbour_properties = neighbour.getPropertiesList();
                for(Structs.Property prop : neighbour_properties){
                    if ((prop.getKey()).equals("elevation") == true){
                        String neighbour_elevation = prop.getValue();
                        neighbour_elevation_int = Integer.parseInt(neighbour_elevation);
                        break;
                    }
                }
                List<Structs.Property> lowest_properties = lowest_neighbour.getPropertiesList();
                for(Structs.Property lowest_prop: lowest_properties){
                    if ((lowest_prop.getKey()).equals("elevation") == true){
                        String lowest_neighbour_elevation = lowest_prop.getValue();
                        lowest_neighbour_elevation_int = Integer.parseInt(lowest_neighbour_elevation);
    
                    }
                }
                if(neighbour_elevation_int <= lowest_neighbour_elevation_int){
                    lowest_neighbour = neighbour;
                }
            }

            //make p into a lake if its the lowest elevated polygon
            if(p == lowest_neighbour){
                List<Structs.Property> p_properties = p.getPropertiesList();
                
            }

            List<Integer> lowest_neighbour_segment_idxs = lowest_neighbour.getSegmentIdxsList();
            for(int lowest_seg_idx: lowest_neighbour_segment_idxs){
                Structs.Segment lowest_seg = islandGen.inSegments.get(lowest_seg_idx);
                int lowest_seg_v1_idx = lowest_seg.getV1Idx();
                int lowest_seg_v2_idx = lowest_seg.getV2Idx();
                Structs.Vertex lowest_seg_v1 = islandGen.inVertices.get(lowest_seg_v1_idx);
                Structs.Vertex lowest_seg_v2 = islandGen.inVertices.get(lowest_seg_v2_idx);
                if((lowest_seg_v1 == river_start_v1 && lowest_seg_v2 != river_start_v2) || (lowest_seg_v1 == river_start_v2 && lowest_seg_v2 != river_start_v1)){
                    river_segments.add(lowest_seg_idx);
                }

            }
            
            for(int segment_idx: river_segments){
                Structs.Segment river_seg = islandGen.inSegments.get(segment_idx);
                List<Structs.Property> river_seg_properties = river_seg.getPropertiesList();
                for(Structs.Property prop: river_seg_properties){
                    if ((prop.getKey()).equals("rgb_color") == true){
                        Property color = Property.newBuilder().setKey("rgb_color").setValue("75,0,130").build();
                        Property segThickness = Property.newBuilder().setKey("thickness").setValue("2").build();
                        Segment withProperties = Segment.newBuilder(river_seg).addProperties(color).addProperties(segThickness).build();
                        river_segments_with_properties.add(withProperties);
    
                    }
                }

            }
        }

        //return river_segments_with_properties;

         */

    }
}
