package ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleMenu extends AbstractMenu {
    protected Map<String, UserOption> options = new LinkedHashMap<>();
    
    public void addOption(UserOption option) {
        options.put(option.getKey(), option);
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Menu ===");
        for (UserOption option : options.values()) {
            System.out.println(option.getKey() + ". " + option.getDescription());
        }
        System.out.println("Type 'b' to exit.");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Enter option: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        UserOption option = options.get(input);
        if (option != null) {
            option.getAction().handle(input);
        } else {
            System.out.println("Invalid option.");
        }
    }
}
