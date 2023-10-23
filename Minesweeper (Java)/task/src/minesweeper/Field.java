package minesweeper;

import java.lang.reflect.Array;
import java.util.*;

public class Field {
    String[][] field = new String[9][9];
    String[][] minesField = new String[9][9];

    Random random = new Random();

    Scanner scanner = new Scanner(System.in);

    int mines;
    int ctr = 0;

    int correctGuesses = 0;
    
    int guessX = 0;
    int guessY  = 0;

    List<String> num = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");

    ArrayList<String> numbers = new ArrayList();


    public Field() {
        for (String[] strings : field) {
            Arrays.fill(strings, ".");
        }

        for (String[] strings : minesField) {
            Arrays.fill(strings, ".");
        }

        numbers.addAll(num);

        System.out.println("How many mines do you want on the field? ");

        mines = scanner.nextInt();

        while(ctr < mines){
            int x = random.nextInt(9);
            int y = random.nextInt(9);

            minesField[x][y] = "X";
            ctr++;
        }
    }
    
    public void createMinefield(int locX, int locY){
        while(ctr < mines){
            int x = random.nextInt(9);
            int y = random.nextInt(9);

            if(x != locX && y != locY && !minesField[x][y].equals("X")){
                minesField[x][y] = "X";
                ctr++;
            }
        }
    }

    public void firstQuestion(){

        int ctr = 1;

        int x;
        int y;
        do {
            ctr = 1;
            System.out.println("Set/unset mine marks or claim a cell as free: ");

            y = scanner.nextInt() - 1;
            x = scanner.nextInt() - 1;
            String free = scanner.next();

            if(x < 0 || x > 8){
                ctr = 0;
            }
            if(y < 0 || y > 8){
                ctr = 0;
            }

        }while(ctr == 0);

        createMinefield(x,y);
    }

    public void minesLoop(){

        //check if the first time through
        int first = 1;
        drawField();

        while(correctGuesses < mines){
            System.out.println("Set/unset mine marks or claim a cell as free: ");

            //have to switch x and y due to how the field prints
            guessY = scanner.nextInt() - 1;
            if(guessX < 0 || guessX > 8){
                continue;
            }
            guessX = scanner.nextInt() - 1;
            if(guessY < 0 || guessY > 8){
                continue;
            }

            String freeOrMark = scanner.next();

            //check if first time through, create the mines by passing the first pick and then set the field and numbers
            if(first == 1){
                createMinefield(guessX,guessY);
                setMines();
                first = 0;
            }

            //free--------------------------------------------------------------------------
            if(freeOrMark.equals("free")){

                //check if mine
                if(minesField[guessX][guessY].equals("X")){
                    exposeMines();
                    drawField();
                    System.out.println("You stepped on a mine and failed!");
                    break;
                    //check if already a number
                }else if(numbers.contains(field[guessX][guessY])){
                    System.out.println("There is a number here!");

                    //if unexplored
                }else if(field[guessX][guessY].equals(".")){
                    //if no mines nearby
                    checkNearby(guessX,guessY);
                }else{
                    //if it's explored

                }
                //mine-------------------------------------------------------------------
            }else if(freeOrMark.equals("mine")){
                //if already marked
                if(field[guessX][guessY].equals("*")){
                    field[guessX][guessY] = (".");
                    if(minesField[guessX][guessY].equals("X")){
                        correctGuesses--;
                    }
                    //if not marked
                }else if(field[guessX][guessY].equals(".")){
                    field[guessX][guessY] = ("*");
                    if(minesField[guessX][guessY].equals("X")){
                        correctGuesses++;
                    }
                    //else if an idiot is playing
                }else{
                    continue;
                }
            }else{
                continue;
            }

            drawField();
        }

        System.out.println("Congratulations! You found all the mines!");
    }

