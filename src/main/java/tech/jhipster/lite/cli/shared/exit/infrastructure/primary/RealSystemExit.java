package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import org.springframework.stereotype.Component;
import tech.jhipster.lite.cli.shared.exit.domain.SystemExit;

@Component
class RealSystemExit implements SystemExit {

  @Override
  public void exit(int exitCode) {
    System.exit(exitCode);
  }
}
