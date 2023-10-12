
/**
 * Main class that handles game.
 *
 * @Alex Anderson (your name)
 * @Version 5 - Final Game - finishing touchers fully commented.
 * @Java
 */

//import package for input reading.
import java.util.Scanner;

//this class creates the objects for the arrays used as maps, as well as setting the players map to a grid of unchecked boxes to start before getting players input.
public class main
{
    
    //declare variables used for map generation 
    public int mineCount;
    public char map[][];
    public char playersMap[][];
    //declare variables used stats, and game checking functions.
    public int turnCount;
    private boolean gameEnded;
    public String answer;
    //creates object for keyboard input
    Scanner input = new Scanner(System.in);
    
    //when class is called, start a new game.
    public main()
    {   
        startGame();
    }
    
    //creates empty maps, displays them, and asks player for input.
    public void startGame(){
        //creating new maps out of 2D arrays
        map = new char[18][14];
        playersMap = new char[18][14];
        
        //fills in player map with unchecked boxes for display, for first turn.
        for(int x=0;x<18;x++){
           for(int y=0;y<14;y++){
               playersMap[x][y] = '■';
           }
        }
        
        //resets variables to manage game.
        turnCount = 0; 
        gameEnded = false;
        //displays map, then asks for players input.
        drawMap();
        getPlayerInput();
    }
    
    //gets player input then seperates the two coordinates player has entered as one string.
    public void getPlayerInput(){
            
        //prints out instructions, then takes input
        System.out.println();
        System.out.println("To uncover a square, please type the x coordinate, then the y coordinate seperated by a comma. (eg: 6,4)");  
        System.out.println("To place down a flag, type F in front of the coordinates you wish to. (eg: f6,4 OR f 6,4)");
        String playerInput = input.nextLine();
        
        //creates variables to control the seperation of the two coordinates.
        boolean commaFound = false;
        String first = "";
        String last = "";
        int letterUpTo = 0;
        
        //loop runs until first coordinate is found in the players input, or it gets to the end and there's no comma to seperate.
        while((!commaFound)&&(letterUpTo < playerInput.length())){
            //if comma is found, stop the loop then record the two coordinates.
            if(playerInput.charAt(letterUpTo) == ','){
                commaFound = true;
                
                for(int i = 0;i < letterUpTo;i++){
                    first += playerInput.charAt(i);
                }
                for(int i = letterUpTo+1;i < playerInput.length();i++){
                    last += playerInput.charAt(i);
                }
                
                //passes both coordinates through function to verify if they are okay inputs.
                verifyInput(first.toLowerCase(),last.toLowerCase());
            }
            letterUpTo++;
        }
        
        //if no comma to seperate the two coordinates were found, tell player that, then retry the function for input.
        if((letterUpTo <= playerInput.length())&&(!commaFound)){
            System.out.println("Please try again: seperate the two coordinates with a comma.");
            getPlayerInput();
        }
    }
    
    //function that checks what is passed through is acceptable as player input.
    public void verifyInput(String firstHalf,String secondHalf){
        int x;
        int y;
        //boolean that check if the players input is intended for placing down a flag.
        boolean flagCheck = false;
        
        //if the first charecter is an 'f' then accept whatever input after it as the coordinates for flag.
        if((firstHalf.charAt(0) == 'f')){
            String newFirst = "";
            flagCheck = true;
            if(firstHalf.charAt(1) == ' '){
                for(int i = 2;i < firstHalf.length();i++){
                     newFirst += firstHalf.charAt(i);
                }
            }else{
                for(int i = 1;i < firstHalf.length();i++){
                     newFirst += firstHalf.charAt(i);
                }
            }
            //set the new first coordinate to the same as it was, this time without the 'f'.
            firstHalf = newFirst;
        }
        
        //checks that both coordinates are digits, and aren't empty. 
        //also checks that the string length is two or less so it can be converted to int without error.
        if((((firstHalf.chars().allMatch(Character::isDigit))&&(firstHalf != ""))&&((secondHalf.chars().allMatch(Character::isDigit)&&(secondHalf != "")))&&((firstHalf.length() <= 2)&&(secondHalf.length() <= 2)))){
            //converts to int in new variable.
            x = Integer.parseInt(firstHalf);
            y = Integer.parseInt(secondHalf);
            //checks they aren't to big for the map.
            if((x<18)&&(y<14)){
                //checks if it was intended to be a flag by player
                if(flagCheck){
                    //these toggle the flag on and off.
                    if(playersMap[x][y] == '■'){
                       playersMap[x][y] = '◘'; 
                       drawMap();
                    }else{
                        if(playersMap[x][y] == '◘'){
                            playersMap[x][y] = '■';
                            drawMap();
                        }else{
                            //if player tries to place flag on already uncovered lot, show this message.
                            System.out.println("You already have uncovered that, try placing a flag somewhere else!");
                        }
                    }
                }else{
                    //if it is the first turn, passes through function that generates map.
                    if(turnCount==0){
                        generateMap(x,y); 
                    }
                    //passes final coordinates through function that computes the play.
                    playTurn(x,y);
                }   
            }else{
                //if inputs are out of map bounds, re-ask.
                System.out.println("Please choose coordinates inbetween 0 - 17, and 0 - 13.");
            }
        }else{
            //if input is otherwise invalid, re-ask.
            System.out.println("Please re-enter your turn. It cannot contain letters or brackets, just numbers.");
        }
        //as long as game hasn't been won or lost, get player input again.
        if(!gameEnded){
            getPlayerInput();
        }
    }
    
