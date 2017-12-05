package com.jason.tank;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.*;
import java.lang.*;

public class TankGame extends JFrame implements ActionListener{
	MyPanel mp = null;
	Record rd = null;
	boolean gameFlag = false;
	
	//定义一个开始面板
	MyStartPanel msp=null;
	//做出菜单
	JMenuBar jmb=null;
	//开始游戏
	JMenu jm1=null;
	JMenuItem jmi1=null;
	//退出游戏
	JMenuItem jmi2=null;
	//存盘退出
	JMenuItem jmi3=null;
	//接上局
	JMenuItem jmi4=null;
	
	public TankGame(){
		
		//创建菜单及菜单选项
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		//设置快捷方式
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi1.setMnemonic('N');
		//对jmi1相应
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		
		jmi2=new JMenuItem("退出游戏(E)");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		
		jmi3=new JMenuItem("存盘退出(S)");
		jmi3.setMnemonic('S');
		jmi3.addActionListener(this);
		jmi3.setActionCommand("save");
		
		jmi4=new JMenuItem("继续游戏(C)");
		jmi4.setMnemonic('C');
		jmi4.addActionListener(this);
		jmi4.setActionCommand("congame");
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		
		//构建组件
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		this.add(msp);
		
//		mp = new MyPanel();
//		Thread t = new Thread(mp);
//		this.add(mp);
//		this.addKeyListener(mp);
		this.setTitle("坦克大战");
		this.setSize(600,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
//		t.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("newgame")) {
			mp = new MyPanel(gameFlag);
			Thread t = new Thread(mp);
			this.add(mp);
			this.addKeyListener(mp);
			this.remove(msp);
			t.start();
			this.setVisible(true);
		}else if(e.getActionCommand().equals("exit")){
			rd = new Record();
			rd.setRecord(mp.ets, mp.hero);
			System.exit(0);
		}else if(e.getActionCommand().equals("save")){
			rd = new Record();
			rd.setRecord(mp.ets, mp.hero);
			System.exit(0);
		}else if(e.getActionCommand().equals("congame")){
			gameFlag = true;
			
			mp = new MyPanel(gameFlag);
			Thread t = new Thread(mp);
			this.add(mp);
			this.addKeyListener(mp);
			this.remove(msp);
			t.start();
			this.setVisible(true);
		}
	}
	
	public static void main(String[] args) {
		TankGame tg = new TankGame();
	}

}

class MyStartPanel extends JPanel implements Runnable{
	int times=0;
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 300);
		//提示信息
		if(times%2==0){
			g.setColor(Color.red);
			//开关信息的字体
			Font myFont=new Font("华文新魏", Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("Stage: 1", 150, 150);
		}	
	}

	public void run() {
		while(true){
			//休眠
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			times++;
			//重画
			this.repaint();
		}
	}
}

class MyPanel extends JPanel implements KeyListener,Runnable{
	HeroTank hero = null;
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	Vector<Bomb> bombs = new Vector<Bomb>();
	
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	int entersize = 4;
	boolean start = true;
	
	public MyPanel(boolean flag){
		image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
		image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
		
		if(flag){
			Record rd= new Record();
			rd.getAllRecord();
			
			if(rd.flag){
				ets = rd.ets;
				hero = rd.ht;
				
				for (int i = 0; i < ets.size(); i++) {
					EnemyTank et = ets.get(i);
					et.setEts(ets);

					// 增加子弹
					et.shotHero();

					Thread t = new Thread(et);
					t.start();
				}
			}else{
				System.exit(-1);
			}
		} else {
			hero = new HeroTank(200, 285);
			for (int i = 0; i < entersize; i++) {
				EnemyTank et = new EnemyTank(100 + i * 80, 15);
				et.setEts(ets);
				ets.add(et);

				// 增加子弹
				et.shotHero();

				Thread t = new Thread(et);
				t.start();
			}
		}
		
	}
	public void paint(Graphics g){
		super.paint(g);
		this.setBackground(Color.BLACK);
		this.setSize(400,300);
		drawHeroTank(g);
		drawEnemyTank(g);
		drawHeroBullets(g);
		drawBombs(g);
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(50);
			}catch(Exception e){
				e.printStackTrace();
			}
			this.hitEnemyTank();
			this.hitHeroTank();
			
