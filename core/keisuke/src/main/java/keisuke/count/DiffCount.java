package keisuke.count;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.sf.amateras.stepcounter.Util;
import keisuke.MessageDefine;
import keisuke.count.diff.DiffCountArgFunc;
import keisuke.count.diff.DiffCounter;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.diff.DiffStatusText;
import keisuke.count.diff.renderer.Renderer;
import keisuke.count.diff.renderer.RendererFactory;

/**
 * Diffcountのメイン
 * keisuke: パッケージ変更とオリジナルからクラス名変更(Main -> DiffCount)
 */
public class DiffCount {

	private OutputStream output = System.out;

	private File srcdir = null;
	private File olddir = null;
	private String format = null;
	//private String encoding = null;
	// keisuke: xmlオプション追加
	private String xmlFileName = null;
	public Map<String, String> argMap = null;
	public MessageDefine msgDef = null;

	public void setSrcdir(File srcdir) {
		this.srcdir = srcdir;
	}

	public void setOlddir(File olddir) {
		this.olddir = olddir;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}

	/*
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	*/
	
	/** keisuke: 言語定義XMLファイル名をセット */
	public void setXmlFileName(String name){
		this.xmlFileName = name;
	}

	/** 引数の処理 */
	public void diffProc(String[] args) {
		
		DiffCountArgFunc argFunc = new DiffCountArgFunc();
		this.argMap = argFunc.makeMapOfArgs(args);
		if (this.argMap == null) {
			return;
		}
		String encoding = this.argMap.get(SCCommonDefine.OPT_ENCODE);
		String outfile = this.argMap.get(SCCommonDefine.OPT_OUTPUT);
		String format = this.argMap.get(SCCommonDefine.OPT_FORMAT);
		String xmlfile = this.argMap.get(SCCommonDefine.OPT_XML);
		String[] argArray = argFunc.makeRestArgArray();
		if (argArray == null || argArray.length != 2) {
			System.err.println("!! Just 2 directories must be specified.");
			argFunc.showUsage();
			return;
		}
		List<File> fileList = Arrays.asList(new File(argArray[0]), new File(argArray[1]));
		for (int i = 0; i < fileList.size(); i++) {
			if(!fileList.get(i).isDirectory()){
				System.err.println("!! '" + fileList.get(i).getAbsolutePath() + "' is not directory.");
				argFunc.showUsage();
				return;
			}
		}
		setSrcdir(fileList.get(0));
		setOlddir(fileList.get(1));

		if(encoding != null && encoding.length() > 0){
//			encoding = System.getProperty("file.encoding");
			Util.setFileEncoding(encoding);
		}

		setFormat(format);

		// keisuki: xmlオプション追加
		if (xmlfile != null) {
			setXmlFileName(xmlfile);
		}
		
		try {
			if(outfile != null && outfile.length() > 0){
				setOutput(new PrintStream(new FileOutputStream(new File(outfile))));
				//setOutput(new FileOutputStream(outfile));
			}
			executeCount();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (this.output != null && this.output != System.out) try { this.output.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	
	public void executeCount() throws IOException{
		String[] prefixes = {"diff.status.", "diff.render."};
		this.msgDef = new MessageDefine(prefixes);
		DiffStatusText dstext = new DiffStatusText(this.msgDef);
		
		// フォーマッタが設定されていない場合はデフォルトを使用
		if(this.format == null || this.format.length() == 0){
			setFormat("text");
		}
		Renderer renderer = RendererFactory.getRenderer(this.format, this.msgDef);
		if(renderer == null){
			throw new RuntimeException(this.format + " is invalid format!");
		}

		DiffCounter diffcounter = new DiffCounter(this.xmlFileName, dstext);
		DiffFolderResult result = diffcounter.count(this.olddir, this.srcdir);

		byte[] bytes = renderer.render(result);
		this.output.write(bytes);
		this.output.flush();
		//System.out.println("[DEBUG] " + outputFile.getAbsolutePath() + "にカウント結果を出力しました。");
	}

	public static void main(String[] args) throws IOException {
		if(args==null || args.length==0){
			//System.exit(0);
			return;
		}
		/*
		String format = null;
		String output = null;
		String encoding = null;
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
			// keisuke: xmlオプション追加
			} else if(args[i].startsWith("-xml=")){
				String[] dim = Util.split(args[i],"=");
				xmlfile = dim[1];
			} else {
				fileList.add(new File(args[i]));
			}
		}	
		*/
		DiffCount main = new DiffCount();
		main.diffProc(args);
	}

}
