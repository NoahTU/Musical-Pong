package com.nudofia.app;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

import android.view.MotionEvent;

/**
 * Created by xxnoa_000 on 3/14/2017.
 */

public class GameState {
    private Canvas c;
    private boolean aiState = false;

    //screen width and height 1200, 4000
    //final int _screenWidth = 1200;
    //final int _screenHeight = 1130;

    final int _screenWidth = getScreenWidth()-30;
    final int _screenHeight = getScreenHeight();

    int ballstart= 600;

    //The ball
    final int _ballSize = 20;
    int _ballX = ballstart; 	int _ballY = ballstart;
    int _ballVelocityX = 9; 	int _ballVelocityY = 9;

    //The bats
    final int _batLength = 10;	final int _batHeight = 150;

    int leftBatX = 20;

    int leftBatY = (_screenHeight/2);

    int rightBatX = _screenWidth-50;

    int rightBatY = (_screenHeight/2);

    int _batSpeed = 20;

    public GameState()
    {
    }

    //The update method
    public void update(Boolean AI) {

        aiState=AI;

        _ballX += _ballVelocityX;
        _ballY += _ballVelocityY;

//death hello darkness my old friend
        if(_ballY > _screenHeight-150 || _ballY < 40)
        {_ballVelocityY *= -1;}  	//Collisions with the sides _ballX = ballstart; 	_ballY = ballstart;

        if(_ballX > _screenWidth || _ballX < 0) {
            _ballX = ballstart;
            _ballY = ballstart;
            _batSpeed = 20;

        } 	//Collisions with the bats _ballVelocityX *= -1

        if(_ballY > leftBatY && _ballY < leftBatY+_batHeight && _ballX < leftBatX)
            _ballVelocityX *= -1;  //Collisions with the bats
            _batSpeed++;

        if(_ballY > rightBatY && _ballY < rightBatY+_batHeight
                && _ballX > rightBatX)
            _ballVelocityX *= -1;
            _batSpeed++;

        //AI
        if (aiState){
            rightBatY=_ballY;
        }


    }


    public void barHitU (int barY) {

        //System.out.println("Hit Upper");

        if (barY<_ballY){
            //System.out.println("HIIIIIITTT");
            _ballVelocityY *= -1;
        }

    }

    public void barHitL (int barY) {
        //System.out.println("Hit Lower");

        if (barY<_ballY){
            //System.out.println("HIIIIIITTT");
            _ballVelocityY *= -1;
        }

    }


    public void movePaddleLDown() {


        leftBatY += _batSpeed;

    }
    public void movePaddleLUp() {

        leftBatY -= _batSpeed;

    }

    public void movePaddleRDown() {

        rightBatY += _batSpeed;

    }
    public void movePaddleRUp() {
        rightBatY -= _batSpeed;


    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

        c=canvas;

//Clear the screen
        canvas.drawRGB(20, 20, 20);

//set the colour
        paint.setARGB(200, 0, 200, 0);

//draw the ball
        canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize),
                paint);

//draw the bats
        canvas.drawRect(new Rect(leftBatX, leftBatY, leftBatX + _batLength,
                leftBatY + _batHeight), paint); //top bat
        canvas.drawRect(new Rect(rightBatX, rightBatY, rightBatX + _batLength,
                rightBatY + _batHeight), paint); //bottom bat

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}