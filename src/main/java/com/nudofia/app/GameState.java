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
    private int convertedone=0, convertedtwo=0, saveball=0;
    private int[] testingT= new int[17], testingB = new int[17];

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
            check(scoretwo);

        }


        if(_ballX < 0) {
            _ballX = ballstart;
            _ballY = ballstart;
            _batSpeed = 20;
            scoreone++;
            check(scoreone);

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

    public void pause (int s){
        if (s==1){
            saveball=_ballVelocityY;
            _ballVelocityY=0;
            _ballVelocityX=0;
            System.out.println("SAVED: "+saveball);
        }
        else{
            System.out.println("EXECUTING: "+saveball);
            _ballVelocityY=saveball;
            _ballVelocityX=saveball;
            System.out.println("EXECUTED: "+_ballVelocityX);
        }
    }

    public void check (int didwin){
        if (didwin==10){
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

        testingT=tarray;
        //convertedone=i*69;
        //System.out.println("Hit Upper");
        for (int i=0; i<tarray.length; i++)
        {
            convertedone=i*69;
            //System.out.println("Ball X: "+_ballX+" Ball Y: "+_ballY);
            if (_ballX>convertedone-34&&_ballX<convertedone+34&&_ballY<tarray[i]){
                //System.out.println("HIT");
                //if(_ballVelocityY>0){
                    //System.out.println("BAR X: "+convertedone+" BAR Y: "+tarray[i]);
                    _ballVelocityY *= -1;
                    _ballY += _ballVelocityY;
               // }
            }

        }


    }

    public void barHitL (int[] barray) {
        //System.out.println("Hit Lower");
        //System.out.println("LOWER: "+barY);

        /*if (barY<_ballY){
            //System.out.println("HIIIIIITTT");
            _ballVelocityY *= -1;
            _ballY += _ballVelocityY;
        }*/
        testingB=barray;


        for (int i=0; i<barray.length; i++)
        {
            convertedone=i*69;
            System.out.println("Ball X: "+_ballX+" Ball Y: "+_ballY);
            //System.out.println("Ball Y: "+_ballY);
            if (_ballX>convertedone-34&&_ballX<convertedone+34&&_ballY>(1110-barray[i])){
                System.out.println("HIT");
                //if(_ballVelocityY>0){
                //System.out.println("YOOOOOOOOOOOOOOOOOOOOOO");
                //System.out.println("Ball X: "+_ballX);
                //System.out.println("Ball Y: "+_ballY);
                System.out.println("BAR X: "+convertedone+" BAR Y: "+barray[i]);
                //System.out.println("BAR Y: "+tarray[i]);
                _ballVelocityY *= -1;
                _ballY += _ballVelocityY;
                // }
            }
            // System.out.println("Y TOP BARS: "+tarray[i]);
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

        //testing placeing for visualizer bar hits, will be applied to barHitU soon
        convertedtwo=0;
        for (int t=0; t< testingT.length; t++){

            convertedtwo=(testingT[t]+1500)-(testingT[t]*2);
            canvas.drawRect(new Rect(t*69,convertedtwo,(t*69)+10,convertedtwo+10), paint);
        }
        for (int t=0; t< testingB.length; t++){

            canvas.drawRect(new Rect(t*69,testingT[t],(t*69)+10,testingT[t]+10), paint);
        }

//draw the ball
        canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize),
                paint);
        //draw scores
        paint.setTextSize(60);
        canvas.drawText(Integer.toString(scoretwo), 10, 50, paint);
        canvas.drawText(Integer.toString(scoreone), 1100, 50, paint);

        //check win
        if (scoreone>scoretwo) {
            if (win) {
                paint.setTextSize(60);
                canvas.drawText("Player 1 Wins!", 350, 600, paint);
                //canvas.drawRGB(20, 20, 20);
                /*while (win){
                    System.out.println("PAUSED");
                }*/
            }
        }
        else{
            if (win) {
                paint.setTextSize(60);
                canvas.drawText("Player Two Wins!", 350, 600, paint);
                //canvas.drawRGB(20, 20, 20);
                //wait(1000);
                /*while (win) {
                    System.out.println("PAUSED");
                }*/
            }
        }

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