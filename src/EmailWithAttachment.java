import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailWithAttachment {
    public static void main(String[] args){
        EmailWithAttachment solution = new EmailWithAttachment();
        solution.sendMail("username@mail.ru", "******************", "recipients@gmail.com");
    }

    public void sendMail(final String username, final String password, final String recipients) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.ssl.trust", "smtp.mail.ru");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.starttls.enable", "true");

        //props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            setSubject(message, "Тестовое письмо");
            setAttachment(message, "D:\\book.txt");

            Transport transport = session.getTransport();
            transport.connect("smtp.mail.ru", 465, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            //Transport.send(message);
            System.out.println("Письмо было отправлено.");
        } catch (MessagingException e) {
            System.out.println("Ошибка при отправке: " + e.toString());
        }
    }
    public static void setSubject(Message message, String subject) throws MessagingException {
        message.setSubject(subject);
    }
    public static void setAttachment(Message message, String filename) throws MessagingException {
        MimeBodyPart p1 = new MimeBodyPart();
        p1.setText("This is part one of a test multipart e-mail." +
                "The second part is file as an attachment");
        MimeBodyPart p2 = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filename);
        p2.setDataHandler(new DataHandler(fds));
        p2.setFileName(fds.getName());

        // Создание экземпляра класса Multipart. Добавление частей сообщения в него.
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(p1);
        mp.addBodyPart(p2);
        message.setContent(mp);
    }
}