			if(start){
				this.repaint();
			}
		}
	}
	
	public void drawBombs(Graphics g) {
		for (int i = 0; i < bombs.size(); i++) {
			Bomb bb = bombs.get(i);

			if (bb.life > 10) {
				g.drawImage(image1, bb.getX(), bb.getY(), 30, 30, this);
				System.out.println("go");
//				g.drawString("hello", 100, 100);
			} else if (bb.life > 5) {
				g.drawImage(image2, bb.getX(), bb.getY(), 30, 30, this);
			} else if (bb.life > 0) {
				g.drawImage(image3, bb.getX(), bb.getY(), 30, 30, this);
			} else {

				bombs.remove(bb);
			}
			bb.lifeDown();
		}
	}
	
	public void drawHeroTank(Graphics g){
		if(hero.isAlive){
			drawTank(hero.getX(), hero.getY(), hero.getDir(), 1, g);
		}else{
			g.setColor(Color.red);
			g.setFont(new Font("黑体",Font.BOLD,40));
			g.drawString("Game Over", 100, 150);
			
			start = false;
		}
	}
	
	public void drawEnemyTank(Graphics g){
		if(ets.size() == 0){
			g.setColor(Color.red);
			g.setFont(new Font("黑体",Font.BOLD,40));
			g.drawString("You Win", 100, 150);
			
			start = false;
		}
		for(int i=0;i<ets.size();i++){
			EnemyTank et = ets.get(i);
			
			if(et.isAlive()){
				
				//画tank
				drawTank(et.getX(), et.getY(), et.getDir(), 2, g);
				//画子弹
				if(et.ht_bullets.size() == 0){
					et.shotHero();
				}
				drawBullets(g, et.ht_bullets);
			}else{
				ets.remove(et);
			}
		}
	}
	
	public void drawHeroBullets(Graphics g){
		drawBullets(g, hero.ht_bullets);
	}
	
	
	public void drawBullets(Graphics g, Vector<Bullet> bullets){
		for(int i=0;i<bullets.size();i++){
			Bullet bullet = bullets.get(i);
			if(bullet.isAlive()){
				drawBullet(bullet.getX(), bullet.getY(), bullet.getType(), bullet.getDir(), g);
			}else{
				bullets.remove(bullet);
			}
		}
	}
	
    public void keyTyped(KeyEvent e){
    	
    }
    public void keyPressed(KeyEvent e){
    	if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
    		this.hero.moveUp();
    	}else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
    		this.hero.moveDown();
    	}else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
    		this.hero.moveLeft();
    	}else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
    		this.hero.moveRight();
    	}
    	
    	if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if (hero.ht_bullets.size() < 5) {
				hero.shotEnemy();
			}
    	}
    	
    	this.repaint();
    }
    public void keyReleased(KeyEvent e){
    	
    }
	
	public void drawTank(int x, int y, int dir, int type, Graphics g){
		switch (type) {
		case 1:
			//Hero Tank,blue
			g.setColor(Color.cyan);
			break;
		case 2:
			//Enemy Tank,yellow
			g.setColor(Color.YELLOW);
			break;
		default:
			System.exit(-1);
			break;
		}
		
		switch (dir) {
		case 1:
			//up
			g.fill3DRect(x-10, y-15, 5, 30, false);
			g.fill3DRect(x+5, y-15, 5, 30, false);
			g.fill3DRect(x-5, y-10, 10, 20, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y-15, x, y);
			g.fill3DRect(x-1, y-15, 3, 3, false);
			break;
		case 2:
			//down
			g.fill3DRect(x-10, y-15, 5, 30, false);
			g.fill3DRect(x+5, y-15, 5, 30, false);
			g.fill3DRect(x-5, y-10, 10, 20, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y+15, x, y);
			g.fill3DRect(x-1, y+12, 3, 3, false);
			break;
		case 3:
			//left
			g.fill3DRect(x-15, y-10, 30, 5, false);
			g.fill3DRect(x-15, y+5, 30, 5, false);
			g.fill3DRect(x-10, y-5, 20, 10, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x-15, y, x, y);
			g.fill3DRect(x-15, y-1, 3, 3, false);
			break;
		case 4:
			//right
			g.fill3DRect(x-15, y-10, 30, 5, false);
			g.fill3DRect(x-15, y+5, 30, 5, false);
			g.fill3DRect(x-10, y-5, 20, 10, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x+15, y, x, y);
			g.fill3DRect(x+13, y-1, 3, 3, false);
			break;
		default:
			System.exit(-1);
			break;
		}
	}
	
	public void drawBullet(int x, int y, int type, int dir, Graphics g){
		switch (type) {
		case 1:
			//Hero Bullet,red
			g.setColor(Color.RED);
			
			break;
		case 2:
			//Enemy Bullet,yellow
			g.setColor(Color.YELLOW);
			break;
		default:
			System.exit(-1);
			break;
		}
		
		if(dir == 1 || dir == 2){
			g.fill3DRect(x, y, 3, 5, false);
		}else if(dir == 3 || dir == 4){
			g.fill3DRect(x, y, 5, 3, false);
		}
		
	}
	
	public void hitEnemyTank(){
		for(int i=0;i<ets.size();i++){
			if(ets.get(i).isAlive()){
				for(int j=0;j<hero.ht_bullets.size();j++){
					if(hero.ht_bullets.get(j).isAlive()){
						hitTank(hero.ht_bullets.get(j), ets.get(i));
					}
				}
			}
		}
	}
	
	public void hitHeroTank(){
		for(int i=0;i<ets.size();i++){
			if(ets.get(i).isAlive()){
				for(int j=0;j<ets.get(i).ht_bullets.size();j++){
					if(ets.get(i).ht_bullets.get(j).isAlive()){
						hitTank(ets.get(i).ht_bullets.get(j), hero);
					}
				}
			}
		}
	}
	
	public void hitTank(Bullet bullet, Tank tk){
		
		switch(bullet.getDir()){
		case 1:
			switch(tk.getDir()){
			case 1:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 15){
					tk.setAlive(false);
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 2:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 15){
					tk.setAlive(false);
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 3:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 10 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			case 4:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 10 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			default:
				System.exit(-1);
				break;
			}
			break;
		case 2:
			switch(tk.getDir()){
			case 1:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 12 <= bullet.getY() && bullet.getY() <= tk.getY() + 18){
					tk.setAlive(false);
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 2:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 12 <= bullet.getY() && bullet.getY() <= tk.getY() + 18){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 3:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 7 <= bullet.getY() && bullet.getY() <= tk.getY() + 13){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			case 4:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 7 <= bullet.getY() && bullet.getY() <= tk.getY() + 13){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			default:
				System.exit(-1);
				break;
			}
			break;
		case 3:
			switch(tk.getDir()){
			case 1:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 16){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 2:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 16){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 3:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 13 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			case 4:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 13 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			default:
				System.exit(-1);
				break;
			}
			break;
		case 4:
			switch(tk.getDir()){
			case 1:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 16){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 2:
				if(tk.getX() - 13 <= bullet.getX() && bullet.getX() <= tk.getX() +10 && tk.getY() - 15 <= bullet.getY() && bullet.getY() <= tk.getY() + 16){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 10, tk.getY() - 15);
					
					bombs.add(bb);
				}
				break;
			case 3:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 13 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			case 4:
				if(tk.getX() - 18 <= bullet.getX() && bullet.getX() <= tk.getX() +15 && tk.getY() - 13 <= bullet.getY() && bullet.getY() <= tk.getY() + 10){
					tk.setAlive(false);
					
					bullet.setAlive(false);
					
					Bomb bb = new Bomb(tk.getX() - 15, tk.getY() - 10);
					
					bombs.add(bb);
				}
				break;
			default:
				System.exit(-1);
				break;
			}
			break;
		default:
			System.exit(-1);
			break;
		}
	}
}
