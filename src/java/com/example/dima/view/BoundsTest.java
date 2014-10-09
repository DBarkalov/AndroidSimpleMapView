package com.example.dima.view;

import android.app.Application;
import android.test.ApplicationTestCase;


public class BoundsTest extends ApplicationTestCase<Application> {
    public BoundsTest() {
        super(Application.class);
    }


    public  void testBounds1(){
        Bounds bounds = new Bounds(0,0,0,0,100, 0, 0);

        int width = 480;
        int height = 800;

        Bounds b1= bounds.getNewBounds((int)10.0f,(int)10.0f, width, height);
        assertEquals(-1, b1.getLeft());
        assertEquals(-90,b1.getX());
        assertEquals(-1, b1.getTop());
        assertEquals(-90,b1.getY());

        Bounds b2= bounds.getNewBounds((int)110.0f,(int)110.0f, width, height);
        assertEquals(-2, b2.getLeft());
        assertEquals(-90,b2.getX());
        assertEquals(-2, b2.getTop());
        assertEquals(-90,b2.getY());


        Bounds b3= bounds.getNewBounds((int)100.0f,(int)100.0f, width, height);
        assertEquals(-1, b3.getLeft());
        assertEquals(0,b3.getX());
        assertEquals(-1, b3.getTop());
        assertEquals(0,b3.getY());


        Bounds b5= bounds.getNewBounds((int)-20.0f,(int)-20.0f, width, height);
        assertEquals(0, b5.getLeft());
        assertEquals(-20,b5.getX());
        assertEquals(0, b5.getTop());
        assertEquals(-20,b5.getY());


        Bounds b4= bounds.getNewBounds((int)-100.0f,(int)-100.0f, width, height);
        assertEquals(1, b4.getLeft());
        assertEquals(0,b4.getX());
        assertEquals(1, b4.getTop());
        assertEquals(0,b4.getY());


        Bounds b6= bounds.getNewBounds((int)-150.0f,(int)-150.0f, width, height);
        assertEquals(1, b6.getLeft());
        assertEquals(-50,b6.getX());
        assertEquals(1, b6.getTop());
        assertEquals(-50,b6.getY());


        Bounds b7= bounds.getNewBounds((int)-250.0f,(int)-250.0f, width, height);
        assertEquals(2, b7.getLeft());
        assertEquals(-50,b7.getX());
        assertEquals(2, b7.getTop());
        assertEquals(-50,b7.getY());
    }

}