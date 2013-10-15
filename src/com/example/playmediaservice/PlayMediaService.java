
package com.example.playmediaservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;
import java.net.URL;
import android.webkit.URLUtil;

public class PlayMediaService extends Service{

	// VARIABLES
	public static final String TAG = "PlayMediaService";
	
	final String URL_MP3 = "http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3";	
	private MediaPlayer mediaPlayer; 
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(TAG, "onCreate");
		
		try {
//			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oneday);
//			mediaPlayer.prepare();
//			mediaPlayer.setLooping(false);
		
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			setDataSource(URL_MP3);
			mediaPlayer.prepareAsync();
			//mediaPlayer.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Intent i = new Intent("abc");
			i.putExtra("Message", "Error: "+ e.toString());
			sendBroadcast(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Intent i = new Intent("abc");
			i.putExtra("Message", "Error: "+ e.toString());
			sendBroadcast(i);
		}
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mediaPlayer.start();
				
				Intent i = new Intent("abc");
				i.putExtra("Message", "Start Media Player");
				sendBroadcast(i);
			}
		});
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		if(!mediaPlayer.isPlaying()){
			//mediaPlayer.start();
		}
		
		
		return Service.START_NOT_STICKY;
	}
	
	private void setDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            mediaPlayer.setDataSource(path);
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            mediaPlayer.setDataSource(tempPath);
            try {
                stream.close();
            }
            catch (IOException ex) {
                Log.e(TAG, "error: " + ex.getMessage(), ex);
            }
        }
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		// stop mediaPlayer when the mediaPlayer is Playing
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		Intent i = new Intent("abc");
		i.putExtra("Message", "Stop Media Player");
		sendBroadcast(i);
	}
}
