package com.tsinghua.sec.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XingSQ
 */
public class ShareKeys {

    public static final int KEY_NUM = 4;

    public static final int degree=3;

    private static final ShareKeys _INSTANCE = new ShareKeys();

    public lagrFun fun;

    private ShareKeys() {

    }

    public static ShareKeys getInstance() {
        return _INSTANCE;
    }

    /**
     * 根据要加密的内容和要生成的密钥数生成密钥
     *
     * @param keyNum 要加密的内容
     * @param num    要生成的密钥个数
     * @param degree 解方程所需point的最少个数
     * @return
     */
    public List<PointNum> Producekeys(int keyNum, int num, int degree) {
        List<PointNum> lstResult = new ArrayList<PointNum>();
        if (degree - 1 < 1) return null;
        if (num < degree) return null;
        fun = new lagrFun(degree - 1, keyNum);
        for (int i = 0; i < degree - 1; i++) {
            int n = (int) (Math.random() * 10000);
            fun.InputCoefficient(n);

            //System.out.println(i+1+"系数次: "+n);
        }
        //System.out.println("常数项： "+keyNum);

        boolean bflag = false;
        for (int m = 0; m < num; m++) {
            bflag = false;
            int x = (int) (Math.random() * 100);
            if (x == 0) {
                m--;
                continue;
            }
            for (int i = 0; i < lstResult.size(); i++) {
                if (lstResult.get(i).X == x) {
                    m--;
                    bflag = true;
                    continue;
                }
            }
            if (bflag) continue;
            double y = fun.GetY(x);
            lstResult.add(new PointNum(x, y));
        }

        return lstResult;
    }

    /**
     * 求解密码
     *
     * @param param
     * @return 密码
     */
    public int SolveKey(List<PointNum> param) {
        if (param.size() > fun.intCoefficientNum) {
            double[][] matrix = new double[fun.intCoefficientNum + 1][fun.intCoefficientNum + 2];
            for (int i = 0; i < fun.intCoefficientNum + 1; i++) {
                for (int n = 0, m = fun.intCoefficientNum; n < fun.intCoefficientNum + 2; n++, m--) {
                    matrix[i][n] = Math.pow(param.get(i).X, m);
                    if (m == -1) {
                        matrix[i][n] = param.get(i).Y;
                    }
                }
            }
            DecimalFormat df = new DecimalFormat("#");
            double[] rst = new Equation().caculate(matrix);
            for (int i = 0; i < rst.length; i++) {
                System.out.println(df.format(rst[i]));
            }
            double dPassword = rst[fun.intCoefficientNum];
            String pw = df.format(dPassword);
            System.out.println("the password: " + pw);
            return Integer.valueOf(pw);
        }
        return 0;
    }


    /**
     * 一元n次方程
     *
     * @author XingSQ
     */
    public class lagrFun {
        /**
         * 方程的构造函数
         *
         * @param degree 方程的次数
         * @param con    方程的常数项
         */
        public lagrFun(int degree, int con) {
            intCoefficientNum = degree;
            intConstant = con;
        }

        /**
         * 方程的系数
         *
         * @param c
         */
        public void InputCoefficient(int c) {
            coefficientlist.add(new Integer(c));
        }

        /**
         * 根据x求得y值
         *
         * @param x
         * @return
         */
        public double GetY(int x) {
            double result = intConstant;

            String tmp = String.valueOf(intConstant);
            for (int i = 0; i < coefficientlist.size(); i++) {
                result = result + Math.pow(x, i + 1) * coefficientlist.get(i);
                tmp += "+ x^" + (i + 1) + "*" + String.valueOf(coefficientlist.get(i));
            }
            tmp += " = y";
            System.out.println(tmp);
            return result;
        }

        //方程的系数
        public List<Integer> coefficientlist = new ArrayList<Integer>();
        //方程的最大次数（即是几次方程）
        public int intCoefficientNum;
        //方程的常数项
        public int intConstant;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        ShareKeys a = new ShareKeys();
        //生成密码为“1234”的5分共享密钥至少3个共享密钥凑在一起才能解出“1234”密码 ，blist是五个共享密钥
        List<PointNum> blist = a.Producekeys(1234, 5, 3);
        //用blist中的共享密钥求解密码
        blist.remove(0);
        blist.remove(0);
//		blist.remove(0);
//		blist.remove(0);
//		blist.remove(0);
        int dPassword = a.SolveKey(blist);
        System.out.println(dPassword);
    }
}


