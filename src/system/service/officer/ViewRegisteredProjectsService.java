package system.service.officer;

import entity.model.BTOProject;
import java.util.List;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewRegisteredProjectsService extends AbstractViewProjectsMenu { 

    public ViewRegisteredProjectsService(List<BTOProject> projects, FilterSettings filterSettings) {
       super(projects, filterSettings) ;
    }
    
    @Override
    protected String getProjectString(BTOProject proj) {
        return proj.toStringForManager();
    }
}
