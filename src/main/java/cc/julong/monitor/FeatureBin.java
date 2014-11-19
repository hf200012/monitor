package cc.julong.monitor;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangfeng on 2014/10/23.
 */
public class FeatureBin {

    public static void main(String args[]){
        String str = "1|2|3|0.12,12.34,23,34";
        String str1 = str.substring(0,str.lastIndexOf("|"));
        System.out.println(str1);
        String str2 = str.substring(str.lastIndexOf("|")+1);
        System.out.println(str2);

        //readTxt("RANKSVM_6.txt");

    }

    private static List readTxt(String fileName) {
        String filePath = "d:/test/" + fileName;
        BufferedReader reader = null;
        List<Float> datas = new ArrayList<Float>();
        try {
            reader = new BufferedReader(new InputStreamReader( new FileInputStream(filePath)));
            String str = null;
            String featureBin = "";
            while ((str = reader.readLine()) != null) {
                if (str != null && !str.equals("")) {
                    Float data = Float.parseFloat(str);
                    datas.add(data);
                    featureBin += str + ",";
                }
            }
            System.out.println(featureBin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

}
