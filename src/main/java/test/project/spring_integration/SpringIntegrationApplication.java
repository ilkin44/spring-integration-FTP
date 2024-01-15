package test.project.spring_integration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import test.project.spring_integration.config.FTPConfiguration;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class SpringIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(FTPConfiguration.GateFile gateFile) {
        return args -> {
            List<File> files = gateFile.mget(".");
            for (File file : files) {
                System.out.println("File" + file.getAbsolutePath());
            }
        };
    }

}
