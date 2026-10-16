package net.jmp.demo.java27;

/*
 * (#)ISO8601.java  0.1.0   10/16/2026
 *
 * @author   Jonathan Parker
 *
 * MIT License
 *
 * Copyright (c) 2026 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.jmp.util.logging.LoggerUtils.entry;
import static net.jmp.util.logging.LoggerUtils.exit;

/// The ISO-8601 class.
final class ISO8601 {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The default constructor.
    ISO8601() {
        super();
    }

    /// The demo method.
    void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.standardForm();
        this.shortForm();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// The standard form method.
    private void standardForm() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        final ZonedDateTime zonedOriginalDateTime = ZonedDateTime.parse("2026-06-01T22:57:00+02:00", formatter);
        final ZonedDateTime zonedLocalDateTime = zonedOriginalDateTime.withZoneSameInstant(ZoneId.systemDefault());
        final LocalDateTime localDateTime = zonedLocalDateTime.toLocalDateTime();

        this.logger.info("Standard form: {}", localDateTime);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// The short form method.
    private void shortForm() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        final ZonedDateTime zonedOriginalDateTime = ZonedDateTime.parse("2026-06-01T22:57:00+02", formatter);
        final ZonedDateTime zonedLocalDateTime = zonedOriginalDateTime.withZoneSameInstant(ZoneId.systemDefault());
        final LocalDateTime localDateTime = zonedLocalDateTime.toLocalDateTime();

        this.logger.info("Short form: {}", localDateTime);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
