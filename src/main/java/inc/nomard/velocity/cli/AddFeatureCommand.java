package inc.nomard.velocity.cli;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(
        name = "add-feature",
        description = "Add a new feature to existing project"
)
public class AddFeatureCommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Feature name")
    private String featureName;

    @CommandLine.Option(
            names = {"-p", "--path"},
            description = "Project path",
            defaultValue = "."
    )
    private Path projectPath;

    @Override
    public void run() {
        // Implementation
    }
}
