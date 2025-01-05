module velo {
    requires info.picocli;
    requires com.fasterxml.jackson.databind;
    requires org.eclipse.jgit;
    exports inc.nomard.velo;
    opens inc.nomard.velo;
    exports inc.nomard.velo.cli;
    opens inc.nomard.velo.cli;
    exports inc.nomard.velo.exception;
    opens inc.nomard.velo.exception;
    exports inc.nomard.velo.git;
    opens inc.nomard.velo.git;
    exports inc.nomard.velo.model;
    opens inc.nomard.velo.model;
    exports inc.nomard.velo.templating;
    opens inc.nomard.velo.templating;
    exports inc.nomard.velo.validator;
    opens inc.nomard.velo.validator;
}