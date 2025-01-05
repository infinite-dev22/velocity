package inc.nomard.velocity.cli;

import inc.nomard.velocity.model.ProjectType;

import java.util.ArrayList;
import java.util.List;

public class ProjectTypes extends ArrayList<String> {
    ProjectTypes() {
        super(List.of(
                ProjectType.SPRINGBOOT.toString(),
                ProjectType.FLUTTER.toString(),
                ProjectType.KOTLIN.toString(),
                ProjectType.REACT.toString(),
                ProjectType.SWIFT.toString()));
    }
}
