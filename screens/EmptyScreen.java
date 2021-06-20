import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;

import screens.CleaningScreen;

class EmptyScreen extends State {
    HiRes16Color screen;
    int fill;
    
    void init(){
        screen = Globals.screen;
        fill = Globals.fill;
    }
    void update(){

        if( Button.A.justPressed() ){
            fill-=10;
            if(fill <= 0){
                Globals.fill = 0;
                Game.changeState( new CleaningScreen() );
            }
        }
        
        screen.clear(0);
        screen.setTextColor(1);
        screen.setTextPosition(0, 35);
        screen.println("Not yet complete. Press <A>");
        
        screen.drawLine(0, 50, fill, 50, 9, true);
        
        screen.flush();
    }
    void shutdown(){
        screen = null;
    }
}