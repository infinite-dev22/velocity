package inc.nomard.velocity.cli;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(
        name = "add-component",
        description = "Add a new component to existing feature"
)
public class AddComponentCommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Feature name")
    private String featureName;

    @CommandLine.Parameters(index = "1", description = "Component name")
    private String componentName;

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
