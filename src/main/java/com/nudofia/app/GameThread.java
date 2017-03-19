package com.nudofia.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

/**
 * Created by xxnoa_000 on 3/14/2017.
 */

public class GameThread extends Thread {

    /** Handle to the surface manager object we interact with */
    private Canvas _surfaceHolder;
    private Paint _paint;
    private GameState _state;

    //SurfaceHolder surfaceHolder, Context context, Handler handler
    public GameThread(Canvas canvas, Context context, Handler handler)
    {
        _surfaceHolder = canvas;
        _paint = new Paint();
        _state = new GameState();
    }

    @Override
    public void run() {
        while(true)
        {
            //Canvas c= new Canvas();
            //Canvas canvas = _surfaceHolder.lockCanvas();
            _state.update();
            System.out.println("It should have updated...");
            _state.draw(_surfaceHolder,_paint);
           // _surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public GameState getGameState()
    {
        return _state;
    }
}