enum Dir {
      up(0, -1), right(1, 0), down(0, 1), left(-1, 0),stop(0,0);
 
      Dir(int x, int y) {
         this.x = x; this.y = y;
      }
 
      final int x, y;
   }