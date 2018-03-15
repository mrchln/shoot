package com.hfqs.shoot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.*;
  
@SuppressWarnings("restriction")  
public class SimpleAudioPlayer implements ControllerListener{  
    private Player audioPlayer = null;//����һ�����Žӿ�  
    public SimpleAudioPlayer(URL url) throws Exception{//����һ��׼��Player,׼���ò���  
        audioPlayer = Manager.createRealizedPlayer(url);  
        audioPlayer.addControllerListener(this); //��player����ע����������ܹ�������¼�������ʱ��ִ����Ӧ�Ķ���
        audioPlayer.realize(); //�Բ���ǰ����Ԥ����״̬�����ǻ�����Դ ,��player���������ص���Դ����
    }  
    @SuppressWarnings("deprecation")  
    public SimpleAudioPlayer(String path) throws MalformedURLException, Exception{//�������ļ���ΪURL  
        //SimpleAudioPlayer Player = new SimpleAudioPlayer(file);  
    	this((new File(path)).toURL());  
    }  
      
    public void play(){//ֱ�ӵ��ò��ŷ����Ϳ���  
        audioPlayer.start();
        //controllerUpdate(null);
    } 
    
    public void stop(){//��ͣ
        audioPlayer.stop();  
    }
    
    public void close(){//�ͷ���Դ  
        audioPlayer.close();  
    }
      
    public static void main(String [] args) throws MalformedURLException, Exception{  
        
    }
    public void controllerUpdate(ControllerEvent ce) { //����player������¼�
    	if (ce instanceof EndOfMediaEvent) { 
			audioPlayer.prefetch(); //playerʵ������ɺ����player����ǰԤ����
			audioPlayer.setMediaTime(new Time(0)); //��������Ƶ��ɺ󣬰�ʱ��������ָ�����ʼ��
			audioPlayer.start(); //�ٴ����¿�ʼ����
    	}
    }
    
    
}  