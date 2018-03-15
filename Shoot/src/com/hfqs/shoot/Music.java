package com.hfqs.shoot;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.JFrame;


public class Music extends JFrame {
	File f;
	URI uri;
	URL url;
	String musicPath;  
	AudioClip aau;
	// Music(){
	// bgMusic();
	// }
	Music(String path) {
		this.musicPath = path;
		try {
			f = new File(this.musicPath);
			uri = f.toURI();
			url = uri.toURL(); // 解析地址
			this.aau = Applet.newAudioClip(url);
			this.aau.loop(); // 循环播放
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		this.aau.stop();
	}
	
	public void play() {
		this.aau.play();
	}
}
