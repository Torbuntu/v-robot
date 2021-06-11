import managers.SaveManager;

class Global {
    static final SaveManager saveManager = new SaveManager();
    
    static GameState state;
    static int coin;
    static int battery;
    static int cartridge;
    
    static void init(){
        state = GameState.RUNNING;
    }
}
