package zzheng.gamedbx;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    private MyGameView myGameView;
    private TextView startTB;
    private TextView restoreTextBT;
    private TextView bestTextBT;
    private TextView resetTextBT;
    private LinearLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGameView =new MyGameView(this);
        setContentView(R.layout.activity_main);
        myGameView=(MyGameView)findViewById(R.id.mygameview);
        startTB=(TextView)findViewById(R.id.start_textbutton);
        restoreTextBT=(TextView)findViewById(R.id.restore_textbutton);
        resetTextBT=(TextView)findViewById(R.id.reset_textbutton);
        bestTextBT=(TextView)findViewById(R.id.best_textbutton);
        main_layout=(LinearLayout)findViewById(R.id.main_layout);

        resetTextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGameView.reset();
            }
        });
        bestTextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGameView.reset();
                myGameView.setBestBegin(true);
            }
        });
        restoreTextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGameView.restore();
            }
        });
        startTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_layout.setVisibility(View.VISIBLE);
                myGameView.setShapeRandom((int)(Math.random()*8+3));
                startTB.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
