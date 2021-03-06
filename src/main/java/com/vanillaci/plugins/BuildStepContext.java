package com.vanillaci.plugins;

import java.io.*;
import java.util.*;

/**
 * Provides context for BuildSteps.
 * Allows plugin developers to easily access things like the working directory of the job, the SDK, and parameters.
 *
 * @author Joel Johnson
 * @since 0.0.1
 */
public interface BuildStepContext {
	/**
	 * All the parameters provided globally, specifically to this build step, or provided by previous buildSteps
	 * To add parameters for later buildSteps, use the {@link #addParameter(String, String)} method.
	 * @return read-only map of the parameters.
	 * @since 0.0.1
	 */
	Map<String, String> getParameters();

	/**
	 * @return The working directory of all the Work. Creating or touching files outside of this directory should be avoided.
	 * @since 0.0.1
	 */
	File getWorkspace();

	/**
	 * Adds the given parameter to the parameter map for future build steps.
	 * @since 0.0.1
	 */
	void addParameter(String parameterName, String parameterValue);

	/**
	 * Returns the last known result of all the buildSteps. Typically, it's the worst result of all build steps that have run so far,
	 * but it's possible, albeit rare, that another buildStep has overwritten the value.
	 * @since 0.0.1
	 */
	BuildStep.Result getResult();

	/**
	 * Returns the last known status of all the buildSteps.
	 * @return Typically CONTINUE will be returned if it's in the Build phase,
	 * POST_BUILD will be returned if it's in the PostBuild phase,
	 * and HALT will be returned if it's in the Build phase and the last build step errored out, or was told to halt by another plugin.
	 * @since 0.0.1
	 */
	BuildStep.Status getStatus();

	/**
	 * Manually overwrites the Result and Status.
	 * Typically you will want to use {@link #setResult(com.vanillaci.plugins.BuildStep.Result, com.vanillaci.plugins.BuildStep.Status)} instead.
	 * This would be used if the plugin needs to set the result or status to a better state than it is currently.
	 * @since 0.0.1
	 */
	void forceSetResult(BuildStep.Result result, BuildStep.Status status);

	/**
	 * Only sets the given result if it's worse than the current result.
	 * @param result if the result.isWorseThan(currentResult) == true, then the current result will be overwritten.
	 * @param status if the status is HALT and the current status is not POST_BUILD, then the current status will be set to HALT.
	 * @since 0.0.1
	 */
	void setResult(BuildStep.Result result, BuildStep.Status status);

	/**
	 * @return The build step that's going to be run.
	 * 			From the context of a BuildStep's execute method, BuildContext.getBuildStep() == this.
	 * @since 0.0.1
	 */
	BuildStep getBuildStep();

	/**
	 * Override the buildStep that's about to run. Obviously, calling this will only have effect if it's called before the
	 * build step is run. For example, calling it from {@link com.vanillaci.plugins.BuildStepInterceptor#after} will have no effect.
	 *
	 * @param buildStep The buildStep that should execute.
	 * @since 0.0.1
	 */
	void setBuildStep(BuildStep buildStep);

	/**
	 * Get the number of steps executed before now.
	 * @since 0.0.1
	 */
	int getBuildStepIndex();

	/**
	 * Gets the total number of steps that will be executed.
	 * @since 0.0.1
	 */
	int getTotalBuildSteps();
}
