package keisuke.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

import keisuke.StepCountResult;
import keisuke.count.step.StepCountProceduralFunc;

/**
 * ステップ数計測(keisuke.count.StepCount)を実行するAntタスク
 */
public class StepCountTask extends AbstractCountTask {

	private boolean showDirectory = false;
	private boolean directoryAsCategory = false;
	// handling FileSet and FileList
	private List<ResourceCollection> resourceCollections = new ArrayList<ResourceCollection>();
	private File baseDir = null;
	// results of counting resources
	private List<StepCountResult> countResults = null;
	// Ant Task attributes
	private boolean failonerror = true;
	private boolean defaultExcludes = true;
	// counter
	private StepCountProceduralFunc stepCounter = null;

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
	    	// カウンターの初期化
	    	this.initCounter();
	    	// タスクで指定されたファイルをカウントする
	    	this.countAllResources();
	    	log("" + this.resourceCollections.size() + " target directories / " + this.countResults.size()
	    			+ " files.\n", Project.MSG_INFO);
	    	// 結果を出力する
	    	this.writeResultsTo(out);
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
    			if (this.directoryAsCategory) {
    				this.countResults.addAll(this.fillCategoryWithDirectoryName(
    		    			this.stepCounter.getResultOfCountingFileSet(this.baseDir, fnameArray)));
    			} else {
    				this.countResults.addAll(
    						this.stepCounter.getResultOfCountingFileSet(this.baseDir, fnameArray));
    			}
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
				if (this.failonerror || !this.getMessage(e)
							.endsWith(DirectoryScanner.DOES_NOT_EXIST_POSTFIX)) {
					throw e;
				} else {
					log("Warning: " + getMessage(e), Project.MSG_WARN);
					return fnameArray;
				}
			}
			fnameArray = dirScanner.getIncludedFiles();
		}
		//this.debugLog("basePath = " + this.baseDir.getAbsolutePath());
		return fnameArray;
    }

    private void initCounter() {
    	StepCountProceduralFunc procFunc = new StepCountProceduralFunc();
    	procFunc.setSourceEncoding(this.sourceEncoding());
    	procFunc.setXmlFileName(this.xmlFileName());
    	procFunc.setShowDirectory(this.showDirectory);
    	procFunc.setPathStyle(this.pathStyleName());
    	procFunc.setSortOrder(this.sortType());
    	procFunc.setFormat(this.formatType());
    	this.stepCounter = procFunc;
    }

    private List<StepCountResult> fillCategoryWithDirectoryName(final List<StepCountResult> results) {
    	for (StepCountResult result : results) {
    		result.setSourceCategory(this.baseDir.getName());
    	}
    	return results;
    }

    private void writeResultsTo(final OutputStream out) throws IllegalArgumentException, IOException {
    	this.stepCounter.doFormattingAndWritingAbout(this.countResults, out, this.formatType());
    }

    private String getMessage(final Exception ex) {
    	if (ex.getMessage() == null) {
    		return ex.toString();
    	}
        return ex.getMessage();
    }

}
