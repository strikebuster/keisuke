package keisuke.ant;

import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_ON;
import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_OS;
import static keisuke.count.option.CountOptionConstant.OPT_SORT;

import java.io.BufferedOutputStream;
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
import keisuke.count.StepCountResultForCount;
import keisuke.count.StepCounter;
import keisuke.count.language.XmlDefinedStepCounterFactory;
import keisuke.count.option.StepCountOption;
import keisuke.count.step.Formatter;
import keisuke.count.step.format.FormatterFactory;
import keisuke.count.util.FileNameUtil;

/**
 * ステップ数計測(keisuke.count.StepCount)を実行するAntタスク
 */
public class StepCountTask extends AbstractCountTask {

	private boolean showDirectory = false;
	private String sortType = OPTVAL_SORT_ON;
	private boolean directoryAsCategory = false;
	private XmlDefinedStepCounterFactory factory = null;
	private List<ResourceCollection> resourceCollections = new ArrayList<ResourceCollection>();
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
	 * keisuke.count.StepCountFunctionを呼び出さずに、同様の処理を
	 * このメソッドで実装する。
	 *
	 * @see org.apache.tools.ant.Task#execute()
	 * @throws BuildException when one of some exception occured
	 */
    @Override
	public void execute() throws BuildException {
    	StepCountOption optdef = new StepCountOption();
    	if (!optdef.valuesAs(OPT_SORT).contains(this.sortType)) {
    		throw new BuildException(this.sortType + " is invalid sort value.");
    	}
    	Formatter formatter = FormatterFactory.getFormatter(this.formatType());
    	if (formatter == null) {
			throw new BuildException(this.formatType() + " is invalid format value.");
		}

		this.factory = new XmlDefinedStepCounterFactory();
    	if (this.xmlFileName() != null && !this.xmlFileName().isEmpty()) {
    		this.factory.appendCustomizeXml(this.xmlFileName());
    	}
    	OutputStream out = null;
    	try {
	    	if (this.outputFile() != null) {
	    		try {
					out = new BufferedOutputStream(new FileOutputStream(this.outputFile()));
				} catch (FileNotFoundException e) {
					throw new BuildException("fail to open " + this.outputFile().getPath(), e);
				}
	    	} else {
	    		out = System.out;
	    	}

	    	List<StepCountResult> results = new ArrayList<StepCountResult>();

	    	for (ResourceCollection resource : this.resourceCollections) {
		    	File baseDir = null;
		    	String baseFullPath = null;
		    	FileList fileList = null;
		    	FileSet fileSet = null;

	    		if (resource instanceof FileList && resource.isFilesystemOnly()) {
	    			fileList = (FileList) resource;
	    			baseDir = fileList.getDir(this.getProject());
	    		} else if (resource instanceof FileSet && resource.isFilesystemOnly()) {
	    			fileSet = (FileSet) resource;
	    			fileSet.setDefaultexcludes(this.defaultExcludes);
	                baseDir = fileSet.getDir();
	    		} else {
	    			throw new BuildException("Only FileSystem resources are supported.");
	    		}
	    		// 基点ディレクトリのチェック
	    		if (!baseDir.exists()) {
	    			String msg = "basedir '" + baseDir.getPath() + "' does not exist!";
	    			if (this.failonerror) {
	    				throw new BuildException(msg);
	    			} else {
	    				log("Warning: " + msg, Project.MSG_WARN);
	    				continue;
	    			}
	    		}
	    		try {
					if (this.showDirectory) {
						baseFullPath = baseDir.getCanonicalPath().replace('\\', '/');
					}
				} catch (IOException e) {
					if (this.failonerror) {
                        throw new BuildException("I/O Error: " + baseDir, e);
                    } else {
                        log("Warning: " + getMessage(e), Project.MSG_WARN);
                        continue;
                    }
				}
	    		// resource内の全ファイル名を取得
	    		String[] fnameArray = null;
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
	                        continue;
	                    }
	                }
	                fnameArray = dirScanner.getIncludedFiles();
	    		}

	    		//this.debugLog("basePath = " + basePath);
	    		if (fnameArray == null) {
	    			continue;
	    		}
	    		for (String name : fnameArray) {
	    			File file = null;
	    			try {
	    				file = new File(baseDir, name);
	    				StepCountResultForCount result = this.count(file);
	    				if (this.showDirectory) {
	    					result.setBaseDirPath(baseFullPath);
	    					// filelist|filesetのdir指定ディレクトリからのファイルパスに上書きします。
	    					result.setFilePathAsSubPathFromBase();
	    				}
	    				if (directoryAsCategory) {
	    					result.setSourceCategory(baseDir.getName());
	    				}
	    				results.add((StepCountResult) result);
	    				//this.debugLog(result.filePath());
	    			} catch (IOException e) {
	    				if (this.failonerror) {
	    					throw new BuildException("I/O Error: " + file, e);
	    				} else {
	    					log("Warning: " + getMessage(e), Project.MSG_WARN);
	    					continue;
	    				}
	    			}
	    		}
	    	}

	    	log("" + this.resourceCollections.size() + " target directories / " + results.size() + " files.\n",
	    			Project.MSG_INFO);

	    	if (this.sortType.equals(OPTVAL_SORT_ON)) {
				Collections.sort(results, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInCodeOrder(o1.filePath(), o2.filePath());
					}
				});
			} else if (this.sortType.equals(OPTVAL_SORT_OS)) {
				Collections.sort(results, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInOsOrder(o1.filePath(), o2.filePath());
					}
				});
			}

	    	out.write(formatter.format(results.toArray(new StepCountResult[results.size()])));
	    	out.flush();
    	} catch (IOException e) {
			throw new BuildException("I/O Error", e);
    	} finally {
    		try {
    			if (out != null && out != System.out) out.close();
			} catch (IOException e) {
				throw new BuildException("I/O Error", e);
			}
    	}
    }

    private StepCountResultForCount count(final File file) throws IOException {
		StepCounter counter = this.factory.getCounter(file.getName());
		if (counter != null) {
			return counter.count(file, this.sourceEncoding());
		} else {
			return new StepCountResultForCount(file, file.getName(), null, null, 0, 0, 0);
		}
    }

    private String getMessage(final Exception ex) {
    	if (ex.getMessage() == null) {
    		return ex.toString();
    	}
        return ex.getMessage();
    }

}
