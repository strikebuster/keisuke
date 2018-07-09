package keisuke.ant;

import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_ON;
import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_OS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

import keisuke.StepCountResult;
import keisuke.count.step.StepCountProceduralFunc;
import keisuke.count.util.FileNameUtil;

/**
 * ステップ数計測(keisuke.count.StepCount)を実行するAntタスク
 */
public class StepCountTask extends AbstractCountTask {

	private boolean showDirectory = false;
	private String sortType = OPTVAL_SORT_ON;
	private boolean directoryAsCategory = false;
	// handling FileSet and FileList
	private List<ResourceCollection> resourceCollections = new ArrayList<ResourceCollection>();
	private File baseDir = null;
	// results of counting resources
	private List<StepCountResult> countResults = null;
	// Ant Task attributes
	private boolean failonerror = true;
	private boolean defaultExcludes = true;

    /**
	 * ファイルが存在しないなどエラー発生時に動作を停止させるか設定する
	 * デフォルトは true です
	 * @param failFlag 動作を停止させる場合はtrueを指定
	 */
    public void setFailOnError(final boolean failFlag) {
        this.failonerror = failFlag;
    }

    /**
	 * デフォルトの除外設定を有効にするか設定する
	 * デフォルトは true です
	 * @param excludeFlag デフォルトの除外設定を有効にする場合はtrueを指定
	 */
    public void setDefaultExcludes(final boolean excludeFlag) {
    	this.defaultExcludes = excludeFlag;
    }

    /**
     * Add a set of files to count.
     * @param fileset a set of files to count.
     */
    public void addFileset(final FileSet fileset) {
        this.add(fileset);
    }

	/**
	 * Add a collection of files to count.
	 * @param resource a resource collection to count.
	 */
    public void add(final ResourceCollection resource) {
    	this.resourceCollections.add(resource);
    }

	/**
	 * 出力内容に書き出すファイル名にディレクトリパスを記述するか設定する
	 * デフォルトは false です
	 * @param showFlag ディレクトリパスを記述する場合はtrueを指定
	 */
    public void setShowDirectory(final boolean showFlag) {
    	this.showDirectory = showFlag;
    }

    /**
	 * 出力内容に書き出すファイルリストのソート順を設定する
	 * デフォルトは on です
	 * @param order ソート順
	 */
    public void setSort(final String order) {
    	this.sortType = order;
    }

	/**
	 * カテゴリ名としてトップディレクトリ名を使用するか設定する
	 * デフォルトは false です
	 * @param categoryFlag ディレクトリ名を使用する場合はtrueを指定
	 */
    public void setDirectoryAsCategory(final boolean categoryFlag) {
    	this.directoryAsCategory = categoryFlag;
    }

