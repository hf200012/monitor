package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zhangfeng on 2014/10/27.
 */
public class TestYue {

    public static void main(String args[]) {
        args = new String[]{"1","24"};
        int i = Integer.parseInt(args[0]);
        int j = Integer.parseInt(args[1]);
        int sum = 0;
        for(;i < j ;i++){
            sum += i;
        }
        System.out.println(sum);

    }

    public static void yueSeFu(int totalNum, int countNum) {
        // 初始化人数
        List<Integer> start = new ArrayList<Integer>();
        for (int i = 1; i <= totalNum; i++) {
            start.add(i);
        }
        //从第K个开始计数
        int k = 0;
        while (start.size() > 0) {
            k = k + countNum;
            //第m人的索引位置
            k = k % (start.size()) - 1;
            // 判断是否到队尾
            if (k < 0) {
                System.out.println(start.get(start.size() - 1));
                start.remove(start.size() - 1);
                k = 0;
            } else {
                System.out.println(start.get(k));
                start.remove(k);
            }
        }
    }
}
