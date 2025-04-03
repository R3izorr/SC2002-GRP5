package system.service.officer;

import java.util.List;
import model.BTOProject;
import model.HDBOfficer;
import system.service.common.AbstractProjectViewService;
import utils.FilterSettings;

public class ViewRegisteredProjectsService extends AbstractProjectViewService {
    private HDBOfficer officer;
    
    public ViewRegisteredProjectsService(HDBOfficer officer, FilterSettings filterSettings) {
        super(filterSettings);
        this.officer = officer;
    }
    
    @Override
    protected List<BTOProject> getBaseProjects() {
        // Return the list of projects the officer is approved for.
        return officer.getAssignedProjects();
    }
}
