package ld23;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ld23.env.Shape;
import ld23.mobs.Mob;

/**
 * 
 * @author tobi
 * @date 21.4.2012
 * This basically handles the whole game.. main loop is in here and so forth!
 */
public class GameComponent extends JComponent implements Runnable{
    
    //dimensions for the game screen...
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    
    private static final long ONESECOND = 1000000000;
    private static final long SLEEPTIME = ONESECOND/100;//in nanoseconds.. aiming at 100FPS
        
    private Thread looper;//main loop'll be in a thread for max time precision
    private boolean running=false;//used in main loop
    private Graphics2D graphics;
    private BufferedImage bufferImage=null;//for double buffering ;-) isn't that enabled by default in Java now anyways? hm! 
    private KeyPoller keyPoller;
    
    private Mob player;
    private Shape testShape;
    private int fps,ups;
        
    public GameComponent(KeyPoller keyPoller){
        fps = ups = 100;
        player = new Mob(0,0,WIDTH,HEIGHT);
        testShape = new Shape(0,460,640,20);
        this.keyPoller = keyPoller;
        setPreferredSize(new Dimension(WIDTH,HEIGHT));}
    
    @Override
    public void addNotify(){
        super.addNotify();
        startGame();}
    
    @Override
    public void removeNotify(){
        super.removeNotify();
        stopGame();}
    
    private void startGame(){
        if(!running || looper == null){
            looper = new Thread(this);
            looper.start();}}

    public void stopGame(){
        running=false;}

    @Override
    public void run(){
        running = true;
        long preUpdate, updateTime, preFps, fpsTime;
        int fps, ups;
        fps = ups = 0;
        //boolean updated = false;
        updateTime = SLEEPTIME;//consider first run to be perfect
        preFps = System.nanoTime();
        while(running){           
            preUpdate = System.nanoTime();  
            
            do{
                gameUpdate();
                ups++;}
            while((updateTime-=SLEEPTIME) > SLEEPTIME);
            
            gameRenderBuffer();
            gameDrawBuffer();
            fps++;
            
            if((fpsTime=System.nanoTime()-preFps)>=ONESECOND){
                this.fps = (int)((fps*ONESECOND)/fpsTime);
                this.ups = (int)((ups*ONESECOND)/fpsTime);
                fps=0;
                ups=0;
                preFps = System.nanoTime();}
            
            updateTime = System.nanoTime()-preUpdate;
            if(updateTime < SLEEPTIME){//sleep some time to spare the CPU some work ;)
                try{
                    Thread.sleep((SLEEPTIME-updateTime)/1000000);
                    updateTime = System.nanoTime()-preUpdate;}
                catch(InterruptedException e){e.printStackTrace();}}}}

    private void gameDrawBuffer(){
        Graphics2D g;
        try{
            g = (Graphics2D)this.getGraphics();
            if(g!=null && bufferImage!=null)
                g.drawImage(bufferImage,0,0,null);
            g.dispose();}
        catch(Exception e){
            System.err.println("Error while handling graphics context!");
            e.printStackTrace();}}

    private void gameRenderBuffer(){
        if(bufferImage==null){//create our buffer image in the first run
            bufferImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
            graphics = bufferImage.createGraphics();}
        //draw background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,WIDTH,HEIGHT);
        
        //draw testShape
        testShape.draw(graphics);
        
        //draw fps
        graphics.setColor(Color.GREEN);
        graphics.drawString(String.format("%03dFPS",fps),WIDTH-50,10);
        graphics.setColor(Color.CYAN);
        graphics.drawString(String.format("%03dUPS",ups),WIDTH-50,20);
        
        //draw player object
        player.draw(graphics);
        
        //you dead yet?
        if(!running) graphics.drawString("YOU DIED!",200,200);}

    private void gameUpdate(){
        //player movement
        int i = 0;
        if(keyPoller.isKeyDown(KeyEvent.VK_DOWN))
            player.moveVertically(1);
        else if(keyPoller.isKeyDown(KeyEvent.VK_UP))
            player.moveVertically(-1);
        else
            player.stayIdle();
        if(keyPoller.isKeyDown(KeyEvent.VK_RIGHT))
            player.moveHorizontally(1);
        else if(keyPoller.isKeyDown(KeyEvent.VK_LEFT))
            player.moveHorizontally(-1);
        
        player.setFalling(!testShape.topOfSolid(player.getPosX(),player.getPosY()+64));
        player.update();
        if(player.isDead()) running = false; 
    }
    
    public static void main(String[] args){
        JFrame frame=new JFrame();
        KeyPoller keyPoller = new KeyPoller();
        GameComponent gC = new GameComponent(keyPoller);
        
        frame.setSize(WIDTH+100, HEIGHT+100);
        frame.addKeyListener(keyPoller);
        frame.add(gC);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);}

}
