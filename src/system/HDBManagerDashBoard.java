package system;

import controller.EnquiryController;
import controller.UserController;
import model.user.HDBManager;
import repository.ApplicationRepository;
import repository.ProjectRepository;
import repository.UserRepository;
import system.service.common.ChangePasswordService;
import system.service.manager.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;

public class HDBManagerDashBoard extends SimpleMenu {
    private FilterSettings filterSettings = new FilterSettings();
    public HDBManagerDashBoard(HDBManager manager, UserController userController,
                               ProjectRepository projectRepository, ApplicationRepository applicationRepository,
                               UserRepository userRepository, EnquiryController enquiryController) {
        addOption(new UserOption("1", "Create a New BTO Project", input -> new CreateProjectService(manager, projectRepository).run()));
        addOption(new UserOption("2", "Edit/Delete Project Listing", input -> new EditDeleteProjectService(manager, projectRepository, userRepository.getOfficers()).run()));
        addOption(new UserOption("3", "Toggle Project Visibility", input -> new ToggleProjectVisibilityService(manager, projectRepository).run()));
        addOption(new UserOption("4", "View All Projects", input -> new ViewAllProjectsService(projectRepository.getProjects(), filterSettings).run()));
        addOption(new UserOption("5", "Manage Applicant Applications", input -> new ManageApplicantApplicationsService(manager, applicationRepository).run()));
        addOption(new UserOption("6", "Manage Withdrawn Applicant Requests", input -> new ManageApplicantWithdrawalsService(manager, applicationRepository).run()));
        addOption(new UserOption("7", "Manage HDB Officer Registrations", input -> new ManageOfficerRegistrationsService(manager, userRepository, projectRepository).run()));
        addOption(new UserOption("8", "View/Reply to Enquiries", input -> new ViewAndReplyEnquiriesService(manager, enquiryController).run()));
        addOption(new UserOption("9", "Generate Report for Booked Applicants", input -> new GenerateReportService(manager, applicationRepository).run()));
        addOption(new UserOption("10", "View Assigned Projects", input -> new ViewAssignedProjectsService(manager.getManagedProjects(), filterSettings).run()));
        addOption(new UserOption("11", "Change Password", input -> {
            new ChangePasswordService(userController, manager).run();
            exit();
        }));
    }
}
