package test.project.spring_integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.multipart.MultipartFile;

@Configuration
@EnableIntegration
public class FileServerIntegrationConfig {
    @Bean
    DefaultFtpSessionFactory defaultFtpSessionFactory() {
        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
        defaultFtpSessionFactory.setPassword("");
        defaultFtpSessionFactory.setUsername("anonymous");
        defaultFtpSessionFactory.setHost("localhost");
        defaultFtpSessionFactory.setPort(2121);
        return defaultFtpSessionFactory;
    }

    @Bean
    public IntegrationFlow ftpOutboundFlow() {
        return IntegrationFlow.from("toFtpChannel")
                .handle(Ftp.outboundAdapter(defaultFtpSessionFactory())
                        .remoteDirectoryExpression("headers['ftp_remoteDirectory']"))
                .get();
    }

    @Bean("inputChannel")
    public MessageChannel inputChannel() {
        return MessageChannels.direct().getObject();
    }
    @Bean
    FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
        return new FtpRemoteFileTemplate(dsf);
    }
}