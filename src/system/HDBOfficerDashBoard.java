package system;

import controller.ProjectController;
import controller.UserController;
import model.HDBOfficer;
import repository.ApplicationRepository;
import repository.ProjectRepository;
import system.service.common.*;
import system.service.officer.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;

public class HDBOfficerDashBoard extends SimpleMenu {
    private FilterSettings filterSettings = new FilterSettings();
    public HDBOfficerDashBoard(HDBOfficer officer, UserController userController,
                               ProjectRepository projectRepository, ApplicationRepository applicationRepository, ProjectController projectController) {
        addOption(new UserOption("1", "View your Registered Project Details", input -> new ViewRegisteredProjectsService(officer, filterSettings).run()));
        addOption(new UserOption("2", "Register for a New Project", input -> new RegisterForProjectService(officer, projectRepository).run()));
        addOption(new UserOption("3", "View your Registration Status", input -> new ViewRegistrationStatusService(officer).run()));
        addOption(new UserOption("4", "Process Flat Booking for an Applicant", input -> new ProcessFlatBookingService(officer, applicationRepository).run()));
        addOption(new UserOption("5", "Generate Receipt for Booked Applicants", input -> new GenerateReceiptService(officer, applicationRepository).run()));
        addOption(new UserOption("6", "View Available Projects", input -> new ViewAvailableProjectsService(projectRepository, filterSettings).run()));
        addOption(new UserOption("7", "Apply for a Project (as Applicant)", input -> new ApplyForProjectService(projectController, officer).run()));
        addOption(new UserOption("8", "View your Application Status", input -> new ViewApplicationStatusService(projectController).run()));
        addOption(new UserOption("9", "Withdraw your Application", input -> new WithdrawApplicationService(projectController).run()));
        addOption(new UserOption("10", "Request for Booking a Flat", input -> new RequestFlatBookingService(projectController).run()));
        addOption(new UserOption("11", "View/Reply to Enquiries for Your Project", input -> new ViewAndReplyEnquiriesService(officer, new controller.EnquiryController(projectRepository), projectRepository).run()));
        addOption(new UserOption("12", "Change Password", input -> {
            new ChangePasswordService(userController, officer).run();
            exit();
        }));
    }
}
