public class Game
{
    private Grid grid;
    private int speed;
    private int bulletSpeed;
    private int userRow;
    private int msElapsed;
    private int timesGet;
    private int timesAvoid;
    private int col;
    private int score;
    private int HEALTH;
    private int medkit;
    private boolean intense;
    private boolean shoot;
    private int bulletRow;
    private int bulletCol;
    private boolean scrollIncoming;
    private int bossRow;
    private int bossCol;
    private boolean boss;
    private boolean bossUp;
    private boolean clearForBoss;
    private int bossHEALTH;
    private boolean shield;
    public Game()
    {
        grid = new Grid(10, 20);
        speed = 75;
        bulletSpeed = 50;
        userRow = 5;
        col = 0;
        msElapsed = 0;
        timesGet = 0;
        timesAvoid = 0;
        updateTitle();
        HEALTH = 100;
        bossHEALTH = 100;
        intense = false;
        shoot = false;
        boss = false;
        bossRow = 4;
        bossCol = 19;
        bossUp = true;
        scrollIncoming = true;
        clearForBoss = false;
        shield = true;
        grid.setImage(new Location(userRow, col), "ship.gif");

        for(int a = 0; a < 20; a++){
            grid.setImage(new Location(9, a), "wall1.gif");
        }

    }

    public void play()
    {
        while (!isGameOver())
        {
            if(timesGet >= 750){
                speed = 50;
            }

            if(msElapsed == 50000){
                clearForBoss = true;
                //boss = true;
                scrollIncoming = true;
            }
            if(msElapsed == 57000){
                scrollIncoming = false;
                boss = true;
            }
            Grid.pause(speed);
            handleKeyPress();
            if (msElapsed % 300 == 0){
                scrollLeft();
                scrollRight();
                populateRightEdge();
            }
            updateTitle();
            msElapsed += 100;
        }
    }

    public void handleKeyPress()
    {
        int key = grid.checkLastKeyPressed();

        if(key == 38 || key == 87){                              //UP KEY 
            if(userRow > 0 && userRow < 9 && userRow != 0){
                grid.setImage(new Location(userRow, col), null);
                if(grid.getImage(new Location(userRow - 1, col)) != null){
                    handleCollision(new Location(userRow - 1,col));
                } 
                grid.setImage(new Location(userRow -1,col), "ship.gif");
                userRow = userRow - 1;
            }
        }
        if(key == 40 || key == 83){                              //DOWN KEY
            if(userRow <= 10 && userRow >= 0 && userRow != 8){
                grid.setImage(new Location(userRow, col), null);
                if(grid.getImage(new Location(userRow + 1, col)) != null){
                    handleCollision(new Location(userRow + 1,col));
                } 
                grid.setImage(new Location(userRow +1,col), "ship.gif");
                userRow = userRow + 1;
            }
        }
        if(key == 39 || key == 68){                              //RIGHT KEY
            if(col >= 0 && col < 16 && col != 16 ){
                grid.setImage(new Location(userRow, col), null);
                if(grid.getImage(new Location(userRow, col + 1)) != null){
                    handleCollision(new Location(userRow,col + 1));
                } 
                grid.setImage(new Location(userRow ,col +1 ), "ship.gif");
                col +=1;
            }
        }
        if(key == 37 || key == 65){                              //LEFT KEY
            if(col >= 0 && col < 17 && col != 0){
                grid.setImage(new Location(userRow, col), null);
                if(grid.getImage(new Location(userRow, col -1)) != null){
                    handleCollision(new Location(userRow, col - 1));
                } 
                grid.setImage(new Location(userRow  , col -1), "ship.gif");
                col -=1;
            }
        }
        //shooting mechanism 
        if(key == 32 && grid.getImage(new Location(userRow, col + 1)) == null){
            grid.setImage(new Location(userRow, col + 1), "bullet.gif");
            bulletRow = userRow;
            bulletCol = col + 1;
            shoot = true;
        }
    }

    public void populateRightEdge()
    {
        int x = (int)((Math.random() * 9) + 1);
        int y = grid.getNumCols() -1;
        int placeAvoid = (int)(Math.random() * 15) + 1;
        int placeGet = (int)(Math.random() * 100);
        int placemedkit = (int)(Math.random() * 1000);
        int shieldSpawn = (int)(Math.random() * 100000000);
        

        if(boss == true){
            if(bossUp){
                if(bossRow > 0){
                    grid.setImage(new Location(bossRow -1, bossCol), "boss.gif");
                    grid.setImage(new Location(bossRow , bossCol), null); 
                    bossRow--;
                }else {
                    bossUp = false;
                }
            }
            if(!bossUp){
                if(bossRow < grid.getNumRows()-1 && bossRow <= userRow -1){
                    grid.setImage(new Location(bossRow + 1, bossCol), "boss.gif");
                    grid.setImage(new Location(bossRow , bossCol), null);
                    bossRow++;
                }else{
                    bossUp = true;
                }
            }
            if(bossHEALTH <= 0){
                boss = false;
                clearForBoss = false;
                scrollIncoming = true;
            }
        }

        if(HEALTH < 50){
            intense = true;
        }
        if(HEALTH >= 50){
            intense = false;
        }

        if(scrollIncoming == true && clearForBoss == false){
            //System.out.println("Scrolling intense: " + intense);
            if(intense == true){
                grid.setImage(new Location(x-1, y), "enemy.gif");
                if(placemedkit % 99 == 0){
                    grid.setImage(new Location(x-1, y), "medkit.gif");
                }
            }
            if(intense == false){
                if(shield == true){
                    if(shieldSpawn % 99 == 0 && shieldSpawn != userRow + 9){
                        grid.setImage(new Location(x - 1, y), "shield.gif");
                    }
                }
                
                if(placeAvoid % 4 == 0 && placeAvoid > 0 && placeAvoid != userRow + 9){     
                    grid.setImage(new Location(x-1, y), "enemy.gif");     
                    if(placeAvoid == 5){
                        grid.setImage(new Location(userRow , y), "enemy.gif");
                    }
                }
                
                if(placeGet == 2 || placeGet == 5 && placeGet > 0 && placeGet != userRow + 9){
                    grid.setImage(new Location(x -1, y), "point2.gif");
                }
                if(placemedkit % 99 == 0 && placemedkit != userRow + 9){
                    grid.setImage(new Location(x- 1, y), "medkit.gif");
                }
            }
        }
    }

