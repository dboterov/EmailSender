package prueba;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author dbotero
 */
public class SendHTMLEmail {

    //TODO: cargar parametros de conexion desde properties
    private String username = "dbotero@matisses.co";
    private String password = "Dani1984";
    private String host = "hermes.matisses.co";

    public SendHTMLEmail() {}

    private boolean sendMail(MailMessageDTO mailMessage, String templateName, Map<String, String> params) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        // Get the Session object.
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailMessage.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailMessage.getToList()));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailMessage.getCcList()));
            message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailMessage.getBccList()));
            message.setSubject(mailMessage.getSubject());
            try {
                String fullTemplateName = "C:\\Users\\dbotero\\Documents\\NetBeans8\\EmailSender\\html\\" + templateName + ".html";
                String templateContent = new String(Files.readAllBytes(Paths.get(fullTemplateName)), StandardCharsets.UTF_8);
                message.setContent(StrSubstitutor.replace(templateContent, params), "text/html");
            } catch (Exception e) {
                throw new Exception("No fue posible enviar ");
            }
            // Send message
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("invoiceNumber", "61000235");
        params.put("customerName", "Daniel Botero");
        params.put("customerId", "8.356.881");

        MailMessageDTO msg = new MailMessageDTO();
        msg.setFrom("Factura POS M6 <posm6-1@matisses.co>");
        msg.setSubject("Factura creada");
        msg.addToAddress("dbotero@matisses.co");
        //msg.addCcAddress("sistemas@matisses.co");

        SendHTMLEmail sender = new SendHTMLEmail();
        System.out.println(sender.sendMail(msg, "basic", params));
    }
}

class MailMessageDTO {

    private String from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;

    public MailMessageDTO() {
        to = new ArrayList<>();
        cc = new ArrayList<>();
        bcc = new ArrayList<>();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public String getToList() {
        StringBuilder sb = new StringBuilder();
        for (String address : to) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(address);
        }
        return sb.toString();
    }

    public void addToAddress(String to) {
        this.to.add(to);
    }

    public List<String> getCc() {
        return cc;
    }

    public String getCcList() {
        StringBuilder sb = new StringBuilder();
        for (String address : cc) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(address);
        }
        return sb.toString();
    }

    public void addCcAddress(String cc) {
        this.cc.add(cc);
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getBccList() {
        StringBuilder sb = new StringBuilder();
        for (String address : bcc) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(address);
        }
        return sb.toString();
    }

    public void addCcoAddress(String bcc) {
        this.bcc.add(bcc);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
