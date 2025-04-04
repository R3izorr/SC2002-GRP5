package system.service.manager;

import java.util.List;
import model.BTOProject;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewAllProjectsService extends AbstractViewProjectsMenu {
    
    public ViewAllProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
        super(projects, filterSettings); // No filter settings needed for viewing all projects
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForManagerOfficer(); // Assuming this method provides the desired string representation for managers
    }
}

