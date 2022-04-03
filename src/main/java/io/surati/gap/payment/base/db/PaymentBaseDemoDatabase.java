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
package io.surati.gap.payment.base.db;

import com.baudoliver7.jdbc.toolset.wrapper.DataSourceWrap;
import javax.sql.DataSource;

/**
 * Database with demo data.
 *
 * @since 0.1
 */
public final class PaymentBaseDemoDatabase extends DataSourceWrap {

    /**
     * Ctor.
     * @param src Data source
     */
    public PaymentBaseDemoDatabase(final DataSource src) {
        super(
            new PaymentBaseDatabaseBuiltWithLiquibase(
                src, "base,demo"
            )
        );
    }
}
