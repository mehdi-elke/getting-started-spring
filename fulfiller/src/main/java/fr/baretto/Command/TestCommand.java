package fr.baretto.Command;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCommand {
    private final TestCommandService testCommandService;

    public TestCommand(TestCommandService testCommandService) {
        this.testCommandService = testCommandService;
    }

    @Bean
    public CommandLineRunner testCommandRunner() {
        return args -> {
            if (args.length > 0 && "test".equals(args[0])) {
                testCommandService.runTest();
                System.exit(0);
            }
        };
    }
} 