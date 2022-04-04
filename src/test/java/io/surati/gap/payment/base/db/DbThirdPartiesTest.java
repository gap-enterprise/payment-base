package io.surati.gap.payment.base.db;

import com.lightweight.db.EmbeddedPostgreSQLDataSource;
import io.surati.gap.database.utils.extensions.DatabaseSetupExtension;
import io.surati.gap.payment.base.api.ThirdParties;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class DbThirdPartiesTest {

    @RegisterExtension
    final DatabaseSetupExtension src = new DatabaseSetupExtension(
        new PaymentBaseDatabaseBuiltWithLiquibase(
            new EmbeddedPostgreSQLDataSource()
        )
    );

    private ThirdParties items;

    @BeforeEach
    void setUp() {
        this.items = new DbThirdParties(this.src);
    }

    @Test
    void checksExistence() {
        final String code = "T021";
        MatcherAssert.assertThat(
            "Should not contain code",
            this.items.has(code),
            Matchers.is(false)
        );
        this.items.add(code, "SURATI SAS", "SURATI");
        MatcherAssert.assertThat(
            "Should contain code",
            this.items.has(code),
            Matchers.is(true)
        );
    }
}
