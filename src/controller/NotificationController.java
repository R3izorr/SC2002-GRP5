package controller;

import entity.model.Notification;
import java.util.List;
import java.util.stream.Collectors;
import repository.ICRUDRepository;

public class NotificationController {
    private ICRUDRepository<Notification> notficationRepository;

    public NotificationController(ICRUDRepository<Notification> notificationRepository) {
        this.notficationRepository = notificationRepository;
    }

    public List<Notification> getForUser(String nric) {
        return notficationRepository.getAll().stream()
            .filter(n -> n.getRecipientNric().equals(nric))
            .collect(Collectors.toList());
    }

    public void send(String recipientNric, String message) {
        Notification notif = new Notification(recipientNric, message);
        notficationRepository.add(notif);
        notficationRepository.update();
    }

    public void markAsRead(int notificationId) {
        Notification n = notficationRepository.getById(notificationId);
        if (n != null) {
            n.markAsRead();
            notficationRepository.update();
        }
    }
}