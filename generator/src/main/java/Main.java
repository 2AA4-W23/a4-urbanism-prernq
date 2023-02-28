import JTS.Irregular;
import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.util.Objects;

public class Main {

    public static String arg1;
    public static String arg2;
    public static int arg3;
    public static int arg4;

    public static void main(String[] args) throws IOException {
        try {
            if (args[1].equals("IR")) {
                arg2 = "irregular";
                if (args.length > 2) {
                    if (args.length == 4) {
                        arg3 = Integer.parseInt(args[2]);
                        arg4 = Integer.parseInt(args[3]);
                    }
                    else if (args.length == 3) {
                        arg3 = Integer.parseInt(args[2]);
                    }
                    else if (args.length > 4) {
                        throw new Exception();
                    }
                }
                else {
                    arg3 = 150;
                    arg4 = 5;
                }
                DotGen generator = new DotGen();
                Mesh myMesh = generator.generateirregular(arg3, arg4);
                MeshFactory factory = new MeshFactory();
                factory.write(myMesh, args[0]);
            }
            else if (args[1].equals("GR")) {
                arg2 = "grid";
                if (args.length > 2) {
                    throw new Exception();
                }
                DotGen generator = new DotGen();
                Mesh myMesh = generator.generategrid();
                MeshFactory factory = new MeshFactory();
                factory.write(myMesh, args[0]);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please enter the arguments correctly! Follow the instructions in the README file.");
        }
    }
}