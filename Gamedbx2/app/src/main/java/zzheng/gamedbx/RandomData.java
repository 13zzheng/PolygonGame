package zzheng.gamedbx;

import java.util.Random;

/**
 * Created by neng-zheng on 2016/5/31.
 */
public class RandomData {
    private  int[] edgestyle;
    private int[] pointvalue;

    public int[] getEdgestyle(int n){
        Random random=new Random();
        edgestyle=new int[n];
        for (int i=0;i<n;i++){
            edgestyle[i]=random.nextInt(2)+1;
        }
//        edgestyle[0]=2;
//        edgestyle[1]=1;
//        edgestyle[2]=2;
//        edgestyle[3]=1;
//        edgestyle[4]=2;
        return edgestyle;
    }

    public int[] getPointvalue(int n){
        Random random=new Random();
        pointvalue=new int[n];
        for (int i=0;i<n;i++){
            pointvalue[i]=random.nextInt(21)-10;
        }
//        pointvalue[0]=-10;
//        pointvalue[1]=3;
//        pointvalue[2]=-9;
//        pointvalue[3]=4;
//        pointvalue[4]=-1;
        return pointvalue;
    }
}
