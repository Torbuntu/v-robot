import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import sprites.Battery;

import screens.CleaningScreen;

class ChargeScreen extends State {
    HiRes16Color screen;
    
    int battery, x, xs;
    Battery batterySprite;
    
    void init(){
        screen = Globals.screen;
        battery = 0;
        x = 10;
        xs = 1;
        batterySprite = new Battery();
        batterySprite.empty();
    }
    
    void update(){
        if(x > 200)xs = -1;
        if(x < 20) xs = 1;
        x+=xs;
        
        // render
        screen.clear(5);
        
        if( Button.A.justPressed() && x > 75 && x < 145  ){
            battery+=10;
            updateBattery();
        }
        
        if( battery >= 110 ){
            Game.changeState(new CleaningScreen());
        }
        
        screen.fillRect(75, 0, 70, 176, 14, true);
        
        batterySprite.draw(screen, x,60);
        screen.flush();
    }
    
    void updateBattery(){
        if(battery > 95)batterySprite.full();
        if(battery < 95)batterySprite.q4();
        if(battery < 75)batterySprite.q3();
        if(battery < 50)batterySprite.q2();
        if(battery < 25)batterySprite.q1();
        if(battery < 2)batterySprite.empty();
    }
    
    void shutdown(){
        screen = null;
        batterySprite = null;
    }
    
    
}