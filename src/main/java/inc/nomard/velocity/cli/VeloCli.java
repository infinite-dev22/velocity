package inc.nomard.velocity.cli;

import inc.nomard.velocity.exception.ParameterExceptionHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(
        name = "velocity",
        version = "1.0",
        description = """
                🚀 Velocity: Enterprise-Grade Project Scaffolding Engine
                
                Velocity is an intelligent project generator that creates production-ready codebases
                following battle-tested architectural patterns. It's designed for teams that need
                to move fast without compromising on code quality.
                
                Key Features:
                • Feature-Based Architecture (FBA) implementation
                • Built-in support for modern development practices
                • Team collaboration setup (Git workflows)
                
                Roadmap:
                • Microservices-ready project structure
                • Automated CI/CD pipeline setup
                • Cloud-native configuration
                • Dependency management and resolution
                
                Supported Project Types:
                • Spring Boot (Java/Kotlin) - Enterprise backends
                • Flutter - Cross-platform mobile apps
                • React - Modern web applications
                • Swift - Native iOS applications
                • Kotlin - Android and multiplatform
                
                Example Usage:
                $ velocity -t springboot -n user-service --cloud-native --ci github-actions
                $ velocity -t flutter -n mobile-app --features auth,payments
                
                All generated projects follow:
                • SOLID principles
                • Clean Architecture guidelines
                • Industry-standard testing practices
                • Security best practices
                • Modern DevOps workflows
                """,
        mixinStandardHelpOptions = true,
        subcommands = {
                CreateCommand.class,
                AddFeatureCommand.class,
                AddComponentCommand.class
        }
)
public class VeloCli implements Callable<Integer> {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    private boolean verbose;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new VeloCli())
                .setExecutionStrategy(new VeloExecutionStrategy())
                .setParameterExceptionHandler(new ParameterExceptionHandler())
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        try {
            // Show help if no subcommand provided
            CommandLine.usage(this, System.out);
            return 0;
        } catch (Exception e) {
            spec.commandLine().getErr().println(e.getMessage());
            return 1;
        }
    }
}

