package inc.nomard.velocity.templating;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.nomard.velocity.VeloResourceLoader;
import inc.nomard.velocity.exception.ErrorCode;
import inc.nomard.velocity.exception.VeloException;
import inc.nomard.velocity.model.ProjectType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

// Enhanced template loading with better validation
public class TemplateLoader {
    private static final Set<String> VALID_PROJECT_TYPES = Set.of(
            ProjectType.SPRINGBOOT.toString(),
            ProjectType.FLUTTER.toString(),
            ProjectType.KOTLIN.toString(),
            ProjectType.REACT.toString(),
            ProjectType.SWIFT.toString()
    );

    public JsonNode loadTemplate(String projectType) {
        validateProjectType(projectType);
        try {
            Path templatePath = resolveTemplatePath(projectType);
            JsonNode template = loadAndValidateTemplate(templatePath);
            validateTemplateStructure(template);
            return template;
        } catch (IOException | URISyntaxException e) {
            throw new VeloException(ErrorCode.TEMPLATE_NOT_FOUND,
                    "Failed to load template for " + projectType + ": " + e.getMessage());
        }
    }

    private void validateProjectType(String projectType) {
        if (!VALID_PROJECT_TYPES.contains(projectType.toLowerCase())) {
            throw new VeloException(ErrorCode.INVALID_PROJECT_TYPE,
                    "Unsupported project type: " + projectType +
                            ". Valid types are: " + String.join(", ", VALID_PROJECT_TYPES));
        }
    }

    private Path resolveTemplatePath(String projectType) throws IOException, URISyntaxException {
        var templateResource = VeloResourceLoader.loadResourceFile(projectType + ".json");
        if (templateResource == null) {
            throw new VeloException(ErrorCode.TEMPLATE_NOT_FOUND,
                    "Template file not found for project type: " + projectType);
        }
        return Path.of(templateResource.toURI());
    }

    private JsonNode loadAndValidateTemplate(Path templatePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(Files.newInputStream(templatePath));
        } catch (JsonParseException e) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Invalid template format: " + e.getMessage());
        }
    }

    private void validateTemplateStructure(JsonNode template) {
        if (!template.has("structure") || !template.get("structure").isObject()) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Template must contain a 'structure' object");
        }
        if (!template.has("version") || !template.get("version").isObject()) {
            throw new VeloException(ErrorCode.INVALID_TEMPLATE_FORMAT,
                    "Template must contain 'version' information");
        }
    }
}
