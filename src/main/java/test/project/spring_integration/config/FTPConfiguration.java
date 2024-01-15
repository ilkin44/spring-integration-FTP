package test.project.spring_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.gateway.FtpOutboundGateway;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.List;

@Configuration
public class FTPConfiguration {
    @Bean
    public MessageChannel results() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(tap());
        return channel;
    }

    @Bean
    public DefaultFtpSessionFactory sessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("localhost");
        sf.setPassword("");
        sf.setUsername("anonymous");
        sf.setPort(2121);
        return sf;
    }

    @ServiceActivator(inputChannel = "ftpMGET")
    @Bean
    public FtpOutboundGateway getFiles(DefaultFtpSessionFactory sessionFactory) {

        FtpOutboundGateway gateway = new FtpOutboundGateway(sessionFactory, "mget", "payload");
        gateway.setAutoCreateDirectory(true);
        gateway.setLocalDirectory(new File("./downloads/"));
        gateway.setFileExistsMode(FileExistsMode.REPLACE_IF_MODIFIED);
        gateway.setFilter(new AcceptOnceFileListFilter<>());
        gateway.setOutputChannelName("results");
        return gateway;
    }


    @Bean
    public WireTap tap() {
        return new WireTap("logging");
    }

    @ServiceActivator(inputChannel = "logging")
    @Bean
    public LoggingHandler loogger() {
        LoggingHandler logger = new LoggingHandler(LoggingHandler.Level.INFO);
        logger.setLogExpressionString("'Files:' + payload");
        return logger;
    }

    @MessagingGateway(defaultReplyChannel = "results", defaultRequestChannel = "ftpMGET")
    public interface GateFile {
        List<File> mget(String directory);
    }
}
