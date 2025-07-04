package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tech.jhipster.lite.cli.shared.exit.domain.SpringApplicationExit;

@Component
@Primary
class FakedSpringApplicationExit implements SpringApplicationExit {

  @Override
  public int exit(ApplicationContext context) {
    return 0;
  }
}
