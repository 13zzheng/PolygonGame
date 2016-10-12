package zzheng.gamedbx;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by neng-zheng on 2016/5/16.
 */
public class MyGameView extends View {

    private final int ADD_RED=1;
    private final int MULT_YELLOW=2;
    private final int NULL=0;
    private final int TEXT_SIZE=40;
    private final int POINT_RADIUS=50;
    private float shapeRadius;//整个多边形圆的半径
    private int checkedPoint1=-1;//被选定的点
    private int checkedPoint2=-1;//被选定的点
    private int result=0; //最终结果
    private int n;//n边形
    private int m=0;//用于最优过程演示
    private boolean bestBegin;//用于控制最优演示的开始
    private boolean showResult;

    private BestResult bestResult;
    private Paint paint;
    private int height;
    private int width;
    private float[][] points;
    private int[] pointvalue;
    private int[] bestProcess;//最优解的路径
    private int bestValue;//最优值
    //状态变量
    private int[] edgestyle; //边的颜色和计算方法
    private boolean [] pointsLink;//边上两个点是否连接(即统一数据)
    private int[] edgeStep;//边的步骤，用于记录历史操作
    private int checkedPointsNumber=0;//选中点的数量，用于控制点的数量不超过2个
    private int steps=0;  //步骤数
    //存储用以撤销的变量
    private int [][]  point_restore; //已消除边的两个点
    private int [][] pointvalue_restore;//已消除边对应的点的值
    private int [] edge_restore;
    private int [] edgeStyle_restore;

    public void setBestBegin(boolean b){
        bestBegin=b;
    }
    public MyGameView(Context context) {
        super(context);

    }
    public MyGameView(Context context,AttributeSet attrs){
        super(context,attrs);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(14);
    }
    public void getHW(){
        height=getHeight();
        width=getWidth();
    }
    //随机设置n边形
    public void setShapeRandom(int n){
        ShapeData shapeData=new ShapeData();
        RandomData randomData=new RandomData();
        bestResult=new BestResult();
        getHW();
        this.n=n;
        float x=width/2;
        float y=height/2;
        shapeRadius=x-100;
        points=shapeData.getPoints(n, x, y, shapeRadius);
        edgestyle=randomData.getEdgestyle(n);
        pointvalue=randomData.getPointvalue(n);
        //传入初始数据求最优
        bestResult.setEdgestyle(edgestyle);
        bestResult.setPointvalue(pointvalue);
        pointsLink=new boolean[n];
        edgeStep=new int[n];
        point_restore=new int[n+1][2];
        pointvalue_restore=new int[n+1][2];
        edge_restore=new int[n+1];
        edgeStyle_restore=new int[n+1];
        caculteBest();
        bestProcess=bestResult.getBestProcess();
        bestValue=bestResult.getBestValue();
        invalidate();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFF000000);
            for (int i = 0; i < n; i++) {
                //画边
                if (edgestyle[i]==ADD_RED) {
                    paint.setColor(Color.RED);
                    paint.setStrokeWidth(14);
                    if (i + 1 < n) {
                        canvas.drawLine(points[i][0], points[i][1], points[i + 1][0], points[i + 1][1], paint);
                    } else {
                        canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
                    }
                } else if (edgestyle[i]==MULT_YELLOW){
                    paint.setColor(Color.YELLOW);
                    paint.setStrokeWidth(14);
                    if (i + 1 < n) {
                        canvas.drawLine(points[i][0], points[i][1], points[i + 1][0], points[i + 1][1], paint);
                    } else {
                        canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
                    }
                }
                if (edgestyle[i]==NULL){
                    paint.setColor(0xFFBFBFBF);
                    paint.setStrokeWidth(3);
                    float x;
                    float y;
                    if (i+1<n){
                        //画消除的边
                        canvas.drawLine(points[i][0], points[i][1], points[i + 1][0], points[i + 1][1], paint);
                        //获得xy
                        x = Math.abs(points[i][0]-points[i+1][0])/2+Math.min(points[i][0],points[i+1][0]);
                        y=Math.abs(points[i][1]-points[i+1][1])/2+Math.min(points[i][1],points[i+1][1]);

                    }else {
                        canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
                        x = Math.abs(points[i][0]-points[0][0])/2+Math.min(points[i][0],points[0][0]);
                        y=Math.abs(points[i][1]-points[0][1])/2+Math.min(points[i][1],points[0][1]);
                    }
                    //画圆圈
                    paint.setColor(Color.GREEN);
                    paint.setStrokeWidth(5);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(x, y, 30, paint);
                    paint.setStyle(Paint.Style.FILL);
                    //画步骤

                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(TEXT_SIZE);
                    Paint.FontMetrics fontMetrics0 = paint.getFontMetrics();
                    float top0 = y - POINT_RADIUS;
                    float bottom0 = y + POINT_RADIUS;
                    float textBaseY0 = top0 + (bottom0 - top0 - fontMetrics0.bottom + fontMetrics0.top) / 2 - fontMetrics0.top;
                    canvas.drawText(edgeStep[i] + "", x, textBaseY0, paint);
                }
                //画点
                if (points[i][2]!=1) {
                    paint.setColor(0xFFFFFFFF);
                }else {
                    paint.setColor(0xFF6699FF);
                }
                canvas.drawCircle(points[i][0], points[i][1], POINT_RADIUS, paint);

                //画文本，点的数值
                if (points[i][2]!=1) {
                    paint.setColor(0xFF6699FF);
                }else {
                    paint.setColor(0xFFFFFFFF);
                }
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(TEXT_SIZE);
                Paint.FontMetrics fontMetrics0 = paint.getFontMetrics();
                float top0=points[i][1]-POINT_RADIUS;
                float bottom0=points[i][1]+POINT_RADIUS;
                float textBaseY0 = top0 + (bottom0 -top0 - fontMetrics0.bottom + fontMetrics0.top) / 2 - fontMetrics0.top;
                canvas.drawText(pointvalue[i]+"",points[i][0],textBaseY0,paint);
                //画最后一条边时重画第一个点
                if (i==n-1){
                    if (points[0][2]!=1) {
                        paint.setColor(0xFFFFFFFF);
                    }else {
                        paint.setColor(0xFF6699FF);
                    }
                    canvas.drawCircle(points[0][0], points[0][1], POINT_RADIUS, paint);
                    if (points[0][2]!=1) {
                        paint.setColor(0xFF6699FF);
                    }else {
                        paint.setColor(0xFFFFFFFF);
                    }
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(TEXT_SIZE);
                    Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                    float top=points[0][1]-POINT_RADIUS;
                    float bottom=points[0][1]+POINT_RADIUS;
                    float textBaseY = top + (bottom -top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                    canvas.drawText(pointvalue[0]+"",points[0][0],textBaseY,paint);
                }
                if (steps==n  && showResult) {
                    paint.setColor(0xFF6699FF);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(TEXT_SIZE+20);
                    String s = "Your result:" + result + "  Best result:" + bestValue;
                    canvas.drawText(s,width/2,height-100,paint);
                    showResult=false;
                }
                if ((steps==0 || steps==1)&& !bestBegin){
                    paint.setColor(0xFFFFFFFF);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(TEXT_SIZE);
                    String s;
                    if (steps==0) {
                         s = "step1: Remove one edge.";
                    }else {
                         s="step2: Caculate now !";
                    }
                    canvas.drawText(s,width/2,100,paint);
                }

            }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (bestBegin){
            bestProcess();
        }else {
            float x = event.getX();
            float y = event.getY();
            pointChecked(x, y);
        }
        return super.onTouchEvent(event);
    }

