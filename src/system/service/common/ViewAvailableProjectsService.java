package system.service.common;

import entity.model.BTOProject;
import java.util.List;
import ui.AbstractViewProjectsMenu;
import utils.FilterSettings;

public class ViewAvailableProjectsService extends AbstractViewProjectsMenu {
    private String maritalStatus;
    
    public ViewAvailableProjectsService(List<BTOProject> projects, FilterSettings filterSettings, String maritalStatus) {
        super(projects, filterSettings);
        this.maritalStatus = maritalStatus;
    }

    @Override
    protected String getProjectString(BTOProject proj) {
        if (maritalStatus.equalsIgnoreCase("Single")) {
            return proj.toStringForApplicant(true);
        } else if (maritalStatus.equalsIgnoreCase("Married")) {
            return proj.toStringForApplicant(false);
        }
        return "";
    }

}