import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.Castpixel16;
import femto.font.TIC80;

class Main extends State {

    HiRes16Color screen; // the screenmode we want to draw with

    Robo robo;
    Trash trash;
    
    int vx, vy, battery, fill, tic;
    
    // 0 - left, 1 - up, 2 - right, 3 - down
    int direction, nudge;
    boolean charging, primeChange, full;

    public static void main(String[] args){
        Game.run( TIC80.font(), new Main() );
    }
    
    void init(){
        screen = new HiRes16Color(Castpixel16.palette(), TIC80.font());
        
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
        charging = false;
        primeChange = false;

    }
    

    void shutdown(){
        screen = null;
    }
    
    void update(){
        //if( Button.A.justPressed() ) Game.changeState( new Main() );
        screen.clear(5);
        
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 8; j++){
                screen.drawRect(14+i*16, 32+j*16, 16, 16, 9, true);    
            }
        }
        screen.fillRect(14, 32, 16, 16, 2, true);
        //draw HUD box
        screen.fillRect(0, 0, 240, 16, 11, true);
        screen.fillRect(0, 24, battery, 3, 7, false);
        screen.fillRect(0, 22, fill, 3, 9, false);
        screen.setTextPosition(0, 0);
        screen.setTextColor(0);
        screen.println(robo.x);
        screen.println(robo.y);
        screen.setTextPosition(40, 0);
        screen.print(trash.x);
        screen.setTextPosition(40, 8);
        screen.print(trash.y);
        
        if(primeChange){
            screen.setTextPosition(100, 0);
            screen.print("<-- NUDGE -->");
        }

        
        trash.draw(screen);
        robo.draw(screen);
        
        screen.flush();
        if(full){
            if(Button.A.justPressed() || Button.B.justPressed()){
                fill-=10;
                if(fill <= 0){
                    fill = 0;
                    full = false;
                }
            }
            return;
        }
        if(charging){
            if(Button.Left.justPressed() || Button.Right.justPressed() ||
                Button.Up.justPressed() || Button.Down.justPressed()){
                battery+=10;
            }
            
            if(battery >= 100){
                battery = 100;
                charging = false;
            }
            return;
        }
        
        if(tic > 0)tic--;
        else{
            battery--;
            tic = 15;
        }
        
        if(battery <= 0){
            robo.x = 14;
            robo.y = 32;
            charging = true;
            robo.setMirrored(false);
            robo.setFlipped(false);
            robo.hor();
            direction = 2;
            vx = 16;
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

        // movement
        if(primeChange && vx == 0 && vy == 0){
            primeChange = false;
            direction = nudge;
            switch(nudge){
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

            return;
        }
        if(vx != 0){
            if(vx > 0){
                vx--;
                robo.x += 1;
            } else {
                vx++;
                robo.x -= 1;
            }
        }else if(vy != 0){
            if(vy > 0){
                vy--;
                robo.y += 1;
            } else {
                vy++;
                robo.y -= 1;
            }
        }else{
            checkTrash();
            switch(direction){
                case 0: 
                    if(robo.x > 14){
                        vx = -16;
                    }else{
                        robo.x = 14;
                        changeDirection();
                    }
                    
                    break;
                case 2:
                    if(robo.x < 14+11*16){
                        vx = 16;
                    }else{
                        robo.x = 14+11*16;
                        changeDirection();
                    }
                    break;
                case 1:
                    if( robo.y > 32 ) {
                        vy = -16;
                    }else{
                        robo.y = 32;
                        changeDirection();
                    }
                    break;
                case 3:
                    if( robo.y < 32+7*16 ){
                        vy = 16;
                    }else{
                        robo.y = 32+7*16;
                        changeDirection();
                    }
                    break;
            }
        }
        
    }
    void tryLeft(){
        if( robo.x > 14 ) {
            robo.hor();
            robo.setMirrored(true);
            robo.setFlipped(false);
            vx = -16;
        } else{
            changeDirection();
        }
    }
    
    void tryRight(){
        if( robo.x < 14+11*16 ){
            robo.hor();
            robo.setMirrored(false);
            robo.setFlipped(false);
            vx = 16;
        }else{
            changeDirection();
        }
    }
    
    void tryUp(){
        if(robo.y > 32 ){
            robo.ver();
            robo.setFlipped(false);
            vy = -16;
        }else{
            changeDirection();
        }
    }
    
    void tryDown(){
        if( robo.y < 32+7*16 ){
            robo.ver();
            robo.setFlipped(true);
            vy = 16;
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

        x = 14+x*16;
        y = 32+y*16;
        
        if(Math.floor(robo.x) == Math.floor(x) && Math.floor(robo.y) == Math.floor(y)){
            setTrash();
        }
        
        trash.setPosition(x, y);
        trash.cheese();
    }
    
    void checkTrash(){
        if(Math.floor(robo.x) == Math.floor(trash.x) && Math.floor(robo.y) == Math.floor(trash.y)){
            setTrash();
            fill+=10;
            if(fill > 200){
                full = true;
                robo.hor();
                robo.setMirrored(true);
                robo.setPosition(14+11*16, 32+7*16);
                vx=-16;
                direction = 0;
            }
        }
    }
}
