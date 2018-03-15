package com.hfqs.shoot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.*;
  
@SuppressWarnings("restriction")  
public class SimpleAudioPlayer implements ControllerListener{  
    private Player audioPlayer = null;//建立一个播放接口  
    public SimpleAudioPlayer(URL url) throws Exception{//创建一个准备Player,准备好播放  
        audioPlayer = Manager.createRealizedPlayer(url);  
        audioPlayer.addControllerListener(this); //对player对象注册监听器，能够在相关事件发生的时候执行响应的动作
        audioPlayer.realize(); //对播放前进行预处理状态，就是缓冲资源 ,让player对象进行相关的资源分配
    }  
    @SuppressWarnings("deprecation")  
    public SimpleAudioPlayer(String path) throws MalformedURLException, Exception{//将本地文件改为URL  
        //SimpleAudioPlayer Player = new SimpleAudioPlayer(file);  
    	this((new File(path)).toURL());  
    }  
      
    public void play(){//直接调用播放方法就可以  
        audioPlayer.start();
        //controllerUpdate(null);
    } 
    
    public void stop(){//暂停
        audioPlayer.stop();  
    }
    
    public void close(){//释放资源  
        audioPlayer.close();  
    }
      
    public static void main(String [] args) throws MalformedURLException, Exception{  
        
    }
    public void controllerUpdate(ControllerEvent ce) { //监听player的相关事件
    	if (ce instanceof EndOfMediaEvent) { 
			audioPlayer.prefetch(); //player实例化完成后进行player播放前预处理
			audioPlayer.setMediaTime(new Time(0)); //当播放视频完成后，把时间进度条恢复到开始，
			audioPlayer.start(); //再次重新开始播放
    	}
    }
    
    
}  