    public void bestProcess(){
        //第一步为m=0，后序顺序为倒序。
        if (m<=n-1 && bestBegin) {
            if (m == 0) {
                if (bestProcess[m] == n-1) {
                    checkedPoint1 = n-1;
                    checkedPoint2 = 0;
                } else {
                    checkedPoint1 = bestProcess[m];
                    checkedPoint2 = bestProcess[m] + 1;
                }
            } else if (m>=1){
                if (bestProcess[n - m] == n-1) {
                    checkedPoint1 = n - 1;
                    checkedPoint2 = 0;
                } else {
                    checkedPoint1 = bestProcess[n - m];
                    checkedPoint2 = bestProcess[n - m] + 1;
                }
            }
            m++;
            eliminate();
            if (m==n){
                bestBegin=false;
                m=0;
            }
            invalidate();
        }
    }

    //顶点被选中
    public void pointChecked(float x,float y){
        for (int i=0;i<n;i++){
            float sum=0;
            sum=(x-points[i][0])*(x-points[i][0])+(y-points[i][1])*(y-points[i][1]);
            if (sum<POINT_RADIUS*POINT_RADIUS){
                if (points[i][2]==0){
                    if (checkedPointsNumber==0){
                        points[i][2]=1;
                        checkedPoint1=i;
                        checkedPointsNumber++;
                    }else if (checkedPointsNumber==1){ //判断点的左右是否被选中，只有相邻的点才可以被选中。
                        if (i==0){
                            if (points[n-1][2]==1 || points[i+1][2]==1){
                                points[i][2]=1;
                                checkedPoint2=i;
                                checkedPointsNumber++;
                                eliminate();
                            }
                        }else if (i==n-1){
                            if (points[i-1][2]==1 || points[0][2]==1){
                                points[i][2]=1;
                                checkedPoint2=i;
                                checkedPointsNumber++;
                                eliminate();
                            }
                        }else {
                            if (points[i - 1][2] == 1 || points[i + 1][2] == 1) {
                                points[i][2] = 1;
                                checkedPoint2=i;
                                checkedPointsNumber++;
                                eliminate();
                            }
                        }
                    }else if (checkedPointsNumber==2){

                    }
                }else {
                    points[i][2]=0;
                    if (checkedPoint1==i){
                        checkedPoint1=-1;
                    }else {
                        checkedPoint2=-1;
                    }
                    checkedPointsNumber--;
                }
            }
        }
        invalidate();
    }

