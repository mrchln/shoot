package com.hfqs.shoot;
/**
 * Ӣ�ۻ����Ƿ�����
 * @author hanfuqingshi
 *
 */

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	protected BufferedImage[] images = {};//Ӣ����ͼ
	protected int index = 0;//��ͼ������ʾ�ļ���
	
	private int doubleFire;
	private int life;
	
	public Hero() {
		life = 3;
		doubleFire = 0;
		this.image = ShootGame.hero0;
		images = new BufferedImage[] {ShootGame.hero0,ShootGame.hero1};
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
	}

	@Override
	public void step() {
		if (images.length>0) {
			image = images[index++/10%images.length];
			System.out.println(index++%images.length);
		}
		
	}
	/**
	 * �����ӵ�
	 * @return �ӵ�����
	 */
	public Bullet[] shoot() {
		int xStep = width/4;
		int yStep = 20;
		if (doubleFire>0) {
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x+xStep, y-yStep);
			bullets[1] = new Bullet(x+3*xStep, y-yStep);
			doubleFire-=2;
			return bullets;
		}else {//����
			Bullet[] bullets = new Bullet[1];
			//y-yStep �ӵ����ɻ���λ��
			bullets[0] = new Bullet(x+2*xStep, y-yStep);
			return bullets;
		}
	}
	
	/**
	 * �������ƶ���һ�£���Ծ��룬x��y���λ��
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		this.x = x -width/2;
		this.y = y - height/2;
	}
	
	public void addDoubleFire() {
		doubleFire += 40;
	}
	
	public void addLife() {//����
		life++;
	}
}
