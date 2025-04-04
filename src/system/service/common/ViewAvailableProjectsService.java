package system.service.common;

import java.util.List;
import model.BTOProject;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewAvailableProjectsService extends AbstractViewProjectsMenu {
    
    public ViewAvailableProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
        super(projects, filterSettings);
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForApplicant();
    }

}