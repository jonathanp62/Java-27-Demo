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

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.jmp.util.logging.LoggerUtils.*;

/// The lazy constants class.
final class LazyConstants {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The string validator is a lazy constant.
    private final LazyConstant<StringValidator> stringValidator = LazyConstant.of(this::createStringValidator);

    /// A lazy list of 100 square roots, each evaluated only when selected
    private final List<Double> squareRoots = List.ofLazy(100, i -> {
        this.logger.info("Initializing list element at index {}", i);

        return Math.sqrt(i);
    });

    /// A lazy map of exchange rates.
    private final Map<String, BigDecimal> exchangeRates = Map.ofLazy(
            Set.of("USD", "GBP", "JPY", "CHF"),
            this::fetchExchangeRate
    );

    /// The default constructor.
    LazyConstants() {
        super();
    }

    /// The demo method.
    void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.stringValidation();
        this.lists();
        this.maps();
        this.sets();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate lazy string validation.
    private void stringValidation() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        if (this.stringValidator.get().isStringValid("Hello")) {
            this.logger.info("Hello is valid!");
        }

        if (!this.stringValidator.get().isStringValid(" ")) {
            this.logger.info("<Blank> is not valid!");
        }

        if (!this.stringValidator.get().isStringValid("")) {
            this.logger.info("<Empty> is not valid!");
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate lazy lists.
    private void lists() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.logger.info("squareRoots[0]    = {}", this.squareRoots.get(0));
        this.logger.info("squareRoots[1]    = {}", this.squareRoots.get(1));
        this.logger.info("squareRoots[2]    = {}", this.squareRoots.get(2));
        this.logger.info("squareRoots[0]    = {}", this.squareRoots.get(0));
        this.logger.info("squareRoots.first = {}", this.squareRoots.getFirst());
        this.logger.info("squareRoots.last  = {}", this.squareRoots.getLast());

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate lazy maps.
    private void maps() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        for (final Map.Entry<String, BigDecimal> entry : this.exchangeRates.entrySet()) {
            this.logger.info("The exchange for {} is {}", entry.getKey(), entry.getValue());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Demonstrate the lazy sets.
    private void sets() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Fetch the exchange rate based on the currency.
    ///
    /// @param  currency    java.lang.String
    /// @return             java.math.BigDecimal
    private BigDecimal fetchExchangeRate(final String currency) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final BigDecimal result = switch (currency) {
            case "USD" -> new BigDecimal("1.00");
            case "GBP" -> new BigDecimal("0.7552");
            case "JPY" -> new BigDecimal("157.85");
            case "CHF" -> new BigDecimal("0.8288");
            default    -> BigDecimal.ONE;
        };

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Create a new string validator. Called the first
    /// time a get() is invoked on the lazy constant.
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
