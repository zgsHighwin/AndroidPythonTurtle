package com.zgs.androidpythonturtle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zgs.turtle.TurtleView;

public class MainActivity extends AppCompatActivity {

    private TurtleView turtleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turtleView = (TurtleView) findViewById(R.id.turtle_view);
        turtleView.setSpeed(2000);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(100);
//        turtleView.right(30);
//        turtleView.forward(200);
//        turtleView.right(270);
//        turtleView.forward(200);

//        for (int i = 0; i < 100000; i++) {
//            turtleView.forward(i >> 2);
//            turtleView.left(i+179);
//        }

        turtleView.gotoPoint(0, 0);
        turtleView.forward(200);
        turtleView.right(144);
        turtleView.gotoPoint(0, 0);
        turtleView.forward(200);
        turtleView.right(144);
        turtleView.forward(200);
        turtleView.right(144);
        turtleView.forward(100);
        turtleView.left(144);
        turtleView.forward(100);

//        for (int i = 0; i < 1000; i++) {
//            turtleView.gotoPoint(0, 0);
//            turtleView.forward(200);
//            turtleView.right(144);
//        }
        turtleView.start();

    }
}
