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

import java.util.List;

import java.util.concurrent.*;

import java.util.function.Consumer;
import java.util.function.ObjLongConsumer;

import org.jspecify.annotations.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.jmp.util.logging.LoggerUtils.*;

/// The structured concurrency class.
final class StructuredConcurrency {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The user identifier.
    private final String userId = "Jonathan";

    /// The default constructor.
    StructuredConcurrency() {
        super();
    }

    /// Log dashboard function.
    final Consumer<Dashboard> logDashboard =
            d -> this.logger.info("Dashboard : {}", d);

    /// Log duration function.
    final ObjLongConsumer<String> logDuration =
            (label, durationMs) -> {
                this.logger.info("{}: {} ms", label, durationMs);
            };

    /// The demo method.
    void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.sequential();
        this.parallel();
        this.structured();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    // Handle the work sequentially.
    private void sequential() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        try {
            final long start = System.currentTimeMillis();
            final Dashboard dashboard = this.loadDashboardSequentially(this.userId);
            final long end = System.currentTimeMillis();

            this.logDuration.accept("Sequential", end - start);
            this.logDashboard.accept(dashboard);
        } catch (final InterruptedException e) {
            this.logger.error("loadDashboardSequentially interrupted: ", e);
            Thread.currentThread().interrupt();
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    // Handle the work in parallel.
    private void parallel() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        try {
            final long start = System.currentTimeMillis();
            final Dashboard dashboard = this.loadDashboardInParallel(this.userId);
            final long end = System.currentTimeMillis();

            this.logDuration.accept("Parallel", end - start);
            this.logDashboard.accept(dashboard);
        } catch (final Exception e) {
            this.logger.error("loadDashboardInParallel exception: ", e);
            Thread.currentThread().interrupt();
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    // Handle the work in a structured way.
    private void structured() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        try {
            final long start = System.currentTimeMillis();
            final Dashboard dashboard = this.loadDashboardStructurally(this.userId, false);
            final long end = System.currentTimeMillis();

            this.logDuration.accept("Structured", end - start);
            this.logDashboard.accept(dashboard);
        } catch (final Exception e) {
            this.logger.error("loadDashboardStructurally exception: ", e);
            Thread.currentThread().interrupt();
        }

        try {
            final long start = System.currentTimeMillis();
            final Dashboard dashboard = this.loadDashboardStructurallyWithCustomJoiner(this.userId, false);
            final long end = System.currentTimeMillis();

            this.logDuration.accept("Structured", end - start);
            this.logDashboard.accept(dashboard);
        } catch (final Exception e) {
            this.logger.error("loadDashboardStructurally exception: ", e);
            Thread.currentThread().interrupt();
        }

        try {
            final long start = System.currentTimeMillis();
            final Dashboard dashboard = this.loadDashboardStructurallyWithCustomJoiner(this.userId, true);
            final long end = System.currentTimeMillis();

            this.logDuration.accept("Structured", end - start);
            this.logDashboard.accept(dashboard);
        } catch (final Exception e) {
            this.logger.error("loadDashboardStructurally exception: ", e);
            Thread.currentThread().interrupt();
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    // Load the dashboard sequentially.
    //
    // @param  id    java.lang.String
    // @return       net.jmp.demo.java27.StructuredConcurrency.Dashboard
    private Dashboard loadDashboardSequentially(final String id) throws InterruptedException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(id));
        }

        final User user = findUser(id, false);
        final Orders orders = fetchOrders(id);
        final Dashboard dashboard = new Dashboard(user, orders);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(dashboard));
        }

        return dashboard;
    }

    // Load the dashboard in parallel.
    //
    // @param  id    java.lang.String
    // @return       net.jmp.demo.java27.StructuredConcurrency.Dashboard
    private Dashboard loadDashboardInParallel(final String id) throws ExecutionException, InterruptedException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(id));
        }

        Dashboard dashboard = null;

        try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Both calls start now, in parallel, each on its own virtual thread

            final Future<User> user = executor.submit(() -> findUser(id, false));
            final Future<Orders> orders = executor.submit(() -> fetchOrders(id));

            // Problem 1: If user.get() throws, nobody tells the order task. It just keeps running and close()
            // waits for it to finish.

            // Problem 2: We notice the failure at 200ms only because we happened to call get() on the failing future
            // first. Swap the two get() calls and we wait on orders for the full second before learning the user failed.
            // The order you write your get() calls decides how fast you notice, and you cannot know the right order in advance.

            dashboard = new Dashboard(user.get(),orders.get());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(dashboard));
        }

        return dashboard;
    }

    // Load the dashboard structurally.
    //
    // @param  id                   java.lang.String
    // @param  isUserServiceDown    boolean
    // @return                      net.jmp.demo.java27.StructuredConcurrency.Dashboard
    private Dashboard loadDashboardStructurally(final String id, final boolean isUserServiceDown) throws ExecutionException, InterruptedException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(id, isUserServiceDown));
        }

        Dashboard dashboard = null;

        // Default policy: wait for all subtasks to succeed or fail as soon
        // as one fails. close() runs at the end of the block whether we
        // returned or threw, and does not return until both threads are done.
        // Specify the default joiner.

        try (final StructuredTaskScope<Object, Void, ExecutionException> scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())
        ) {
            // Subtask, not Future. No blocking get(), no cancel().
            // Waiting and cancelling belong to the scope now.

            final StructuredTaskScope.Subtask<User> user = scope.fork(() -> findUser(id, isUserServiceDown));
            final StructuredTaskScope.Subtask<Orders> orders = scope.fork(() -> fetchOrders(id));

            // One wait for the whole group. If either subtask fails, the other
            // is interrupted, and the failure comes out of here as an ExecutionException.

            scope.join();

            // join() returned, so both succeeded and get() cannot fail.
            dashboard = new Dashboard(user.get(), orders.get());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(dashboard));
        }

        return dashboard;
    }

    /// Load the dashboard structurally using a custom joiner.
    private Dashboard loadDashboardStructurallyWithCustomJoiner(final String id, final boolean isUserServiceDown) throws ExecutionException, InterruptedException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(id, isUserServiceDown));
        }

        Dashboard dashboard = null;

        try (final StructuredTaskScope<Object, Dashboard, ExecutionException> scope = StructuredTaskScope.open(
                new DashboardJoiner())
        ) {
            scope.fork(() -> findUser(id, isUserServiceDown));
            scope.fork(() -> fetchOrders(id));

            dashboard = scope.join();
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(dashboard));
        }

        return dashboard;
    }

    record User(String name) {}
    record Orders(List<String> items) {}
    record Dashboard(User user, Orders orders) {}

    static User findUser(final String id, final boolean isUserServiceDown) throws InterruptedException {
        if (isUserServiceDown) {
            Thread.sleep(200);
            throw new IllegalStateException("User service is down");
        }

        Thread.sleep(1_000);    // Pretend this is a network call

        return new User(id);
    }

    static Orders fetchOrders(String id) throws InterruptedException {
        Thread.sleep(1_000);    // Pretend this is another network call

        if ("Jonathan".equals(id)) {
            return new Orders(List.of("Fundamentals of Software Engineering"));
        }

        return new Orders(List.of("Something Else"));
    }

    // A custom Joiner implementing Java 27 specs
    // <T> subtask type, <R> scope.join() return type, <R_X> exception type
    static class DashboardJoiner implements StructuredTaskScope.Joiner<Object, Dashboard, ExecutionException> {
        // Internal state to hold completed subtask data
        private @Nullable User capturedUser;
        private @Nullable Orders capturedOrders;
        private @Nullable Throwable failure;

        @Override
        public boolean onComplete(StructuredTaskScope.Subtask<Object> subtask) {
            if (subtask.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                final Object result = subtask.get();

                // Dynamically identify the data type as it arrives

                if (result instanceof User u) {
                    this.capturedUser = u;
                } else if (result instanceof Orders o) {
                    this.capturedOrders = o;
                }
            } else if (subtask.state() == StructuredTaskScope.Subtask.State.FAILED) {
                this.failure = subtask.exception();

                return true;    // Short-circuit: cancel all other tasks immediately on failure
            }

            return false;       // Keep waiting for other tasks
        }

        @Override
        public Dashboard result() throws ExecutionException {
            // This is invoked by scope.join() to produce the final object
            if (this.failure != null) {
                throw new ExecutionException("Dashboard compilation failed", failure);
            }

            if (this.capturedUser == null || capturedOrders == null) {
                throw new ExecutionException("Required metrics were missing", null);
            }

            return new Dashboard(this.capturedUser, this.capturedOrders);
        }

        @Override
        public Dashboard timeout() throws ExecutionException {
            throw new ExecutionException("Scope timed out fetching dashboard details", null);
        }
    }
}
