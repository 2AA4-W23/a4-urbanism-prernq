import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;

public class Main {

    public static String arg1;

    public static void main(String[] args) throws IOException {

        try {
            if (args.length > 1) {
                if ((args[1].equals("debug")) || (args[1].equals("Debug")) || (args[1].equals("DEBUG"))) {
                    arg1 = "debug";
                }
                else {
                    throw new Exception();
                }
            }
            else if (args.length > 2) {
                throw new Exception();
            }
            //if no arguments are present, strategies are automatically set to "combo".
            else {
                arg1 = "normal";
            }

            DotGen generator = new DotGen(arg1);
            Mesh myMesh = generator.generategrid();
            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, args[0]);

        }
        catch(Exception e) {
            System.out.println("Something went wrong! Please enter the mode correctly! Don't enter an argument for normal mode and \"Debug\" for debug mode.");
        }


    }

}
