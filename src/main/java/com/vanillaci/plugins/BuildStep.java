package com.vanillaci.plugins;

import org.jetbrains.annotations.*;

/**
 * Extension point for creating custom build-steps.
 * Use the provided BuildStepContext parameter to set status, halt the build process, access build parameters, add build parameters, or access other useful objects.
 *
 * @author Joel Johnson
 * @since 0.0.1
 */
public interface BuildStep {
	/**
	 * Implementation of what the build step should do.
	 *
	 * @param context The context of the build when the build step is called. Contains useful data like what step the build is on, how many steps total, etc.
	 *                You can set the {@link com.vanillaci.plugins.BuildStep.Result} or the {@link com.vanillaci.plugins.BuildStep.Status} of the build step.
	 *                For example if your build step detects a problem, you can set the status on the context to {@link com.vanillaci.plugins.BuildStep.Status#HALT} and no more build steps will be executed after this one.
	 * @throws Exception
	 * @since 0.0.1
	 */
	public void execute(BuildStepContext context) throws Exception;

	/**
	 * The result of the build. This is to help give the user an at-a-glance idea of what happened with the build.
	 * @since 0.0.1
	 */
	public static enum Result {
		/**
		 * The result when the build finished without any problems.
		 * @since 0.0.1
		 */
		SUCCESS,

		/**
		 * Sanity checks have failed or tests have failed.
		 * @since 0.0.1
		 */
		FAILURE,

		/**
		 * An unexpected error occurred. This is typically only used if there was an unhandled exception thrown by the BuildStep.
		 * @since 0.0.1
		 */
		ERROR,

		/**
		 * The BuildStep was canceled while it was running. Typically because a user or plugin canceled the run.
		 * @since 0.0.1
		 */
		ABORTED;

		public boolean isBetterThan(@NotNull Result that) {
			return this.compareTo(that) < 0;
		}

		public boolean isBetterThanOrEqualTo(@NotNull Result that) {
			return this.compareTo(that) <= 0;
		}

		public boolean isWorseThan(@NotNull Result that) {
			return this.compareTo(that) > 0;
		}

		public boolean isWorseThanOrEqualTo(@NotNull Result that) {
			return this.compareTo(that) >= 0;
		}
	}

	/**
	 * Lets to worker know whether or not it should continue running build steps.
	 * It doesn't matter what the Result is, the worker determines whether or not to continue solely on this status.
	 * @since 0.0.1
	 */
	public static enum Status {
		/**
		 * Tells the worker to continue running build steps.
		 * @since 0.0.1
		 */
		CONTINUE,

		/**
		 * Tells the worker to stop running build steps.
		 * @since 0.0.1
		 */
		HALT,

		/**
		 * Tells the worker that we're in the post-build phase and that we should attempt to continue running all post build steps no matter what.
		 * POST_BUILD and CONTINUE effectively behave the same way,
		 * however, {@link com.vanillaci.plugins.BuildStepContext#setResult(com.vanillaci.plugins.BuildStep.Result, com.vanillaci.plugins.BuildStep.Status)}
		 * will not override POST_BUILD like it will CONTINUE.
		 * @since 0.0.1
		 */
		POST_BUILD
	}
}
