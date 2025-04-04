package system.service.officer;

import java.util.List;
import model.BTOProject;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewRegisteredProjectsService extends AbstractViewProjectsMenu { 

    public ViewRegisteredProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
       super(projects, filterSettings) ;
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForManagerOfficer();
    }
}