    public void scrollLeft()
    {
        for(int i = 0; i < grid.getNumRows(); i++){
            for(int j = 0; j < grid.getNumCols(); j++){
                Location current = new Location(i, j);
                String savedloc = grid.getImage(current);
                if(scrollIncoming == true){

                    if(savedloc != null && j > 0 && savedloc != "ship.gif" && savedloc != "wall1.gif" && savedloc != "wall2.gif" && savedloc != "wall3.gif"
                    && savedloc != "bullet.gif"){

                        if(!shipBulletCollision(new Location(i, j - 1))){
                            grid.setImage(new Location(i, j - 1), savedloc);
                        }else{ 
                        }
                        grid.setImage(new Location(i, j), null);
                    }
                    else if(j == 0 && grid.getImage(new Location(i, j)) != "ship.gif" && grid.getImage(new Location(i, j)) != "wall1.gif"
                    && grid.getImage(new Location(i, j)) != "wall2.gif" && grid.getImage(new Location(i, j)) != "wall3.gif" && savedloc != "bullet.gif"){
                        grid.setImage(new Location(i, j), null);
                    }
                }
            }
        }
        handleCollision(new Location(userRow, col));
    }

    public void handleCollision(Location loc)   //PLAYER
    {
        if(grid.getImage(loc) != null){

            if(grid.getImage(loc).equals("point2.gif")){  
                timesGet += 100;
            } else if(grid.getImage(loc).equals("enemy.gif")){
                HEALTH = HEALTH - 25;
            } else if(grid.getImage(loc).equals("medkit.gif")){
            		if(HEALTH < 100) {
            			HEALTH += 20;
            		}
                
            } 
            else if(grid.getImage(loc).equals("point.gif")){
                timesGet += 50;
            }
            else if(grid.getImage(loc).equals("shield.gif")){
                HEALTH = 120;
            }
            grid.setImage(loc, "ship.gif");
        }

    }

    public void scrollRight(){           //bullet
        for(int i = 0; i < grid.getNumRows(); i++){
            for(int j = grid.getNumCols() -1; j >=0; j--){
                Location current = new Location(i, j);
                String savedloc = grid.getImage(current);
                if(savedloc != null && savedloc.equals("bullet.gif")){
                    if(j < grid.getNumCols() -1){
                        if(!bulletCollision(new Location(i, j + 1)))
                            grid.setImage(new Location(i, j + 1), "bullet.gif");
                        grid.setImage(new Location(i, j ), null);
                    }
                }
            }
        }

    }

    public boolean shipBulletCollision(Location bulletcurrent){
        String bulletLoc = grid.getImage(bulletcurrent);
        if(bulletLoc != null){
            if(bulletLoc.equals("bullet.gif")){
                grid.setImage(bulletcurrent, "explosion.gif");
                shoot = false;
                return true;
            }
        }
        return false;
    }

    public boolean bulletCollision(Location bulletcurrent){
        String bulletLoc = grid.getImage(bulletcurrent);
        if(bulletLoc != null){
            //if(grid.getImage(("bullet.gif")){
            if(bulletLoc.equals("enemy.gif")){

                grid.setImage(bulletcurrent, "explosion.gif");
                //  <enter> explosion image needs to disapear after showing.
                shoot = false;
                if(true){
                    grid.setImage(bulletcurrent, "point.gif");
                }
                return true;
            }

            if(bulletLoc.equals("boss.gif")){ 
                bossHEALTH -= 10;
                //System.out.println("working, boss health: " + bossHEALTH);
                if(bossHEALTH > 0){
                    grid.setImage(new Location(bossRow, bossCol), "boss.gif");
                    boss = true;
                }else{
                    boss = false;
                }
                if(boss == false){
                    scrollIncoming = true;
                    clearForBoss = false;
                }
                return true;
            }
        }
        return false;
    }

    public int getScore()
    {
        return timesGet;
    }

    public int health()
    {
        return HEALTH;
    }

    public void updateTitle()
    {
        grid.setTitle("SCORE:  " + getScore() +" " + " HEALTH: " + health() + "% " + " TIME: " + msElapsed + " BossHealth: " + bossHEALTH + "%");

    }

    public boolean isGameOver()
    {  
        if(HEALTH <= 0){
            userRow = 3;
            col = 5;
            grid.setImage(new Location(userRow, col + 1), "G.gif");
            grid.setImage(new Location(userRow, col + 2), "A.gif");
            grid.setImage(new Location(userRow, col + 3), "M.gif");
            grid.setImage(new Location(userRow, col + 4), "E.gif");
            grid.setImage(new Location(userRow, col + 5), "O.gif");
            grid.setImage(new Location(userRow, col + 6), "V.gif");
            grid.setImage(new Location(userRow, col + 7), "E.gif");
            grid.setImage(new Location(userRow, col + 8), "R.gif");
            return true;
        }
        return false;
    }

    public static void test()
    {
        Game game = new Game();
        game.play();

    }

    public static void main(String[] args)
    {
        test();
    }
}