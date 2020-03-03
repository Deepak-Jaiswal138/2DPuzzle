package deepak.puzzle.com.a2dpuzzle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static GestureDetectGridView mGridView;

    private static final int COLUMNS=3;
    private static final int DIMENTIONS=COLUMNS*COLUMNS;
    private static int mColumnWidth,mColumnHeight;

    public static final String up="up";
    public static final String down="down";
    public static final String left="left";
    public static final String right="right";

    private static String[] tileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        scramble();
        setDimensions();
    }
    private void init() {
        mGridView=(GestureDetectGridView)findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);

        tileList=new String[DIMENTIONS];
        for (int i=0;i<DIMENTIONS;i++)
        {
            tileList[i]=String.valueOf(i);
        }
    }
    private void scramble() {
        int index;
        String temp;
        Random random=new Random();

        for (int i=tileList.length-1;i>0;i--){
            index=random.nextInt(i+1);
            temp=tileList[index];
            tileList[index]=tileList[i];
            tileList[i]=temp;
        }
    }
    private void setDimensions(){
        ViewTreeObserver vto=mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth=mGridView.getMeasuredWidth();
                int displayHeight=mGridView.getMeasuredHeight();

                int statusbarHeight=getStatusBarHeight(getApplicationContext());
                int requiredHeight=displayHeight-statusbarHeight;

                mColumnWidth=displayWidth/COLUMNS;
                mColumnHeight=requiredHeight/COLUMNS;
                display(getApplicationContext());
            }
        });
    }
    private int getStatusBarHeight(Context context){
        int result=0;
        int resourcedId=context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourcedId>0){
            result=context.getResources().getDimensionPixelSize(resourcedId);
        }
        return result;
    }


    private static void display(Context context) {
        ArrayList<Button> buttons=new ArrayList<>();
        Button button;

        for (int i=0;i<tileList.length;i++){
            button=new Button(context);

            if (tileList[i].equals("0"))
                button.setBackgroundResource(R.drawable.pigeon_piece1);
            else if (tileList[i].equals("1"))
                button.setBackgroundResource(R.drawable.pigeon_piece2);
            else if (tileList[i].equals("2"))
                button.setBackgroundResource(R.drawable.pigeon_piece3);
            else if (tileList[i].equals("3"))
                button.setBackgroundResource(R.drawable.pigeon_piece4);
            else if (tileList[i].equals("4"))
                button.setBackgroundResource(R.drawable.pigeon_piece5);
            else if (tileList[i].equals("5"))
                button.setBackgroundResource(R.drawable.pigeon_piece6);
            else if (tileList[i].equals("6"))
                button.setBackgroundResource(R.drawable.pigeon_piece7);
            else if (tileList[i].equals("7"))
                button.setBackgroundResource(R.drawable.pigeon_piece8);
            else if (tileList[i].equals("8"))
                button.setBackgroundResource(R.drawable.pigeon_piece9);

            buttons.add(button);

        }
        mGridView.setAdapter(new CustomAdapter(buttons,mColumnWidth,mColumnHeight));
    }
    private static void swap(Context context,int currentPosition,int swap){
        String newPosition=tileList[currentPosition+swap];
        tileList[currentPosition+swap]=tileList[currentPosition];
        tileList[currentPosition]=newPosition;
        display(context);
        if (isSolved()) Toast.makeText(context, "You Win", Toast.LENGTH_SHORT).show();
    }
    public static void moveTiles(Context context,String direction,int position){
        //upper-left-corner tile
        if (position==0){
            if (direction.equals(right)) swap(context,position,1);
            else if (direction.equals(down)) swap(context,position,COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //upper center tile
        }else if (position>0&&position<COLUMNS-1){
            if (direction.equals(left)) swap(context,position,-1);
            else if (direction.equals(down)) swap(context,position,COLUMNS);
            else if (direction.equals(right)) swap(context,position,1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //upper right corner tile
        }else if (position==COLUMNS-1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //left side tile
        } else if (position>COLUMNS-1&&position<DIMENTIONS-COLUMNS&&position%COLUMNS==0) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position,1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //right side and bottom right corner tiles
        }else if (position==COLUMNS*2-1||position==COLUMNS*3-1){
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position,-1);
            else if (direction.equals(down)){
                if (position<=DIMENTIONS-COLUMNS-1) swap(context,position,COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //bottom left corner tile

        }else if (position==DIMENTIONS-COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //bottom center tiles
        } else if (position<DIMENTIONS-1&&position>DIMENTIONS-COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position,1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            //center tiles
        }else {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context,position,COLUMNS);
        }
    }
    private static boolean isSolved(){
        boolean solved=false;

        for (int i=0;i<tileList.length;i++){
            if (tileList[i].equals(String.valueOf(i))){
                solved=true;
            }else {
                solved=false;
                break;
            }
        }
        return solved;
    }

}
