package keisuke;

public class ClassifyFuncFactory {

	public static IfClassifyFunc createClassifyFunc(String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.OPTVAL_EXTENSION)) {
			return new ExtensionClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGUAGE)) {
			return new LanguageClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			return new LanguageClassifyFunc(CommonDefine.OPTVAL_LANGGROUP);
		} else if (type.startsWith(CommonDefine.OPTVAL_FW)) {
			int pos = CommonDefine.OPTVAL_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifyFunc(fw);
			}
			System.err.println("!! not specified <name> argument in the option -fw:<name>");
			return null;
		} else {
			return null;
		}
	}
	
	public static IfClassifyFunc createClassifyFunc(String type, String fname) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.OPTVAL_EXTENSION)) {
			return new ExtensionClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGUAGE)) {
			LanguageClassifyFunc lct = new LanguageClassifyFunc();
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			LanguageClassifyFunc lct = new LanguageClassifyFunc(CommonDefine.OPTVAL_LANGGROUP);
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.startsWith(CommonDefine.OPTVAL_FW)) {
			int pos = CommonDefine.OPTVAL_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifyFunc(fw, fname);
			}
			System.err.println("!! not specified <name> argument in the option -fw:<name>");
			return null;
		} else {
			return null;
		}
	}
}
