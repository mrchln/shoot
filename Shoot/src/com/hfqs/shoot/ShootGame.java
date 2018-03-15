package com.hfqs.shoot;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	//public static Music bgMusic;
	public static SimpleAudioPlayer bgMusic;  
	
	private FlyingObject[] flyings = {};//敌机数组
	private Bullet[] bullets = {};
	private Hero[] heros = {};
	private Timer timer;//定时器
	private int intervel = 1000/80;//时间间隔（毫秒）
	private int score = 0;//得分
	private int state;
	
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
		bgMusic.stop();
	}
	
	//静态代码块
	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			bgMusic = new SimpleAudioPlayer("bgMusic.wav");
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
		paintScore(g);//画分数
		paintState(g);//画游戏状态
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
		int index=-1;
    	for(int i=0;i<flyings.length;i++){
    		FlyingObject obj=flyings[i];
    		if(obj.shootBy(bullet)){
    			index=i;
    			break;
    		}
    	}
    	if(index!=-1){//被撞上了
    		FlyingObject one=flyings[index];
    		if(one instanceof Enemy){//敌机
    			Enemy e=(Enemy)one;
    			score+=e.getScore();
    		}
    		if(one instanceof Award){//奖励
    			Award a=(Award)one;
    			int type=a.getType();
    			switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
    		}
    		FlyingObject t=flyings[index];
    		flyings[index]=flyings[flyings.length-1];
    		flyings[flyings.length-1]=t;
    		flyings=Arrays.copyOf(flyings, flyings.length-1);//缩容
    	}
	}
	/**
	 * 画分数
	 * @param g
	 */
	public void paintScore(Graphics g) {
		int x =10;
		int y = 25;
		Font mf = new Font(Font.SANS_SERIF,Font.BOLD,14);
		g.setColor(new Color(0x3A3B3B));//设置字体
		g.setFont(mf);
		g.drawString("SCORE:"+score, x, y);//画分数
		y+=20;
		g.drawString("LIFT:"+hero.getLife(), x, y);
	}
	
	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	/**
	 * 飞行物越界情况
	 */
	public void outOfBoundsAction() {
		int index = 0;
		//存储活着的飞行物
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index++] = f;//不越界的留着
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);//将不越界的飞行物都留着
		index = 0;//重置为0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);//将不越界的子弹都留着
	}
	
	/**
	 * 判断是否没有生命数
	 * @return
	 */
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) {//检测英雄机与飞行物是否碰撞
				hero.substractLife();
				hero.setDoubleFire(0);;
				index = i;
			}
			if (index!=-1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length-1];
				flyings[flyings.length-1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLife()<=0;
	}
	
	/**
	 * 检测游戏是否结束
	 */
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER;//改变状态
		}
	}

	/**
	 * 流程控制
	 */
	public void action() {
		//鼠标操作事件
		MouseAdapter l=new MouseAdapter() {
			/** 鼠标移动事件*/
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING){
				int x=e.getX();//获取鼠标的x坐标
				int y=e.getY();//获取鼠标的y坐标
				hero.moveTo(x, y);//英雄机随鼠标移动
				}
			}
			public void mouseClicked(MouseEvent e) {//鼠标点击事件
				switch (state) {//判断当前状态
				case START:
					state=RUNNING;
					try {
						bgMusic = new SimpleAudioPlayer("bgMusic.wav");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					bgMusic.play();
					break;
				case GAME_OVER:
					flyings = new FlyingObject[0]; // 清空飞行物
					bullets = new Bullet[0]; // 清空子弹
					hero = new Hero(); // 重新创建英雄机
					score = 0; // 清空成绩
					state = START; // 状态设置为启动
					bgMusic.stop();
					bgMusic.close();
				
				}
			}
			public void mouseExited(MouseEvent e) {//鼠标移开事件
				if(state==RUNNING){
					state=PAUSE;
					bgMusic.stop();
				}
			}
			public void mouseEntered(MouseEvent e) {//移入事件
				if(state==PAUSE){
					state=RUNNING;
					bgMusic.play();
				}
			}
		};
		
		this.addMouseListener(l);//处理鼠标一般操作
		this.addMouseMotionListener(l);//处理鼠标滑动
		timer=new Timer();//创建定时器对象
		timer.schedule(new TimerTask(){
			public void run() {
				if(state==RUNNING){
				enterAction();//敌人入场
				stepAction();//飞行物走一步
				shootAction();//英雄机发子弹
				bangAction();//子弹和敌人的碰撞
				outOfBoundsAction();//删除出界对象
				checkGameOverAction();//检查游戏结束  
				}
				repaint();
			}}, intervel,intervel);//第一个intervel:程序启动到第一次干事的间隔
									//第二个intervel:每次干事的间隔
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();//面板对象
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认关闭操作
		frame.setLocationRelativeTo(null);//设置窗体初始位置
		frame.addWindowListener(new WindowAdapter() {  
            @Override  
            public void windowClosing(WindowEvent e) {  
                // TODO Auto-generated method stub  
                System.exit(0);  
                bgMusic.close();
            }  
        });  
		frame.setVisible(true);//尽快调用paint
		//bgMusic.stop();
		
		game.action();//启动后执行
	}
	
}
