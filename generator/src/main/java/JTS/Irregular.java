package JTS;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import static java.lang.Math.abs;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

public class Irregular {
    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;
    public String mode;
    public List<Coordinate> centroids = new ArrayList<>();
    public List<Polygon> polygons = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<Double>>> polyCoords = new ArrayList<>();

    public void setCentroids(ArrayList<Double[]> centroid_coords){
        ArrayList<ArrayList<ArrayList<Double>>> polyCoords = new ArrayList<>();
        for (Double[] coord : centroid_coords){
            centroids.add(new Coordinate(coord[0],coord[1]));
        }
        System.out.println("set"+centroids);
    }

    public void resetCentroids(){
        centroids.clear();

        //compute new centroid coordinates based on polygons
        for (Polygon p: polygons){
            Point centroidPoint = p.getCentroid();
            centroids.add(centroidPoint.getCoordinate());
        }

        System.out.println("reset"+ centroids);
    }

    public ArrayList<Double[]> getCentroids(){
        ArrayList<Double[]> centroidsList = new ArrayList<Double[]>();

        for (Coordinate c : centroids){
            Double[] coordinates = {(double) c.getX(), (double) c.getY()};
            centroidsList.add(coordinates);
        }

        return centroidsList;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getPolygonCoords(){
        return polyCoords;
    }

    public void voronoiDiagram() {
/*
        ArrayList<ArrayList<ArrayList<Double>>> polyCoords = new ArrayList<>();
        for (Double[] coord : centroid_coords){
            centroids.add(new Coordinate(coord[0],coord[1]));
        }

 */
/*
        int numPoints = 120;
        Random bag = new Random();


        for (int i = 0; i < numPoints; i++){
            int randX = bag.nextInt(width + 1);
            int randY = bag.nextInt(height + 1);
            //System.out.println("Adding vertex: ("+randX+","+randY+")");
            if(randX <= width && randY <= height){
                randCoords.add(new Coordinate(randX,randY));}
        }

        //System.out.println("coords \n\n"+coords); //outputs list of coords: [(169.0, 354.0, 0.0), (413.0, 376.0, 0.0),...,]
*/
        polyCoords.clear();
        GeometryFactory geomFact = new GeometryFactory();
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        voronoi.setSites(centroids);
        Envelope clipEnv = new Envelope((double) 0.0, (double) width, (double) 0.0, (double) height);
        voronoi.setClipEnvelope(clipEnv);

        Geometry diagram = voronoi.getDiagram(geomFact);
        //System.out.println("polygon collection: \n\n"+polygonCollection);


        if(diagram instanceof GeometryCollection) {

            GeometryCollection geometryCollection = (GeometryCollection) diagram;

            //loop makes 40 polygons and adds them to producePolygons, but i think they have holes
            for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
                //System.out.println("\n\n\n\n"+polygonCollection);
                Polygon p = (Polygon) geometryCollection.getGeometryN(i);



                Coordinate[] shell = p.getCoordinates();
                polyCoords.add(new ArrayList<ArrayList<Double>>(shell.length));


                for (int j=0; j<shell.length; j++){
                    polyCoords.get(i).add(new ArrayList<Double>(2));
                    Double x = shell[j].getX();
                    Double y = shell[j].getY();
                    Double[] point = {(double) x, (double) y};

                    polyCoords.get(i).get(j).add(0,x);
                    polyCoords.get(i).get(j).add(1,y);

                    //System.out.println("\n\nshell: "+shell[j]+"\tpolycoords: ("+polyCoords.get(i).get(j).get(0)+","+polyCoords.get(i).get(j).get(1)+")");
                }
                polygons.add(p);
                //System.out.println("\n\nP"+i+"\t"+polygon); //Output: POLYGON ((956 51.185897435897466, 956 -100.27027027027027, 404 34, 495.17123287671234 155.56164383561645, 956 51.185897435897466))
                // System.out.println(producedPolygons); //ouputs: POLYGON info like above, but all of it in one line
            }
        }


        //return polyCoords;
    }

}
