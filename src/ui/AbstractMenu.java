package ui;

public abstract class AbstractMenu implements IUIElement {
    protected boolean exitRequested = false;
    
    public abstract void display();
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
