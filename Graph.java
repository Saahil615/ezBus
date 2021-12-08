import java.util.ArrayList;
public class Graph
{
    int size;
    int num;
    int[][] distances;
    ArrayList<Point> points= new ArrayList<>();
    public Graph(int n,int s){
        num = n;
        size = s;
        distances = new int[num][num];
        init();
    }
    public void init(){
      for(int i=0; i<num;i++){
        Point p = new Point((int)(Math.random()*size),(int)(Math.random()*size));
        if(!points.contains(p)){
          points.add(p);
        }
      }
      for(int i = 0; i < num; i++){
          for(int j = 0; j < num; j++){
            distances[i][j] = points.get(i).dist(points.get(j));
          }
      }
    }
}