    /**
   	 * ステップ数測定を実行します。
   	 * 基点ディレクトリ名をカテゴリとして設定するAnt I/F固有のオプション機能があるため
   	 * keisuke.count.StepCountProcの汎用I/Fでなく専用I/Fを呼び出す。
   	 *
   	 * @see org.apache.tools.ant.Task#execute()
   	 * @throws BuildException when one of some exception occured
   	 */
    @Override
    public void execute() throws BuildException {
    	OutputStream out = null;
    	try {
	    	if (this.outputFile() != null) {
	    		try {
	    			out = new FileOutputStream(this.outputFile());
				} catch (FileNotFoundException e) {
					throw new BuildException("fail to open " + this.outputFile().getPath(), e);
				}
	    	} else {
	    		out = System.out;
	    	}
	    	// タスクで指定されたファイルをカウントする
	    	this.countAllResources();

	    	log("" + this.resourceCollections.size() + " target directories / " + this.countResults.size()
	    			+ " files.\n", Project.MSG_INFO);
	    	// カウント結果のソート
	    	if (this.sortType.equals(OPTVAL_SORT_ON)) {
				Collections.sort(this.countResults, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInCodeOrder(o1.filePath(), o2.filePath());
					}
				});
			} else if (this.sortType.equals(OPTVAL_SORT_OS)) {
				Collections.sort(this.countResults, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInOsOrder(o1.filePath(), o2.filePath());
					}
				});
			}
	    	// 結果を出力する
	    	StepCountProceduralFunc procFunc = new StepCountProceduralFunc();
	    	procFunc.doFormattingAndWritingAbout(this.countResults,	out, this.formatType());
    	} catch (BuildException e) {
			throw e;
    	} catch (IllegalArgumentException e) {
			throw new BuildException(e.getMessage(), e);
    	} catch (IOException e) {
			throw new BuildException("I/O Error", e);
    	} catch (Exception e) {
			throw new BuildException("Error", e);
    	} finally {
    		try {
    			if (out != null && out != System.out) out.close();
			} catch (IOException e) {
				throw new BuildException("I/O Error", e);
			}
    	}
    }

    private void countAllResources() throws BuildException {
    	this.countResults = new ArrayList<StepCountResult>();

    	for (ResourceCollection resource : this.resourceCollections) {
	    	String[] fnameArray = this.convertToArray(resource);
	    	if (fnameArray == null) {
	    		continue;
	    	}
    		try {
		    	this.countResults.addAll(this.getCountingResult(fnameArray));
    		} catch (Exception e) {
				if (this.failonerror) {
					throw new BuildException(e.getMessage(), e);
				} else {
					log("Warning: " + getMessage(e), Project.MSG_WARN);
					continue;
				}
			}
    	}
    }

    private String[] convertToArray(final ResourceCollection resource) throws BuildException {
    	FileList fileList = null;
    	FileSet fileSet = null;
    	String[] fnameArray = null;

    	if (resource == null) {
    		return null;
    	}
    	// resourceの種類を判定しファイル名抽出の前処理
		if (resource instanceof FileList && resource.isFilesystemOnly()) {
			fileList = (FileList) resource;
			this.baseDir = fileList.getDir(this.getProject());
		} else if (resource instanceof FileSet && resource.isFilesystemOnly()) {
			fileSet = (FileSet) resource;
			fileSet.setDefaultexcludes(this.defaultExcludes);
            this.baseDir = fileSet.getDir();
		} else {
			throw new BuildException("Only FileSystem resources are supported.");
		}
		// 基点ディレクトリのチェック
		if (!this.baseDir.exists()) {
			String msg = "basedir '" + this.baseDir.getPath() + "' does not exist!";
			if (this.failonerror) {
				throw new BuildException(msg);
			} else {
				log("Warning: " + msg, Project.MSG_WARN);
				return fnameArray;
			}
		}
		// resource内の全ファイル名を取得
		if (fileList != null) {
				fnameArray = fileList.getFiles(this.getProject());
		} else if (fileSet != null) {
			DirectoryScanner dirScanner = null;
            try {
                dirScanner = fileSet.getDirectoryScanner();
            } catch (BuildException e) {
                if (this.failonerror
                		|| !this.getMessage(e).endsWith(DirectoryScanner.DOES_NOT_EXIST_POSTFIX)) {
                    throw e;
                } else {
                    log("Warning: " + getMessage(e), Project.MSG_WARN);
                    return fnameArray;
                }
            }
            fnameArray = dirScanner.getIncludedFiles();
		}
		//this.debugLog("basePath = " + basePath);
		return fnameArray;
    }

    private List<StepCountResult> getCountingResult(final String[] fnameArray) throws IllegalArgumentException {
    	if (fnameArray == null) {
    		return null;
    	}
    	StepCountProceduralFunc procFunc = new StepCountProceduralFunc();
    	procFunc.setSourceEncoding(this.sourceEncoding());
    	procFunc.setXmlFileName(this.xmlFileName());
    	procFunc.setShowDirectory(this.showDirectory);
    	procFunc.setSortOrder(this.sortType);
    	return procFunc.getResultOfCountingFileSet(this.baseDir, fnameArray, this.directoryAsCategory);
    }

    private String getMessage(final Exception ex) {
    	if (ex.getMessage() == null) {
    		return ex.toString();
    	}
        return ex.getMessage();
    }

}
