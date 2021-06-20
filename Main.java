import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.font.TIC80;
import femto.sound.Mixer;
import screens.StartScreen;

public class Main extends State {

    public static void main(String[] args){
        // Must initialize the mixer if you intend to play sound
        Mixer.init();
        
        // This initializes the Globals static variables
        Globals.setup();
        
        Game.run( TIC80.font(), new StartScreen() );
    }
    
    void init(){}
    void shutdown(){}
    void update(){}
}