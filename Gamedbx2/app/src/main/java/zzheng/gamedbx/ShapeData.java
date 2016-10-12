package zzheng.gamedbx;

/**
 * Created by neng-zheng on 2016/5/31.
 */
public class ShapeData {
    private float[][] points;

    public float[][] getPoints(int n,float x,float y,float r){
        points=new float[n][4];
        float ca=0;
        float aiv=360/n;
        float ata=(float)Math.PI/180;
        if (n%2==0){
            ca=90-aiv/2;
            for (int i=0;i<n;i++){
                points[i][0]=x+(float)Math.cos(ca * ata) * r;
                points[i][1]=y-(float)Math.sin(ca * ata) * r;
                ca-=aiv;
            }

        }else {
            ca=90;
            for (int i=0;i<n;i++){
                points[i][0]=x+(float)Math.cos(ca * ata) * r;
                points[i][1]=y-(float)Math.sin(ca * ata) * r;
                ca-=aiv;
            }
        }
        return  points;

    }
}