    //消除操作
    public void eliminate(){

        if (steps==0){
            removeEdge();
        }else {
            caculatePointValue();
            removeEdge();
        }
        steps++;
    }
    public void caculatePointValue(){
        int edge;
        if (Math.abs(checkedPoint1-checkedPoint2)==n-1){
            edge=n-1;
            pointsLink[edge]=true;
        }else {
            edge=Math.min(checkedPoint1,checkedPoint2);
            pointsLink[edge]=true;
        }
        if (edgestyle[edge]==ADD_RED){
            point_restore[steps+1][0]=checkedPoint1;
            point_restore[steps+1][1]=checkedPoint2;
            pointvalue_restore[steps+1][0]=pointvalue[checkedPoint1];
            pointvalue_restore[steps+1][1]=pointvalue[checkedPoint2];
            pointvalue[checkedPoint1]=pointvalue[checkedPoint1]+pointvalue[checkedPoint2];
            linkPointValueN(checkedPoint1,0);
            linkPointValueS(checkedPoint1, 0);

        }else if (edgestyle[edge]==MULT_YELLOW){
            point_restore[steps+1][0]=checkedPoint1;
            point_restore[steps+1][1]=checkedPoint2;
            pointvalue_restore[steps+1][0]=pointvalue[checkedPoint1];
            pointvalue_restore[steps+1][1]=pointvalue[checkedPoint2];
            pointvalue[checkedPoint1]=pointvalue[checkedPoint1]*pointvalue[checkedPoint2];
            linkPointValueN(checkedPoint1,0);
            linkPointValueS(checkedPoint1,0);
        }else {
         steps--;
        }
    }

    public void removeEdge(){
        int  edge;
        if (Math.abs(checkedPoint1-checkedPoint2)==n-1){
            edge=n-1;
        }else {
            edge=Math.min(checkedPoint1,checkedPoint2);
        }
        edgeStyle_restore[steps+1]=edgestyle[edge];
        edge_restore[steps+1]=edge;
        edgestyle[edge]=NULL;
        edgeStep[edge]=steps+1;
        points[checkedPoint1][2]=0;
        points[checkedPoint2][2]=0;
        if (steps==n-1 && !bestBegin) {
            result = pointvalue[checkedPoint1];
            showResult=true;
        }
        checkedPointsNumber = 0;
        checkedPoint1 = -1;
        checkedPoint2 = -1;
    }


    //逆时针连接点值
    public void linkPointValueN(int a,int i){

        if (a==0){
            if (pointsLink[n-1]){
                pointvalue[n-1]=pointvalue[a];
                i++;
                linkPointValueN(n-1,i);
            }
        }else {
            if (pointsLink[a-1]){
                pointvalue[a-1]=pointvalue[a];
                i++;
                linkPointValueN(a-1,i);
            }
        }
    }
    //顺时针连接点值
    public void linkPointValueS(int a,int i){
        if (a==n-1){
            if (pointsLink[a]){
                pointvalue[0]=pointvalue[a];
                i++;
                linkPointValueS(0, i);
            }
        }else {
            if (pointsLink[a]){
                pointvalue[a+1]=pointvalue[a];
                i++;
                linkPointValueS(a + 1, i);
            }
        }
    }

    //撤消操作
    public void restore(){
        if (steps==1) {
            edgestyle[edge_restore[steps]] = edgeStyle_restore[steps];
            steps--;
            if (m>0) m--;
        }else if (steps>1){
            pointvalue[point_restore[steps][0]] = pointvalue_restore[steps][0];
            pointvalue[point_restore[steps][1]] = pointvalue_restore[steps][1];
            edgestyle[edge_restore[steps]] = edgeStyle_restore[steps];
            pointsLink[edge_restore[steps]]=false;
            steps--;
            if (m>0) m--;
        }
        invalidate();
    }
    //重置操作
    public void reset(){
        int m=steps;
        for (int i=0;i<m;i++){
            restore();
        }
    }

    public void caculteBest(){
        bestResult.caculate();
    }
}
