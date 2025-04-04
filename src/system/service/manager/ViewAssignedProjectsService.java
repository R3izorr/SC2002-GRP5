package system.service.manager;

import java.util.List;
import model.BTOProject;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewAssignedProjectsService extends AbstractViewProjectsMenu {
    public ViewAssignedProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
        super(projects, filterSettings);
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForManagerOfficer();
    }
}
