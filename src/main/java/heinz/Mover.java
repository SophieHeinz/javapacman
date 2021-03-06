package heinz;

import java.util.HashSet;
import java.util.Set;

/* Both src.main.java.Player and src.main.java.Ghost inherit src.main.java.Mover.  Has generic functions relevant to both*/
abstract class Mover
{
    /* Framecount is used to count animation frames*/
    int frameCount=0;

    /* State contains the game map */
    boolean[][] state;

    /* gridSize is the size of one square in the game.
       max is the height/width of the game.
       increment is the speed at which the object moves,
       1 increment per move() call */
    int gridSize;
    int max;
    int increment;

    /* Direction mover is heading */
    char direction;

    /* Last mover location*/
    int lastX;
    int lastY;

    /* Current mover location */
    int x;
    int y;

    /* The pellet the ghost is on top of */
    int pelletX,pelletY;

    /* Generic constructor */
    public Mover()
    {
        gridSize=20;
        increment = 4;
        max = 400;
        state = new boolean[20][20];
        for(int i =0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                state[i][j] = false;
            }
        }
    }
/*
    public Mover(int x, int y){
        pelletX = x/gridSize-1;
        pelletY = y/gridSize-1;
        this.lastX=x;
        this.lastY=y;
        this.x = x;
        this.y = y;
    }

 */

    /* Updates the state information */
    public void updateState(boolean[][] state)
    {
        for(int i =0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                this.state[i][j] = state[i][j];
            }
        }
    }

    /* Determines if a set of coordinates is a valid destination.*/
    public boolean isValidDest(int x, int y)
    {
    /* The first statements check that the x and y are inbounds.  The last statement checks the map to
       see if it's a valid location */
        if ((((x)%20==0) || ((y)%20)==0) && 20<=x && x<400 && 20<= y && y<400 && state[x/20-1][y/20-1] )
        {
            return true;
        }
        return false;
    }


    /* Determines if the location is one where the mover has to make a decision*/
    public boolean isChoiceDest()
    {
        if (  x%gridSize==0&& y%gridSize==0 )
        {
            return true;
        }
        return false;
    }

    public void updatePellet()
    {
        if (x%gridSize ==0 && y%gridSize == 0)
        {
            pelletX = x/gridSize-1;
            pelletY = y/gridSize-1;
        }
    }

    public abstract void move();

    public abstract char newDirection();
}
