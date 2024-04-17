package sn.sonatel.dsi.ins.ftsirc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.sonatel.dsi.ins.ftsirc.config.AsyncSyncConfiguration;
import sn.sonatel.dsi.ins.ftsirc.config.EmbeddedSQL;
import sn.sonatel.dsi.ins.ftsirc.config.JacksonConfiguration;
import sn.sonatel.dsi.ins.ftsirc.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { SaytuBackendApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
public @interface IntegrationTest {
}
