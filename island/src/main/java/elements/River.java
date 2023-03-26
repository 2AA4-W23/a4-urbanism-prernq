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
    public List<Segment> assignRiverSegments(List<Integer> outsideCircle, List<Integer> insideCircle){
        
        List<Polygon> polysWithRiverSegments = new ArrayList<>();
        int river_start_idx= 100;
        List<Integer> river_segments = new ArrayList<>();
        List<Segment> river_segments_with_properties = new ArrayList<>();
        
        Structs.Segment river_start_segment = islandGen.inSegments.get(river_start_idx);
        int river_start_v1_idx = river_start_segment.getV1Idx();
        int river_start_v2_idx = river_start_segment.getV2Idx();
        Structs.Vertex river_start_v1 = islandGen.inVertices.get(river_start_v1_idx);
        Structs.Vertex river_start_v2 = islandGen.inVertices.get(river_start_v2_idx);


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

        return river_segments_with_properties;
    }
}
