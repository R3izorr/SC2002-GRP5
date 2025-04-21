package system;

import controller.*;
import entity.user.HDBManager;
import system.service.common.ChangePasswordService;
import system.service.manager.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;

public class HDBManagerDashBoard extends SimpleMenu {
    private FilterSettings filterSettings1 = new FilterSettings();
    private FilterSettings filterSettings2 = new FilterSettings();
    public HDBManagerDashBoard(HDBManager manager,ProjectController projectController,ApplicationController applicationController,
                                EnquiryController enquiryController, UserController userController) {
        addOption(new UserOption("1", "Create a New BTO Project", input -> new CreateProjectService(manager, projectController).run()));
        addOption(new UserOption("2", "Edit/Delete Project Listing", input -> new EditDeleteProjectService(manager, userController.getAllOfficers(), projectController).run()));
        addOption(new UserOption("3", "Toggle Project Visibility", input -> new ToggleProjectVisibilityService(manager, projectController).run()));
        addOption(new UserOption("4", "View All Projects", input -> new ViewAllProjectsService(projectController.getAllProjects(), filterSettings1).run()));
        addOption(new UserOption("5", "Manage Applicant Applications", input -> new ManageApplicantApplicationsService(manager, applicationController).run()));
        addOption(new UserOption("6", "Manage Withdrawn Applicant Requests", input -> new ManageApplicantWithdrawalsService(manager, applicationController).run()));
        addOption(new UserOption("7", "Manage HDB Officer Registrations", input -> new ManageOfficerRegistrationsService(manager, userController.getAllOfficers(), projectController).run()));
        addOption(new UserOption("8", "View/Reply to Enquiries", input -> new ViewAndReplyEnquiriesService(manager, enquiryController).run()));
        addOption(new UserOption("9", "Generate Report for Booked Applicants", input -> new GenerateReportService(manager, applicationController.getAllApplications()).run()));
        addOption(new UserOption("10", "View Assigned Projects", input -> new ViewAssignedProjectsService(manager.getManagedProjects(), filterSettings2).run()));
        addOption(new UserOption("11", "Change Password", input -> {
            new ChangePasswordService(userController, manager).run();
            exit();
        }));
    }
}
