package inc.nomard.velocity.exception;

import picocli.CommandLine;

import java.io.PrintWriter;

public class ParameterExceptionHandler implements CommandLine.IParameterExceptionHandler {
    @Override
    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        PrintWriter err = cmd.getErr();

        err.println(cmd.getColorScheme().errorText("Error: " + ex.getMessage()));

        if (!CommandLine.UnmatchedArgumentException.printSuggestions(ex, err)) {
            err.printf("Try '%s --help' for more information.%n",
                    cmd.getCommandName());
        }

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : cmd.getCommandSpec().exitCodeOnInvalidInput();
    }
}
