using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Mail;
using System.Net.Mime;
using System.IO;
using System.Linq;

public class ProcessingOverEmail
{
    public static bool SendEmail(
        string receiverEmail,
        string subject,
        string body,
        List<string> attachmentPaths = null,
        string senderEmail = "your_email@example.com",
        string smtpServer = "smtp.gmail.com",
        int smtpPort = 587,
        string password = "your_app_password"
    )
    {
        try
        {
            // Create the email message
            using (var message = new MailMessage())
            {
                message.From = new MailAddress(senderEmail);
                message.To.Add(new MailAddress(receiverEmail));
                message.Subject = subject;
                message.Body = body;
                message.IsBodyHtml = false;

                // Process attachments if any
                if (attachmentPaths != null)
                {
                    foreach (var attachmentPath in attachmentPaths)
                    {
                        try
                        {
                            var attachment = new Attachment(attachmentPath, MediaTypeNames.Application.Octet);
                            var disposition = attachment.ContentDisposition;
                            disposition.FileName = Path.GetFileName(attachmentPath);
                            message.Attachments.Add(attachment);
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine($"Error processing attachment {attachmentPath}: {e.Message}");
                            continue;
                        }
                    }
                }

                // Create SMTP client and send email
                using (var client = new SmtpClient(smtpServer, smtpPort))
                {
                    client.EnableSsl = true;
                    client.Credentials = new NetworkCredential(senderEmail, password);
                    client.Send(message);
                }
            }

            return true;
        }
        catch (Exception e)
        {
            Console.WriteLine($"Error sending email: {e.Message}");
            return false;
        }
    }
}
