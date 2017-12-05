package com.jason.tank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.*;
import java.util.Vector;

class Tank{
	int x = 0;//横坐标
	int y = 0;//纵坐标
	int dir;//方向，上下左右	
//	private int color;//颜色
	int speed = 3;//速度
	boolean isAlive = true;
	
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
//	public int getColor() {
//		return color;
//	}
//	public void setColor(int color) {
//		this.color = color;
//	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}

class EnemyTank extends Tank implements Runnable{
	Vector<Bullet> ht_bullets = new Vector<Bullet>();
	Vector<EnemyTank> ets = new Vector<EnemyTank>();

	private int step = 10;
	
	public EnemyTank(int x, int y) {
		super(x, y);
		this.setDir(2);//默认向下
	}
	
	public void setEts(Vector<EnemyTank> ets) {
		this.ets = ets;
	}
	
	public void moveUp(){
		dir = 1;
		y -= speed;
		
//		if(y < 15){
//			y = 15;
//		}
	}
	//坦克向下移动
	public void moveDown(){
		dir = 2;
		y += speed;
		
//		if(y > 285){
//			y = 285;
//		}
	}
	//坦克向左移动
	public void moveLeft(){
		dir = 3;
		x -= speed;
		
//		if(x < 15){
//			x = 15;
//		}
	}
	//坦克向右移动
	public void moveRight(){
		dir = 4;
		x += speed;
		
//		if(x > 385){
//			x = 385;
//		}
	}
	
	public void shotHero(){
		Bullet bullet = new Bullet();
		bullet.setType(2);
		
		if (dir == 1) {
			// up
			bullet.setX(x - 1);
			bullet.setY(y - 20);
			bullet.setDir(1);
		} else if (dir == 2) {
			// down
			bullet.setX(x - 1);
			bullet.setY(y + 15);
			bullet.setDir(2);
		} else if (dir == 3) {
			// left
			bullet.setX(x - 20);
			bullet.setY(y - 1);
			bullet.setDir(3);
		} else if (dir == 4) {
			// right
			bullet.setX(x + 15);
			bullet.setY(y - 1);
			bullet.setDir(4);
		} else {
			System.out.println("enemy's direction is not in range");
			System.exit(-1);
		}
		
		ht_bullets.add(bullet);
		Thread t = new Thread(bullet);
		t.start();
	}
	
