package com.hfqs.shoot;

import java.util.Random;

/**
 * �۷�
 * @author hanfuqingshi
 *
 */
public class Bee extends FlyingObject implements Enemy {
	private int xSpeed = 1;//x�����ƶ�������ٶ�
	private int ySpeed = 1;//y���귽����ƶ��ٶ�
	private int awardType;//��������
	
	public Bee() {
		this.image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y-=height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
//		x = 100;
//		y = 200;
		awardType = rand.nextInt(2);//����һ������0С��2�������
				
	}
	public int getScore() {
		return 0;
	}
	

	public int getType() {
		return awardType;
	}
	
	@Override
	public void step() {//��б��
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
	public boolean outOfBounds() {//����Խ��
		return y>ShootGame.HEIGHT;
	}
}
