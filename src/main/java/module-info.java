module velocity {
    requires info.picocli;
    requires com.fasterxml.jackson.databind;
    requires org.eclipse.jgit;
    exports inc.nomard.velocity;
    opens inc.nomard.velocity;
    exports inc.nomard.velocity.cli;
    opens inc.nomard.velocity.cli;
    exports inc.nomard.velocity.exception;
    opens inc.nomard.velocity.exception;
    exports inc.nomard.velocity.git;
    opens inc.nomard.velocity.git;
    exports inc.nomard.velocity.model;
    opens inc.nomard.velocity.model;
    exports inc.nomard.velocity.templating;
    opens inc.nomard.velocity.templating;
    exports inc.nomard.velocity.validator;
    opens inc.nomard.velocity.validator;
}