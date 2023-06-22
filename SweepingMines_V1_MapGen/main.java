
/**
 * Main class that handles map generation, as well as singleplay turns.
 *
 * @Alex (your name)
 * @1.6.23 SweepingMines_V1_Mapgen
 */
public class main
{
    //variables for map generation
   public int mineCount;
   public char map[][];
   public char playersMap[][];
    /**
     * Constructor for objects of class main
     */
    public main()
    {
        //creates map
        map = new char[18][14];
        playersMap = new char[18][14];
        generateMap(4,4);
        drawMap();
        
        
    }
    
    public void drawMap(){
        
        //prints out map
        for(int y=0;y<14;y++){
            for(int x=0;x<18;x++){
                System.out.print(map[x][y]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    public void generateMap(int xStart,int yStart)
    {   
        //generate empty map with no mines
        //also generates a seperate map, just for the player to see.
        for(int x=0;x<18;x++){
           for(int y=0;y<14;y++){
               map[x][y] = '■';
               playersMap[x][y] = '■';
           }
        }
        
        //generates 32 mines in random positions
        //avoids generating mines where player clicked, or adjacent to where
        //player clicked.
        
        int randomY;
        int randomX;
        System.out.println("clicked at: "+xStart+","+yStart);
        for(int minesUnplaced=32;minesUnplaced>0;minesUnplaced--){
            randomX = (int)Math.floor(Math.random()*18);
            randomY = (int)Math.floor(Math.random()*14);
            if((map[randomX][randomY] == '■')&&(!isAdjacent(xStart,yStart,randomX,randomY))){
                map[randomX][randomY] = 'x';
                //System.out.println("Mine generated at: "+randomX+","+randomY);
            }else{
                minesUnplaced++;
                //System.out.println("Could not generate mine at "+randomX+","+randomY);
            }
            
        }
        
    }
    
    public boolean isAdjacent(int x,int y,int targetX,int targetY){
       
        
        for(int i=x-1 ; i<=x+1 ; i++){
            for(int t=y-1 ; t<=y+1 ; t++){
              
               if((targetX == i)&&(targetY == t)){
                   System.out.println("Checking square ("+i+","+t+") against mine ("+targetX+","+targetY+")"); 
                   System.out.println("found adjacent mine");
                   return true;
                   
               }
            }
        }
        return false;
    }
}//(randomX != xStart)&&(randomY != yStart)
