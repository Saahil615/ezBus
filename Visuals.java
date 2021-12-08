import G.*;
import java.util.ArrayList;
import java.util.List;
class Visuals extends graphics
{
  Graph g;
  Pathfinder p;
  Bus bus;
  int frames = 0;
    List<Request> requests = new ArrayList<>();
  int scale = 50;
  int size = 10;
  int ups = 6;
  boolean on = true;
    public void setup(){
        System.out.println("ezBus travel log");
        createCanvas(scale*(size+1),scale*(size+1));
        setFps(ups*10);
        strokeWeight(2);
        g = new Graph(10,size);
        for(int i = 0;i<5;i++){
            int r = (int)(Math.random()*5);
            int r2 = (int)(Math.random()*5);
            while(r == r2)
                r2 = (int)(Math.random()*5);
            boolean found = false;
            for(Request req:requests){
                if(req.in == r2 && req.out == r) {
                    found = true;
                    break;
                }
            }
            if(!found){
                requests.add(new Request(r,r2));
            }
        }
        p = new Pathfinder(g,requests);
        System.out.println("Avg. time = "+(p.evalTime(p.getPath()))/(p.max+0.0));
        System.out.println("Total distance = "+p.eval(p.getPath()));
        System.out.println("Average distance = "+(p.eval(p.getPath())/(p.max+0.0)));
        bus = new Bus(p.getPath(),g,requests);
    }
    public static void main(String[] args){
        (new Visuals()).go();
    }
    public void keyPressed(){
        if(key == Keys.VK_SPACE) {
            on = !on;
        }
    }
    public void draw(){
        if(on){
            drawGrid();
            if(frames == 10){
                frames = 0;
                if(bus.update()){
                    stop();
                    System.exit(0);
                }
            }
            frames++;
        }
    }
    public void drawGrid(){
      background(0);
      for(int i = 0;i<size+1;i++){
          stroke(255);
          line(i*scale+10,10,i*scale+10,size*scale+10);
          line(10,i*scale+10,size*scale+10,i*scale+10);
      }
      for(Point p:g.points){
        int x = p.x;
        int y = p.y;
        if(p.visited){
            stroke(50,205,169);
            fill(50,205,169);
        }else{
            stroke(255,0,0);
            fill(255,0,0);
        }
        circle(x*scale+10+scale/10,y*scale+10+scale/10,scale/5);
      }
      fill(255,255,0);
      stroke(255,255,0);
      int bx = bus.x*scale+10+scale/10;
      bx+=(int)((scale/10.0)*bus.direction.x*frames);
      int by = bus.y*scale+10+scale/10;
      by+=(int)((scale/10.0)*bus.direction.y*frames);
      circle(bx,by,scale/5);
    }
}