    //this function draws and displays the map out of an array, that the player sees.
    public void drawMap(){
        //clears the screen
        System.out.print('\u000C');
        //some decrotive touches
        System.out.println("-------------------- SWEEPING MINES --------------------");
        //formats and displays collumn numbers 0 - 18
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
        
        //prints out 14 rows of the players map array.
        for(int y=0;y<14;y++){
            //displays row numbers, depending on how big the text is so it formats nicely.
            if(y<10){
                System.out.print(y+"   ");
            }else{
                System.out.print(y+"  ");
            }
            
            //displays each charecter in a row of the map.
            for(int x=0;x<18;x++){
                System.out.print(playersMap[x][y]);
                System.out.print("  ");
            }
            System.out.println();
        }
    }
    
    //generates map with mines around where player clicks.
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
        //System.out.println("clicked at: "+xStart+","+yStart);
        //while all mines aren't placed yet, try to set different map spots to a mine, if it's not adjacent or where player clicked.
        for(int minesUnplaced=11;minesUnplaced>0;minesUnplaced--){
            //generate random x and y in map bounds
            randomX = (int)Math.floor(Math.random()*18);
            randomY = (int)Math.floor(Math.random()*14);
            //if it is a square, and not adjacent to clicked coordinates then set to a mine.
            if((map[randomX][randomY] == '■')&&(!isAdjacent(xStart,yStart,randomX,randomY))){
                map[randomX][randomY] = 'o';
                //System.out.println("Mine generated at: "+randomX+","+randomY);
            }else{
                //if it wasn't avaliable, then bump up the mines unplaced again.
                minesUnplaced++;
                //System.out.println("Could not generate mine at "+randomX+","+randomY);
            }
            
        }
        
        //generates the numbers around each mine by passing it through a function
        for(int x=0;x<18;x++){
            for(int y=0;y<14;y++){
                //if the square's a mine, generate numbers around.
                if(map[x][y] == 'o'){
                    genNumbersAround(x,y);
                }
            }
        }
        
    }
    
    //function that generates numbers around mines on the map.
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
    
    //this function processes the turn with checks to see what the player has uncovered.
    public void playTurn(int clickedX,int clickedY){
        //tests to see where player selected
        if(map[clickedX][clickedY] == 'o'){
            //player has found a mine, they lost.
            //set what players see to the whole map, to show them.
            playersMap = map;
            turnCount++;
            //displays the map
            drawMap();
            gameEnded=true;
            System.out.println("YOU BLEW UP! Unlucky.");        
        }else if(map[clickedX][clickedY] == '■'){
            //start recursive function which discovers and uncovers empty squares until it finds a number on all sides.
            turnCount++;
            discoverSquares(clickedX,clickedY);
            drawMap(); 
            hasWon();
        }else if((map[clickedX][clickedY] == '□')||(playersMap[clickedX][clickedY] != '■')){
            //player has selected an already empty square OR player has selected a flag over a number
            if(playersMap[clickedX][clickedY] != '◘'){
                //player has selected already searched plot.
                System.out.println("You've already searched there. Please try again (CODE FLAG)");
            }else{
                //player has selected a plot with a number, and a flag over it.
                turnCount++;
                playersMap[clickedX][clickedY] = map[clickedX][clickedY];
                drawMap();
                hasWon();
            }
        }else{
            //if a player selects a number, just that number will show.
            turnCount++;
            playersMap[clickedX][clickedY] = map[clickedX][clickedY];
            drawMap();
            hasWon();
        }
    }
    
    //checks if player has won
    public void hasWon(){
        //sets up temporary variable
        boolean won = true;
        //checks through the map array
        for(int x=0;x<18;x++){
            for(int y=0;y<14;y++){
                //if where mines are not placed...
                if(map[x][y] != 'o'){
                    //the player does not have anythin covering it...
                    if((playersMap[x][y] != '■')&&(playersMap[x][y] != '◘')){
                        //then they are all good so far
                    }else{
                        //cant have won as unchecked box has been found.
                        won = false;
                    }
                }
            }
        }
        //if there were no unchecked sqaures player has won.
        if(won){
            gameEnded = true;
            //ask player for rematch.
            System.out.println("YOU WON! Congrats!");
        }
    }
    
    //start recursive function which discovers and uncovers empty squares until it finds a number on all sides.
    public void discoverSquares(int originX,int originY){
        //uncovers selected square.
        playersMap[originX][originY] = '□';
        map[originX][originY] = '□';
        
        //scans all neighbouring squares, 
        for(int i=originX-1 ; i<=originX+1 ; i++){
            for(int t=originY-1 ; t<=originY+1 ; t++){
                //if scanned square is not over the edge of the map, then continue
                if(((i>=0)&&(t>=0))&&((i<18)&&(t<14))){
                    if(map[i][t] == '■'){
                        //if it is another empty square then run this function again with that square
                        //sets found square to empty
                        map[i][t] = '□';
                        playersMap[i][t] = '□';
                        //re-runs the function, with found one this time.
                        discoverSquares(i,t);
                    }else if((map[i][t] != 'o')&&(map[i][t] != '□')){
                        //if it's a number then show the number and stop there.
                        playersMap[i][t] = map[i][t];
                    }
                }
            }
        }
    }
    
    //function that takes in an x and y, then another x and y, and says if the two are adajacent to eachother.
    public boolean isAdjacent(int x,int y,int targetX,int targetY){
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
