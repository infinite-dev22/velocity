package inc.nomard.velocity.cli;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(
        name = "create",
        description = "Create a new project"
)
public class CreateCommand implements Runnable {
    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "Project type: ${COMPLETION-CANDIDATES}",
            required = true,
            completionCandidates = ProjectTypes.class
    )
    private String projectType;

    @CommandLine.Option(
            names = {"-n", "--name"},
            description = "Project name",
            defaultValue = "new-project"
    )
    private String projectName;

    @CommandLine.Option(
            names = {"-p", "--path"},
            description = "Target directory",
            defaultValue = "."
    )
    private Path targetPath;

    @CommandLine.Option(
            names = {"--git"},
            description = "Initialize Git repository"
    )
    private boolean initGit;

    @CommandLine.Option(
            names = {"--git-branch"},
            description = "Default Git branch name",
            defaultValue = "main"
    )
    private String defaultBranch;

    @CommandLine.Option(
            names = {"--git-remote"},
            description = "Git remote URL"
    )
    private String remoteUrl;

    @CommandLine.Option(
            names = {"--push"},
            description = "Push to remote after initialization"
    )
    private boolean pushToRemote;

    @Override
    public void run() {
        // Implementation using the enhanced services
    }
}
