package ld23.env;

import java.awt.Color;
import java.awt.Graphics2D;

public class Shape {
    
    private static final Short[] values = {0,1,2,1};
    private static final Color[] colors = {Color.BLUE,Color.YELLOW,Color.ORANGE,Color.GRAY};
    
    private int px,py,width,height;
    private Short[][] area;
    
    public Shape(int px, int py, int w, int h){
        this.px=px;this.py=py;this.width=w/5;this.height=h/5;
        area = new Short[width][height];
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++)
                area[i][j]=values[(int)(Math.random()*4)];}
    
    //is px,py on top of a solid object of me?
    public boolean topOfSolid(int px, int py){
        py++;//one pixel below needs to be inside a solid
        if(px<this.px || px>=px+width*5 || py<this.py || py>=this.py+height*5)
            return false;
        if(area[(px-this.px)/5][(py-this.py)/5]==0)
            return false;
        return true;}
    
    public void draw(Graphics2D g){
        int block;
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                if((block=area[i][j])!=0){
                    g.setColor(colors[block]);
                    g.fillRect(px+i*5,py+j*5,5,5);}}}
    


}
