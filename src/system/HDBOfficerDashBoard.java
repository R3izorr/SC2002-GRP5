package system;

import controller.*;
import entity.user.HDBOfficer;
import system.service.common.*;
import system.service.officer.*;
import ui.SimpleMenu;
import ui.UserOption;
import utils.FilterSettings;

public class HDBOfficerDashBoard extends SimpleMenu {
    private FilterSettings filterSettings1 = new FilterSettings();
    private FilterSettings filterSettings2 = new FilterSettings();
    public HDBOfficerDashBoard(HDBOfficer officer,ProjectController projectController,ApplicationController applicationController,
                                 EnquiryController enquiryController,UserController userController, NotificationController notificationController) {
        addOption(new UserOption("1", "View your Registered Project Details", input -> new ViewRegisteredProjectsService(officer.getAssignedProjects(), filterSettings1).run()));
        addOption(new UserOption("2", "Register for a New Project", input -> new RegisterForProjectService(officer, projectController, notificationController).run()));
        addOption(new UserOption("3", "View your Registration Status", input -> new ViewRegistrationStatusService(officer).run()));
        addOption(new UserOption("4", "Process Flat Booking for an Applicant", input -> new ProcessFlatBookingService(officer, applicationController, notificationController).run()));
        addOption(new UserOption("5", "Generate Receipt for Booked Applicants", input -> new GenerateReceiptService(officer, applicationController.getAllApplications()).run()));
        addOption(new UserOption("6", "View Available Projects", input -> new ViewAvailableProjectsService(projectController.getVisibleProjects(), filterSettings2, officer.getMaritalStatus()).run()));
        addOption(new UserOption("7", "Apply for a Project (as Applicant)", input -> new ApplyForProjectService(projectController, notificationController , officer).run()));
        addOption(new UserOption("8", "View your Application Status", input -> new ViewApplicationStatusService(officer).run()));
        addOption(new UserOption("9", "Withdraw your Application", input -> new WithdrawApplicationService(applicationController, notificationController).run()));
        addOption(new UserOption("10", "Request for Booking a Flat", input -> new RequestFlatBookingService(applicationController, notificationController).run()));
        addOption(new UserOption("11", "View/Reply to Enquiries for Your Project", input -> new ViewAndReplyEnquiriesService(officer, enquiryController).run()));
        addOption(new UserOption("12", "View Notifications", input -> new ViewNotificationsService(notificationController ,officer.getNric()).run()));
        addOption(new UserOption("13", "View Your Profile", input -> new ViewProfileService(officer).run()));
        addOption(new UserOption("14", "Change Password", input -> {
            new ChangePasswordService(userController, officer).run();
            exit();
        }));
    }
}
