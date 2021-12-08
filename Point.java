class Point{
      int x;
      int y;
      boolean visited = false;
      public Point(int a, int b){
        x = a;
        y = b;
      }
      public int dist(Point a){
        return Math.abs(a.x - this.x) + Math.abs(a.y - this.y);
      }
    }