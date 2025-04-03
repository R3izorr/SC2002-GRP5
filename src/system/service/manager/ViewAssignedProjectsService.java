package system.service.manager;

import java.util.List;
import model.BTOProject;
import model.HDBManager;
import system.service.common.AbstractProjectViewService;
import utils.FilterSettings;

public class ViewAssignedProjectsService extends AbstractProjectViewService {
    private HDBManager manager;
    
    public ViewAssignedProjectsService(HDBManager manager, FilterSettings filterSettings) {
        super(filterSettings);
        this.manager = manager;
    }
    
    @Override
    protected List<BTOProject> getBaseProjects() {
        // Return the list of projects managed by this manager.
        return manager.getManagedProjects();
    }
}
