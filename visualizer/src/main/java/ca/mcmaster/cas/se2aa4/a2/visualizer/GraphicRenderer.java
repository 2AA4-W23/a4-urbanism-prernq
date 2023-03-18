package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphicRenderer {
    private final int width = 500;
    private final int height = 500;
    private static String MODE;

    public void render(Mesh aMesh, Graphics2D canvas, String mode) {
        MODE = mode;
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        // draw polygons
        for (Polygon p : aMesh.getPolygonsList()) {

            List<Integer> segments;

            segments = p.getSegmentIdxsList();

            List<Integer> vertIdxs = new ArrayList<>();
            for (int i : segments) {
                Segment s = aMesh.getSegmentsList().get(i);
                vertIdxs.add(s.getV1Idx());
                vertIdxs.add(s.getV2Idx());
            }

            List<Integer> newVertIdxs = new ArrayList<>();
            for (int i : vertIdxs) {
                if (!(newVertIdxs.contains(i))) {
                    newVertIdxs.add(i);
                }
            }

            ArrayList<Integer> xCoords = new ArrayList<>();
            ArrayList<Integer> yCoords = new ArrayList<>();
            int vertNum = 0;
            for (int i : newVertIdxs) {
                vertNum++;
                Vertex v = aMesh.getVerticesList().get(i);
                xCoords.add((int) v.getX());
                yCoords.add((int) v.getY());
            }

            int[] x = xCoords.stream().mapToInt(i -> i).toArray();
            int[] y = yCoords.stream().mapToInt(i -> i).toArray();

            Color old = canvas.getColor();

            canvas.setColor(extractColor(p.getPropertiesList()));
            canvas.fillPolygon(x, y, vertNum);

            canvas.setColor(old);
        }


        // draw segments
        for (Segment s : aMesh.getSegmentsList()) {
            int vertex1Idx = s.getV1Idx();
            int vertex2Idx = s.getV2Idx();

            Vertex vertex1 = aMesh.getVertices(vertex1Idx);
            Vertex vertex2 = aMesh.getVertices(vertex2Idx);

            double x1 = vertex1.getX();
            double y1 = vertex1.getY();
            double x2 = vertex2.getX();
            double y2 = vertex2.getY();

            //another crop attempt for irregular - seems like an incorrect method (wont work for different widths and heights)

            if(x1 > width){
                x1 = width;
            }if(y1 > height){
                y1 = height;
            }if(x2 > width){
                x2 = width;
            }if(y2 > height){
                y2 = height;
            }

            int thickness;
            Color old = canvas.getColor();
            if (Objects.equals(MODE, "debug")) {
                thickness = 1;
                canvas.setColor(Color.BLACK);
            }
            else {
                thickness = extractThickness(s.getPropertiesList());
                canvas.setColor(extractColor(s.getPropertiesList()));
            }
            Stroke segstroke = new BasicStroke(thickness);
            canvas.setStroke(segstroke);

            canvas.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                
            canvas.setColor(old);
            canvas.setStroke(stroke);
        }

        // draw vertices
        for (Vertex v: aMesh.getVerticesList()) {
            int thickness = 3;
            double v_xcoord = v.getX();
            double v_ycoord = v.getY();

            boolean draw_vertex = true;
            // for cropping to dimensions
            if(v_xcoord > width){
                draw_vertex = false;
            }
            if(v_ycoord > height) {
                draw_vertex = false;
            }

            if(draw_vertex){
                Color old = canvas.getColor();

                if (extractCentroid(v.getPropertiesList()).equals("yes")) {
                    if (Objects.equals(MODE, "debug")) {
                        thickness = 3;
                        canvas.setColor(Color.RED);
                    }
                    else
                    {
                        thickness = extractThickness(v.getPropertiesList());
                        canvas.setColor(extractColor(v.getPropertiesList()));
                    }
                }
                else if (extractCentroid(v.getPropertiesList()).equals("no")) {
                    if (Objects.equals(MODE, "debug")) {
                        thickness = 3;
                        canvas.setColor(Color.BLACK);
                    }
                    else
                    {
                        thickness = extractThickness(v.getPropertiesList());
                        canvas.setColor(extractColor(v.getPropertiesList()));
                    }
                }
                double centre_x = v_xcoord - (thickness/2.0d);
                double centre_y = v_ycoord - (thickness/2.0d);
                Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, thickness, thickness);
                canvas.fill(point);
                canvas.setColor(old);
            }
        }
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    private int extractThickness(List<Property> properties) {
        String val = null;
        for (Property p: properties) {
            if (p.getKey().equals("thickness")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return 3;
        int thickness = Integer.parseInt(val);
        return thickness;
    }

    private String extractCentroid(List<Property> properties) {
        String val = null;
        for (Property p: properties) {
            if (p.getKey().equals("centroid")) {
                System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return "no";
        return val;

    }
}