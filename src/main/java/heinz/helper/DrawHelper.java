package heinz.helper;
import heinz.Board;

import java.awt.*;

public class DrawHelper {
    /* Draws the appropriate number of lives on the bottom left of the screen.
     Also draws the menu */

    public static void drawLives(Board board,Graphics g)
    {
        int max = board.getMax();
        int gridSize = board.getGridSize();
        int numLives = board.getNumLives();
        Font font = board.getFont();

        g.setColor(Color.BLACK);

        /*Clear the bottom bar*/
        g.fillRect(0,max+5,600,gridSize);
        g.setColor(Color.YELLOW);
        for(int i = 0;i<numLives;i++)
        {
            /*Draw each life */
            g.fillOval(gridSize*(i+1),max+5,gridSize,gridSize);
        }
        /* Draw the menu items */
        g.setColor(Color.YELLOW);
        g.setFont(font);
        g.drawString("Reset",100,max+5+gridSize);
        g.drawString("Clear High Scores",180,max+5+gridSize);
        g.drawString("Exit",350,max+5+gridSize);
    }


    /*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
        manually.  Whenever I draw a wall, I call updateMap to invalidate those coordinates.  This way the pacman
        and ghosts know that they can't traverse this area */
    public static void drawBoard(Board b, Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,420,420);

        g.setColor(Color.BLACK);
        g.fillRect(0,0,20,600);
        g.fillRect(0,0,600,20);
        g.setColor(Color.WHITE);
        g.drawRect(19,19,382,382);
        g.setColor(Color.BLUE);

        g.fillRect(40,40,60,20);
        b.updateMap(40,40,60,20);
        g.fillRect(120,40,60,20);
        b.updateMap(120,40,60,20);
        g.fillRect(200,20,20,40);
        b.updateMap(200,20,20,40);
        g.fillRect(240,40,60,20);
        b.updateMap(240,40,60,20);
        g.fillRect(320,40,60,20);
        b.updateMap(320,40,60,20);
        g.fillRect(40,80,60,20);
        b.updateMap(40,80,60,20);
        g.fillRect(160,80,100,20);
        b.updateMap(160,80,100,20);
        g.fillRect(200,80,20,60);
        b.updateMap(200,80,20,60);
        g.fillRect(320,80,60,20);
        b.updateMap(320,80,60,20);

        g.fillRect(20,120,80,60);
        b.updateMap(20,120,80,60);
        g.fillRect(320,120,80,60);
        b.updateMap(320,120,80,60);
        g.fillRect(20,200,80,60);
        b.updateMap(20,200,80,60);
        g.fillRect(320,200,80,60);
        b.updateMap(320,200,80,60);

        g.fillRect(160,160,40,20);
        b.updateMap(160,160,40,20);
        g.fillRect(220,160,40,20);
        b.updateMap(220,160,40,20);
        g.fillRect(160,180,20,20);
        b.updateMap(160,180,20,20);
        g.fillRect(160,200,100,20);
        b.updateMap(160,200,100,20);
        g.fillRect(240,180,20,20);
        b.updateMap(240,180,20,20);
        g.setColor(Color.BLUE);


        g.fillRect(120,120,60,20);
        b.updateMap(120,120,60,20);
        g.fillRect(120,80,20,100);
        b.updateMap(120,80,20,100);
        g.fillRect(280,80,20,100);
        b.updateMap(280,80,20,100);
        g.fillRect(240,120,60,20);
        b.updateMap(240,120,60,20);

        g.fillRect(280,200,20,60);
        b.updateMap(280,200,20,60);
        g.fillRect(120,200,20,60);
        b.updateMap(120,200,20,60);
        g.fillRect(160,240,100,20);
        b.updateMap(160,240,100,20);
        g.fillRect(200,260,20,40);
        b.updateMap(200,260,20,40);

        g.fillRect(120,280,60,20);
        b.updateMap(120,280,60,20);
        g.fillRect(240,280,60,20);
        b.updateMap(240,280,60,20);

        g.fillRect(40,280,60,20);
        b.updateMap(40,280,60,20);
        g.fillRect(80,280,20,60);
        b.updateMap(80,280,20,60);
        g.fillRect(320,280,60,20);
        b.updateMap(320,280,60,20);
        g.fillRect(320,280,20,60);
        b.updateMap(320,280,20,60);

        g.fillRect(20,320,40,20);
        b.updateMap(20,320,40,20);
        g.fillRect(360,320,40,20);
        b.updateMap(360,320,40,20);
        g.fillRect(160,320,100,20);
        b.updateMap(160,320,100,20);
        g.fillRect(200,320,20,60);
        b.updateMap(200,320,20,60);

        g.fillRect(40,360,140,20);
        b.updateMap(40,360,140,20);
        g.fillRect(240,360,140,20);
        b.updateMap(240,360,140,20);
        g.fillRect(280,320,20,40);
        b.updateMap(280,320,20,60);
        g.fillRect(120,320,20,60);
        b.updateMap(120,320,20,60);
        drawLives(b, g);
    }


    /* Draws the pellets on the screen */
    public void drawPellets(Board b,Graphics g)
    {
        boolean[][] pellets = b.getPellets();
        g.setColor(Color.YELLOW);
        for (int i=1;i<20;i++)
        {
            for (int j=1;j<20;j++)
            {
                if ( pellets[i-1][j-1])
                    g.fillOval(i*20+8,j*20+8,4,4);
            }
        }
    }

    /* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
    public void fillPellet(int x, int y, Graphics g)
    {
        g.setColor(Color.YELLOW);
        g.fillOval(x*20+28,y*20+28,4,4);
    }

}
