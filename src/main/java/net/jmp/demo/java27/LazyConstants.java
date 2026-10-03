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

/// The lazy constants class.
final class LazyConstants {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The string validator is a lazy constant.
    private final LazyConstant<StringValidator> stringValidator = LazyConstant.of(this::createStringValidator);

    /// The default constructor.
    LazyConstants() {
        super();
    }

    /// The demo method.
    void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        if (this.stringValidator.get().isStringValid("Hello")) {
            this.logger.info("Hello is valid!");
        }

        if (!this.stringValidator.get().isStringValid(" ")) {
            this.logger.info("<Blank> is not valid!");
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Create a new string validator.
    ///
    /// @return net.jmp.demo.java27.LazyConstants.StringValidator
    private StringValidator createStringValidator() {
        return new StringValidator();
    }

    /// A simple string validator class
    static class StringValidator {
        /// Return true if the string is not blank
        ///
        /// @param  string  java.lang.String
        /// @return         boolean
        boolean isStringValid(final String string) {
            return !string.isBlank();
        }
    }
}
