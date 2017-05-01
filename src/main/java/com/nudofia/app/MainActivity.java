/**Visualizer Code based on Android Visualizer Master by Felix Palmer
 * "Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/"
 *
 *v2
 */
package com.nudofia.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.media.audiofx.Visualizer;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nudofia.utils.TunnelPlayerWorkaround;
import com.nudofia.visualizer.R;
import com.nudofia.visualizer.VisualizerView;
import com.nudofia.visualizer.renderer.BarGraphRenderer;

//Visualizer app developed for Nvidia Shield K1

public class MainActivity extends Activity {
  private MediaPlayer mPlayer;
  private MediaPlayer mSilentPlayer;  /* to avoid tunnel player issue */
  private VisualizerView mVisualizerView;
  private VisualizerView game;
  private boolean ai;
  private int state=0, fileState=0;
  private String url="nope", gif="";
  private String picturePath="";
  private boolean goBlack=true, musicNotPlaying=true;
  private int REQ_CODE_PICK_SOUNDFILE = 1;
  private Uri audioFileUri;
  private static int RESULT_LOAD_IMAGE = 2;


  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainmenu);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    initTunnelPlayerWorkaround();
    init();
  }

  @Override
  protected void onPause()
  {
    cleanUp();
    super.onPause();
  }

  @Override
  protected void onDestroy()
  {
    cleanUp();
    super.onDestroy();
  }

  private void init()
  {
    //needed to begin app
  }

  private void cleanUp()
  {
    //needed to prevent crashes
  }
//left over from source project
  // Workaround (for Galaxy S4)
  //
  // "Visualization does not work on the new Galaxy devices"
  //    https://github.com/felixpalmer/android-visualizer/issues/5
  //
  // NOTE:
  //   This code is not required for visualizing default "test.mp3" file,
  //   because tunnel player is used when duration is longer than 1 minute.
  //   (default "test.mp3" file: 8 seconds)
  //
  private void initTunnelPlayerWorkaround() {
    // Read "tunnel.decode" system property to determine
    // the workaround is needed
    if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
      mSilentPlayer = TunnelPlayerWorkaround.createSilentMediaPlayer(this);
    }
  }

  // Actions for buttons defined in xml
  //1v1 mode
  public void startPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);

    ai=false;

    //checks for backgrounds
    if ((url.equals("nope")||url.equals(null)||url.equals(""))&&picturePath.equals("")){
      System.out.println("URL IS NOT THERE:"+url);
      goBlack=true;
    }
    else if(picturePath!=null){
      goBlack=false;
      ImageView imageView = (ImageView) findViewById(R.id.imageView);
      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    else{

      if (url.length()==3){
        System.out.println("URL NOT VALID: "+url);
        goBlack=true;
      }
      else if (url.length()>3){
        gif=url.substring(url.length()-3);
        System.out.println("Gif: "+gif);
        if (gif.equals("gif")){
          System.out.println("URL GIF IS THERE");
          goBlack=false;
          GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget((ImageView) findViewById(R.id.imageView));
          Glide.with(this).load(url).into(imageViewTarget);
        }
        else{
          System.out.println("URL IS THERE");
          goBlack=false;
          new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);
        }
      }

    }


    //start vizualizer task
    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
    mPlayer = MediaPlayer.create(this, R.raw.mansion);
    mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 0);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");

  }

