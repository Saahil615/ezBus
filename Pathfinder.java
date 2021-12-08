import java.util.List;
import java.util.ArrayList;
@SuppressWarnings("SpellCheckingInspection")
public class Pathfinder{
    private final List<Integer> route;
    private final List<Integer> newRoute;
    private double temperature = 1d;
    public static final double minTemperature = 0.0001d;
    public static final double alpha = 0.9;
    public static final int numIterations = 5;
    public static final boolean TSP = false;
    public List<Request> requests;
    int max;
    Graph g;
    public Pathfinder(Graph in,List<Request> r){
        route = new ArrayList<>();
        newRoute = new ArrayList<>();
        requests = r;
        g = in;
        max = r.size();
        if(TSP)
            init();
        else
            initR();
        improve();
    }
    public List<Integer> getPath(){
        return route;
    }
    private void initR(){
        route.add(0);
        for(Request r:requests){
            if(!route.contains(r.in))
                route.add(r.in);
            if(!route.contains(r.out))
                route.add(r.out);
        }
        route.add(0);
        fix(route);
    }
    private void init(){
        int shortest = -1;
        int index = -1;
        route.add(0);
        while(route.size()<max){
            Point p = g.points.get(route.get(route.size()-1));
            for(int i = 0;i<g.points.size();i++){
                if(route.contains(i)){
                    continue;
                }
                int d = p.dist(g.points.get(i));
                if(d<shortest||shortest == -1){
                    index = i;
                    shortest = d;
                }
            }
            route.add(index);
            index = -1;
            shortest = -1;
        }
        route.add(0);
        fix(route);

    }
    public int eval(List<Integer> route){
        int d = 0;
        for(int i = 1;i<route.size();i++){
            d+=g.distances[route.get(i-1)][route.get(i)];
        }
        return d;
    }
    private int numPeople(int p){
        int a = 0;
        for(Request r:requests){
            if(p == r.in){
                a++;
            }else if(p == r.out && p!=0){
                a--;
            }
        }
        return a;
    }
    public int evalTime(List<Integer> route){
        int d = 0;
        int num = numPeople(0);

        for(int i = 1;i<route.size();i++) {
            num+=numPeople(route.get(i));

            d+=g.distances[route.get(i-1)][route.get(i)]*num;
        }
        return d;
    }
    private void twoOptSwap(int i,int k){
        newRoute.clear();
        newRoute.addAll(route);
        newRoute.set(i,route.get(k));
        newRoute.set(k,route.get(i));
    }
    private boolean acceptable(int original,int toTest){
        double d = Math.pow(Math.E,(original-toTest)/temperature);
        return d>Math.random();
    }
    private boolean fix(List<Integer> route) {
        boolean changed;
        int iter = 0;
        do{
            changed = false;
            for(Request r:requests){
                int in = route.indexOf(r.in);
                int out = route.lastIndexOf(r.out);
                if(in>out){
                    route.set(in,r.out);
                    route.set(out,r.in);
                    changed = true;
                }
            }
            iter++;
            if(iter>route.size()){
                System.out.println("An unexpected error ocurred \nCould not repair the path");
                break;
            }
        }while(changed);
        return iter == 1;
    }
    private boolean check(int r1,int r2, int p1,int p2){
        if(p1 == -1||p2==-1)
            return false;
        if(r1 == r2)
            return true;
        if(r1 == p1 && r2 == p2)
            return true;
        return r1 == p2 && r2 == p1;
    }
    private boolean checkReq(int r,int r2){
        for(Request request:requests){
            if(request.in == r && request.out == r2){
                return true;
            }else if(request.in == r2 && request.out == r){
                return true;
            }
        }
        return false;
    }
    private void improve(){
      int best_distance;
      if(TSP)
          best_distance = eval(route);
      else
          best_distance = evalTime(route);
      int new_distance;
      int prev1 = -1;
      int prev2 = -1;
      while(temperature>minTemperature){
          for(int i = 0;i<numIterations;i++){
              int rand1 = (int)(Math.random()*(max-2))+1;
              int rand2 = (int)(Math.random()*(max-2))+1;
              if(check(rand1,rand2,prev1,prev2)||checkReq(rand1,rand2))
                  rand1 = (int)(Math.random()*(max-2))+1;
              if(check(rand1,rand2,prev1,prev2)||checkReq(rand1,rand2))
                  continue;
              prev1 = rand1;
              prev2 = rand2;
              twoOptSwap(rand1,rand2);
              if(TSP)
                  new_distance = eval(newRoute);
              else
                  new_distance = evalTime(newRoute);
              if(acceptable(best_distance,new_distance)){
                  best_distance = new_distance;
                  route.clear();
                  route.addAll(newRoute);
              }

          }
          temperature*=alpha;
      }
      if(!fix(route)){
          System.out.println("SCREAM");
          System.out.println("----------");
          for(Request r:requests)
              System.out.println(r.in+"->"+r.out);
          for(int i:route)
              System.out.print(i);
      }
    }

}
