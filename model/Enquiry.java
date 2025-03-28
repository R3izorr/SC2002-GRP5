package model;

import java.time.LocalDateTime;

public class Enquiry {
    private Applicant applicant;
    private Project project;
    private String content;
    private LocalDateTime timestamp;

    public Enquiry(Applicant applicant, Project project, String content) {
        this.applicant = applicant;
        this.project = project;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Applicant getApplicant() { return applicant; }
    public Project getProject() { return project; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void editContent(String newContent) {
        this.content = newContent;
    }

    public void delete() {
        this.content = "[deleted]";
    }
}
