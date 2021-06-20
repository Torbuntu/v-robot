import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.font.TIC80;

import sprites.Robo;

import screens.CleaningScreen;

class StartScreen extends State {
    // the screenmode we want to draw with
    HiRes16Color screen;
    Robo robo;
    
    int t = 0, c = 0;
    
    void init(){
        robo = new Robo();
        robo.hor();
        robo.y = 32;
        robo.x = 14;
        screen = Globals.screen;
        

    }
    
    void update(){
        if( Button.A.justPressed() ) {
            Game.changeState( new CleaningScreen() );
        }
        
        t++;
        if(t > 50)c++;
        if(c > 15) c = 0;
        if(t > 50)t=0;
        
        
        
        screen.clear(0);
        screen.setTextColor(c);
        screen.setTextPosition(10, 75);
        screen.println("Time to clean up!");
        screen.println("Press <A> to start the V-Robot.");
        robo.draw(screen);
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}