import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.font.TIC80;
import femto.sound.Mixer;

class Shop extends State {
    // the screenmode we want to draw with
    HiRes16Color screen; 
    
    void init(){
        screen = new HiRes16Color(Castpixel16.palette(), TIC80.font());
        
    }
    
    void update(){
        if( Button.A.justPressed() ) {
            Game.changeState( new Main() );
        }
        
        screen.clear(0);
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