package system;

import controller.ApplicationController;
import controller.EnquiryController;
import controller.NotificationController;
import controller.ProjectController;
import controller.UserController;
import entity.user.Applicant;
import system.service.applicant.*;
import system.service.common.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;


public class ApplicantDashBoard extends SimpleMenu{
    private FilterSettings filterSettings = new FilterSettings();
    public ApplicantDashBoard(Applicant applicant, ProjectController projectController, ApplicationController applicationController,
                              EnquiryController enquiryController, UserController userController, NotificationController notificationController) {
        addOption(new UserOption("1", "View Available Projects", input -> new ViewAvailableProjectsService(projectController.getAvailaBTOProjects(), filterSettings, applicant.getMaritalStatus()).run()));
        addOption(new UserOption("2", "Apply for a Project", input -> new ApplyForProjectService(projectController, notificationController, applicant).run()));
        addOption(new UserOption("3", "View your Application Status", input -> new ViewApplicationStatusService(applicant).run()));
        addOption(new UserOption("4", "Withdraw your Application", input -> new WithdrawApplicationService(applicationController, notificationController).run()));
        addOption(new UserOption("5", "Request Flat Booking", input -> new RequestFlatBookingService(applicationController, notificationController).run()));
        addOption(new UserOption("6", "Submit Enquiry", input -> new SubmitEnquiryService(enquiryController).run()));
        addOption(new UserOption("7", "Edit Enquiry", input -> new EditEnquiryService(enquiryController).run()));
        addOption(new UserOption("8", "Delete Enquiry", input -> new DeleteEnquiryService(enquiryController).run()));
        addOption(new UserOption("9", "View My Enquiries", input -> new ViewMyEnquiriesService(enquiryController).run()));
        addOption(new UserOption("10", "View Notifications", input -> new ViewNotificationsService(notificationController, applicant.getNric()).run()));
        addOption(new UserOption("11", "View My Profile", input -> new ViewProfileService(applicant).run()));
        addOption(new UserOption("12", "Change Password", input -> {
            new ChangePasswordService(userController, applicant).run();
            exit();
        }));
    }
}
        
        
        