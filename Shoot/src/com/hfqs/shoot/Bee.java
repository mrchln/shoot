package com.hfqs.shoot;

import java.util.Random;

/**
 * 蜜蜂
 * @author hanfuqingshi
 *
 */
public class Bee extends FlyingObject implements Enemy {
	private int xSpeed = 1;//x坐标移动方向的速度
	private int ySpeed = 1;//y坐标方向的移动速度
	private int awardType;//奖励类型
	
	public Bee() {
		this.image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y-=height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
//		x = 100;
//		y = 200;
		awardType = rand.nextInt(2);//返回一个大于0小于2的随机数
				
	}
	public int getScore() {
		return 0;
	}
	

	public int getType() {
		return awardType;
	}
	
	@Override
	public void step() {//可斜飞
		x+=xSpeed;
		y+=ySpeed;
		if (x>ShootGame.WIDTH - width) {
			xSpeed = -1;
		}
		if (x<0) {
			xSpeed = 1;
		}
		
	}
	@Override
	public boolean outOfBounds() {//处理越界
		return y>ShootGame.HEIGHT;
	}
}
