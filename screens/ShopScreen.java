import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.font.TIC80;
import femto.sound.Mixer;

import screens.CleaningScreen;

class ShopScreen extends State {
    // the screenmode we want to draw with
    HiRes16Color screen; 
    
    void init(){
        screen = Globals.screen;
    }
    
    void update(){
        if( Button.A.justPressed() ) {
            Game.changeState( new CleaningScreen() );
        }
        
        screen.clear(0);
        screen.setTextColor(1);
        screen.setTextPosition(0, 0);
        screen.println("This is the shop! Buy toys and new robots here.\n");
        screen.println("\nThere don't seem to be many items here yet. Check back later!");
        screen.println("\nPress <A> to begin cleaning.");
        screen.flush();
    }   
    
    void shutdown(){
        screen = null;
    }
    
}