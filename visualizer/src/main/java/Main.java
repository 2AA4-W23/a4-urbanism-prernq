import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.visualizer.GraphicRenderer;
import ca.mcmaster.cas.se2aa4.a2.visualizer.MeshDump;
import ca.mcmaster.cas.se2aa4.a2.visualizer.SVGCanvas;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static String arg1 = "normal";

    public static void main(String[] args) throws IOException {
        // Extracting command line parameters
        try {
            String input = args[0];
            String output = args[1];
            //testing for the debug command line argument
            if (args.length > 2) {
                if (Objects.equals(args[2], "-X")) {
                    arg1 = "debug";
                }
            }

            // Getting width and height for the canvas
            Structs.Mesh aMesh = new MeshFactory().read(input);
            double max_x = Double.MIN_VALUE;
            double max_y = Double.MIN_VALUE;
            for (Structs.Vertex v: aMesh.getVerticesList()) {
                max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
                max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
            }
            // Creating the Canvas to draw the mesh
            Graphics2D canvas = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));
            GraphicRenderer renderer = new GraphicRenderer();
            // Painting the mesh on the canvas
            renderer.render(aMesh, canvas, arg1);
            // Storing the result in an SVG file
            SVGCanvas.write(canvas, output);
            // Dump the mesh to stdout
            MeshDump dumper = new MeshDump();
            dumper.dump(aMesh);
        }
        catch (Exception e) {
            System.out.println("Enter \"-X\" as the final argument if you want debug mode.");
        }
    }
}
