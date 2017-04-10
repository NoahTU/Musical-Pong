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
    private int convertedone=0, convertedtwo=0;

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

    int scoreone=0;
    int scoretwo=0;
    boolean win=false;

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
        if(_ballY > _screenHeight-150 || _ballY < 40 || _ballY<0)
        {_ballVelocityY *= -1;}  	//Collisions with the sides _ballX = ballstart; 	_ballY = ballstart;

        if(_ballX > _screenWidth) {
            _ballX = ballstart;
            _ballY = ballstart;
            _batSpeed = 20;
            scoretwo++;

        }


        if(_ballX < 0) {
            _ballX = ballstart;
            _ballY = ballstart;
            _batSpeed = 20;
            scoreone++;

        }

        //Collisions with the bats _ballVelocityX *= -1

        if(_ballY > leftBatY && _ballY < leftBatY+_batHeight && _ballX < leftBatX)
            _ballVelocityX *= -1;
        _ballVelocityY *= -1;//Collisions with the bats
            _batSpeed++;

        if(_ballY > rightBatY && _ballY < rightBatY+_batHeight
                && _ballX > rightBatX)
            _ballVelocityX *= -1;
        _ballVelocityY *= -1;
            _batSpeed++;

        //AI
        if (aiState){
            rightBatY=_ballY;
        }


    }

    public void check (int didwin){
        if (didwin>5){
            win=true;
        }
    }
    public boolean getWinStat(){
        return win;
    }

    public int getOneS(){
        return scoreone;
    }
    public int getTwoS(){
        return scoretwo;
    }

    public void barHitU (int[] tarray) {

        //convertedone=i*69;
        //System.out.println("Hit Upper");
        for (int i=0; i<tarray.length; i++)
        {
            convertedone=i*69;
            System.out.println("Ball X: "+_ballX);
            System.out.println("Ball Y: "+_ballY);
            if (_ballX>convertedone-34&&_ballX<convertedone+34&&_ballY<tarray[i]){
                ///System.out.println("YO");
                if(_ballVelocityY>0){
                    System.out.println("YOOOOOOOOOOOOOOOOOOOOOO");
                    //System.out.println("Ball X: "+_ballX);
                    //System.out.println("Ball Y: "+_ballY);
                    System.out.println("Bar X: "+convertedone);
                    System.out.println("Bar Y: "+tarray[i]);
                    _ballVelocityY *= -1;
                    _ballY += _ballVelocityY;
                }
            }
           // System.out.println("Y TOP BARS: "+tarray[i]);
        }
       // System.out.println("UPPER Y: "+_ballX);

        /*if (barY<_ballY){
            //System.out.println("HIIIIIITTT");
            _ballVelocityY *= -1;
            _ballY += _ballVelocityY;
        }*/

    }

    public void barHitL (int[] barray) {
        //System.out.println("Hit Lower");
        //System.out.println("LOWER: "+barY);

        /*if (barY<_ballY){
            //System.out.println("HIIIIIITTT");
            _ballVelocityY *= -1;
            _ballY += _ballVelocityY;
        }*/

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

        if (scoreone>scoretwo) {
            while (win) {
                paint.setTextSize(40);
                canvas.drawText("Player 1 Wins!", 10, 25, paint);
            }
        }
        else{
            while (win) {
                paint.setTextSize(40);
                canvas.drawText("Player Two Wins!", 10, 25, paint);
            }
        }

        c=canvas;

//Clear the screen
        canvas.drawRGB(20, 20, 20);

//set the colour
        paint.setARGB(200, 0, 200, 0);

//draw the ball
        canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize),
                paint);
        //draw scores
        paint.setTextSize(60);
        canvas.drawText(Integer.toString(scoretwo), 10, 50, paint);
        canvas.drawText(Integer.toString(scoreone), 1100, 50, paint);

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