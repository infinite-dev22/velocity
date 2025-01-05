package inc.nomard.velocity.git;

import java.util.Map;

// Separate class for gitignore templates
public class GitignoreTemplates {
    private static final Map<String, String> TEMPLATES = Map.of(
            "springboot", """
                    # Compiled output
                    target/
                    !.mvn/wrapper/maven-wrapper.jar
                    
                    # IDEs and editors
                    .idea/
                    .vscode/
                    *.iws
                    *.iml
                    *.ipr
                    
                    # Logs
                    logs/
                    *.log
                    
                    # System Files
                    .DS_Store
                    Thumbs.db
                    """,
            "flutter", """
                    # Dart/Flutter
                    .dart_tool/
                    .flutter-plugins
                    .flutter-plugins-dependencies
                    build/
                    
                    # Dependencies
                    .pub-cache/
                    .pub/
                    
                    # IDEs and editors
                    .idea/
                    .vscode/
                    *.iml
                    
                    # System Files
                    .DS_Store
                    Thumbs.db
                    """,
            // Add other templates...
            "default", """
                    # Build output
                    build/
                    dist/
                    
                    # Dependencies
                    node_modules/
                    
                    # IDEs and editors
                    .idea/
                    .vscode/
                    *.swp
                    *.swo
                    
                    # Logs
                    logs/
                    *.log
                    
                    # System Files
                    .DS_Store
                    Thumbs.db
                    """
    );

    public static String forProjectType(String projectType) {
        return TEMPLATES.getOrDefault(projectType.toLowerCase(), TEMPLATES.get("default"));
    }
}
