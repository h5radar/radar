package com.h5radar.radar;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.h5radar.radar.config.ApplicationTestBaseConfig;
import com.h5radar.radar.config.JpaAuditingConfiguration;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
    JpaAuditingConfiguration.class
})
@ApplicationTestBaseConfig
public abstract class AbstractRepositoryTests  extends AbstractAnyTests{

}
