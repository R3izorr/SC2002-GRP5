package ui;

public abstract class AbstractMenu implements IUIElement {
    protected boolean exitRequested = false;
    
    @Override
    public abstract void display();

    
    @Override
    public abstract void handleInput();
    
    public void run() {
        exitRequested = false;
        while (!exitRequested) {
            display();
            handleInput();
        }
    }
    
    public void exit() {
        exitRequested = true;
    }
}
