package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main{




    public static void main(String[] args) {

        Field gameField = new Field();

        //gameField.startup();

       // gameField.drawField();

        //gameField.firstQuestion();

        //gameField.setMines();

       // gameField.drawField();

       // gameField.drawMineField();

        gameField.minesLoop();

    }
}
