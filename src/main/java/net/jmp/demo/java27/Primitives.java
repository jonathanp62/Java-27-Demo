package net.jmp.demo.java27;

/*
 * (#)LazyConstants.java    0.1.0   10/03/2026
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.jmp.util.logging.LoggerUtils.entry;
import static net.jmp.util.logging.LoggerUtils.exit;

/// The primitives class.
final class Primitives {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The default constructor.
    Primitives() {
        super();
    }

    /// The demo method.
    void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.preJava27Ints();
        this.java27Ints();
        this.java27InstanceOf();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate pre-Java 27 switch expressions
    private void preJava27Ints() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final int port = 443;

        switch (port) {
            case 80  -> this.logger.info("HTTP");
            case 443 -> this.logger.info("HTTPS");
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate Java 27 switch expressions
    private void java27Ints() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final int score = 85;

        switch (score) {
            case int s when s >= 90 -> this.logger.info("very good");
            case int s when s >= 75 -> this.logger.info("good");
            case int s when s >= 60 -> this.logger.info("satisfactory");
            case int s when s >= 50 -> this.logger.info("sufficient");
            default                 -> this.logger.info("failed");
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate Java 27 switch expressions and instanceOf
    private void java27InstanceOf() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final double value = 3.14159;

        switch (value) {
            case byte   b -> this.logger.info(value + " instanceof byte:   " + b);
            case short  s -> this.logger.info(value + " instanceof short:  " + s);
            case char   c -> this.logger.info(value + " instanceof char:   " + c);
            case int    i -> this.logger.info(value + " instanceof int:    " + i);
            case long   l -> this.logger.info(value + " instanceof long:   " + l);
            case float  f -> this.logger.info(value + " instanceof float:  " + f);
            case double d -> this.logger.info(value + " instanceof double: " + d);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
