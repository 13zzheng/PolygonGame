package zzheng.gamedbx;

/**
 * Created by neng-zheng on 2016/6/1.
 */
public class BestResult {
    private int [] pointvalue;
    private int [] edgestyle;
    private int [][][] m;
    private int n;
    private int [][] temp=new int[][]{{0,0},{0,0},{0,1},{1,0},{1,1}};
    private int minf;
    private int maxf;
    private int [][][] p;//记录每条链最优最后合并的边
    private int [][][][][] w;//记录每条链分别取最大最小值时最优最后合并时两条子链取最小还是最大值
    private int[] bestProcess;
    private int bestValue;
    private int bp=1;

    public void setPointvalue(int[] p){
        n=p.length;
        pointvalue=new int[n];
        for (int i=0;i<n;i++){
            pointvalue[i]=p[i];
        }
    }
    public void setEdgestyle(int [] e){
        n=e.length;
        edgestyle=new int[n];
        for (int i=0;i<n;i++){
            edgestyle[i]=e[i];
        }
    }
    public int getBestValue(){
        return bestValue;
    }
    public int[] getBestProcess(){
        return bestProcess;
    }

    //主方法计算最优值和最优解
    public void caculate(){
        m=new int[n][n+1][2];
        p=new int[n][n+1][2];
        w=new int[n][n+1][n][2][2];
        bestProcess=new int[n];
        for (int i=0;i<n;i++){
            m[i][1][0]=pointvalue[i];
            m[i][1][1]=pointvalue[i];
        }
        polyMax();
        for (int i=0;i<n;i++){
            System.out.println("process:"+bestProcess[i]);
        }
    }
    public void minMax(int i,int s,int j){
        int[] e=new int[5];
        int a=m[i][s][0];
        int b=m[i][s][1];
        int r=(i+s)%n;
        int c=m[r][j-s][0];
        int d=m[r][j-s][1];
        if (r==0) {
            r=n;
        }
        if (edgestyle[r - 1] == 1) {
            minf = a + c;
            maxf = b + d;
            w[i][j][s][0][0]=0;
            w[i][j][s][0][1]=0;
            w[i][j][s][1][0]=1;
            w[i][j][s][1][1]=1;
        } else {
            e[1] = a * c;
            e[2] = a * d;
            e[3] = b * c;
            e[4] = b * d;
            minf = e[1];
            maxf = e[1];
            int min=1;
            int max=1;
            for (int k = 2; k < e.length; k++) {
                if (minf > e[k]) {
                    minf = e[k];
                    min=k;
                }
                if (maxf < e[k]) {
                    maxf = e[k];
                    max=k;
                }
            }
            w[i][j][s][0][0]=temp[min][0];
            w[i][j][s][0][1]=temp[min][1];
            w[i][j][s][1][0]=temp[max][0];
            w[i][j][s][1][1]=temp[max][1];
        }
    }

    public void polyMax(){
        for (int j=2;j<=n;j++){
            for (int i=0;i<n;i++){
                for (int s=1;s<j;s++){
                    minMax(i,s,j);
                    if (s==1){
                        m[i][j][0]=minf;
                        m[i][j][1]=maxf;
                        p[i][j][0]=s;
                        p[i][j][1]=s;
                    }
                    if (m[i][j][0]>minf) {
                        m[i][j][0] = minf;
                        p[i][j][0]=s;
                    }
                    if (m[i][j][1]<maxf) {
                        m[i][j][1] = maxf;
                        p[i][j][1]=s;
                    }
                }
            }
        }
        for (int j=1;j<=n;j++){
            for (int i=0;i<n;i++){
                System.out.print("i:"+i+"j:"+j+"   ");
                System.out.print("m:"+m[i][j][0] +" p:"+p[i][j][0]+ " w:"+w[i][j][p[i][j][0]][0][0]+","+w[i][j][p[i][j][0]][0][1]+" xiao  ");
                System.out.println("m:" + m[i][j][1] + " p:" + p[i][j][1]+ " w:"+w[i][j][p[i][j][1]][1][0]+","+w[i][j][p[i][j][1]][1][1]+" da");
                System.out.println();
            }
            System.out.println();
        }
        int temp=m[0][n][1];
        int in=0;
        for (int i=1;i<n;i++){
            if (temp<m[i][n][1]) {
                temp = m[i][n][1];
                in=i;
            }
        }
        bestValue=temp;
        if (in==0){
            bestProcess[0]=n-1;
        }else {
            bestProcess[0]=in-1;
        }
        bp=1;
        makeBestProcess(in,n,1);
        System.out.println("best:"+bestValue+"  i:"+in);
    }

    public void makeBestProcess(int i,int j,int mm){
        if (bp<n) {

                int s = p[i][j][mm];
                int ql = w[i][j][s][mm][0];
                int qr = w[i][j][s][mm][1];
                int r = (i + s) % n;
                if (r == 0) {
                    bestProcess[bp] = n - 1;
                } else {
                    bestProcess[bp] = r - 1;
                }
                if (s != 1) {
                    bp++;
                    makeBestProcess(i, s, ql);
                }
                if (j - s != 1) {
                    bp++;
                    makeBestProcess(r, j - s, qr);
                }
        }
    }
}
