import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by nicks on 4/6/2018.
 */
public class ThreeChickensSolver {
    public static void main(String[] args){
        //System.out.println("Hello World" + args[0] + args[1]+ args[2] + args[3]);

        int[] initialInt = new int[6];
        int[] goalInt = new int[6];

        depthFirst df = new depthFirst();

        //initial array
        initialInt = ConvertDriver(args[0]);

        int i=0;
        /*
        while(i<initialInt.length) {

            System.out.println(initialInt[i]);
            i++;
        }
        */

        //goal array
        goalInt = ConvertDriver(args[1]);

        i=0;
        /*
        while(i<goalInt.length) {
            System.out.println(goalInt[i]);
            i++;
        }
        */

        //read testStart1.txt into java

        //read testGoal1.txt into java

        //mode setter
        if (args[2].equals("dfs")){
            //System.out.println("wow");
            df.depthSolution(initialInt, goalInt);
        }
        //file output

    }

    public static String[] fileRead(String fileName){

        String[] Arr = new String[6];

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            //print contents
            String str;
            int i=0;
            while((str = br.readLine()) != null){
                Arr[i] = str;
                i++;
            }
            br.close();
        }
        //catch error from testStart1.txt reading
        catch(IOException e){
            System.out.println("Error: \n\n" + e);
        }
        return Arr;
    }

    public static String[] stringSplit(String[] toSplit){
        String[] splitArr = new String[6];
        String[] splitArrStr = toSplit[0].split(",");
        String[] splitArrStr2 = toSplit[1].split(",");

        splitArr[0] = splitArrStr[0];
        splitArr[1] = splitArrStr[1];
        splitArr[2] = splitArrStr[2];
        splitArr[3] = splitArrStr2[0];
        splitArr[4] = splitArrStr2[1];
        splitArr[5] = splitArrStr2[2];

        return splitArr;
    }

    public static int[] turnToInt(String strToTransform[]){
    int[] intArr=new int[6];
    int i=0;
    while(i<strToTransform.length){
        intArr[i] = Integer.parseInt(strToTransform[i]);
        i++;
    }

    return intArr;
    }
    public static int[] ConvertDriver(String fileToRead){
        String[] inputStr = new String[6];
        int[] inputInt = new int[6];

        inputStr = fileRead(fileToRead);
        inputStr = stringSplit(inputStr);
        inputInt = turnToInt(inputStr);

        return inputInt;
    }
}
