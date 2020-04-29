package org.jenkinsci.plugins.keisuke;

import static org.jenkinsci.plugins.keisuke.StepCountProjectAction.ICON_FILE_NAME;
import static org.jenkinsci.plugins.keisuke.StepCountProjectAction.PROJECT_URL_PATH;
import static org.jenkinsci.plugins.keisuke.StepCountProjectAction.RESULT_URL_PATH;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Util;
import hudson.model.Action;
import hudson.model.Result;
import hudson.model.Run;
import hudson.util.ChartUtil;
import hudson.util.Graph;
import jenkins.tasks.SimpleBuildStep;

/**
 * Action for Build step which uses this plugin.
 */
public class StepCountBuildAction implements Action, SimpleBuildStep.LastBuildAction {
	private transient PrintStream syslogger = System.out;

	// グラフのサイズ
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 200;

	//PipelineのときにBuildが入る
	private Run<?, ?> ownerBuild;

	// カテゴリごとのステップ数集計結果。キーはユニット名
	private Map<String, BuildResult> stepsMap = new HashMap<String, BuildResult>();

	// 行数遷移グラフの行数指定
	private DisplaySetting displayProp = null;

	/**
	 * constructor for Pipeline BuildStep
	 * @param build 所属するビルド
	 */
	public StepCountBuildAction(final Run<?, ?> build) {
		//syslogger.println("[DEBUG] StepCountBuildAction#StepCountBuildAction(" + build + ")");
		this.ownerBuild = build;
	}

	/**
	 * Gets the build of this step
	 * @return hudson.model.Run instance.
	 */
	public Run<?, ?> getBuild() {
		return this.getOwnerBuild();
	}

	/**
	 * Gets the build of this step
	 * @return hudson.model.Run instance.
	 */
	protected Run<?, ?> getOwnerBuild() {
		return this.ownerBuild;
	}

	/**
	 * Gets stepsMap
	 * @return map of counting unit and its result.
	 */
	public Map<String, BuildResult> getStepsMap() {
		return this.stepsMap;
	}

	/**
	 * Gets result by unit name.
	 * @param key unit name.
	 * @return counting result of unit.
	 */
	public BuildResult getResultByUnit(final String key) {
		return this.stepsMap.get(key);
	}

	/**
	 * Puts result into stepsMap.
	 * @param key category
	 * @param result counting result
	 */
	public void put(final String key, final BuildResult result) {
		this.stepsMap.put(key, result);
	}

