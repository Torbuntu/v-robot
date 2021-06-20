import femto.Game;
import femto.mode.HiRes16Color;
import femto.font.TIC80;
import managers.SaveManager;

public class Globals {
    static final SaveManager saveManager = new SaveManager();
    static final HiRes16Color screen = new HiRes16Color(Castpixel16.palette(), TIC80.font());

    static int coin;
    static int fill;

    static void setup(){
        if(saveManager.coin == 0) coin = 0;
    }

}
