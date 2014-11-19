package test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangfeng on 2014/10/28.
 */
public class TestEnum {
    public enum Light {
        // 利用构造函数传参
        RED (1), GREEN (3), YELLOW (2);

        // 定义私有变量
        private int nCode ;

        // 构造函数，枚举类型只能为私有
        private Light( int _nCode) {
            this . nCode = _nCode;
        }

        @Override
        public String toString() {
            return String.valueOf ( this . nCode );
        }
    }

    public static void main(String args[]){

        Date date = new Date();
      //  date.setTime(1415081872001l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
    }
}
