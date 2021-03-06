package com.nudofia.app;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import java.util.Random;

import android.view.MotionEvent;

/**
 * Created by xxnoa_000 on 3/14/2017.
 */

public class GameState {
    private Canvas c;
    private boolean aiState = false, aiStateTen= false;
    private int convertedone=0, convertedtwo=0, saveball=0, saveballtwo=0, ct=0, l=0;
    private int[] testingT= new int[17], testingB = new int[17];
    private Random rand = new Random();


    final int _screenWidth = getScreenWidth()-30;
    final int _screenHeight = getScreenHeight();

    int ballstart= 600;

    int AIcounter=0;
    int AIstop=rand.nextInt(5) + 1;

    //The ball
    final int _ballSize = 40;
    int _ballX = ballstart; 	int _ballY = ballstart;
    int _ballVelocityX = 22; 	int _ballVelocityY = -22;
    int speedBoost=0;

    //The bats
    final int _batLength = 20;	final int _batHeight = 250;

    int scoreone=0;
    int scoretwo=0;
    boolean win=false;

    final int leftBatX = 10;

    int leftBatY = (_screenHeight/2);

    final int rightBatX = _screenWidth-40;

    int rightBatY = (_screenHeight/2);

    int _batSpeed = 100;

    public GameState()
    {
    }

    //The update method
    public void update(Boolean AI, int lev) {

        aiState=AI;
        aiStateTen=AI;

        if(lev==0){
            l=10;
        }
        else{
            l=lev;
        }

        _ballX += _ballVelocityX;
        _ballY += _ballVelocityY;

//death: hello darkness my old friend
        if(_ballY > _screenHeight-250 || _ballY < 40 || _ballY<0)
        {_ballVelocityY *= -1;}  	//Collisions with the sides _ballX = ballstart; 	_ballY = ballstart;

        if(_ballX > _screenWidth) {
            _ballX = ballstart;
            _ballY = ballstart;
            AIcounter=0;
            if (aiState){
                aiStateTen=true;}
            _ballVelocityX=22+(speedBoost/2);
            _ballVelocityY=22+(speedBoost/2);
            speedBoost=0;
            AIstop=rand.nextInt(l) + 1;
            scoreone++;
            check(scoreone);

        }


        if(_ballX < 0) {
            _ballX = ballstart;
            _ballY = ballstart;
            AIcounter=0;
            if (aiState){
                aiStateTen=true;}
            _ballVelocityX=25+(speedBoost/2);
            _ballVelocityY=25+(speedBoost/2);
            speedBoost=0;
            AIstop=rand.nextInt(l) + 1;
            scoretwo++;
            check(scoretwo);

        }

        //Collisions with the bats

        if(_ballY > leftBatY && _ballY < leftBatY+_batHeight && _ballX < leftBatX+6){
            _ballVelocityX *= -1;
            _ballVelocityY *= -1;
            _ballVelocityX-=3;
            _ballVelocityY+=3;
            speedBoost+=3;
            AIcounter++;
            }


        if(_ballY > rightBatY && _ballY < rightBatY+_batHeight && _ballX > rightBatX-5){
            _ballVelocityX *= -1;
            _ballVelocityY *= -1;
            _ballVelocityX+=3;
            _ballVelocityY+=3;
            speedBoost+=3;
            AIcounter++;
            }

        //AI
        AIcheck(AIcounter);
        if (aiStateTen){
            rightBatY=_ballY-50;//hard int added for smoother animation on testing tablet
        }


    }

    //pause game assets
    public void pause (int s){ //s for state
        if (s==1){
            saveball=_ballVelocityY;
            saveballtwo=_ballVelocityX;
            _ballVelocityY=0;
            _ballVelocityX=0;
            _batSpeed=0;
            System.out.println("SAVED: "+saveball);
        }
        else{
            System.out.println("EXECUTING: "+saveball);
            _batSpeed=100;
            _ballVelocityY=saveball;
            _ballVelocityX=saveballtwo;
            System.out.println("EXECUTED: "+_ballVelocityX);
        }
    }

    //function used to check wins
    public void check (int didwin){
        if (didwin==l){
            win=true;
            _ballVelocityY=0;
            _ballVelocityX=0;
            _batSpeed=0;
        }
    }

    public void AIcheck (int questionstop){
        System.out.println("Hits: "+questionstop);
        System.out.println("Random: "+AIstop);
        System.out.println("Random: "+aiStateTen);
        if (questionstop==AIstop||questionstop>AIstop){
            aiStateTen=false;
        }
    }
    //give out win status to other classes
    public boolean getWinStat(){
        return win;
    }

    //scores for updating in other classes
    public int getOneS(){
        return scoreone;
    }
    public int getTwoS(){
        return scoretwo;
    }



