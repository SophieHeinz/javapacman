package heinz;/* Drew Schuster */
import heinz.helper.DrawHelper;

import java.awt.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.*;
import java.io.*;



/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel
{
  /* Initialize the images*/
  /* For JAR File*/
  /*
  Image pacmanImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/pacman.jpg"));
  Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/pacmanup.jpg"));
  Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/pacmandown.jpg"));
  Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/pacmanleft.jpg"));
  Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/pacmanright.jpg"));
  Image ghostRedRight = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostRedRight.jpg"));
  Image ghostOrangeRight = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostOrangeRight.jpg"));
  Image ghostTealRight = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostTealRight.jpg"));
  Image ghostPinkRight = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostPinkRight.jpg"));
  Image ghostRedLeft = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostRedLeft.jpg"));
  Image ghostOrangeLeft = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostOrangeLeft.jpg"));
  Image ghostTealLeft = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostTealLeft.jpg"));
  Image ghostPinkLeft = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/ghostPinkLeft.jpg"));
  Image titleScreenImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/titleScreen.jpg"));
  Image gameOverImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/gameOver.jpg"));
  Image winScreenImage = Toolkit.getDefaultToolkit().getImage(src.main.java.Pacman.class.getResource("img/winScreen.jpg"));
  */
  /* For NOT JAR file*/
  Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg"); 
  Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg"); 
  Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg"); 
  Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg"); 
  Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg"); 
  Image ghostRedRight = Toolkit.getDefaultToolkit().getImage("img/ghostRedRight.jpg");
  Image ghostOrangeRight = Toolkit.getDefaultToolkit().getImage("img/ghostOrangeRight.jpg");
  Image ghostTealRight = Toolkit.getDefaultToolkit().getImage("img/ghostTealRight.jpg");
  Image ghostPinkRight = Toolkit.getDefaultToolkit().getImage("img/ghostPinkRight.jpg");
  Image ghostRedLeft = Toolkit.getDefaultToolkit().getImage("img/ghostRedLeft.jpg");
  Image ghostOrangeLeft = Toolkit.getDefaultToolkit().getImage("img/ghostOrangeLeft.jpg");
  Image ghostTealLeft = Toolkit.getDefaultToolkit().getImage("img/ghostTealLeft.jpg");
  Image ghostPinkLeft = Toolkit.getDefaultToolkit().getImage("img/ghostPinkLeft.jpg");
  Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg"); 
  Image gameOverImage = Toolkit.getDefaultToolkit().getImage("img/gameOver.jpg"); 
  Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

  /* Initialize the player and ghosts */
  Player player = new Player(200,300);
  Ghost[] ghosts = {new Ghost(180,180), new Ghost(200,180), new Ghost(220,180),new Ghost(220,180)};



  /* Timer is used for playing sound effects and animations */
  long timer = System.currentTimeMillis();

  /* Dying is used to count frames in the dying animation.  If it's non-zero,
     pacman is in the process of dying */
  int dying=0;
 
  /* Score information */
  int currScore;
  int highScore;

  /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
  boolean clearHighScores= false;

  int numLives=2;

  /*Contains the game map, passed to player and ghosts */
  boolean[][] state;

  /* Contains the state of all pellets*/
  boolean[][] pellets;

  /* Game dimensions */
  int gridSize;
  int max;

  /* State flags*/
  boolean stopped;
  boolean titleScreen;
  boolean winScreen = false;
  boolean overScreen = false;
  boolean demo = false;
  int New;

  public Player getPlayer() {
    return player;
  }

  public Ghost[] getGhosts() {
    return ghosts;
  }

  public int getNumLives() {
    return numLives;
  }

  public boolean[][] getState() {
    return state;
  }

  public boolean[][] getPellets() {
    return pellets;
  }

  public int getGridSize() {
    return gridSize;
  }

  public int getMax() {
    return max;
  }

  /* Used to call sound effects */
  GameSounds sounds;

  int lastPelletEatenX = 0;
  int lastPelletEatenY=0;

  /* This is the font used for the menus */
  Font font = new Font("Monospaced",Font.BOLD, 12);

  /* Constructor initializes state flags etc.*/
  public Board() 
  {
    initHighScores();
    sounds = new GameSounds();
    currScore=0;
    stopped=false;
    max=400;
    gridSize=20;
    New=0;
    titleScreen = true;
  }

  /* Reads the high scores file and saves it */
  public void initHighScores()
  {
    File file = new File("highScores.txt");
    Scanner sc;
    try
    {
        sc = new Scanner(file);
        highScore = sc.nextInt();
        sc.close();
    }
    catch(Exception e)
    {
    }
  }

  /* Writes the new high score to a file and sets flag to update it on screen */
  public void updateScore(int score)
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println(score);
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=score;
    clearHighScores=true;
  }

  /* Wipes the high scores file and sets flag to update it on screen */
  public void clearHighScores()
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println("0");
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=0;
    clearHighScores=true;
  }

  /* Reset occurs on a new game*/
  public void reset()
  {
    numLives=2;
    state = new boolean[20][20];
    pellets = new boolean[20][20];

    /* Clear state and pellets arrays */
    for(int i=0;i<20;i++)
    {
      for(int j=0;j<20;j++)
      {
        state[i][j]=true;
        pellets[i][j]=true;
      }
    }

    /* Handle the weird spots with no pellets*/
    for(int i = 5;i<14;i++)
    {
      for(int j = 5;j<12;j++)
      {
        pellets[i][j]=false;
      }
    }
    pellets[9][7] = false;
    pellets[8][8] = false;
    pellets[9][8] = false;
    pellets[10][8] = false;

  }


  /* Function is called during drawing of the map.
     Whenever the a portion of the map is covered up with a barrier,
     the map and pellets arrays are updated accordingly to note
     that those are invalid locations to travel or put pellets
  */
  public void updateMap(int x,int y, int width, int height)
  {
    for (int i =x/gridSize; i<x/gridSize+width/gridSize;i++)
    {
      for (int j=y/gridSize;j<y/gridSize+height/gridSize;j++)
      {
        state[i-1][j-1]=false;
        pellets[i-1][j-1]=false;
      }
    }
  } 


  /* Draws the appropriate number of lives on the bottom left of the screen.
     Also draws the menu */
  public void drawLives(Graphics g)
  {
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
  public void drawBoard(Graphics g)
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
          updateMap(40,40,60,20);
        g.fillRect(120,40,60,20);
          updateMap(120,40,60,20);
        g.fillRect(200,20,20,40);
          updateMap(200,20,20,40);
        g.fillRect(240,40,60,20);
          updateMap(240,40,60,20);
        g.fillRect(320,40,60,20);
          updateMap(320,40,60,20);
        g.fillRect(40,80,60,20);
          updateMap(40,80,60,20);
        g.fillRect(160,80,100,20);
          updateMap(160,80,100,20);
        g.fillRect(200,80,20,60);
          updateMap(200,80,20,60);
        g.fillRect(320,80,60,20);
          updateMap(320,80,60,20);

        g.fillRect(20,120,80,60);
          updateMap(20,120,80,60);
        g.fillRect(320,120,80,60);
          updateMap(320,120,80,60);
        g.fillRect(20,200,80,60);
          updateMap(20,200,80,60);
        g.fillRect(320,200,80,60);
          updateMap(320,200,80,60);

        g.fillRect(160,160,40,20);
          updateMap(160,160,40,20);
        g.fillRect(220,160,40,20);
          updateMap(220,160,40,20);
        g.fillRect(160,180,20,20);
          updateMap(160,180,20,20);
        g.fillRect(160,200,100,20);
          updateMap(160,200,100,20);
        g.fillRect(240,180,20,20);
        updateMap(240,180,20,20);
        g.setColor(Color.BLUE);


        g.fillRect(120,120,60,20);
          updateMap(120,120,60,20);
        g.fillRect(120,80,20,100);
          updateMap(120,80,20,100);
        g.fillRect(280,80,20,100);
          updateMap(280,80,20,100);
        g.fillRect(240,120,60,20);
          updateMap(240,120,60,20);

        g.fillRect(280,200,20,60);
          updateMap(280,200,20,60);
        g.fillRect(120,200,20,60);
          updateMap(120,200,20,60);
        g.fillRect(160,240,100,20);
          updateMap(160,240,100,20);
        g.fillRect(200,260,20,40);
          updateMap(200,260,20,40);

        g.fillRect(120,280,60,20);
          updateMap(120,280,60,20);
        g.fillRect(240,280,60,20);
          updateMap(240,280,60,20);

        g.fillRect(40,280,60,20);
          updateMap(40,280,60,20);
        g.fillRect(80,280,20,60);
          updateMap(80,280,20,60);
        g.fillRect(320,280,60,20);
          updateMap(320,280,60,20);
        g.fillRect(320,280,20,60);
          updateMap(320,280,20,60);

        g.fillRect(20,320,40,20);
          updateMap(20,320,40,20);
        g.fillRect(360,320,40,20);
          updateMap(360,320,40,20);
        g.fillRect(160,320,100,20);
          updateMap(160,320,100,20);
        g.fillRect(200,320,20,60);
          updateMap(200,320,20,60);

        g.fillRect(40,360,140,20);
          updateMap(40,360,140,20);
        g.fillRect(240,360,140,20);
          updateMap(240,360,140,20);
        g.fillRect(280,320,20,40);
          updateMap(280,320,20,60);
        g.fillRect(120,320,20,60);
          updateMap(120,320,20,60);
        drawLives(g);
  } 


  /* Draws the pellets on the screen */
  public void drawPellets(Graphics g)
  {
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

  /* This is the main function that draws one entire frame of the game */
  public void paint(Graphics g)
  {
    /* If we're playing the dying animation, don't update the entire screen.
       Just kill the pacman*/ 
    if (dying > 0)
    {
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();

      /* Draw the pacman */
      g.drawImage(pacmanImage,player.x,player.y,Color.BLACK,null);
      g.setColor(Color.BLACK);
      
      /* Kill the pacman */
      if (dying == 4)
        g.fillRect(player.x,player.y,20,7);
      else if ( dying == 3)
        g.fillRect(player.x,player.y,20,14);
      else if ( dying == 2)
        g.fillRect(player.x,player.y,20,20); 
      else if ( dying == 1)
      {
        g.fillRect(player.x,player.y,20,20); 
      }
     
      /* Take .1 seconds on each frame of death, and then take 2 seconds
         for the final frame to allow for the sound effect to end */ 
      long currTime = System.currentTimeMillis();
      long temp;
      if (dying != 1)
        temp = 100;
      else
        temp = 2000;
      /* If it's time to draw a new death frame... */
      if (currTime - timer >= temp)
      {
        dying--;
        timer = currTime;
        /* If this was the last death frame...*/
        if (dying == 0)
        {
          if (numLives==-1)
          {
            /* Demo mode has infinite lives, just give it more lives*/
            if (demo)
              numLives=2;
            else
            {
            /* Game over for player.  If relevant, update high score.  Set gameOver flag*/
              if (currScore > highScore)
              {
                updateScore(currScore);
              }
              overScreen=true;
            }
          }
        }
      }
      return;
    }

    /* If this is the title screen, draw the title screen and return */
    if (titleScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(titleScreenImage,0,0,Color.BLACK,null);

      /* Stop any pacman eating sounds */
      sounds.nomNomStop();
      New = 1;
      return;
    } 

    /* If this is the win screen, draw the win screen and return */
    else if (winScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(winScreenImage,0,0,Color.BLACK,null);
      New = 1;
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();
      return;
    }

    /* If this is the game over screen, draw the game over screen and return */
    else if (overScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(gameOverImage,0,0,Color.BLACK,null);
      New = 1;
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();
      return;
    }

    /* If need to update the high scores, redraw the top menu bar */
    if (clearHighScores)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,18);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      clearHighScores= false;
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
    }
   
    /* oops is set to true when pacman has lost a life */ 
    boolean oops=false;
    
    /* Game initialization */
    if (New==1)
    {
      initializeGame(g);
    }
    /* Second frame of new game */
    else if (New == 2)
    {
      New++;
    }
    /* Third frame of new game */
    else if (New == 3)
    {
      New++;
      /* Play the newGame sound effect */
      sounds.newGame();
      timer = System.currentTimeMillis();
      return;
    }
    /* Fourth frame of new game */
    else if (New == 4)
    {
      /* Stay in this state until the sound effect is over */
      long currTime = System.currentTimeMillis();
      if (currTime - timer >= 5000)
      {
        New=0;
      }
      else
        return;
    }
    
    /* Drawing optimization */
    g.copyArea(player.x-20,player.y-20,80,80,0,0);
    for (Ghost ghost : ghosts) {
      g.copyArea(ghost.x-20,ghost.y-20,80,80,0,0);
    }

    /* Detect collisions */
    oops = isCollision(player, ghosts);

    /* Kill the pacman */
    if (oops && !stopped)
    {
      /* 4 frames of death*/
      dying=4;
      
      /* Play death sound effect */
      sounds.death();
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();

      /*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
      numLives--;
      stopped=true;
      drawLives(g);
      timer = System.currentTimeMillis();
    }

    /* Delete the players and ghosts */
    g.setColor(Color.BLACK);
    g.fillRect(player.lastX,player.lastY,20,20);
    for (Ghost ghost : ghosts) {
      g.fillRect(ghost.lastX,ghost.lastY,20,20);
    }

    /* Eat pellets */
    if ( pellets[player.pelletX][player.pelletY] && New!=2 && New !=3)
    {
      lastPelletEatenX = player.pelletX;
      lastPelletEatenY = player.pelletY;

      /* Play eating sound */
      sounds.nomNom();
      
      /* Increment pellets eaten value to track for end game */
      player.pelletsEaten++;

      /* Delete the pellet*/
      pellets[player.pelletX][player.pelletY]=false;

      /* Increment the score */
      currScore += 50;

      /* Update the screen to reflect the new score */
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,20);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);

      /* If this was the last pellet */
      if (player.pelletsEaten == 173)
      {
        /*Demo mode can't get a high score */
        if (!demo)
        {
          if (currScore > highScore)
          {
            updateScore(currScore);
          }
          winScreen = true;
        }
        else
        {
          titleScreen = true;
        }
        return;
      }
    }

    /* If we moved to a location without pellets, stop the sounds */
    else if ( (player.pelletX != lastPelletEatenX || player.pelletY != lastPelletEatenY ) || player.stopped)
    {
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();
    }


    /* Replace pellets that have been run over by ghosts */
    for (Ghost ghost : ghosts) {
      if ( pellets[ghost.lastPelletX][ghost.lastPelletY])
        fillPellet(ghost.lastPelletX,ghost.lastPelletY,g);
    }


    /*Draw the ghosts */
    if (ghosts[0].frameCount < 5)
    {
      /* Draw first frame of ghosts */
      g.drawImage(ghostRedRight,ghosts[0].x,ghosts[0].y,Color.BLACK,null);
      g.drawImage(ghostOrangeRight,ghosts[1].x,ghosts[1].y,Color.BLACK,null);
      g.drawImage(ghostTealRight,ghosts[2].x,ghosts[2].y,Color.BLACK,null);
      g.drawImage(ghostPinkRight,ghosts[3].x,ghosts[3].y,Color.BLACK,null);
      ghosts[0].frameCount++;
    }
    else
    {
      /* Draw second frame of ghosts */
      g.drawImage(ghostRedLeft,ghosts[0].x,ghosts[0].y,Color.BLACK,null);
      g.drawImage(ghostOrangeLeft,ghosts[1].x,ghosts[1].y,Color.BLACK,null);
      g.drawImage(ghostTealLeft,ghosts[2].x,ghosts[2].y,Color.BLACK,null);
      g.drawImage(ghostPinkLeft,ghosts[3].x,ghosts[3].y,Color.BLACK,null);
      if (ghosts[0].frameCount >=10)
        ghosts[0].frameCount=0;
      else
        ghosts[0].frameCount++;
    }

    /* Draw the pacman */
    if (player.frameCount < 5)
    {
      /* Draw mouth closed */
      g.drawImage(pacmanImage,player.x,player.y,Color.BLACK,null);
    }
    else
    {
      /* Draw mouth open in appropriate direction */
      if (player.frameCount >=10)
        player.frameCount=0;

      switch(player.currDirection)
      {
        case 'L':
           g.drawImage(pacmanLeftImage,player.x,player.y,Color.BLACK,null);
           break;     
        case 'R':
           g.drawImage(pacmanRightImage,player.x,player.y,Color.BLACK,null);
           break;     
        case 'U':
           g.drawImage(pacmanUpImage,player.x,player.y,Color.BLACK,null);
           break;     
        case 'D':
           g.drawImage(pacmanDownImage,player.x,player.y,Color.BLACK,null);
           break;     
      }
    }

    /* Draw the border around the game in case it was overwritten by ghost movement or something */
    g.setColor(Color.WHITE);
    g.drawRect(19,19,382,382);

  }

  private boolean isCollision(Player player, Ghost[] ghosts) {
    boolean oops = false;

    for (Ghost ghost : ghosts) {
      if ((player.x == ghost.x && Math.abs(player.y-ghost.y) < 10) ||
              (player.y == ghost.y && Math.abs(player.x-ghost.x) < 10)){
        oops =true;
      }
    }

    return oops;
  }

  private void initializeGame(Graphics g) {
    reset();
    player = new Player(200,300);
    Ghost[] ghosts = {new Ghost(180,180),
            new Ghost(200,180),
            new Ghost(220,180),
            new Ghost(220,180)};
    currScore = 0;
    drawBoard(g);
    drawPellets(g);
    drawLives(g);
    /* Send the game map to player and all ghosts */
    player.updateState(state);
    /* Don't let the player go in the ghost box*/
    player.state[9][7]=false;
    for (Ghost ghost : ghosts) {
      ghost.updateState(state);
    }

    /* Draw the top menu bar*/
    g.setColor(Color.YELLOW);
    g.setFont(font);
    if (demo)
      g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
    else
      g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
    New++;
  }
}
