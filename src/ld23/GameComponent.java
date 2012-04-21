package ld23;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

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
    
    private int px,py;
    
    public GameComponent(KeyPoller keyPoller){
        px = py = 0;
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
        int fps = 0;
        //boolean updated = false;
        updateTime = SLEEPTIME;//consider first run to be perfect
        preFps = System.nanoTime();
        while(running){           
            preUpdate = System.nanoTime();  
            
            gameUpdate(updateTime/(double)SLEEPTIME);
            gameRenderBuffer();
            gameDrawBuffer();
   
            fps++;
            if((fpsTime=System.nanoTime()-preFps)>=ONESECOND){
                System.out.println(""+(fps*ONESECOND)/fpsTime);
                fps=0;
                preFps = System.nanoTime();}
            
            updateTime = System.nanoTime()-preUpdate;
            if(updateTime < SLEEPTIME){//sleep some time to spare the CPU some work ;)
                try{Thread.sleep((SLEEPTIME-updateTime)/1000000);}
                catch(InterruptedException e){e.printStackTrace();}
                updateTime = System.nanoTime()-preUpdate;}}}

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
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,WIDTH,HEIGHT);
        graphics.setColor(Color.RED);
        graphics.fillRect(px,py,5,5);}

    private void gameUpdate(double delta){
        if(keyPoller.isKeyDown(KeyEvent.VK_DOWN))
            py++;
        else if(keyPoller.isKeyDown(KeyEvent.VK_UP))
            py--;
        if(keyPoller.isKeyDown(KeyEvent.VK_RIGHT))
            px++;
        else if(keyPoller.isKeyDown(KeyEvent.VK_LEFT))
            px--;
        while(px<0) px+=640;
        while(py<0) py+=480;
        py%=480;px%=640;
    }
    
    public static void main(String[] args){
        JFrame frame=new JFrame();
        KeyPoller keyPoller = new KeyPoller();
        GameComponent gC = new GameComponent(keyPoller);
        
        frame.setSize(WIDTH, HEIGHT);
        frame.addKeyListener(keyPoller);
        frame.add(gC);
        frame.setVisible(true);}

}
