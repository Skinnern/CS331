import java.io.*;

/**
 * Created by nicks on 4/6/2018.
 */
public class ThreeChickensSolver {
    public static void main(String[] args){
        //System.out.println("Hello World" + args[0] + args[1]+ args[2] + args[3]);

        //inital array

        //goal array


        //read testStart1.txt into java
        try {
            FileReader fr = new FileReader(args[0]);
            System.out.println("Grabbed file!");
            BufferedReader br = new BufferedReader(fr);

            //print contents
            String str;
            while((str = br.readLine()) != null){
                System.out.println(str + "\n");
            }
            br.close();
        }
        //catch error from testStart1.txt reading
        catch(IOException e){
            System.out.println("Error: \n\n" + e);
        }


        //read testGoal1.txt into java
        try {
            FileReader fr2 = new FileReader(args[1]);
            System.out.println("Grabbed file!");
            BufferedReader br2 = new BufferedReader(fr2);

            //print contents
            String str2;
            while((str2 = br2.readLine()) != null){
                System.out.println(str2 + "\n");

            }
            br2.close();
        }
        //catch error from testGoal1.txt reading
        catch(IOException e){
            System.out.println("Error: \n\n" + e);
        }


        //mode setter




        //file output





    }
}
