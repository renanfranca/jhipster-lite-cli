package tech.jhipster.lite.cli.command.infrastructure.primary;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import tech.jhipster.lite.cli.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import tech.jhipster.lite.cli.shared.spinnerprogress.domain.SpinnerProgress;
import tech.jhipster.lite.cli.shared.spinnerprogress.infrastructure.primary.SpinnerProgressProvider;

@Component
class JHLiteCommandRunner implements CommandLineRunner, ExitCodeGenerator {

  private final JHLiteCommandsFactory commandsFactory;
  private final SpinnerProgress spinnerProgress;
  private int exitCode;

  public JHLiteCommandRunner(JHLiteCommandsFactory commandsFactory) {
    this.commandsFactory = commandsFactory;
    this.spinnerProgress = SpinnerProgressProvider.get();
  }

  @Override
  public void run(String... args) {
    spinnerProgress.show("Running command");

    CommandLineOutput output = captureCommandExecution(args);

    displayResultsWithSpinner(spinnerProgress, output);
    displayOutputAndErrors(output);
  }

  private CommandLineOutput captureCommandExecution(String[] args) {
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();

    try (PrintWriter outputWriter = new PrintWriter(outputBuffer, true); PrintWriter errorWriter = new PrintWriter(errorBuffer, true)) {
      CommandLine commandLine = new CommandLine(commandsFactory.buildCommandSpec());
      commandLine.setOut(outputWriter);
      commandLine.setErr(errorWriter);

      exitCode = commandLine.execute(args);

      return new CommandLineOutput(outputBuffer.toString(), errorBuffer.toString(), exitCode);
    }
  }

  private void displayResultsWithSpinner(SpinnerProgress spinnerProgress, CommandLineOutput output) {
    if (output.isSuccessful()) {
      spinnerProgress.success("Command executed");
    } else {
      spinnerProgress.failure("Command failed");
    }
  }

  private void displayOutputAndErrors(CommandLineOutput output) {
    if (!output.output().isEmpty()) {
      System.out.print(output.output());
    }

    if (!output.errors().isEmpty()) {
      System.err.print(output.errors());
    }
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage(reason = "Don not need to test when using picocli framework")
  public int getExitCode() {
    return exitCode;
  }

  private record CommandLineOutput(String output, String errors, int exitCode) {
    boolean isSuccessful() {
      return exitCode == 0;
    }
  }
}
