package inc.nomard.velo.validator;

import com.fasterxml.jackson.databind.JsonNode;
import inc.nomard.velo.exception.ErrorCode;
import inc.nomard.velo.exception.VeloException;
import inc.nomard.velo.model.ProjectType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ProjectValidator {
    private static final Pattern VALID_PROJECT_NAME = Pattern.compile("^[a-z][a-z0-9-]*$");
    private static final Pattern VALID_PATH = Pattern.compile("^[a-zA-Z0-9/_.-]+$");
    private static final int MAX_PATH_LENGTH = 255;
    private static final Set<String> RESERVED_NAMES = Set.of("con", "prn", "aux", "nul", "com1", "com2", "com3", "com4",
            "com5", "com6", "com7", "com8", "com9", "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9");

    public static void validateProjectCreation(String projectName, String projectType, Path targetPath) {
        validateProjectName(projectName);
        validateProjectType(projectType);
        validateTargetPath(targetPath);
        validateProjectPath(targetPath.resolve(projectName));
    }

    public static void validateProjectName(String projectName) {
        if (projectName == null || projectName.isEmpty()) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_NAME, "Project name cannot be empty");
        }
        if (!VALID_PROJECT_NAME.matcher(projectName).matches()) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_NAME,
                    "Project name must start with a lowercase letter and contain only lowercase letters, numbers, and hyphens");
        }
        if (RESERVED_NAMES.contains(projectName.toLowerCase())) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_NAME,
                    "Project name cannot be a reserved system name: " + projectName);
        }
    }

    public static void validateProjectType(String projectType) {
        try {
            ProjectType.valueOf(projectType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_TYPE,
                    "Invalid project type. Supported types: " + Arrays.toString(ProjectType.values()));
        }
    }

    public static void validateTargetPath(Path path) {
        if (path == null) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_PATH, "Target path cannot be null");
        }
        String pathStr = path.toString();
        if (!VALID_PATH.matcher(pathStr).matches()) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_PATH,
                    "Path contains invalid characters: " + pathStr);
        }
        if (pathStr.length() > MAX_PATH_LENGTH) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_PATH,
                    "Path exceeds maximum length of " + MAX_PATH_LENGTH + " characters");
        }
    }

    public static void validateProjectPath(Path projectPath) {
        if (projectPath.toFile().exists()) {
            throw new VeloException(ErrorCode.PATH_ALREADY_EXISTS,
                    "Project path already exists: " + projectPath);
        }
    }

    public static void validateTemplate(JsonNode template) {
        validateTemplateStructure(template);
        validateTemplateMetadata(template);
        validateTemplateFiles(template.get("structure"));
    }

    private static void validateTemplateStructure(JsonNode template) {
        if (!template.isObject()) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT, "Template must be a JSON object");
        }

        List<String> requiredFields = Arrays.asList("name", "version", "description", "structure");
        for (String field : requiredFields) {
            if (!template.has(field)) {
                throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                        "Template missing required field: " + field);
            }
        }
    }

    private static void validateTemplateMetadata(JsonNode template) {
        if (!template.get("version").isTextual() || !isValidVersion(template.get("version").asText())) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Invalid version format. Expected format: x.y.z");
        }
    }

    private static boolean isValidVersion(String version) {
        return Pattern.compile("^\\d+\\.\\d+\\.\\d+$").matcher(version).matches();
    }

    private static void validateTemplateFiles(JsonNode structure) {
        validateNode(structure, new ArrayList<>());
    }

    private static void validateNode(JsonNode node, List<String> path) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String name = entry.getKey();
                validateFileName(name, path);

                List<String> newPath = new ArrayList<>(path);
                newPath.add(name);
                validateNode(entry.getValue(), newPath);
            });
        } else if (node.isTextual()) {
            // Valid leaf node (file content)
        } else {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Invalid node type at path: " + String.join("/", path));
        }
    }

    private static void validateFileName(String name, List<String> path) {
        if (!Pattern.compile("^[a-zA-Z0-9._-]+$").matcher(name).matches()) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Invalid file name '" + name + "' at path: " + String.join("/", path));
        }
    }
}