	public boolean isTouched(){
		for(int i=0;i<ets.size();i++){
			EnemyTank et = ets.get(i);
			
			for(int j=i+1;j<ets.size();j++){
				EnemyTank et1 = ets.get(j);
//				math.a
				switch(et.dir){
				case 1:
					if(et1.dir == 1 || et1.dir == 2){
						if(Math.abs(et.x - et1.x) <= 20 && Math.abs(et.y - et1.y) <= 60){
							return true;
						}
					}else if(et1.dir == 3 || et1.dir == 4){
						if(Math.abs(et.x - et1.x) <= 25 && Math.abs(et.y - et1.y) <= 50){
							return true;
						}
					}
					break;
				case 2:
					if(et1.dir == 1 || et1.dir == 2){
						if(Math.abs(et.x - et1.x) <= 20 && Math.abs(et.y - et1.y) <= 60){
							return true;
						}
					}else if(et1.dir == 3 || et1.dir == 4){
						if(Math.abs(et.x - et1.x) <= 25 && Math.abs(et.y - et1.y) <= 50){
							return true;
						}
					}
					break;
				case 3:
					if(et1.dir == 1 || et1.dir == 2){
						if(Math.abs(et.x - et1.x) <= 50 && Math.abs(et.y - et1.y) <= 50){
							return true;
						}
					}else if(et1.dir == 3 || et1.dir == 4){
						if(Math.abs(et.x - et1.x) <= 60 && Math.abs(et.y - et1.y) <= 40){
							return true;
						}
					}
					break;
				case 4:
					if(et1.dir == 1 || et1.dir == 2){
						if(Math.abs(et.x - et1.x) <= 50 && Math.abs(et.y - et1.y) <= 50){
							return true;
						}
					}else if(et1.dir == 3 || et1.dir == 4){
						if(Math.abs(et.x - et1.x) <= 60 && Math.abs(et.y - et1.y) <= 40){
							return true;
						}
					}
					break;
				default:
					System.out.println("Direction error in isTouched().");
					System.exit(-1);
					break;
				}
			}
			
		}
		
		return false;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			dir = (int) (Math.random() * (5 - 1) + 1);// 随机得到坦克的方向1-4
			switch (dir) {
			case 1:
				for(int i=0;i<step;i++){
					if(y > 15 && !isTouched()){
						moveUp();
					}else{
						break;
					}
					try {
						Thread.sleep(150);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=0;i<step;i++){
					if(y < 285 && !isTouched()){
						moveDown();
					}else{
						break;
					}
					try {
						Thread.sleep(150);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for(int i=0;i<step;i++){
					if(x > 15 && !isTouched()){
						moveLeft();
					}else{
						break;
					}
					try {
						Thread.sleep(150);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 4:
				for(int i=0;i<step;i++){
					if(x < 385 && !isTouched()){
						moveRight();
					}else{
						break;
					}
					try {
						Thread.sleep(150);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				System.exit(-1);
			}

			if (!isAlive) {
				break;
			}
		}

	}

}

class HeroTank extends Tank{
	Vector<Bullet> ht_bullets = new Vector<Bullet>();
	
	public HeroTank(int x, int y) {
		super(x, y);
		dir = 1;//默认向上
	}
	
	
	//坦克向上移动
	public void moveUp(){
		dir = 1;
		y -= speed;
		
		if(y < 15){
			y = 15;
		}
	}
	//坦克向下移动
	public void moveDown(){
		dir = 2;
		y += speed;
		
		if(y > 285){
			y = 285;
		}
	}
	//坦克向左移动
	public void moveLeft(){
		dir = 3;
		x -= speed;
		
		if(x < 15){
			x = 15;
		}
	}
	//坦克向右移动
	public void moveRight(){
		dir = 4;
		x += speed;
		
		if(x > 385){
			x = 385;
		}
	}
	
	public void shotEnemy(){
		Bullet bullet = new Bullet();
		bullet.setType(1);
		
		if (dir == 1) {
			// up
			bullet.setX(x - 1);
			bullet.setY(y - 20);
			bullet.setDir(1);
		} else if (dir == 2) {
			// down
			bullet.setX(x - 1);
			bullet.setY(y + 15);
			bullet.setDir(2);
		} else if (dir == 3) {
			// left
			bullet.setX(x - 20);
			bullet.setY(y - 1);
			bullet.setDir(3);
		} else if (dir == 4) {
			// right
			bullet.setX(x + 15);
			bullet.setY(y - 1);
			bullet.setDir(4);
		} else {
			System.out.println("hero's direction is not in range");
			System.exit(-1);
		}
		
		ht_bullets.add(bullet);
		Thread t = new Thread(bullet);
		t.start();
	}
}

class Bullet implements Runnable{
	private int x = 0;
	private int y = 0;
	private int dir;
	private boolean isAlive = true;
	private int type;
	private int speed = 5;
	
	public void run(){
		while (true) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (this.dir == 1) {
				this.y -= this.speed;
			} else if (this.dir == 2) {
				this.y += this.speed;
			} else if (this.dir == 3) {
				this.x -= this.speed;
			} else if (this.dir == 4) {
				this.x += this.speed;
			}

			if (x < 0 || x > 400 || y < 0 || y > 300) {
				this.isAlive = false;
				break;
			}
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

class Bomb{
	private int x;
	private int y;
	int life;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Bomb(int x, int y){
		this.x = x;
		this.y = y;
		life = 15;
	}
	
	public void lifeDown(){
		life--;
	}
}

class Record{
	private FileWriter fw = null;
	private FileReader fr = null;
	private BufferedWriter bw = null;
	private BufferedReader br = null;
	
	HeroTank ht = null;
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	boolean flag = false;
	
	public void setRecord(Vector<EnemyTank> ets, HeroTank ht){
		try {
			fw = new FileWriter("D:\\java_dev\\TankGame\\src\\tankRecord");
			bw = new BufferedWriter(fw);
			
			if (ht.isAlive) {
				bw.write("HeroTank" + " " + ht.x + " " + ht.y + " " + ht.dir + "\r\n");
			}
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				
				if(et.isAlive){
					bw.write("EnemyTank" + " " + et.x + " " + et.y + " " + et.dir + "\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				bw.close();
				fw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void getAllRecord(){
		try{
			fr = new FileReader("D:\\java_dev\\TankGame\\src\\tankRecord");
			br = new BufferedReader(fr);
			
			String rl;
			while((rl = br.readLine()) != null){
				String []Recovery=rl.split(" ");
				if(Recovery[0].equals("HeroTank")){
					ht = new HeroTank(0, 0);
					ht.x = Integer.parseInt(Recovery[1]);
					ht.y = Integer.parseInt(Recovery[2]);
					ht.dir = Integer.parseInt(Recovery[3]);
					
					flag = true;
				}else if(Recovery[0].equals("EnemyTank")){
					EnemyTank et = new EnemyTank(0, 0);
					et.x = Integer.parseInt(Recovery[1]);
					et.y = Integer.parseInt(Recovery[2]);
					et.dir = Integer.parseInt(Recovery[3]);
					ets.add(et);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}



