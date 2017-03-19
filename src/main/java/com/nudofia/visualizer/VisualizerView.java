/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.nudofia.visualizer;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.nudofia.app.GameState;
import com.nudofia.app.GameThread;
import com.nudofia.visualizer.renderer.Renderer;

/**
 * A class that draws visualizations of data received from a
 * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture } and
 * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
 */
//public class VisualizerView extends SurfaceView implements SurfaceHolder.Callback{
public class VisualizerView extends View implements SurfaceHolder.Callback{
  private static final String TAG = "VisualizerView";

  private byte[] mBytes;
  private byte[] mFFTBytes;
  private Rect mRect = new Rect();
  private Visualizer mVisualizer;
  private GameThread _thread;
  private Context viewcon;
  private Canvas gcanvas;
  private GameState _state;

  private Set<Renderer> mRenderers;

  private Paint mFlashPaint = new Paint();
  private Paint mFadePaint = new Paint();

  public VisualizerView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs);
    init();
  }

  public VisualizerView(Context context, AttributeSet attrs)
  {
    //super(context, attrs);
    this(context, attrs, 0);
    viewcon= context;
    _state = new GameState();

    //So we can listen for events...
    //SurfaceHolder holder = getHolder();
    //holder.addCallback(this);
   // setFocusable(true);

    //and instantiate the thread
   // _thread = new GameThread(context, new Handler());
  }

  public VisualizerView(Context context)
  {
    this(context, null, 0);
  }

  private void init() {
    mBytes = null;
    mFFTBytes = null;

    mFlashPaint.setColor(Color.argb(50, 255, 255, 255));
    mFadePaint.setColor(Color.argb(150, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
    mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

    mRenderers = new HashSet<Renderer>();
  }


  /*public void play(){
    _thread = new GameThread(gcanvas,viewcon, new Handler());
  }*/

  /**
   * Links the visualizer to a player
   * @param player - MediaPlayer instance to link to
   */
  public void link(MediaPlayer player)
  {
    if(player == null)
    {
      throw new NullPointerException("Cannot link to null MediaPlayer");
    }

    // Create the Visualizer object and attach it to our media player.
    mVisualizer = new Visualizer(player.getAudioSessionId());
    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

    // Pass through Visualizer data to VisualizerView
    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
    {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
          int samplingRate)
      {
        updateVisualizer(bytes);
      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
          int samplingRate)
      {
        updateVisualizerFFT(bytes);
      }
    };

    mVisualizer.setDataCaptureListener(captureListener,
        Visualizer.getMaxCaptureRate() , true, true);

    // Enabled Visualizer and disable when we're done with the stream
    mVisualizer.setEnabled(true);
    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer)
      {
        mVisualizer.setEnabled(false);
      }
    });
  }

  public void addRenderer(Renderer renderer)
  {
    if(renderer != null)
    {
      mRenderers.add(renderer);
    }
  }

  public void clearRenderers()
  {
    mRenderers.clear();
  }

  /**
   * Call to release the resources used by VisualizerView. Like with the
   * MediaPlayer it is good practice to call this method
   */
  public void release()
  {
    mVisualizer.release();
  }

  /**
   * Pass data to the visualizer. Typically this will be obtained from the
   * Android Visualizer.OnDataCaptureListener call back. See
   * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
   * @param bytes
   */
  public void updateVisualizer(byte[] bytes) {
    mBytes = bytes;
    invalidate();
  }

  /**
   * Pass FFT data to the visualizer. Typically this will be obtained from the
   * Android Visualizer.OnDataCaptureListener call back. See
   * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
   * @param bytes
   */
  public void updateVisualizerFFT(byte[] bytes) {
    mFFTBytes = bytes;
    invalidate();
  }

  boolean mFlash = false;

  /**
   * Call this to make the visualizer flash. Useful for flashing at the start
   * of a song/loop etc...
   */
  public void flash() {
    mFlash = true;
    invalidate();
  }

  Bitmap mCanvasBitmap;
  Canvas mCanvas;


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // Create canvas once we're ready to draw




    mRect.set(0, 0, getWidth(), getHeight());

    _state.update();
    _state.draw(canvas,mFlashPaint);






    if(mCanvasBitmap == null)
    {
      mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
    }
    if(mCanvas == null)
    {
      mCanvas = new Canvas(mCanvasBitmap);
    }

    if (mBytes != null) {
      // Render all audio renderers
      AudioData audioData = new AudioData(mBytes);
      for(Renderer r : mRenderers)
      {
        r.render(mCanvas, audioData, mRect);
      }
    }

    if (mFFTBytes != null) {
      // Render all FFT renderers
      FFTData fftData = new FFTData(mFFTBytes);
      for(Renderer r : mRenderers)
      {
        r.render(mCanvas, fftData, mRect);
      }
    }

    // Fade out old contents
    mCanvas.drawPaint(mFadePaint);

    if(mFlash)
    {
      mFlash = false;
      mCanvas.drawPaint(mFlashPaint);
    }

    canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);

    gcanvas= canvas;
  }

  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    int X = 0;
    int Y = 0;



    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        X = (int) event.getX();
        Y = (int) event.getY();
        System.out.println("X is: "+X);
        System.out.println("Y is: "+Y);
        if (X<getScreenWidth()/2&& Y>getScreenHeight()/2){
          _state.movePaddleLDown();
        }
        if (X<getScreenWidth()/2&& Y<getScreenHeight()/2){
          _state.movePaddleLUp();
        }
        if (X>getScreenWidth()/2&& Y>getScreenHeight()/2){
          _state.movePaddleRDown();
        }
        if (X>getScreenWidth()/2&& Y<getScreenHeight()/2){
          _state.movePaddleRUp();
        }
        return true;
      case MotionEvent.ACTION_MOVE:


        break;
      case MotionEvent.ACTION_UP:

        break;
      default:
        return false;
    }

    if (Y> getScreenHeight() && X> getScreenWidth()){
      System.out.println("Move left paddle up");
    }
    if (Y< getScreenHeight() && X> getScreenWidth()){
      System.out.println("Move left paddle down");
    }


    return true;
  }




  //Implemented as part of the SurfaceHolder.Callback interface
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {
    //Mandatory, just swallowing it for this example

  }

  //Implemented as part of the SurfaceHolder.Callback interface
  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    _thread.start();
  }

  //Implemented as part of the SurfaceHolder.Callback interface
  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    _thread.stop();
  }
}