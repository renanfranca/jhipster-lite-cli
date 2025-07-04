package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tech.jhipster.lite.cli.shared.exit.domain.SystemExit;

@Component
@Primary
public class FakedSystemExit implements SystemExit {

  private int lastExitCode = 0;

  @Override
  public void exit(int exitCode) {
    this.lastExitCode = exitCode;
  }

  public int lastExitCode() {
    return lastExitCode;
  }

  public void reset() {
    lastExitCode = 0;
  }
}
