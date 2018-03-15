package com.hfqs.shoot;
/**
 * 子弹类：是飞行物
 * @author hanfuqingshi
 *
 */
public class Bullet extends FlyingObject{
	private int speed = 2;//移动速度
	
	public Bullet(int x,int y) {
		this.x = x;
		this.y = y;
		this.image = ShootGame.bullet;
	}

	@Override
	public void step() {
		y-=speed;
		
	}
	
	@Override
	public boolean outOfBounds() {//处理越界
		return y<-height;
	}
}
