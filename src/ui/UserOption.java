package ui;

public class UserOption {
    private String key;
    private String description;
    private InputHandler action;
    
    public UserOption(String key, String description, InputHandler action) {
        this.key = key;
        this.description = description;
        this.action = action;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getDescription() {
        return description;
    }
    
    public InputHandler getAction() {
        return action;
    }
}
