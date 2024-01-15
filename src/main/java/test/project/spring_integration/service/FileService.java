package test.project.spring_integration.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.remote.session.DelegatingSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {


//    public void upload(MultipartFile multipartFile) {
//        // Save the file locally
//        Path uploadDir = Paths.get("C:\\Users\\Owner\\Desktop");
//        String fileName = multipartFile.getOriginalFilename();
//        Path filePath = uploadDir.resolve(fileName);
//
//        try {
//            multipartFile.transferTo(filePath);
//
//            // Set thread key for the current request
//            delegatingSessionFactory.setThreadKey(Thread.currentThread().getId());
//
//            // Create a message with the file payload
//            Message<MultipartFile> message = MessageBuilder.withPayload(multipartFile).build();
//
//            // Send the message through the existing IntegrationFlow (gateway)
//            MessageChannel incomingChannel = gateway.getInputChannel();
//            boolean sent = incomingChannel.send(message);
//
//            if (sent) {
//                System.out.println("File sent successfully through FTP.");
//            } else {
//                System.out.println("Failed to send file through FTP.");
//                // Handle the failure appropriately
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace(); // Handle the exception appropriately
//        } finally {
//            // Clear thread key
//            delegatingSessionFactory.clearThreadKey();
//        }
//    }

    @Autowired
    private IntegrationFlow ftpOutboundFlow;

    public void upload(MultipartFile multipartFile) throws IOException {
        // Save the file locally
        Path uploadDir = Paths.get(".\\apache-ftpserver-1.2.0");
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        // Prepare a message with file path and original file name
        Message<String> message = org.springframework.integration.support.MessageBuilder
                .withPayload(filePath.toString())
                .setHeader(FileHeaders.FILENAME, fileName)
                .build();

        // Send the message to the FTP outbound flow
        ftpOutboundFlow.getInputChannel().send(message);
    }
}

