package com.t9radar.radar.domain;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.t9radar.radar.config.ApplicationTestBaseConfig;
import com.t9radar.radar.config.JpaAuditingConfiguration;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
    JpaAuditingConfiguration.class
})
@ApplicationTestBaseConfig
public abstract class AbstractRepositoryTests  extends AbstractAnyTests{

}