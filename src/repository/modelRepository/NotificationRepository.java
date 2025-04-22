package repository.modelrepository;

import entity.model.Notification;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import repository.ICRUDRepository;
import utils.FileUtils;

public class NotificationRepository implements ICRUDRepository<Notification> {
    private List<Notification> notifications = new ArrayList<>();
    private String filePath;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public NotificationRepository(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void load() {
        List<String[]> lines = FileUtils.readCSV(this.filePath);
        for (String[] tokens : lines) {
            if (tokens.length < 4) continue;
            String recipient = tokens[1];
            String msg = tokens[2];
            Date ts;
            try { ts = dateFormat.parse(tokens[3]); }
            catch (Exception e) { ts = new Date(); }
            boolean read = Boolean.parseBoolean(tokens[4]);
            Notification n = new Notification(recipient, msg);
            // reflect parsed values
            if (read) n.markAsRead();
            this.notifications.add(n);
        }
    }

    @Override
    public List<Notification> getAll() {
        return notifications;
    }

    @Override
    public Notification getById(Object id) {
        for (Notification n : notifications) if (n.getId() == (int)id) return n;
        return null;
    }

    @Override
    public void add(Notification item) {
        notifications.add(item);
    }

    @Override
    public void remove(Notification item) {
        notifications.remove(item);
    }

    @Override
    public void update() {
        List<String[]> out = new ArrayList<>();
        out.add(new String[]{"ID","NRIC","Message","Date","Read"});
        for (Notification n : notifications) {
            out.add(new String[]{
                String.valueOf(n.getId()),
                n.getRecipientNric(),
                n.getMessage().replace(",", " "),
                n.getTimestamp() == null ? "" : dateFormat.format(n.getTimestamp()),
                String.valueOf(n.isRead())
            });
        }
        FileUtils.writeCSV(filePath, out);
}
}