//ai modes
  public void aiPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.select);

  }

  public void easyPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    //checks for backgrounds
    if ((url.equals("nope")||url.equals(null)||url.equals(""))&&picturePath.equals("")){
      System.out.println("URL IS NOT THERE:"+url);
      goBlack=true;
    }
    else if(picturePath!=null){
      goBlack=false;
      ImageView imageView = (ImageView) findViewById(R.id.imageView);
      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    else{

      if (url.length()==3){
        System.out.println("URL NOT VALID: "+url);
        goBlack=true;
      }
      else if (url.length()>3){
        gif=url.substring(url.length()-3);
        System.out.println("Gif: "+gif);
        if (gif.equals("gif")){
          System.out.println("URL GIF IS THERE");
          goBlack=false;
          GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget((ImageView) findViewById(R.id.imageView));
          Glide.with(this).load(url).into(imageViewTarget);
        }
        else{
          System.out.println("URL IS THERE");
          goBlack=false;
          new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);
        }
      }

    }

    //start vizualizer task
    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 5);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  public void mediumPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    //checks for backgrounds
    if ((url.equals("nope")||url.equals(null)||url.equals(""))&&picturePath.equals("")){
      System.out.println("URL IS NOT THERE:"+url);
      goBlack=true;
    }
    else if(picturePath!=null){
      goBlack=false;
      ImageView imageView = (ImageView) findViewById(R.id.imageView);
      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    else{

      if (url.length()==3){
        System.out.println("URL NOT VALID: "+url);
        goBlack=true;
      }
      else if (url.length()>3){
        gif=url.substring(url.length()-3);
        System.out.println("Gif: "+gif);
        if (gif.equals("gif")){
          System.out.println("URL GIF IS THERE");
          goBlack=false;
          GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget((ImageView) findViewById(R.id.imageView));
          Glide.with(this).load(url).into(imageViewTarget);
        }
        else{
          System.out.println("URL IS THERE");
          goBlack=false;
          new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);
        }
      }

    }

    //start vizualizer task
    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 10);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  public void hardPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    //checks for backgrounds
    if ((url.equals("nope")||url.equals(null)||url.equals(""))&&picturePath.equals("")){
      System.out.println("URL IS NOT THERE:"+url);
      goBlack=true;
    }
    else if(picturePath!=null){
      goBlack=false;
      ImageView imageView = (ImageView) findViewById(R.id.imageView);
      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    else{

      if (url.length()==3){
        System.out.println("URL NOT VALID: "+url);
        goBlack=true;
      }
      else if (url.length()>3){
        gif=url.substring(url.length()-3);
        System.out.println("Gif: "+gif);
        if (gif.equals("gif")){
          System.out.println("URL GIF IS THERE");
          goBlack=false;
          GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget((ImageView) findViewById(R.id.imageView));
          Glide.with(this).load(url).into(imageViewTarget);
        }
        else{
          System.out.println("URL IS THERE");
          goBlack=false;
          new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);
        }
      }

    }

    //start vizualizer task

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 15);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  //get background
  public void changePressed(View view) throws IllegalStateException, IOException{

    EditText txtDescription = (EditText) findViewById(R.id.txt);
    url = txtDescription.getText().toString();

  }


  //pause game button
  public void stopPressed(View view) throws IllegalStateException, IOException
  {

    if (state==0){
      state++;
      mVisualizerView.mPause(state);
      mPlayer.pause();
    }
    else{
      state--;
      mPlayer.start();
      mVisualizerView.mPause(state);

    }
    System.out.println("STATE: "+state);
  }

//return to main screen
  public void returnPressed (View view) throws IllegalStateException, IOException{

    mVisualizerView.clearRenderers();
    if (state!=0){
      state--;
    }

    setContentView(R.layout.mainmenu);
    picturePath="";
    mVisualizerView.upstat();
    musicNotPlaying=true;
    goBlack=true;
  }

  //select/change music
  public void musicPressed (View view)throws IllegalStateException, IOException{

    Intent intent;
    intent = new Intent();
    intent.setType("audio/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Open Audio file"), REQ_CODE_PICK_SOUNDFILE);
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){

        audioFileUri = data.getData();

        musicNotPlaying=false;
        mPlayer= MediaPlayer.create(this, audioFileUri);

        mPlayer.start();
    }

    else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
      Uri selectedImage = data.getData();
      String[] filePathColumn = { MediaStore.Images.Media.DATA };

      Cursor cursor = getContentResolver().query(selectedImage,
              filePathColumn, null, null, null);
      cursor.moveToFirst();

      int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
      picturePath = cursor.getString(columnIndex);
      cursor.close();



    }
  }

  public void backPressed (View view)throws IllegalStateException, IOException{

    Intent i = new Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    startActivityForResult(i, RESULT_LOAD_IMAGE);
  }



  //end app
  public void exitPressed (View view)throws IllegalStateException, IOException{

    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);

  }



// wip function
  public void clearPressed(View view)
  {
    mVisualizerView.clearRenderers();
  }


}