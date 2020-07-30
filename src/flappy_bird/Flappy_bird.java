/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappy_bird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class Flappy_bird implements ActionListener,MouseListener,KeyListener
{

    public static Flappy_bird flappybird;
    public final int width =800,height=800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> column;
    public Random rand;
    public int ticks,ymotion,score;
    public boolean gameover,started;
    
    public Flappy_bird()
    {
        JFrame jframe=new JFrame();
        
        Timer timer=new Timer(20,this);
        renderer=new Renderer();
        jframe.add(renderer);
        jframe.setVisible(true);
        jframe.setSize(width, height);
        jframe.setTitle("Flappy_bird");
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        bird =new Rectangle(width/2 - 10,height/2 - 10,20,20);
        rand=new Random();
        column=new ArrayList<Rectangle>();
        
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        
        timer.start();
    }

    public void addColumn(boolean start)
    {
        int space =300;
        int Width =100;
        int Height=50+rand.nextInt(300);
        if(start){
        column.add(new Rectangle(width+Width+column.size()*300,height-Height-120,Width,Height));
        column.add(new Rectangle(width+Width+(column.size()-1)*300,0,Width,height-Height-space));
        }
        else
        {
            column.add(new Rectangle(column.get(column.size()-1).x+600,height-Height-120,Width,Height));
            column.add(new Rectangle(column.get(column.size()-1).x,0,Width,height-Height-space));
        }
    }
    
    public void paintColumn(Graphics g,Rectangle Column)
    {
        if(score<10){
        g.setColor(Color.green.darker());
        g.fillRect(Column.x, Column.y, Column.width, Column.height);}
        else
        {
            g.setColor(Color.CYAN.darker());
        g.fillRect(Column.x, Column.y, Column.width, Column.height);
        }
    }
    
    public void jump()
    {
        if(gameover)
        {
            bird =new Rectangle(width/2 - 10,height/2 - 10,20,20);
            
            gameover=false;
            ymotion=0;
            score=0;
            column.clear();
            addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        }
        if(!started)
        {
            started=true;
        }
        else if(!gameover)
        {
            if(ymotion!=0)
                ymotion=0;
                ymotion-=15;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        
        int speed=10;
        
        if(started){
            for (Rectangle col : column) {
                col.x-=speed;
            }
        
            for(int i=0;i<column.size();i++)
            {
                Rectangle col=column.get(i);
                if(col.x+col.width<0)
                {
                    column.remove(col);
        		if(col.y==0)
			addColumn(false);
		}
            }
        
            if(ticks%2==0&&ymotion<15)
            {
                ymotion+=2;
            }
            bird.y+=ymotion;
            for(Rectangle col:column)
            {
                if (col.y == 0 && bird.x + bird.width / 2 > col.x + col.width / 2 - 10 && bird.x + bird.width / 2 < col.x + col.width / 2 + 10)
		{
                    score++;
		}
                if(col.intersects(bird))
                {   
                    gameover=true;
                    if (bird.x <= col.x)
                    {
			bird.x = col.x - bird.width;
                    }
                    else
                    {
                        if (col.y != 0)
                        {
                            bird.y = col.y - bird.height;
                        }
			else if (bird.y < col.height)
                        {
                            bird.y = col.height;
                        }
                    }
                }
            }
            if(bird.y>height-120||bird.y<0)
            gameover=true;
            if (bird.y + ymotion >= height - 120)
            {
		bird.y = height - 120 - bird.height;
		gameover = true;
            }
            if(gameover)
            {
                bird.y=height-120-bird.height;
                started=false;
            }
        }
        renderer.repaint();
    }
    
    
    
    
    void repaint(Graphics g) {
        if(score<10)
        {
    g.setColor(Color.cyan);
    g.fillRect(0, 0, width, height);
    
    g.setColor(Color.orange);
    g.fillRect(0, height-120, width, 120);
    
    g.setColor(Color.green);
    g.fillRect(0, height-120, width, 20);
    g.setColor(Color.red);
    g.fillRect(bird.x, bird.y, bird.width, bird.height);
    
    for(Rectangle col: column)
    {
        paintColumn(g,col);
    }
        }
        else
        {
            g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);
    
    g.setColor(Color.GRAY);
    g.fillRect(0, height-120, width, 120);
    
    g.setColor(Color.green);
    g.fillRect(0, height-120, width, 20);
    g.setColor(Color.YELLOW);
    g.fillRect(bird.x, bird.y, bird.width, bird.height);
    
    for(Rectangle col: column)
    {
        paintColumn(g,col);
    }
        }
    
    g.setColor(Color.white);
    g.setFont(new Font("Arial",1,100));
    if(!started)
    {
        g.drawString("Click to start", 100, width/2-150);
    }
    if(gameover)
    {
        g.drawString("Game Over!", 100, width/2);
    }
    if(!gameover&&started)
    {
        g.drawString(new String(String.valueOf(score)), width/2, 100);
    }
    
    }
    public static void main(String[] args) {
        flappybird=new Flappy_bird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_SPACE)
        {
            jump();
        }
    }

    
    
}
