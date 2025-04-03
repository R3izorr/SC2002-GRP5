package system.service.common;

import java.util.List;
import model.BTOProject;
import repository.ProjectRepository;
import utils.FilterSettings;

public class ViewAvailableProjectsService extends AbstractProjectViewService {
    private ProjectRepository projectRepository;
    
    public ViewAvailableProjectsService(ProjectRepository projectRepository, FilterSettings filterSettings) {
        super(filterSettings);
        this.projectRepository = projectRepository;
    }
    
    @Override
    protected List<BTOProject> getBaseProjects() {
        // For applicants, only return projects that are visible.
        List<BTOProject> all = projectRepository.getProjects();
        all.removeIf(proj -> !proj.isVisible());
        return all;
    }
}
