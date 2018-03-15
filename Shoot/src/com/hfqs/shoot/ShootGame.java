package com.hfqs.shoot;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel{
	public static final int WIDTH = 400;//面板宽
	public static final int HEIGHT = 645;//面板高
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	
	private FlyingObject[] flyings = {};//敌机数组
	private Bullet[] bullets = {};
	private Hero[] heros = {};
	private Timer timer;//定时器
	private int intervel = 1000/80;//时间间隔（毫秒）
	private int score = 0;//得分
	
	int flyEnteredIndex = 0;//飞行物进场计数
	int shootIndex = 0;//射击次数
	
	Hero hero = new Hero();
	
	public ShootGame() {
		//初始化一只蜜蜂，一架飞机
//		flyings = new FlyingObject[2];
//		flyings[0] = new Airplane();
//		flyings[1] = new Bee();
		//初始化子弹
//		bullets = new Bullet[1];
//		bullets[0] =new Bullet(200, 350);
	}
	
	//静态代码块
	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			//start = ImageIO.read(ShootGame.class.getResource("start.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			//pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			//gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);//画背景图
		paintHero(g);//画英雄机
		paintBullets(g);//画子弹
		paintFlyingObjects(g);//画飞行物
	}
	/**
	 * 画英雄机
	 * @param g
	 */
	public void paintHero(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			g.drawImage(hero.getImage(),hero.getX(),hero.getY(),null);
		}
	}
	/**
	 * 画子弹
	 * @param g
	 */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImage(), b.getX(), b.getY(), null);
			
		}
	}
	
	/**
	 * 画飞行物
	 * @param g
	 */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
			
		}
	}
	
	/**
	 * 随机生成飞行物 
	 * @return 飞行物对象
	 */
	public static FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20);//[0,19]
		if (type==0) {
			return new Bee();
		}else {
			return new Airplane();
		}
	}
	/**
	 * 飞行物入场
	 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex%40==0) {
			FlyingObject obj = nextOne();//随机生成一个飞行物
			flyings = Arrays.copyOf(flyings, flyings.length+1);//扩容
			flyings[flyings.length-1] = obj;//放入最后一位
		}
	}
	
	public void stepAction() {
		//飞行物走一步
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			f.step();
		}
		//子弹走一步
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			b.step();
		}
		Hero hero = new Hero();
		hero.step();
	}
	
	
	/**
	 * 子弹射击方法
	 */
	public void shootAction() {
		
		shootIndex++;
		if (shootIndex%30==0) {//100毫秒发一颗
			Bullet[] bs = hero.shoot();//英雄打出子弹
			bullets = Arrays.copyOf( bullets, bullets.length+bs.length);//扩容
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);//追加数组
			
		}
	}
	
	/**
	 * 子弹与飞行物碰撞检测
	 */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			bang(b);
		}
	}
	
	/**
	 * 子弹和飞行物之间的碰撞检测
	 * @param bullet
	 */
	public void bang(Bullet bullet) {
		int index = -1;//击中飞行物的索引
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) {
				index = i;//记录被击中的飞行物的索引
				break;
			}
		}
		if (index!=-1) {
			FlyingObject one = flyings[index];//记录被击中的飞行物
			FlyingObject temp = flyings[index];//被击中的飞行物与最后一个飞行物交换
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = temp;
			//删除最后一个飞行物（即被击中的）
			flyings = Arrays.copyOf(flyings, flyings.length-1);
			
			//检查one的类型  如果是敌人，就算分
			if (one instanceof Enemy) {
				Enemy e = (Enemy)one;
				score += e.getScore();//加分
			}
			if (one instanceof Award) {
				Award award = (Award)one;
				int type = award.getType();//获取奖励类型
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();//设置双倍火力
					break;
				case Award.LIFT:
					hero.addLife();//设置加命
					break;	
				}
			}
		}
	}

	/**
	 * 流程控制
	 */
	public void action() {
		//鼠标监听时间
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {//鼠标移动
				int x = e.getX();
				int y = e.getY();
				hero.moveTo(x, y);;
			}
		};
		this.addMouseListener(l);;
		timer = new Timer();//主流程控制
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				enterAction();//飞行物入场
				stepAction();//走一步
				shootAction();//射击
				bangAction();
				repaint();//重绘，调用paint()方法
				
			}
		}, intervel,intervel);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();//面板对象
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认关闭操作
		frame.setLocationRelativeTo(null);//设置窗体初始位置
		frame.setVisible(true);//尽快调用paint
		
		game.action();//启动后执行
	}
	
}
