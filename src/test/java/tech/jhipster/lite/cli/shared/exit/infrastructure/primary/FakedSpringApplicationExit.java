package tech.jhipster.lite.cli.shared.exit.infrastructure.primary;

import org.springframework.context.ApplicationContext;
import tech.jhipster.lite.cli.shared.exit.domain.SpringApplicationExit;

//@Component
//@Primary
//TODO DELETE IT
class FakedSpringApplicationExit implements SpringApplicationExit {

  @Override
  public int exit(ApplicationContext context) {
    return 0;
  }
}
