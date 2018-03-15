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
	public static final int WIDTH = 400;//����
	public static final int HEIGHT = 645;//����
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	
	private FlyingObject[] flyings = {};//�л�����
	private Bullet[] bullets = {};
	private Hero[] heros = {};
	private Timer timer;//��ʱ��
	private int intervel = 1000/80;//ʱ���������룩
	private int score = 0;//�÷�
	
	int flyEnteredIndex = 0;//�������������
	int shootIndex = 0;//�������
	
	Hero hero = new Hero();
	
	public ShootGame() {
		//��ʼ��һֻ�۷䣬һ�ܷɻ�
//		flyings = new FlyingObject[2];
//		flyings[0] = new Airplane();
//		flyings[1] = new Bee();
		//��ʼ���ӵ�
//		bullets = new Bullet[1];
//		bullets[0] =new Bullet(200, 350);
	}
	
	//��̬�����
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
		g.drawImage(background, 0, 0, null);//������ͼ
		paintHero(g);//��Ӣ�ۻ�
		paintBullets(g);//���ӵ�
		paintFlyingObjects(g);//��������
	}
	/**
	 * ��Ӣ�ۻ�
	 * @param g
	 */
	public void paintHero(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			g.drawImage(hero.getImage(),hero.getX(),hero.getY(),null);
		}
	}
	/**
	 * ���ӵ�
	 * @param g
	 */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImage(), b.getX(), b.getY(), null);
			
		}
	}
	
	/**
	 * ��������
	 * @param g
	 */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
			
		}
	}
	
	/**
	 * ������ɷ����� 
	 * @return ���������
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
	 * �������볡
	 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex%40==0) {
			FlyingObject obj = nextOne();//�������һ��������
			flyings = Arrays.copyOf(flyings, flyings.length+1);//����
			flyings[flyings.length-1] = obj;//�������һλ
		}
	}
	
	public void stepAction() {
		//��������һ��
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			f.step();
		}
		//�ӵ���һ��
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			b.step();
		}
		Hero hero = new Hero();
		hero.step();
	}
	
	
	/**
	 * �ӵ��������
	 */
	public void shootAction() {
		
		shootIndex++;
		if (shootIndex%30==0) {//100���뷢һ��
			Bullet[] bs = hero.shoot();//Ӣ�۴���ӵ�
			bullets = Arrays.copyOf( bullets, bullets.length+bs.length);//����
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);//׷������
			
		}
	}
	
	/**
	 * �ӵ����������ײ���
	 */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			bang(b);
		}
	}
	
	/**
	 * �ӵ��ͷ�����֮�����ײ���
	 * @param bullet
	 */
	public void bang(Bullet bullet) {
		int index = -1;//���з����������
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) {
				index = i;//��¼�����еķ����������
				break;
			}
		}
		if (index!=-1) {
			FlyingObject one = flyings[index];//��¼�����еķ�����
			FlyingObject temp = flyings[index];//�����еķ����������һ�������ｻ��
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = temp;
			//ɾ�����һ��������������еģ�
			flyings = Arrays.copyOf(flyings, flyings.length-1);
			
			//���one������  ����ǵ��ˣ������
			if (one instanceof Enemy) {
				Enemy e = (Enemy)one;
				score += e.getScore();//�ӷ�
			}
			if (one instanceof Award) {
				Award award = (Award)one;
				int type = award.getType();//��ȡ��������
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();//����˫������
					break;
				case Award.LIFT:
					hero.addLife();//���ü���
					break;	
				}
			}
		}
	}

	/**
	 * ���̿���
	 */
	public void action() {
		//������ʱ��
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {//����ƶ�
				int x = e.getX();
				int y = e.getY();
				hero.moveTo(x, y);;
			}
		};
		this.addMouseListener(l);;
		timer = new Timer();//�����̿���
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				enterAction();//�������볡
				stepAction();//��һ��
				shootAction();//���
				bangAction();
				repaint();//�ػ棬����paint()����
				
			}
		}, intervel,intervel);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();//������
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ĭ�Ϲرղ���
		frame.setLocationRelativeTo(null);//���ô����ʼλ��
		frame.setVisible(true);//�������paint
		
		game.action();//������ִ��
	}
	
}
