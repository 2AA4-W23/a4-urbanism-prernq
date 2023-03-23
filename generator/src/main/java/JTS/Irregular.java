package JTS;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import static java.lang.Double.compare;
import static java.lang.Math.abs;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

public class Irregular {
    private final int width = 500;
    private final int height = 500;
    public String mode;
    public List<Coordinate> centroids = new ArrayList<>();
    public List<Polygon> polygons = new ArrayList<>();
    public List<Polygon> convexPolys = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<Double>>> polyCoords = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> polyNeighbours = new ArrayList<>(centroids.size());

    public void setCentroids(ArrayList<Double[]> centroid_coords){
        for (Double[] coord : centroid_coords){
            centroids.add(new Coordinate(coord[0],coord[1]));
            System.out.println("Centroid: "+coord[0]+" "+coord[1]);
        }
    }

    public void resetCentroids(){
        centroids.clear();  //clear old list of centroids

        //compute new centroid coordinates based on polygons and add to class variable of list of centroids
        for (Polygon p: polygons){
            Point centroidPoint = p.getCentroid();
            centroids.add(centroidPoint.getCoordinate());
        }
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
    public ArrayList<ArrayList<Integer>> getPolyNeighbours(){
        return polyNeighbours;
    }
    public void convexHull() {
        convexPolys.clear();
        GeometryFactory geomFact = new GeometryFactory();

        for (Polygon p: polygons){
            System.out.println("POLY"+p.getCentroid());
            Coordinate[] coords = p.getCoordinates();
            Point[] points = new Point[(coords.length-1)];

            for (int j=0;j<(coords.length-1); j++){
                System.out.println(coords[j]);
                points[j] = geomFact.createPoint(coords[j]);
            }

            MultiPoint multiPoint = geomFact.createMultiPoint(points);
            Polygon hull = (Polygon) multiPoint.convexHull();
            convexPolys.add(hull);

        }
        setPolyCoords(convexPolys);
    }

    public void setPolyCoords(List<Polygon> polyList){
        polyCoords.clear();

        for (int i = 0; i < polyList.size(); i++){
            Polygon p = polyList.get(i);

            //add all coordinates of each polygon to polyCoords in type double
            Coordinate[] shell = p.getCoordinates();
            polyCoords.add(new ArrayList<ArrayList<Double>>(shell.length));
            for (int j=0; j<shell.length; j++){
                polyCoords.get(i).add(new ArrayList<Double>(2));
                Double x = shell[j].getX();
                Double y = shell[j].getY();

                polyCoords.get(i).get(j).add(0,x);
                polyCoords.get(i).get(j).add(1,y);
            }

        }
    }

    public void voronoiDiagram() {
        //clear old lists so we can append new updated values
        polyCoords.clear();
        polygons.clear();

        GeometryFactory geomFact = new GeometryFactory();
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();

        Envelope clipEnv = new Envelope();
        clipEnv.init((double) 0.0, (double) width, (double) 0.0, (double) height);
        voronoi.setClipEnvelope(clipEnv);

        voronoi.setSites(centroids);

        voronoi.setClipEnvelope(clipEnv);


        Geometry diagram = voronoi.getDiagram(geomFact); //makes the voronoi diagram as a collection of polygons with their respective vertex coordinates

        Geometry boundary = geomFact.createPolygon(new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, height), new Coordinate(width, height), new Coordinate(width, 0), new Coordinate(0, 0)});


        if(diagram instanceof GeometryCollection) { //checks that the diagram was generated correctly

            GeometryCollection diagramGeometries = (GeometryCollection) diagram;


            //loop goes through all geometries (polygons) in the diagram
            for (int i = 0; i < diagramGeometries.getNumGeometries(); i++) {
                //create new polygon and add it to arraylist
                Polygon p = (Polygon) diagramGeometries.getGeometryN(i).intersection(boundary);



                polygons.add(p);
            }
        }

        setPolyCoords(polygons);
    }

    public void delaunayTriangulation(){
        polyNeighbours.clear();
        DelaunayTriangulationBuilder delaunay = new DelaunayTriangulationBuilder();
        GeometryFactory geomFact = new GeometryFactory();
        delaunay.setSites(centroids);

        Geometry triangles = delaunay.getTriangles(geomFact);

        for (Coordinate c: centroids){
            ArrayList<Integer> neighbours = new ArrayList<>();


            for (int i = 0; i < triangles.getNumGeometries(); i++){
                Geometry triangle = triangles.getGeometryN(i);

                for (Coordinate t : triangle.getCoordinates()){
                    if (c.compareTo(t) == 0){

                        for (Coordinate q : triangle.getCoordinates()){
                            if (!(c.equals(q))){

                                for (int j = 0; j < centroids.size(); j++){
                                    if (centroids.get(j).equals(q)){
                                        if (!(neighbours.contains(j))){
                                            neighbours.add(j);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            polyNeighbours.add(neighbours);
        }
    }
}