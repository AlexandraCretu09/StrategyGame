package org.example;

public class PlayerThreads implements Runnable{
    private int[][] matrix;
    private int i, j;
    private boolean running = true;  // Flag to control the thread's loop

    public PlayerThreads(int[][] matrix, String playerID, int i, int j) {
        this.matrix = matrix;
        this.i = i;
        this.j = j;
    }

    @Override
    public void run() {
        while (running) {
            matrix[i][j] = 1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Methods to move the player in the matrix
    public void moveUp() {
        if (i > 0) {
            matrix[i][j] = 0;
            i--;
            matrix[i][j] = 1;
        }else
            System.out.println("Position out of map bounds!");
    }

    public void moveDown() {
        if (i < matrix.length - 1) {
            matrix[i][j] = 0;
            i++;
            matrix[i][j] = 1;
        }else
            System.out.println("Position out of map bounds!");
    }

    public void moveLeft() {
        if (j > 0) {
            matrix[i][j] = 0;
            j--;
            matrix[i][j] = 1;
        }
        else
            System.out.println("Position out of map bounds!");
    }

    public void moveRight() {
        if (j < matrix[0].length - 1) {
            matrix[i][j] = 0;
            j++;
            matrix[i][j] = 1;
        }
        else
            System.out.println("Position out of map bounds!");
    }

    // Method to stop the thread
    public void stopMoving() {
        running = false;
    }

    public void printMap(){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
