package inc.nomard.velocity.git;

import inc.nomard.velocity.exception.ErrorCode;
import inc.nomard.velocity.exception.VeloException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class GitInitializer {
    private static final Map<String, String> DEFAULT_GIT_ATTRIBUTES = Map.ofEntries(
            Map.entry("*.txt", "text=auto eol=lf"),
            Map.entry("*.java", "text=auto eol=lf"),
            Map.entry("*.xml", "text=auto eol=lf"),
            Map.entry("*.json", "text=auto eol=lf"),
            Map.entry("*.properties", "text=auto eol=lf"),
            Map.entry("*.yaml", "text=auto eol=lf"),
            Map.entry("*.yml", "text=auto eol=lf"),
            Map.entry("*.md", "text=auto eol=lf"),
            Map.entry("*.sh", "text=auto eol=lf executable"),
            Map.entry("*.bat", "text=auto eol=crlf"),
            Map.entry("*.cmd", "text=auto eol=crlf"),
            Map.entry("*.jar", "binary"),
            Map.entry("*.war", "binary"),
            Map.entry("*.zip", "binary"),
            Map.entry("*.tar", "binary"),
            Map.entry("*.gz", "binary"),
            Map.entry("*.7z", "binary")
    );
    private final Path projectPath;
    private final String projectType;
    private final GitConfig config;

    public GitInitializer(Path projectPath, String projectType) {
        this(projectPath, projectType, GitConfig.getDefault());
    }

    public GitInitializer(Path projectPath, String projectType, GitConfig config) {
        this.projectPath = projectPath;
        this.projectType = projectType;
        this.config = config;
    }

    public void initialize() throws VeloException {
        try {
            Git git = initializeRepository();
            createGitignore();
            configureRepository(git);
            createInitialCommit(git);
            setupRemote(git);

            if (config.pushToRemote() && config.remoteUrl() != null) {
                pushToRemote(git);
            }
        } catch (GitAPIException | IOException | URISyntaxException e) {
            throw new VeloException(ErrorCode.GIT_INITIALIZATION_FAILED,
                    "Failed to initialize Git repository: " + e.getMessage());
        }
    }

    private Git initializeRepository() throws GitAPIException {
        return Git.init()
                .setDirectory(projectPath.toFile())
                .setInitialBranch(config.defaultBranch())
                .call();
    }

    private void configureRepository(Git git) throws GitAPIException, IOException {
        StoredConfig config = git.getRepository().getConfig();

        // Set core configurations
        config.setBoolean("core", null, "autocrlf", false);
        config.setString("core", null, "eol", "lf");

        // Save configuration
        config.save();
    }

    private void createGitAttributes(Path projectPath) throws IOException {
        Path attributesPath = projectPath.resolve(".gitattributes");
        try (FileWriter writer = new FileWriter(attributesPath.toFile())) {
            for (Map.Entry<String, String> entry : DEFAULT_GIT_ATTRIBUTES.entrySet()) {
                writer.write(String.format("%s %s%n", entry.getKey(), entry.getValue()));
            }
        }
    }

    private void createGitignore() throws IOException {
        Path gitignorePath = projectPath.resolve(".gitignore");
        String content = GitignoreTemplates.forProjectType(projectType);
        Files.writeString(gitignorePath, content);
    }

    private void createInitialCommit(Git git) throws GitAPIException {
        git.add().addFilepattern(".").call();
        git.commit()
                .setMessage(config.commitMessage())
                .setAllowEmpty(false)
                .call();
    }

    private void setupRemote(Git git) throws GitAPIException, URISyntaxException {
        if (config.remoteUrl() != null) {
            git.remoteAdd()
                    .setName("origin")
                    .setUri(new URIish(config.remoteUrl()))
                    .call();
        }
    }

    private void pushToRemote(Git git) throws GitAPIException {
        git.push()
                .setRemote("origin")
                .setRefSpecs(new RefSpec(config.defaultBranch()))
                .call();
    }

    public record GitConfig(
            String defaultBranch,
            String commitMessage,
            String remoteUrl,
            boolean pushToRemote
    ) {
        public static GitConfig getDefault() {
            return new GitConfig(
                    "main",
                    "Initial project setup using Velo",
                    null,
                    false
            );
        }
    }
}

