package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

public class GraphicRenderer {
    private final int width = 500;
    private final int height = 500;

    //private static final int THICKNESS = 10;
    public void render(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

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

            int thickness = extractThickness(s.getPropertiesList());

            // Line2D line = new Line2D.Double(x1, y1, x2, y2);

            Color old = canvas.getColor();
            canvas.setColor(extractColor(s.getPropertiesList()));
            Stroke segstroke = new BasicStroke(thickness);
            canvas.setStroke(segstroke);

            canvas.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            //canvas.drawLine((int) vertex1.getX(), (int) vertex1.getY(), (int) vertex2.getX(), (int) vertex2.getY());

            // previous way to draw segments, when multiple vertices and segments were being created
            /*
            if((int)vertex1.getX() == (int)vertex2.getX() || (int) vertex1.getY() == (int) vertex2.getY()){
                canvas.drawLine((int) vertex1.getX(), (int) vertex1.getY(), (int) vertex2.getX(), (int) vertex2.getY());}*/
                
            canvas.setColor(old);
            canvas.setStroke(stroke);
        }

        // draw vertices
        for (Vertex v: aMesh.getVerticesList()) {
            int thickness = extractThickness(v.getPropertiesList());
            double v_xcoord = v.getX();
            double v_ycoord = v.getY();
            boolean draw_vertex = true;

            // for cropping to dimensions
            if(v_xcoord > width){
                draw_vertex = false;
            }
            if(v_ycoord > height){
                draw_vertex = false;
            }
            if(draw_vertex){
                double centre_x = v_xcoord - (thickness/2.0d);
                double centre_y = v_ycoord - (thickness/2.0d);
                Color old = canvas.getColor();
                canvas.setColor(extractColor(v.getPropertiesList()));
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
                System.out.println(p.getValue());
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
                System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return 3;
        int thickness = Integer.parseInt(val);
        return thickness;
    }
}
