/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.nudofia.visualizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nudofia.app.GameState;
import com.nudofia.app.GameThread;
import com.nudofia.visualizer.renderer.Renderer;
import com.nudofia.visualizer.renderer.BarGraphRenderer;

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
  private boolean AI=false, w=false;
  private int upp=0, low=0;
    private int[] top = new int[17];
    private int[] bot = new int[17];
  private int firsti=0, secondi=0;
  private BarGraphRenderer barGraphRendererBottom;
  private BarGraphRenderer barGraphRendererTop;
    private int stat=1;
    private Visualizer audioOutput = null;
    private boolean gb=true;
    List<String> permissions = new ArrayList<String>();

  private Set<Renderer> mRenderers;

  private Paint mFlashPaint = new Paint();
  private Paint mFadePaint = new Paint();
   //public TextView tone= (TextView) findViewById(R.id.tvv), ttwo=(TextView) findViewById(R.id.tvvv);

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

    public void upstat(){
        stat--;
    }

 public void play(Boolean bool, Boolean b){
    AI=bool;
     gb=b;

  }

    public void barhits( int up, int low){

    }





  /**
   * Links the visualizer to a player
   * //@param player - MediaPlayer instance to link to
   */
  public void link(MediaPlayer player)
  {

    if(player == null)
    {
      throw new NullPointerException("Cannot link to null MediaPlayer");
    }
      //AudioTrack visualizedTrack = null;
     // final int minBufferSize = AudioTrack.getMinBufferSize(Visualizer.getMaxCaptureRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT);
      //visualizedTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Visualizer.getMaxCaptureRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT, minBufferSize, AudioTrack.MODE_STREAM);
      //visualizedTrack.play();
      //mVisualizer = new Visualizer(visualizedTrack.getAudioSessionId());
      //audioOutput = new Visualizer(visualizedTrack.getAudioSessionId());
     // permissions.add(Manifest.permission.RECORD_AUDIO);
      //audioOutput = new Visualizer(0);
      mVisualizer = new Visualizer(player.getAudioSessionId());

    // Create the Visualizer object and attach it to our media player.
    //mVisualizer = new Visualizer(player.getAudioSessionId());

    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
      //audioOutput.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);



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


      /*audioOutput.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
          @Override
          public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
              updateVisualizer(waveform);
          }

          @Override
          public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

              updateVisualizerFFT(fft);
          }
      }, Visualizer.getMaxCaptureRate(), true, false);*/

      //mVisualizer.setEnabled(true);
      //mVisualizer.setEnabled(false);
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

  public void addRenderer()
  {
      Paint paint = new Paint();
      paint.setStrokeWidth(50f); //50f
      paint.setAntiAlias(true);
      paint.setColor(Color.argb(200, 56, 138, 252));
      barGraphRendererBottom = new BarGraphRenderer(17, paint, false);
      mRenderers.add(barGraphRendererBottom);
      //up=barGraphRendererBottom.getHi();

      Paint paint2 = new Paint();
      paint2.setStrokeWidth(50f);
      paint2.setAntiAlias(true);
      paint2.setColor(Color.argb(200, 181, 111, 233));
      barGraphRendererTop = new BarGraphRenderer(17, paint2, true);
      mRenderers.add(barGraphRendererTop);
      //low=barGraphRendererBottom.getHi();
    /*if(renderer != null)
    {
      mRenderers.add(renderer);
        //renderer.getHi();
    }*/
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

      //setContentView(R.layout.main);
      if(gb){
      canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 150,
              Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);}

    _state.update(AI);

      upp=_state.getOneS();
      low=_state.getTwoS();
      w=_state.getWinStat();
      /*tone.setText(" "+upp);
      ttwo.setText(" "+low);*/
    _state.draw(canvas,mFlashPaint);
     /* low=barGraphRendererBottom.getLo();
      upp=barGraphRendererTop.getHi();
      firsti=barGraphRendererTop.getTopi();
      secondi=barGraphRendererBottom.getLowi();
      _state.barHitL(low, secondi);
      _state.barHitU(upp, firsti);*/







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
          top=barGraphRendererBottom.getLo();
          bot=barGraphRendererTop.getHi();
          firsti=barGraphRendererTop.getTopi();
          secondi=barGraphRendererBottom.getLowi();
          _state.barHitL(bot);
          _state.barHitU(top);
       // up=mRenderers.getHi();
        //low=r.getL();
      }
    }

    if (mFFTBytes != null) {
      // Render all FFT renderers
      FFTData fftData = new FFTData(mFFTBytes);
      for(Renderer r : mRenderers)
      {
        r.render(mCanvas, fftData, mRect);
          top=barGraphRendererBottom.getLo();
          bot=barGraphRendererTop.getHi();
          firsti=barGraphRendererTop.getTopi();
          secondi=barGraphRendererBottom.getLowi();
          _state.barHitL(bot);
          _state.barHitU(top);
      }
    }


      /*upp=_state.getOneS();
      low=_state.getTwoS();
      System.out.println(upp);
      tone.setText(""+upp);
      ttwo.setText(""+low);*/
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

    public void mPause(int st){
        _state.pause(st);
    }

  public int getScore1(){
      return upp;
  }

    public int getScore2(){
        return low;
    }

    public boolean getWinStatus(){
        return w;
    }

    public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {

            boolean result = true;
            for (int i = 0; i < permissions.length; i++) {
                result = result && grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
            if (!result) {

                //Toast.makeText(this, "..", Toast.LENGTH_LONG).show();
                // askPermission();
            } else {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean askPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int RECORD_AUDIO = checkSelfPermission(Manifest.permission.RECORD_AUDIO );

            if (RECORD_AUDIO != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }


            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
            } else
                return false;
        } else
            return false;
        return true;

    }*/

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    int X = 0;
    int Y = 0;



    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        X = (int) event.getX();
        Y = (int) event.getY();
        //System.out.println("X is: "+X);
       // System.out.println("Y is: "+Y);
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