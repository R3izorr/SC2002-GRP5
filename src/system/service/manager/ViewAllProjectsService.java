package system.service.manager;

import entity.model.BTOProject;
import java.util.List;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewAllProjectsService extends AbstractViewProjectsMenu {
    
    public ViewAllProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
        super(projects, filterSettings); // No filter settings needed for viewing all projects
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForManager(); // Assuming this method provides the desired string representation for managers
    }
}

