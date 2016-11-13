package keisuke.count;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.Util;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import keisuke.count.format.FormatterFactory;


/**
 *  コマンドラインからの起動クラス
 *  keisuke: パッケージ変更とクラス名変更（Main -> StepCount)
 *  Junit向けにメソッド分割などの改修
 */
public class StepCount {

	private File[] files;
	private ResultFormatter formatter = null;
	private OutputStream output = System.out;
	private boolean showDirectory = false;
	// keisuke: xmlオプション追加
	private String xmlFileName = null;
	public Map<String, String> argMap = null;


	/** 引数で指定したディレクトリからの階層を表示するか設定します */
	public void setShowDirectory(boolean showDirectory) {
		this.showDirectory = showDirectory;
	}

	/** ファイルをセットします */
	public void setFiles(File[] files){
		this.files = files;
	}

	/** フォーマッタをセットします。 */
	public void setFormatter(ResultFormatter formatter){
		this.formatter = formatter;
	}

	/** keisuke: 言語定義XMLファイル名をセット */
	public void setXmlFileName(String name){
		this.xmlFileName = name;
	}
	
	/** 出力ストリームを設定します。 */
	public void setOutput(OutputStream output){
		this.output = output;
	}

	/** 引数の処理 */
	public void countProc(String[] args) {
		
		StepCountArgFunc argFunc = new StepCountArgFunc();
		this.argMap = argFunc.makeMapOfArgs(args);
		if (this.argMap == null) {
			return;
		}
		String encoding = this.argMap.get(SCCommonDefine.OPT_ENCODE);
		String outfile = this.argMap.get(SCCommonDefine.OPT_OUTPUT);
		String format = this.argMap.get(SCCommonDefine.OPT_FORMAT);
		String show = this.argMap.get(SCCommonDefine.OPT_SHOWDIR);
		String xmlfile = this.argMap.get(SCCommonDefine.OPT_XML);
		String[] argArray = argFunc.makeRestArgArray();
		if (argArray == null || argArray.length == 0) {
			System.err.println("!! Not specified source path.");
			argFunc.showUsage();
			return;
		}
		List<File> fileList = new ArrayList<File>();
		for(int i=0;i<argArray.length;i++){
			fileList.add(new File(argArray[i]));
		}
		
		setFiles((File[])fileList.toArray(new File[fileList.size()]));

		if(encoding != null){
			Util.setFileEncoding(encoding);
		}
		
		setFormatter(FormatterFactory.getFormatter(format));
		
		if ("true".equalsIgnoreCase(show)) {
			setShowDirectory(true);
		}
		// keisuki: xmlオプション追加
		if (xmlfile != null) {
			setXmlFileName(xmlfile);
		}
		
		try {
			if(outfile != null && outfile.length() > 0){
				//setOutput(new PrintStream(new FileOutputStream(new File(outfile))));
				setOutput(new FileOutputStream(outfile));
			}
			executeCount();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (this.output != null && this.output != System.out) try { this.output.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	/** カウントを実行します */
	public void executeCount() throws IOException {

		// フォーマッタが設定されていない場合はデフォルトを使用
		if(this.formatter == null){
			this.formatter = FormatterFactory.getFormatter("");
		}

		//keisuke: results生成の主処理はStepCountFiles.countAll()として独立化
		StepCountFiles stepcounter = new StepCountFiles(this.xmlFileName);
		if (this.showDirectory) {
			stepcounter.setShowDirectoryTrue();
		}
		CountResult[] results = stepcounter.countAll(this.files);

		this.output.write(this.formatter.format(results));
		this.output.flush();
	}

	/** コマンドライン起動用メソッド */
	public static void main(String[] args)  {

		if(args==null || args.length==0){
			System.exit(0);
		}
		/* keisuke: 引数処理はCommons CLIに変更
		String format = null;
		String output = null;
		String encoding = null;
		String showDirectory = null;
		String xmlfile = null; // keisuke: xmlオプション追加
		ArrayList<File> fileList = new ArrayList<File>();

		for(int i=0;i<args.length;i++){
			if(args[i].startsWith("-format=")){
				String[] dim = Util.split(args[i],"=");
				format = dim[1];
			} else if(args[i].startsWith("-output=")){
				String[] dim = Util.split(args[i],"=");
				output = dim[1];
			} else if(args[i].startsWith("-encoding=")){
				String[] dim = Util.split(args[i],"=");
				encoding = dim[1];
			} else if(args[i].startsWith("-showDirectory=")){
				String[] dim = Util.split(args[i],"=");
					showDirectory = dim[1];
			// keisuke: xmlオプション追加
			} else if(args[i].startsWith("-xml=")){
				String[] dim = Util.split(args[i],"=");
					xmlfile = dim[1];
			} else {
				fileList.add(new File(args[i]));
			}
		}
		*/
		StepCount main = new StepCount();
		main.countProc(args);
	}

}
