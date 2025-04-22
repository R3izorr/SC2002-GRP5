package controller;

import entity.model.Notification;
import java.util.List;
import java.util.stream.Collectors;
import repository.ICRUDRepository;

public class NotificationController {
    private ICRUDRepository<Notification> repo;

    public NotificationController(ICRUDRepository<Notification> repo) {
        this.repo = repo;
    }

    public List<Notification> getForUser(String nric) {
        return repo.getAll().stream()
            .filter(n -> n.getRecipientNric().equals(nric))
            .collect(Collectors.toList());
    }

    public void send(String recipientNric, String message) {
        Notification notif = new Notification(recipientNric, message);
        repo.add(notif);
        repo.update();
    }

    public void markAsRead(int notificationId) {
        Notification n = repo.getById(notificationId);
        if (n != null) {
            n.markAsRead();
            repo.update();
        }
    }
}