    //draw the field obv
    public void drawField(){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i = 0; i < field.length; i++){
            System.out.print((i + 1) + "|");
            for(int j = 0; j < field[i].length; j++){
                System.out.print(field[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    //for testing
    public void drawMineField(){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i = 0; i < minesField.length; i++){
            System.out.print((i + 1) + "|");
            for(int j = 0; j < minesField[i].length; j++){
                System.out.print(minesField[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    //goes through each square on the field and creates all the numbers on the minesfield
    public void setMines(){
        for(int x = 0; x < minesField.length; x++){
            for(int y = 0; y < minesField[x].length; y++){
                if(!minesField[x][y].equals("X")){
                    int minesCtr = 0;
                    int ex = 0;
                    int why = 0;

                    if(x != 0){
                        ex = x - 1;
                    }

                    if(y != 0){
                        why = y - 1;
                    }


                    for(int i = ex; i < minesField.length && i <= x + 1 ; i++){
                        for(int j = why; j < minesField[i].length && j <= y + 1; j++){

                            if(minesField[i][j].equals("X")){
                                minesCtr++;
                            }
                        }
                    }
                    if(minesCtr == 0){
                        minesField[x][y] = "/";
                    }else{
                        minesField[x][y] = Integer.toString(minesCtr);
                    }
                }
            }
        }
    }

    public boolean checkIfMine(int x, int y){
        return minesField[x][y].equals("X");
    }

    public boolean checkIfMinesNearby(int x, int y){
        int ex = 0;
        int why = 0;
        if(x != 0){
            ex = x - 1;
        }

        if(y != 0){
            why = y - 1;
        }

        for(int i = ex; i < minesField.length && i <= x + 1 ; i++){
            for(int j = why; j < minesField[i].length && j <= y + 1; j++){

                if(minesField[i][j].equals("X")){
                    return true;
                }
            }
        }
        return false;
    }

    public void checkNearbySquares(int x, int y){
        int ex = 0;
        int why = 0;

        if(x != 0){
            ex = x - 1;
        }

        if(y != 0){
            why = y - 1;
        }

        for(int i = ex; i < minesField.length && i <= x + 1 ; i++){
            for(int j = why; j < minesField[i].length && j <= y + 1; j++){
                System.out.println("In check nearby squares loops: " + minesField[i][j]);
                System.out.println("i: " + i + " j: " + j);
                //if a number
                if(checkIfANumber(minesField[i][j])){
                    field[i][j] = minesField[i][j];
                }else{
                    drawField();
                    //mark it and run it again
                    if(!checkIfMinesNearby(i,j)){
                        field[i][j] = "/";
                        checkNearbySquares(i,j);
                    }
                }
            }
        }
    }
    
    public void checkNearby(int x, int y){
        System.out.println("Initial: " +x + y);
        
        int ex = 0;
        int why = 0;

        if(x != 0){
            ex = x - 1;
        }

        if(y != 0){
            why = y - 1;
        }

        for(int i = ex; i < minesField.length && i <= x + 1 ; i++){
            for(int j = why; j < minesField[i].length && j <= y + 1; j++){
                if(field[i][j].equals("/")){
                    continue;
                }
                if(checkIfANumber(minesField[i][j])){
                    //System.out.println("It's a number");
                    field[i][j] = minesField[i][j];
                } else if (minesField[i][j].equals("/")) {
                   // System.out.println("It's empty: " +i+j);
                    field[i][j] = "/";
                    checkNearby(i,j);
                }
            }
        }
    }

    public void exposeMines(){
        for(int i = 0; i < minesField.length; i++){
            for(int j = 0; j < minesField[i].length; j++){
                if(minesField[i][j].equals("X")){
                    field[i][j] = "X";
                }
            }
        }
    }

    public boolean checkIfANumber(String letter){
        if(letter.equals("1")||letter.equals("2")||letter.equals("3")||letter.equals("4")||letter.equals("5")||letter.equals("6")||letter.equals("7")||letter.equals("8")){
            return true;
        }
        return false;
    }

    public String[][] getField() {

        return field;
    }

    public String[][] getMinesField() {

        return minesField;
    }

    /*public void countMines(int x, int y){
        int minesCtr = 0;
        int ex = 0;
        int why = 0;

        if(x != 0){
            ex = x - 1;
        }

        if(y != 0){
            why = y - 1;
        }

        for(int i = ex; i < minesField.length && i <= x + 1 ; i++){
            for(int j = why; j < minesField[i].length && j <= y + 1; j++){

                if(minesField[i][j].equals("X")){
                    minesCtr++;
                }
            }
        }
        if(minesCtr != 0){
            field[x][y] = Integer.toString(minesCtr);
        }
    }*/

    //figured out a better and super obvious way
    public void findNewSpotForMine(int x, int y){

            for(int i = 0; i <minesField.length; i++){
                for(int j = 0; j <minesField.length; j++){
                    if(i != x && j != y && minesField[i][j].equals("X")){

                    }
                }
            }
    }
    
    /* public void startup(){
        

    }*/
    /*if(!field[guessX][guessY].equals("*")&&!field[guessX][guessY].equals(".")){
                System.out.println("There is a number here!");
                continue;
            }else if(field[guessX][guessY].equals("*")){
                field[guessX][guessY] = ".";
                if(checkIfMine(guessX,guessY)){
                    correctGuesses--;
                }
                allGuesses--;
            }else{
                field[guessX][guessY] = "*";
                if(checkIfMine(guessX,guessY)){
                    correctGuesses++;
                }
                allGuesses++;
            }*/

}
