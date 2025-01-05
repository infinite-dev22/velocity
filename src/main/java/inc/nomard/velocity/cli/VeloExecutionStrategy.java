package inc.nomard.velocity.cli;

import inc.nomard.velocity.exception.VeloException;
import picocli.CommandLine;

public class VeloExecutionStrategy implements CommandLine.IExecutionStrategy {
    @Override
    public int execute(CommandLine.ParseResult parseResult) {
        try {
            return new CommandLine.RunLast().execute(parseResult);
        } catch (VeloException e) {
            System.err.printf("Error %d: %s%n", e.getCode(), e.getMessage());
            return e.getCode().getCode();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