	/**
	 * Asks does this have any results.
	 * @return if has some results, returns true.
	 */
	protected boolean hasResult() {
		if (this.stepsMap == null || this.stepsMap.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Asks does this have any diff results.
	 * @return if has some diff results, returns true.
	 */
	public boolean isDiffExist() {
		if (!this.hasResult()) {
			return false;
		}
		for (Entry<String, BuildResult> entry : this.stepsMap.entrySet()) {
			BuildResult result = entry.getValue();
			if (result.isDiffExist()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets configured DisplaySetting instance.
	 * @param prop DisplaySetting instance.
	 */
	protected void setDisplaySetting(final DisplaySetting prop) {
		this.displayProp = prop;
	}

	/**
	 * Gets DisplaySetting instance.
	 * @return DisplaySetting instance.
	 */
	public DisplaySetting getDisplaySetting() {
		if (this.displayProp == null) {
			return null;
		}
		return this.displayProp;
	}

	/**
	 * Gets selected DisplayStepKindEnum element.
	 * @return selected element of DisplayStepKindEnum.
	 */
	protected DisplayStepKindEnum getDisplayStepKindEnum() {
		if (this.displayProp == null) {
			return null;
		}
		return this.displayProp.getDisplayStepKindEnum();
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
	 * Gets ProjectAction Collection.
	 * actually this has only one instance in collection.
	 * method for implying SimpleBuildStep.LastBuildAction#getProjectActions().
	 * @return Collection of StepCountProjectAction.
	 */
	@Override
	public Collection<? extends Action> getProjectActions() {
		if (this.ownerBuild == null) {
			syslogger.println("[WARNING] StepCountBuildAction#getProjectActions(): owner build == null.");
			return null;
		}
		//syslogger.println("[DEBUG] StepCountBuildAction#getProjectActions(): owner build #"
		//		+ this.ownerBuild.number);
		return Collections.<Action>singleton(new StepCountProjectAction(this.ownerBuild.getParent()));
	}

	/**
	 * Gets a StepCountBuildAction instance which belongs to previous build.
	 * if previous build was failure or has no instance of StepCountBuildAction,
	 * do about previous of the previous build.
	 * @return StepCountBuildAction
	 */
	protected StepCountBuildAction getPreviousBuildAction() {
		if (this.ownerBuild == null) {
			return null;
		}
		Run<?, ?> build = this.ownerBuild; // != null
		while (true) {
			build = build.getPreviousBuild();
			if (build == null) {
				return null;
			}
			if (build.getResult() == Result.FAILURE) {
				continue;
			}
			StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
			if (action == null || !action.hasResult()) {
				continue;
			}
			return action;
		}
	}

	private String getRelPathFrom(final StaplerRequest req) {
		String relPath = req.getParameter("rel");
		if (relPath == null) {
			return "";
		}
		return relPath;
	}

	/**
	 * Sends redirect response which will go to the last finished build page
	 * @param req request
	 * @param res response
	 * @throws IOException signal that redirect failed.
	 */
	public void doIndex(final StaplerRequest req, final StaplerResponse res) throws IOException {
		//syslogger.println("[DEBUG] StepCountBuildAction#doIndex(req, res) req = " + req.getRequestURI());

		// contextRoot/job/project/build/myUrl is current.
		if (this.hasResult()) {
			//syslogger.println("[DEBUG] redirect to 'result'.");
			res.sendRedirect2(RESULT_URL_PATH);
		} else {
			//syslogger.println("[DEBUG] does not have result. redirect to '../'.");
			res.sendRedirect2("../");
		}
	}

	/**
	 * Create clickable map
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMap failed.
	 */
	protected void createClickableMap(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createStepGraph(req, res).doMap(req, res);
	}

	/**
	 * Create trend graph
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraph failed.
	 */
	protected void createGraph(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createStepGraph(req, res).doPng(req, res);
	}

	private Graph createStepGraph(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		if (ChartUtil.awtProblemCause != null) {
			// not available. send out error message
			res.sendRedirect2(req.getContextPath() + "/images/headless.png");
			return null;
		}
		int[] size = this.decideSize(req);
		return new StepCountGraph(this, Calendar.getInstance(),	size[0], size[1], this.getRelPathFrom(req));
	}

	/*
	 * Create clickable map for diff result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMapForDiff failed.
	 *
	protected void createClickableMapForDiff(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffGraph(req, res).doMap(req, res);
	}*/

	/*
	 * Create trend graph for diff result
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraphForDiff failed.
	 *
	protected void createGraphForDiff(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffGraph(req, res).doPng(req, res);
	}

	private Graph createDiffGraph(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		if (ChartUtil.awtProblemCause != null) {
			// not available. send out error message
			res.sendRedirect2(req.getContextPath() + "/images/headless.png");
			return null;
		}
		int[] size = this.decideSize(req);
		return new DiffCountGraph(this, Calendar.getInstance(),	size[0], size[1], this.getRelPathFrom(req));
	}*/

	/**
	 * Create clickable map for diff added steps
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMapForDiff failed.
	 */
	protected void createClickableMapForDiffAdded(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffAddedGraph(req, res).doMap(req, res);
	}

	/**
	 * Create trend graph for diff added steps
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraphForDiff failed.
	 */
	protected void createGraphForDiffAdded(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffAddedGraph(req, res).doPng(req, res);
	}

	private Graph createDiffAddedGraph(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		if (ChartUtil.awtProblemCause != null) {
			// not available. send out error message
			res.sendRedirect2(req.getContextPath() + "/images/headless.png");
			return null;
		}
		int[] size = this.decideSize(req);
		return new DiffCountAddedGraph(this, Calendar.getInstance(),
				size[0], size[1], this.getRelPathFrom(req));
	}

	/**
	 * Create clickable map for diff deleted steps
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createClickableMapForDiff failed.
	 */
	protected void createClickableMapForDiffDeleted(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffDeletedGraph(req, res).doMap(req, res);
	}

	/**
	 * Create trend graph for diff deleted steps
	 * @param req request
	 * @param res response
	 * @throws IOException signal that createGraphForDiff failed.
	 */
	protected void createGraphForDiffDeleted(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		this.createDiffDeletedGraph(req, res).doPng(req, res);
	}

	private Graph createDiffDeletedGraph(final StaplerRequest req, final StaplerResponse res)
			throws IOException {
		if (ChartUtil.awtProblemCause != null) {
			// not available. send out error message
			res.sendRedirect2(req.getContextPath() + "/images/headless.png");
			return null;
		}
		int[] size = this.decideSize(req);
		return new DiffCountDeletedGraph(this, Calendar.getInstance(),
				size[0], size[1], this.getRelPathFrom(req), this.isBuildAxisVisible(req));
	}

	private int[] decideSize(final StaplerRequest req) {
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		String w = Util.fixEmptyAndTrim(req.getParameter("width"));
		String h = Util.fixEmptyAndTrim(req.getParameter("height"));

		if (w != null) {
			try {
				width = Integer.parseInt(w);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (h != null) {
			try {
				height = Integer.parseInt(h);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		int[] size = {width, height};
		return size;
	}

	private boolean isBuildAxisVisible(final StaplerRequest req) {
		String param = req.getParameter("buildAxis");
		if (param == null || param.isEmpty() || param.equals("true")) {
			return true;
		}
		return false;
	}
}
