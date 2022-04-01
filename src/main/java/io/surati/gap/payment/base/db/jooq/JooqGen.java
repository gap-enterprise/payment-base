/*
 * Copyright (c) 2022 Surati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.surati.gap.payment.base.db.jooq;

import com.minlessika.utils.ConsoleArgs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.surati.gap.database.utils.jooq.JooqGenerator;
import io.surati.gap.payment.base.db.PaymentBaseDatabaseBuiltWithLiquibase;
import java.util.Map;

/**
 * Class used to generate jOOQ classes.
 *
 * @since 0.1
 */
public final class JooqGen {

    /**
     * Entry point.
     * @param args Arguments
     */
    public static void main(final String[] args) throws Exception {
        final Map<String, String> map = new ConsoleArgs("--", args).asMap();
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName(map.get("driver"));
        config.setJdbcUrl(map.get("url"));
        config.setUsername(map.get("user"));
        config.setPassword(map.get("password"));
        new JooqGenerator(
            new PaymentBaseDatabaseBuiltWithLiquibase(
                new HikariDataSource(config)
            ),
            "io.surati.gap.payment.base.db.jooq.generated",
            "pay_(.*)",
            "src/main/java"
        ).start();
    }
}
