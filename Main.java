import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.font.TIC80;
import femto.sound.Mixer;

class Main extends State {
    // the screenmode we want to draw with
    HiRes16Color screen; 

    Robo robo;
    Trash trash;
    Charger charger;
    // 0 - cheese, 1 - chips, etc...
    int trashID;
    
    int vx, vy, battery, fill, tic;
    
    // Coordinate system for Robot - r and Trash - t
    int rx, ry, tx, ty, nextX, nextY;
    
    // 0 - left, 1 - up, 2 - right, 3 - down
    int direction, nudge;
    boolean primeChange;
    
    public static void main(String[] args){
        // Must initialize the mixer if you intend to play sound
        Mixer.init(); 
        Game.run( TIC80.font(), new Start() );
    }
    
    void init(){
        screen = new HiRes16Color(Castpixel16.palette(), TIC80.font());
        charger = new Charger();
        charger.charging();
        
        robo = new Robo();
        robo.hor();
        robo.y = 32;
        robo.x = 14;
        
        trash = new Trash();
        setTrash();
        
        battery = 100;
        fill = 0;
        tic = 15;
        
        vx = 16;
        vy = 0;
        
        direction = 2;
        primeChange = false;
        
        rx = 0;
        ry = 0;
        
        nextX = 1;
        nextY = 0;
    }

    void shutdown(){
        screen = null;
    }
    
    void update(){
        // For testing
        if( Button.C.justPressed() ) {
            Game.changeState( new Shop() );
        }
        screen.clear(5);
        
        
        switch(Global.state){
            case GameState.FULL:
                if(Button.A.justPressed() || Button.B.justPressed()){
                    fill-=10;
                    if(fill <= 0){
                        fill = 0;
                        Global.state = GameState.RUNNING;
                    }
                }
                break;
            case GameState.CHARGING:
                if(Button.Left.justPressed() || Button.Right.justPressed() ||
                    Button.Up.justPressed() || Button.Down.justPressed()){
                    battery+=10;
                }
                
                if(battery >= 100){
                    battery = 100;
                    Global.state = GameState.RUNNING;
                }
                break;
            case GameState.RUNNING:
                if(tic > 0)tic--;
                else{
                    battery--;
                    tic = 15;
                }
                
                if(battery <= 0){
                    switchToCharging();
                    return;
                }
                
                if(Button.Left.justPressed()){
                    nudge = 0;
                    primeChange = true;
                } 
                if(Button.Right.justPressed()){
                    nudge = 2;
                    primeChange = true;
                }
                if(Button.Up.justPressed()){
                    nudge = 1;
                    primeChange = true;
                }
                if(Button.Down.justPressed() ){
                    nudge = 3;
                    primeChange = true;
                }
        
                makeMove();
                break;
        }
        
        //debugging grid
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 8; j++){
                screen.drawRect(14+i*16, 32+j*16, 16, 16, 9, true);    
            }
        }

        //draw HUD box
        screen.fillRect(0, 0, 240, 16, 11, true);
        screen.fillRect(0, 24, battery, 3, 7, false);
        screen.fillRect(0, 22, fill, 3, 9, false);
        screen.setTextPosition(0, 0);

        screen.println("Coin: $"+Global.coin );

        charger.draw(screen, 14, 32);
        trash.draw(screen);
        robo.draw(screen);
        
        screen.flush();
    }
    
    void switchToCharging(){
        robo.x = 14;
        robo.y = 32;
        rx = 0;
        ry = 0;
        nextX = 1;
        nextY = 0;
        robo.setMirrored(false);
        robo.setFlipped(false);
        robo.hor();
        direction = 2;
        vx = 16;
        Global.state = GameState.CHARGING;
    }
    
    void makeMove(){
        switch(direction){
            case 0: 
                vx++;
                robo.x-=1;
                if(vx == 0){
                    rx = nextX;
                    if(primeChange){
                        moveNudge();
                    }else{
                        tryLeft();
                    }
                }
                break;
            case 2:
                vx--;
                robo.x+=1;
                if(vx == 0){
                    rx = nextX;
                    if(primeChange){
                        moveNudge();
                    }else{
                        tryRight();
                    }
                }
                break;
            case 1:
                vy++;
                robo.y-=1;
                if(vy == 0){
                    ry = nextY;
                    if(primeChange){
                        moveNudge();
                    }else{
                        tryUp();
                    }
                }
                break;
            case 3:
                vy--;
                robo.y+=1;
                if(vy == 0){
                    ry = nextY;
                    if(primeChange){
                        moveNudge();
                    }else{
                        tryDown();
                    }
                }
                break;
        }
    }
    
    void moveNudge(){
        primeChange = false;
        direction = nudge;
        switch(direction){
            case 0:
                tryLeft();
                break;
            case 2:
                tryRight();
                break;
            case 1:
                tryUp();
                break;
            case 3:
                tryDown();
                break;
        }
    }
    void tryLeft(){
        checkTrash();
        if( rx > 0 ) {
            robo.hor();
            robo.setMirrored(true);
            robo.setFlipped(false);
            vx = -16;
            nextX--;
        } else{
            changeDirection();
        }
    }
    
    void tryRight(){
        checkTrash();
        if( rx < 11 ){
            robo.hor();
            robo.setMirrored(false);
            robo.setFlipped(false);
            vx = 16;
            nextX++;
        }else{
            changeDirection();
        }
    }
    
    void tryUp(){
        checkTrash();
        if(ry > 0 ){
            robo.ver();
            robo.setFlipped(false);
            vy = -16;
            nextY--;
        }else{
            changeDirection();
        }
    }
    
    void tryDown(){
        checkTrash();
        if( ry < 7){
            robo.ver();
            robo.setFlipped(true);
            vy = 16;
            nextY++;
        }else{
            changeDirection();
        }
    }
    
    void changeDirection(){
        int dir = Math.random(0,4);
        if(dir != direction){
            direction = dir;
            switch(dir){
                case 0:
                    tryLeft();
                    break;
                case 2:
                    tryRight();
                    break;
                case 1:
                    tryUp();
                    break;
                case 3:
                    tryDown();
                    break;
            }
        }else{
            changeDirection();
        }
    }
    
    void setTrash(){
        int x = Math.random(0, 12);
        int y = Math.random(0, 8);
        if(x == 0 && y == 0){
            x++;
        }
        if(x == 11 && y == 7){
            x--;
        }
        tx = x;
        ty = y;
        if(rx == tx && ry == ty){
            setTrash();
        }

        x = 14+x*16;
        y = 32+y*16;
        
        trash.setPosition(x, y);
        // TODO: increase for different trash types
        trashID = Math.random(0, 2);
        switch(trashID){
            case 0:
                trash.cheese();
                break;
            case 1:
                trash.chips();
                break;
        }
    }
    
    void checkTrash(){
        if(rx == tx && ry == ty){
            setTrash();
            fill+=10;
            Global.coin++;
            if(fill > 200){
                robo.hor();
                robo.setMirrored(true);
                robo.setPosition(14+11*16, 32+7*16);
                rx = 11;
                ry = 7;
                vx =-16;
                direction = 0;
                Global.state = GameState.FULL;
            }
        }
    }
}