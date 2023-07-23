
/**
 * Main class that handles map generation, as well as singleplay turns.
 *
 * @Alex Anderson (your name)
 * @Version 3 - Better player input
 */

//import package for input reading.
import java.util.Scanner;
public class main
{
    
    //declare variables used for map generation 
    public int mineCount;
    public char map[][];
    public char playersMap[][];
    //declare variables used for player turn input
    public int playerSelectX;
    public int playerSelectY;
    private boolean gameEnded;
    Scanner input = new Scanner(System.in);
    /**
     * Constructor for objects of class main
     */
    public main()
    {
        
        //creates two map array, one visible to player, and one that holds entire map.
        map = new char[18][14];
        playersMap = new char[18][14];
        
        getPlayerInput();
        //rough draft of player input just for testing. Will clean up later.
        //System.out.println("Type the x coordinate of where you want to uncover. (eg: '5')");
        //System.out.println("NOTE: 0 is the leftmost coordinate");
        //playerSelectX = input.nextInt();
        //System.out.println("Type the y coordinate of where you want to uncover. (eg: '5')");
       // System.out.println("NOTE: 0 is the leftmost coordinate");
       // playerSelectY = input.nextInt(); 
       // generateMap(playerSelectX,playerSelectY);
       // playTurn(playerSelectX,playerSelectY); 
        
        
       // playerSelectX = input.nextInt();
            
        
       // System.out.println("Type the y coordinate of where you want to uncover. (eg: '5')");
        //System.out.println("NOTE: 0 is the leftmost coordinate");
        //playerSelectY = input.nextInt(); 
         
       // playTurn(playerSelectX,playerSelectY); 
        //takes players input, then passes it through playTurn function. 

        
        
    }
    
    public void getPlayerInput(){
        
            System.out.println("To uncover a square, please type the x coordinate, then the y coordinate seperated by a comma. (eg: 6,4)");
            System.out.println("NOTE: 0 is the leftmost coordinate");
            
            String playerInput = input.nextLine();
            boolean commaFound = false;
            String first = "";
            String last = "";
            int letterUpTo = 0;
            while((!commaFound)&&(letterUpTo < playerInput.length())){
                if(playerInput.charAt(letterUpTo) == ','){
                    commaFound = true;
                    
                    for(int i = 0;i < letterUpTo;i++){
                        first += playerInput.charAt(i);
                    }
                    System.out.println(first);
                    for(int i = letterUpTo+1;i < playerInput.length();i++){
                        last += playerInput.charAt(i);
                    }
                    System.out.println(last);
                    //verifyInput(letterUpTo,);
                   
                }
                letterUpTo++;
            }
            if(letterUpTo < playerInput.length()){
                System.out.println("Error: Please seperate the two coordinates with a comma.");
            }
            
            
            

        
    }
    
    //public void verifyInput(commaIndex,firstHalf,secondHalf
    
    public void drawMap(){
        //displays the map
        
        //formats and displays collumn numbers
        System.out.println();
        System.out.print("    ");
        for(int i=0;i<18;i++){
            if(i<10){
                System.out.print(i+"  "); 
            }else{
                System.out.print(i+" ");  
            }
        }
        System.out.println();
        System.out.println();
        
        
        for(int y=0;y<14;y++){
            //displays row numbers
            if(y<10){
                System.out.print(y+"   ");
            }else{
                System.out.print(y+"  ");
            }
            
            //displays the map array for the player
            for(int x=0;x<18;x++){
                System.out.print(playersMap[x][y]);
                System.out.print("  ");
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
                map[randomX][randomY] = 'o';
                //System.out.println("Mine generated at: "+randomX+","+randomY);
            }else{
                minesUnplaced++;
                //System.out.println("Could not generate mine at "+randomX+","+randomY);
            }
            
        }
        
        //generates the numbers around each mine by passing it through a function
        for(int x=0;x<18;x++){
           for(int y=0;y<14;y++){
               if(map[x][y] == 'o'){
                   genNumbersAround(x,y);
               }
           }
        }
        
    }
    
    public void genNumbersAround(int x,int y){
        //for loop checks each slot in 3x3 grid around current mine
        for(int i=x-1 ; i<=x+1 ; i++){
            for(int t=y-1 ; t<=y+1 ; t++){
               if(((i>=0)&&(t>=0))&&((i<18)&&(t<14))){
                   //if slot is not currently a number of a mine, make it 1
                   if(map[i][t] == '■'){
                       map[i][t] = '1';
                   }else if(map[i][t] != 'o'){
                       //if it is a number, increment it by one.
                       map[i][t] += 1;
                   }
                   
               }
            }
        }
    }
    
    public void playTurn(int clickedX,int clickedY){
        //tests to see where player clicked
        if(map[clickedX][clickedY] == 'o'){
            //player has found a mine, they lost.
            System.out.println("YOU LOST");
            playersMap = map;
            drawMap();
            //stops turn looping
            gameEnded=true;
        }else if(map[clickedX][clickedY] == '■'){
            //start recursive function which discovers and uncovers empty squares until it finds a number on all sides.
            discoverSquares(clickedX,clickedY);
            drawMap();
        }else if(map[clickedX][clickedY] == '□'){
            //player has selected an already empty square
            System.out.println("You've already searched there. Please try again");
        }else{
            //if a player selects a number, just that number will show.
            System.out.println("number");
            playersMap[clickedX][clickedY] = map[clickedX][clickedY];
            drawMap();
        }
    }
    
    public void discoverSquares(int originX,int originY){
        
        //start recursive function which discovers and uncovers empty squares until it finds a number on all sides.
        
        //uncovers selected square.
        playersMap[originX][originY] = '□';
        map[originX][originY] = '□';
        
        
        //scans all neighbouring squares, 
        for(int i=originX-1 ; i<=originX+1 ; i++){
            for(int t=originY-1 ; t<=originY+1 ; t++){
                //if scanned square is not over the edge of the map, then continue
                if(((i>=0)&&(t>=0))&&((i<18)&&(t<14))){
                    if(map[i][t] == '■'){
                        //if they are antnother empty square then run this function again with that square
                        map[i][t] = '□';
                        playersMap[i][t] = '□';
                        discoverSquares(i,t);
                    }else if((map[i][t] != 'o')&&(map[i][t] != '□')){
                        //if it's a number then show the number and stop there.
                        playersMap[i][t] = map[i][t];
                    }
                }
            }
        }
        
    }
        
    public boolean isAdjacent(int x,int y,int targetX,int targetY){
        //function that takes in an x and y, then another x and y, and says if the two are adajacent to eachother.
        
        //scans all neighbouring squares around first x and y
        for(int i=x-1 ; i<=x+1 ; i++){
            for(int t=y-1 ; t<=y+1 ; t++){
                if((targetX == i)&&(targetY == t)){
                    //if any of them are equal to the previously passed coordinates, then return true
                    
                    //debugging logs:
                    //System.out.println("Checking square ("+i+","+t+") against mine ("+targetX+","+targetY+")"); 
                    //System.out.println("found adjacent mine");
                    return true;
                   
                }
            }
        }
        return false;
    }
}
