package test.project.spring_integration.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.outbound.FtpMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class BasicIntegrationConfig{
    @Bean
    public DefaultFtpSessionFactory ftpSessionFactory() {
        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
        ftpSessionFactory.setHost("ftp.example.com");
        ftpSessionFactory.setPort(21);
        ftpSessionFactory.setUsername("your-ftp-username");
        ftpSessionFactory.setPassword("your-ftp-password");
        ftpSessionFactory.setClientMode(2); // 2 for passive mode, 0 for active mode
        return ftpSessionFactory;
    }


    @Bean
    public FtpRemoteFileTemplate ftpRemoteFileTemplate(FtpSession ftpSession) {
        return new FtpRemoteFileTemplate(ftpSession);
    }

    @Bean
    public FtpMessageHandler ftpMessageHandler(FtpSession ftpSession) {
        FtpMessageHandler handler = new FtpMessageHandler(ftpSession);
        handler.setRemoteDirectoryExpressionString("/upload");
        handler.setFileNameGenerator((message) -> {
            return "uploaded_" + message.getHeaders().get(FileHeaders.FILENAME);
        });
        handler.setFileExistsMode(FileExistsMode.FAIL);
        return handler;
    }

    @MessagingGateway(defaultRequestChannel = "toFtpChannel")
    public interface UploadGateway {
        void upload(File file);
    }
}