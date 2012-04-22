package ld23;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ld23.art.Animation;
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
    
    //for shake effect.. explosions and such
    private int offsetX = 0;
    private int offsetY = 0;
    
    private static final long ONESECOND = 1000000000;
    private static final long SLEEPTIME = ONESECOND/100;//in nanoseconds.. aiming at 100FPS
        
    private Thread looper;//main loop'll be in a thread for max time precision
    private boolean running = false;//used in main loop
    private boolean paused = false;
    private boolean showFPS = false;
    private boolean showUPS = false;
    
    private Graphics2D graphics;
    private BufferedImage bufferImage=null;//for double buffering ;-) isn't that enabled by default in Java now anyways? hm! 
    private KeyPoller keyPoller;
    
    private Mob player;
    private Shape testShape;
    private int fps,ups;
        
    public GameComponent(KeyPoller keyPoller){
        fps = ups = 100;
        player = new Mob(100,400,32,64,WIDTH,HEIGHT);
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
            while(updateTime < SLEEPTIME){//sleep some time to spare the CPU some work ;)
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
        
        if(paused){
            Animation.PAUSED.draw(200, 200, 2, 2, 0, graphics);
            return;}
        
        //draw background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,WIDTH,HEIGHT);
        
        //draw testShape
        testShape.draw(offsetX,offsetY,graphics);
        
        //draw fps/ups
        if(showFPS){
            graphics.setColor(Color.GREEN);
            graphics.drawString(String.format("%03dFPS",fps),WIDTH-50,10);}
        if(showUPS){
            graphics.setColor(Color.CYAN);
            graphics.drawString(String.format("%03dUPS",ups),WIDTH-50,20);}
        
        //draw player object
        player.draw(offsetX,offsetY,graphics);
        if(player.coolDown()>0){
            graphics.setColor(Color.RED);
            graphics.drawString("Laser: " + (player.LCOOLDOWN - player.coolDown()), 10, 10);}
        
        //you dead yet?
        if(!running)
            Animation.GAMEOVER.draw(100, 200, 2, 2, 0, graphics);}
    
    

    private void gameUpdate(){
        if(keyPoller.isKeyDown(KeyEvent.VK_SHIFT)){
            if(keyPoller.isKeyDown(KeyEvent.VK_F))
                showFPS = !showFPS;
            else if(keyPoller.isKeyDown(KeyEvent.VK_U))
                showUPS = !showUPS;}
        
        if(keyPoller.isKeyDown(KeyEvent.VK_PAUSE))
            paused=!paused;
        if(paused) return;
        //player movement
        //if(keyPoller.isKeyDown(KeyEvent.VK_DOWN))
        //    player.moveVertically(1);
        if(keyPoller.isKeyDown(KeyEvent.VK_UP))
            //player.moveVertically(-1);
            player.jump();
        if(keyPoller.isKeyDown(KeyEvent.VK_RIGHT))
            player.moveHorizontally(1);
        else if(keyPoller.isKeyDown(KeyEvent.VK_LEFT))
            player.moveHorizontally(-1);
        else
            player.stayIdle();
        if(keyPoller.isKeyDown(KeyEvent.VK_A))
            player.shoot();
        else if(keyPoller.isKeyDown(KeyEvent.VK_S))
            player.stab();
                
        player.update();
        player.setFalling(!testShape.topOfSolid(player.getPosX(),
                                                player.getPosX()+player.getWidth(),
                                                player.getPosY()+player.getHeight()));
        
        if(player.isDead()) running = false;
        if(player.firesLaser()){//shake effect for explosions and such
            offsetX = (int)(Math.random()*10)-5;
            offsetY = (int)(Math.random()*10)-5;}
        else
            offsetX = offsetY = 0;}
    
    
    
    public static void main(String[] args){
        JFrame frame=new JFrame();
        KeyPoller keyPoller = new KeyPoller();
        GameComponent gC = new GameComponent(keyPoller);
        
        frame.setSize(WIDTH+100, HEIGHT+100);
        frame.addKeyListener(keyPoller);
        frame.add(gC);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);}}
