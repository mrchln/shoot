package com.hfqs.shoot;
/**
 * �ӵ��ࣺ�Ƿ�����
 * @author hanfuqingshi
 *
 */
public class Bullet extends FlyingObject{
	private int speed = 2;//�ƶ��ٶ�
	
	public Bullet(int x,int y) {
		this.x = x;
		this.y = y;
		this.image = ShootGame.bullet;
	}

	@Override
	public void step() {
		y-=speed;
		
	}
}
