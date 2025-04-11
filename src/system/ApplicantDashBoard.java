package system;

import controller.EnquiryController;
import controller.ProjectController;
import controller.UserController;
import model.user.Applicant;
import repository.ProjectRepository;
import system.service.applicant.*;
import system.service.common.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;

public class ApplicantDashBoard extends SimpleMenu{
    private FilterSettings filterSettings = new FilterSettings();
    public ApplicantDashBoard(Applicant applicant, ProjectController projectController, ProjectRepository projectRepository,
                              EnquiryController enquiryController, UserController userController) {
        addOption(new UserOption("1", "View Available Projects", input -> new ViewAvailableProjectsService(projectController.getVisibleProjects(), filterSettings, applicant.getMaritalStatus()).run()));
        addOption(new UserOption("2", "Apply for a Project", input -> new ApplyForProjectService(projectController, applicant).run()));
        addOption(new UserOption("3", "View your Application Status", input -> new ViewApplicationStatusService(projectController).run()));
        addOption(new UserOption("4", "Withdraw your Application", input -> new WithdrawApplicationService(projectController).run()));
        addOption(new UserOption("5", "Request Flat Booking", input -> new RequestFlatBookingService(projectController).run()));
        addOption(new UserOption("6", "Submit Enquiry", input -> new SubmitEnquiryService(enquiryController).run()));
        addOption(new UserOption("7", "Edit Enquiry", input -> new EditEnquiryService(enquiryController).run()));
        addOption(new UserOption("8", "Delete Enquiry", input -> new DeleteEnquiryService(enquiryController).run()));
        addOption(new UserOption("9", "View My Enquiries", input -> new ViewMyEnquiriesService(enquiryController).run()));
        addOption(new UserOption("10", "Change Password", input -> {
            new ChangePasswordService(userController, applicant).run();
            exit();
        }));
    }
}
        
        
        