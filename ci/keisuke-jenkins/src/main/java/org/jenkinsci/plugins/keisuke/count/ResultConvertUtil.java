package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import keisuke.StepCountResult;
import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;

/**
 * Utility for converting step/diff count result.
 */
public final class ResultConvertUtil {

	private ResultConvertUtil() { }

	/**
	 * Converts array of StepCountResult to list of StepCountResultForPublish.
	 * @param results array of StepCountResult.
	 * @param category category name.
	 * @param including base directory name including switch.
	 * @return list of StepCountResultForPublish.
	 */
	public static  List<StepCountResultForPublish> convertToListOfFileStepFrom(
			final StepCountResult[] results, final String category,
			final BaseDirIncludingSwitch including) {
		if (results == null) {
			return null;
		}
		List<StepCountResultForPublish> stepList = new ArrayList<StepCountResultForPublish>();
		for (StepCountResult fileResult : results) {
			StepCountResultForPublish fileStep =
					new StepCountResultForPublish(fileResult);
			fileStep.setSourceCategory(category);
	    	stepList.add(fileStep);
		}
		return stepList;
	}

	/**
	 * Converts list of StepCountResult to list of StepCountResultForPublish.
	 * @param results array of StepCountResult.
	 * @param category category name.
	 * @param including base directory name including switch.
	 * @return list of StepCountResultForPublish.
	 */
	public static List<StepCountResultForPublish> convertToListOfFileStepFrom(
			final List<StepCountResult> results, final String category,
			final BaseDirIncludingSwitch including) {
		if (results == null) {
			return null;
		}
		List<StepCountResultForPublish> stepList = new ArrayList<StepCountResultForPublish>();
		for (StepCountResult fileResult : results) {
			StepCountResultForPublish fileStep =
					new StepCountResultForPublish(fileResult);
			fileStep.setSourceCategory(category);
	    	stepList.add(fileStep);
		}
		return stepList;
	}

	/**
	 * Converts DiffFolderResult to list of DiffCountResultForPublish.
	 * @param result DiffFolderResult.
	 * @param baseDir base directory for relative path.
	 * @param category category name.
	 * @param including base directory name including switch.
	 * @return list of DiffCountResultForPublish.
	 */
	public static DiffFolderResultForPublish convertToDiffResultForPublishFrom(
			final DiffFolderResult result, final File baseDir, final String category,
			final BaseDirIncludingSwitch including) {
		if (result == null) {
			return null;
		}
		return convertForPublishFrom(result, baseDir, category, including);
	}

	private static AbstractDiffResultForCount convertForPublishFrom(
			final AbstractDiffResultForCount result, final File baseDir, final String category,
			final BaseDirIncludingSwitch including) {
		if (result instanceof DiffFolderResult) {
			return convertForPublishFrom((DiffFolderResult) result, baseDir, category, including);
		} else if (result instanceof DiffFileResult) {
			return convertForPublishFrom((DiffFileResult) result, baseDir, category, including);
		} else {
			return null;
		}
	}

	private static DiffFolderResultForPublish convertForPublishFrom(final DiffFolderResult result,
			final File baseDir, final String category, final BaseDirIncludingSwitch including) {
		DiffFolderResultForPublish folder = new DiffFolderResultForPublish(result, baseDir, including);
		for (AbstractDiffResultForCount obj : result.getChildren()) {
			folder.addChild(convertForPublishFrom(obj, baseDir, category, including));
		}
		return folder;
	}

	private static DiffFileResultForPublish convertForPublishFrom(final DiffFileResult result,
			final File baseDir, final String category, final BaseDirIncludingSwitch including) {
		DiffFileResultForPublish fileResult = new DiffFileResultForPublish(result, baseDir, including);
		fileResult.setSourceCategory(category);
		return fileResult;
	}

	/**
	 * Converts DiffFolderResult to list of DiffCountResultForPublish.
	 * @param result DiffFolderResult.
	 * @param baseDir base directory for relative path.
	 * @param category category name.
	 * @param including base directory name including switch.
	 * @return list of DiffCountResultForPublish.
	 */
	public static List<DiffCountResultForPublish> convertToListOfDiffStepFrom(
			final DiffFolderResult result, final File baseDir, final String category,
			final BaseDirIncludingSwitch including) {
		if (result == null) {
			return null;
		}
		return convertFrom(result, baseDir, category, including);
	}

	private static List<DiffCountResultForPublish> convertFrom(
			final AbstractDiffResultForCount result, final File baseDir, final String category,
			final BaseDirIncludingSwitch including) {
		List<DiffCountResultForPublish> list = new ArrayList<DiffCountResultForPublish>();
		if (result instanceof DiffFolderResult) {
			return convertFrom((DiffFolderResult) result, baseDir, category, including);
		} else if (result instanceof DiffFileResult) {
			list.add(convertFrom((DiffFileResult) result, baseDir, category, including));
		}
		return list;
	}

	private static List<DiffCountResultForPublish> convertFrom(final DiffFolderResult result,
			final File baseDir, final String category, final BaseDirIncludingSwitch including) {
		List<DiffCountResultForPublish> list = new ArrayList<DiffCountResultForPublish>();
		for (AbstractDiffResultForCount obj : result.getChildren()) {
			list.addAll((convertFrom(obj, baseDir, category, including)));
		}
		return list;
	}

	private static DiffCountResultForPublish convertFrom(final DiffFileResult result,
			final File baseDir, final String category, final BaseDirIncludingSwitch including) {
		return new DiffCountResultForPublish(result, baseDir, including);
	}

}
