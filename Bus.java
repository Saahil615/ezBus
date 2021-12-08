import java.util.List;
import java.util.ArrayList;
public class Bus
{
    List<Point> path = new ArrayList<>();
    List<Request> requests = new ArrayList<>();
    int pointer;
    int x;
    int y;
    int capacity = 0;
    Dir direction;
    public Bus(List<Integer> p,Graph g,List<Request> r){
        for(int i:p){
            path.add(g.points.get(i));
        }
        for(Request req:r){
            requests.add(new Request(p.indexOf(req.in),p.lastIndexOf(req.out)));
        }
        x = path.get(0).x;
        y = path.get(0).y;
        direction = Dir.stop;
        pointer = 0;
    }
    public void stopped(int p){
        int pick = 0;
        int drop = 0;
        for(Request r:requests){
            if(r.in == p){
                capacity++;
                pick++;
            }else if(r.out == p) {
                capacity--;
                drop++;
            }
        }
        if(pick>0)
            System.out.println(pick+" person(s) picked up  \tCurrent no. of people = "+capacity);
        if(drop>0)
            System.out.println(drop+" person(s) dropped off\tCurrent no.of people = "+capacity);
        System.out.println("----------");
        if(capacity<0){
            System.out.println("SCREAM");
            for(Request r:requests){
                System.out.println(r.in+"->"+r.out);
            }
        }
    }
    private boolean chooseDir(){
        if(pointer == -1){
            return true;
        }
        Point p = path.get(pointer);
        if(x<p.x){
            direction = Dir.right;
        }else if(x>p.x){
            direction = Dir.left;
        }else if(y<p.y){
            direction = Dir.down;
        }else if(y>p.y){
            direction = Dir.up;
        }else{
            direction = Dir.stop;
            stopped(pointer);
            pointer++;
        }
        if(pointer == path.size()){
            pointer = -1;
        }
        if(direction == Dir.stop){
            p.visited = true;
        }
        return false;
    }
    private void move(){
        x+=direction.x;
        y+=direction.y;
    }
    public boolean update(){
        move();
        return chooseDir();
    }
}
