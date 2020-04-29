package org.jenkinsci.plugins.keisuke;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;

/**
 * Action for the page of the Jenkins project which uses this plugin.
 */
public class StepCountProjectAction implements Action {
	private transient PrintStream syslogger = System.out;

	public static final String PROJECT_URL_PATH = "keisuke";
	public static final String RESULT_URL_PATH = "result";
	public static final String ICON_FILE_NAME = "graph.png";

	//PipelineならWorkFlowJob、FreeStyleならFreeStyleProjectが入る
	private Job<?, ?> ownerProject;

	/**
	 * constructor for FreeStyleProject
	 * @param project 所属するプロジェクト
	 */
	public StepCountProjectAction(final Job<?, ?> project) {
		//syslogger.println("[DEBUG] StepCountProjectAction#StepCountProjectAction(" + project + ")");
		this.ownerProject = project;
	}

	/**
	 * Gets the string to be displayed.
	 * @return display name
	 */
	@Override
	public String getDisplayName() {
		if (this.hasResult()) {
			return Messages.steps();
		}
		return null;
	}

	/**
	 * Gets the file name of the icon.
	 * @return file name
	 */
	@Override
	public String getIconFileName() {
		if (this.hasResult()) {
			return ICON_FILE_NAME;
		}
		return null;
	}

	/**
	 * Gets the URL path name.
	 * @return URL path
	 */
	@Override
	public String getUrlName() {
		return PROJECT_URL_PATH;
	}

	/**
	 * Gets the URL path name of result
	 * @return URL path
	 */
	public String getResultUrlName() {
		return RESULT_URL_PATH;
	}

	private boolean hasResult() {
		StepCountBuildAction action = this.getLastBuildAction();
		return (action != null);
	}

	/**
	 * Create trend graph
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraph failed.
	 */
	public void doTrend(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createGraph(req, res);
		} else {
			new StepCountBuildAction(this.ownerProject.getFirstBuild()).createGraph(req, res);
		}
	}

	/**
	 * Create clickable map
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMap failed.
	 */
	public void doTrendMap(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createClickableMap(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/*
	 * Create trend graph for diff result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraph failed.
	 *
	public void doTrendDiff(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createGraphForDiff(req, res);
		} else {
			new StepCountBuildAction(this.ownerProject.getFirstBuild()).createGraphForDiff(req, res);
		}
	}*/

	/*
	 * Create clickable map for diff result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMap failed.
	 *
	public void doTrendMapDiff(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createClickableMapForDiff(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}*/

	/**
	 * Create trend graph for diff added result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraph failed.
	 */
	public void doTrendDiffAdded(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createGraphForDiffAdded(req, res);
		} else {
			new StepCountBuildAction(this.ownerProject.getFirstBuild()).createGraphForDiffAdded(req, res);
		}
	}

	/**
	 * Create clickable map for diff added result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMap failed.
	 */
	public void doTrendMapDiffAdded(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createClickableMapForDiffAdded(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * Create trend graph for diff deleted result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraph failed.
	 */
	public void doTrendDiffDeleted(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createGraphForDiffDeleted(req, res);
		} else {
			new StepCountBuildAction(this.ownerProject.getFirstBuild()).createGraphForDiffDeleted(req, res);
		}
	}

	/**
	 * Create clickable map for diff deleted result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMap failed.
	 */
	public void doTrendMapDiffDeleted(final StaplerRequest req, final StaplerResponse res) throws IOException {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action != null) {
			action.createClickableMapForDiffDeleted(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * Gets the instance of StepCountBuildAction of last finished build.
	 * @return StepCountBuildAction of last finished build.
	 */
	public StepCountBuildAction getLastBuildAction() {
		if (this.ownerProject == null) {
			syslogger.println("[DEBUG] StepCountProjectAction#getLastBuildAction()"
					+ ": owner project == null.");
			return null;
		}
		Run<?, ?> build = this.getLastActedBuildSince(this.ownerProject.getLastBuild());
		if (build == null) { // Not found
			//syslogger.println("[DEBUG] StepCountProjectAction#getLastBuildAction()"
			//		+ ": last acted build is null.");
			return null;
		}
		//syslogger.println("[DEBUG] StepCountProjectAction#getLastBuildAction()"
		//		+ ": last acted build #" + build.number);
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		// action is never null because found
		return action;
	}

	// 指定されたビルドを含み、ビルド完了済みかつ結果作成済みである直近のビルドを返す
	private Run<?, ?> getLastActedBuildSince(final Run<?, ?> lastBuild) {
		Run<?, ?> build = lastBuild;
		while (build != null) {
			if (!build.isBuilding()) {
				StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
				if (action != null && action.hasResult()) {
					return build;
				}
			}
			// 前のビルドをチェック
			build = build.getPreviousBuild();
		}
		return null;
	}

	/**
	 * Sends redirect response which will go to the last build page
	 * @param req request
	 * @param res response
	 * @throws IOException signal that redirect failed.
	 */
	public void doIndex(final StaplerRequest req, final StaplerResponse res) throws IOException {
		//syslogger.println("[DEBUG] StepCountProjectAction#doIndex(req, res) req = " + req.getRequestURI());
		Run<?, ?> build = this.getLastActedBuildSince(this.ownerProject.getLastBuild());
		// contextRoot/job/project/myUrl is current.
		if (build != null) {
			//syslogger.println("[DEBUG] StepCountProjectAction#doIndex(build!=null):"
			//		+ getResultUrlOf("../", build));
			res.sendRedirect2(getResultUrlOf("../", build));
		} else {
			//syslogger.println("[DEBUG] StepCountProjectAction#doIndex(build==null):../");
			res.sendRedirect2("../");
		}
	}

	/**
	 * Gets URL for the result of the build.
	 * @param base project URL path which ends with "/".
	 * @param build target build.
	 * @return URL for result of the build.
	 */
	public static String getResultUrlOf(final String base, final Run<?, ?> build) {
		StringBuilder sb = new StringBuilder();
		if (base != null && !base.isEmpty()) {
			sb.append(base);
		}
		if (build != null) {
			sb.append(build.getNumber()).append('/').append(PROJECT_URL_PATH).append('/')
				.append(RESULT_URL_PATH);
		}
		return sb.toString();
	}

	/**
	 * Gets selected DisplayStepKindEnum element from BuildAction.
	 * @return DisplayStepKindEnum element.
	 */
	public DisplayStepKindEnum getDisplayStepKindEnum() {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action == null || !action.hasResult()) {
			return null;
		}
		return action.getDisplayStepKindEnum();
	}

	/**
	 * Asks does this have any diff results.
	 * @return if has some diff results, returns true.
	 */
	public boolean isDiffExist() {
		StepCountBuildAction action = this.getLastBuildAction();
		if (action == null || !action.hasResult()) {
			return false;
		}
		return action.isDiffExist();
	}
}
