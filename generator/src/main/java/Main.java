import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;

public class Main {

    public static String arg1;
    public static String arg2;

    public static void main(String[] args) throws IOException {

        try {
            if (args.length > 2) {
                if ((args[2].equals("debug")) || (args[2].equals("Debug")) || (args[2].equals("DEBUG"))) {
                    arg1 = "debug";
                }
                else {
                    throw new Exception();
                }
            }
            else if (args.length > 3) {
                throw new Exception();
            }
            else {
                arg1 = "normal";
            }
            DotGen generator = new DotGen(arg1);

            if (args[1].equals("IR")) {
                arg2 = "irregular";
                Mesh myMesh = generator.generateirregular();
                MeshFactory factory = new MeshFactory();
                factory.write(myMesh, args[0]);
            }
            else if (args[1].equals("GR")) {
                arg2 = "grid";
                Mesh myMesh = generator.generategrid();
                MeshFactory factory = new MeshFactory();
                factory.write(myMesh, args[0]);
            }
            else {
                throw new Exception();
            }
        }
        catch(Exception e) {
            System.out.println("Something went wrong! Please enter the mode correctly! Don't enter an argument for normal mode and \"Debug\" for debug mode.");
        }

    }

}
