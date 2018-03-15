package com.hfqs.shoot;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	protected int x;//����x
	protected int y;//����y
	protected int width;//��
	protected int height;//��
	protected BufferedImage image;//ͼƬ
	/**
	 * �������ƶ�һ��
	 */
	public abstract void step();
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int y) {
		this.y = y;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * ��鵱ǰ�������Ƿ��ӵ�����
	 * true��ʾ�����У���������Ա�����
	 * @param bullet �ӵ�����
	 * @return true ��ʾ��������
	 */
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x;//�ӵ�������
		int y = bullet.y;//�ӵ�������
		return this.x<x&&x<this.x+width&&this.y<y&&y<this.y+height;
	}
}