    // functions used to have visualizer bars hit the balls, wip
    public void barHitU (int[] tarray) {

        testingT=tarray;
        //convertedone=i*69;
        //System.out.println("Hit Upper");
        for (int i=1; i<tarray.length; i++)
        {
            convertedone=i*69;
            if (tarray[i]!=0){
                ct=(tarray[i]+1500)-(tarray[i]*2);}
            else{
                ct=0;
            }
            //System.out.println("Ball X: "+_ballX+" Ball Y: "+_ballY);
            if (_ballX>convertedone-34&&_ballX<convertedone+34&&_ballY<ct){
                //System.out.println("HIT 1");

                //System.out.println("BALL Y: "+_ballY+" BarU Y: "+ct);
                //System.out.println("velocity: "+_ballVelocityY);


                if (_ballVelocityY<0){
                    //System.out.println("HIIIIIIIIIT");
                    _ballVelocityY *= -1;
                    _ballVelocityY++;

                    if(_ballVelocityX<0){
                        _ballVelocityX--;
                    }
                    else{
                        _ballVelocityX++;
                    }
                    speedBoost++;
                    _ballY += _ballVelocityY;
                    }
                else{
                    _ballVelocityY++;
                    if(_ballVelocityX<0){
                        _ballVelocityX--;
                    }
                    else{
                        _ballVelocityX++;
                    }
                    speedBoost++;
                }


            }
        }
    }

    public void barHitL (int[] barray) {
        //System.out.println("Hit Lower");
        //System.out.println("LOWER: "+barY);

        testingB=barray;


        for (int i=1; i<barray.length; i++)
        {
            convertedone=i*69;

            if (_ballX>convertedone-34&&_ballX<convertedone+34&&_ballY>barray[i]){
                //System.out.println("HIT 2");
               // System.out.println("BALL Y: "+_ballY+" BarL Y: "+barray[i]);
                //System.out.println("velocity: "+_ballVelocityY);


                if (_ballVelocityY>0){
                    _ballVelocityY *= -1;
                    speedBoost++;
                    _ballVelocityY++;
                    _ballY += _ballVelocityY;
                    if(_ballVelocityX<0){
                        _ballVelocityX--;
                    }
                    else{
                        _ballVelocityX++;
                    }

                }
                else{
                    _ballVelocityY--;
                    if(_ballVelocityX<0){
                        _ballVelocityX--;
                    }
                    else{
                        _ballVelocityX++;
                    }
                    speedBoost++;
                }
                // }
            }
            // System.out.println("Y TOP BARS: "+tarray[i]);
        }

    }


    //move paddles
    public void movePaddleLDown() {

        if (leftBatY<1300) {

            leftBatY += _batSpeed;
        }

    }
    public void movePaddleLUp() {

        if (leftBatY>50) {
            leftBatY -= _batSpeed;

        }
    }

    public void movePaddleRDown() {

        if (rightBatY<1300) {
            rightBatY += _batSpeed;
        }

    }
    public void movePaddleRUp() {
        if (rightBatY>50) {
            rightBatY -= _batSpeed;
        }

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
            //canvas.drawRect(new Rect(t*69,convertedtwo,(t*69)+10,convertedtwo+10), paint);
        }
        for (int t=0; t< testingB.length; t++){

            //canvas.drawRect(new Rect(t*69,testingT[t],(t*69)+10,testingT[t]+10), paint);
        }

        if (_ballVelocityY==0) {
            paint.setTextSize(60);
            canvas.drawText("Paused", 475, 700, paint);
        }

        //draw the ball
        canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize),
                paint);
        //draw scores
        paint.setTextSize(60);
        canvas.drawText(Integer.toString(scoreone), 10, 50, paint);
        canvas.drawText(Integer.toString(scoretwo), 1100, 50, paint);

        //check win
        if (scoreone>scoretwo) {
            if (win) {
                paint.setTextSize(60);
                canvas.drawText("Player 1 Wins!", 475, 600, paint);
                canvas.drawText("Press Return", 475, 500, paint);
            }
        }
        else{
            if (win) {
                paint.setTextSize(60);
                if (aiState){
                    canvas.drawText("AI Wins!", 475, 600, paint);
                    canvas.drawText("Press Return", 475, 500, paint);
                }
                else{
                    canvas.drawText("Player Two Wins!", 475, 600, paint);
                    canvas.drawText("Press Return", 475, 500, paint);
                }
            }
        }

//draw the bats
        canvas.drawRect(new Rect(leftBatX, leftBatY, leftBatX + _batLength,
                leftBatY + _batHeight), paint); //top bat
        canvas.drawRect(new Rect(rightBatX, rightBatY, rightBatX + _batLength,
                rightBatY + _batHeight), paint); //bottom bat

    }

    //get the standard screen dimesnions to attempt a global version of the code
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}