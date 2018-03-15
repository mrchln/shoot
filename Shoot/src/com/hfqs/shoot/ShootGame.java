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
	public static final int WIDTH = 400;//����
	public static final int HEIGHT = 645;//����
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
	
	private FlyingObject[] flyings = {};//�л�����
	private Bullet[] bullets = {};
	private Hero[] heros = {};
	private Timer timer;//��ʱ��
	private int intervel = 1000/80;//ʱ���������룩
	private int score = 0;//�÷�
	private int state;
	
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
		bgMusic.stop();
	}
	
	//��̬�����
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
		g.drawImage(background, 0, 0, null);//������ͼ
		paintHero(g);//��Ӣ�ۻ�
		paintBullets(g);//���ӵ�
		paintFlyingObjects(g);//��������
		paintScore(g);//������
		paintState(g);//����Ϸ״̬
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
		int index=-1;
    	for(int i=0;i<flyings.length;i++){
    		FlyingObject obj=flyings[i];
    		if(obj.shootBy(bullet)){
    			index=i;
    			break;
    		}
    	}
    	if(index!=-1){//��ײ����
    		FlyingObject one=flyings[index];
    		if(one instanceof Enemy){//�л�
    			Enemy e=(Enemy)one;
    			score+=e.getScore();
    		}
    		if(one instanceof Award){//����
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
    		flyings=Arrays.copyOf(flyings, flyings.length-1);//����
    	}
	}
	/**
	 * ������
	 * @param g
	 */
	public void paintScore(Graphics g) {
		int x =10;
		int y = 25;
		Font mf = new Font(Font.SANS_SERIF,Font.BOLD,14);
		g.setColor(new Color(0x3A3B3B));//��������
		g.setFont(mf);
		g.drawString("SCORE:"+score, x, y);//������
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
	 * ������Խ�����
	 */
	public void outOfBoundsAction() {
		int index = 0;
		//�洢���ŵķ�����
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index++] = f;//��Խ�������
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);//����Խ��ķ����ﶼ����
		index = 0;//����Ϊ0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);//����Խ����ӵ�������
	}
	
	/**
	 * �ж��Ƿ�û��������
	 * @return
	 */
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) {//���Ӣ�ۻ���������Ƿ���ײ
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
	 * �����Ϸ�Ƿ����
	 */
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER;//�ı�״̬
		}
	}

	/**
	 * ���̿���
	 */
	public void action() {
		//�������¼�
		MouseAdapter l=new MouseAdapter() {
			/** ����ƶ��¼�*/
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING){
				int x=e.getX();//��ȡ����x����
				int y=e.getY();//��ȡ����y����
				hero.moveTo(x, y);//Ӣ�ۻ�������ƶ�
				}
			}
			public void mouseClicked(MouseEvent e) {//������¼�
				switch (state) {//�жϵ�ǰ״̬
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
					flyings = new FlyingObject[0]; // ��շ�����
					bullets = new Bullet[0]; // ����ӵ�
					hero = new Hero(); // ���´���Ӣ�ۻ�
					score = 0; // ��ճɼ�
					state = START; // ״̬����Ϊ����
					bgMusic.stop();
					bgMusic.close();
				
				}
			}
			public void mouseExited(MouseEvent e) {//����ƿ��¼�
				if(state==RUNNING){
					state=PAUSE;
					bgMusic.stop();
				}
			}
			public void mouseEntered(MouseEvent e) {//�����¼�
				if(state==PAUSE){
					state=RUNNING;
					bgMusic.play();
				}
			}
		};
		
		this.addMouseListener(l);//�������һ�����
		this.addMouseMotionListener(l);//������껬��
		timer=new Timer();//������ʱ������
		timer.schedule(new TimerTask(){
			public void run() {
				if(state==RUNNING){
				enterAction();//�����볡
				stepAction();//��������һ��
				shootAction();//Ӣ�ۻ����ӵ�
				bangAction();//�ӵ��͵��˵���ײ
				outOfBoundsAction();//ɾ���������
				checkGameOverAction();//�����Ϸ����  
				}
				repaint();
			}}, intervel,intervel);//��һ��intervel:������������һ�θ��µļ��
									//�ڶ���intervel:ÿ�θ��µļ��
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();//������
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ĭ�Ϲرղ���
		frame.setLocationRelativeTo(null);//���ô����ʼλ��
		frame.addWindowListener(new WindowAdapter() {  
            @Override  
            public void windowClosing(WindowEvent e) {  
                // TODO Auto-generated method stub  
                System.exit(0);  
                bgMusic.close();
            }  
        });  
		frame.setVisible(true);//�������paint
		//bgMusic.stop();
		
		game.action();//������ִ��
	}
	
}
