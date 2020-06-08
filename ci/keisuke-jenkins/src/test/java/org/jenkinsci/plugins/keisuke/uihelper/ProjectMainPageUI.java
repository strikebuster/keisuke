package org.jenkinsci.plugins.keisuke.uihelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfCaptionDivisionFromImageOfDiffCountAdded;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfCaptionDivisionFromImageOfDiffCountDeleted;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfCaptionDivisionFromImageOfStepCount;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfStepTrFromAnchor;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.DisplayStepKindEnum;
import org.jenkinsci.plugins.keisuke.Messages;
import org.jvnet.hudson.test.JenkinsRule;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

/**
 * UI helper for jenkins project page using KeisukePublisher.
 */
public class ProjectMainPageUI extends AbstractReportPageUI {

	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param page HtmlPage of jenkins web ui.
	 */
	public ProjectMainPageUI(final JenkinsRule.WebClient client, final HtmlPage page) {
		super(client, page);
	}

	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param projectName name of jenkins project.
	 */
	public ProjectMainPageUI(final JenkinsRule.WebClient client, final String projectName) {
		super(client, projectName);
	}

	/** {@inheritDoc}	 */
	@Override
	protected String getPageUrlPath(final String projectName) {
		return "job/" + projectName + "/";
	}

	/**
	 * Open project main page.
	 * @param projectName project name.
	 */
	public void openProjectPage(final String projectName) {
		this.openPage(projectName);
	}

	/**
	 * Open build result page.
	 * @param url URL path to build result page.
	 * @return HtmlPage instance.
	 */
	public HtmlPage openResultPage(final String url) {
		return this.openWebPage(url);
	}

	/**
	 * Finds html anchor of keisuke Title in project main page.
	 * @param xpath main-panel xpath string.
	 * @return html anchor.
	 */
	public HtmlAnchor findAnchorOfKeisukeStepCountInMain(final String xpath) {
		HtmlAnchor foundAnchor = null;
		String target = "keisuke";
		List<HtmlAnchor> list = this.getPage().getAnchors();
		for (HtmlAnchor anchor : list) {
			String aXpath = anchor.getCanonicalXPath();
			if (!aXpath.startsWith(xpath)) {
				continue;
			}
			String href = anchor.getHrefAttribute();
			//System.out.println("[TEST DEBUG] anchor href:" + href);
			if (target.equals(href)) {
				//System.out.println("[TEST] keisuke anchor XPath:" + anchor.getCanonicalXPath());
				//System.out.println("[TEST DEBUG] anchor text:" + anchor.getTextContent());
				String text = anchor.getTextContent();
				if (!text.isEmpty()) {
					foundAnchor = anchor;
					break;
				}
			}
		}
		System.out.println("[TEST] keisuke main Anchor XPath:" + foundAnchor.getCanonicalXPath());
		return foundAnchor;
	}

	/**
	 * Finds &lt;td&gt; for label of counting result in N-th row.
	 * @param anchor &lt;a&gt; for Keisuke result.
	 * @param index N that means N-th row.
	 * @return HtmlTableDataCell instance.
	 */
	public HtmlTableDataCell findTdataOfStepLabelFrom(final HtmlAnchor anchor, final int index) {
		String stepTrXpath = getXPathOfStepTrFromAnchor(anchor, index);
		String stepLabelTdXpath = stepTrXpath + "/td[1]";
		System.out.println("[TEST] step label td Xpaht:" + stepLabelTdXpath);
		return this.getPage().getFirstByXPath(stepLabelTdXpath);
	}

	/**
	 * Finds &lt;td&gt; for value of counting result in N-th row.
	 * @param anchor &lt;a&gt; for Keisuke result.
	 * @param index N that means N-th row.
	 * @return HtmlTableDataCell instance.
	 */
	public HtmlTableDataCell findTdataOfStepValueFrom(final HtmlAnchor anchor, final int index) {
		String stepTrXpath = getXPathOfStepTrFromAnchor(anchor, index);
		String stepValueTdXpath = stepTrXpath + "/td[2]";
		System.out.println("[TEST] step value td Xpaht:" + stepValueTdXpath);
		return this.getPage().getFirstByXPath(stepValueTdXpath);
	}

	/**
	 * Finds html image of the trend graph for code steps in floating panel of main page.
	 * @return html image.
	 */
	public HtmlImage findImageOfFloatingTrendGraphForStep() {
		return this.findImageOfFloatingTrendGraphAbout("keisuke/trendMap");
	}

	/**
	 * Finds html image of the trend graph for diff added steps in floating panel of main page.
	 * @return html image.
	 */
	public HtmlImage findImageOfFloatingTrendGraphForDiffAdded() {
		return this.findImageOfFloatingTrendGraphAbout("keisuke/trendMapDiffAdded");
	}

	/**
	 * Finds html image of the trend graph for diff deleted steps in floating panel of main page.
	 * @return html image.
	 */
	public HtmlImage findImageOfFloatingTrendGraphForDiffDeleted() {
		return this.findImageOfFloatingTrendGraphAbout("keisuke/trendMapDiffDeleted");
	}

	private HtmlImage findImageOfFloatingTrendGraphAbout(final String target) {
		HtmlImage foundImg = null;
		List<DomElement> list = this.getPage().getElementsByTagName("img");
		for (DomElement node : list) {
			String lazymap = node.getAttribute("lazymap");
			//System.out.println("[TEST DEBUG] node lazymap:" + lazymap);
			if (target.equals(lazymap)) {
				System.out.println("[TEST] node lazymap:" + lazymap);
				foundImg = (HtmlImage) node;
				System.out.println("[TEST] floating Trend graph XPath:" + foundImg.getCanonicalXPath());
			}
		}
		return foundImg;
	}

	/**
	 * Verifies floating graph image's caption.
	 * The caption includes expected DisplayStepKind message.
	 * And Gets image instance.
	 * @param times builds running times.
	 * @param expectedKind expected DisplayStepKind.
	 * @return HtmlImage instance.
	 */
	public HtmlImage verifyFloatingGraphForStep(final int times, final DisplayStepKindEnum expectedKind) {
		// 右フローティンググラフ
		HtmlImage img = this.findImageOfFloatingTrendGraphForStep();
		if (times < 1) {
			System.out.println("[TEST] img is " + img);
			assertThat(img, is(nullValue()));
			return null;
		}
		assertThat(img, is(not(nullValue())));
		System.out.println("[TEST] img src:" + img.getAttribute("src"));
		System.out.println("[TEST] img alt:" + img.getAttribute("alt"));
		System.out.println("[TEST] img usemap:" + img.getAttribute("usemap"));
		assertThat(img.getAttribute("alt"), is(equalTo("Trend graph of StepCount")));
		// グラフキャプション
		String graphCaptionXpath = getXPathOfCaptionDivisionFromImageOfStepCount(img);
		HtmlDivision captionDiv = (HtmlDivision) this.getPage().getFirstByXPath(graphCaptionXpath);
		System.out.println("[TEST] graph caption:" + captionDiv.getTextContent());
		String stepKind = getDisplayStepKindCaption(captionDiv.getTextContent());
		assertThat(stepKind, is(equalTo(getExpectedDisplayStepKindCaption(expectedKind))));
		String classValue = captionDiv.getAttribute("class");
		assertThat(classValue, is(equalTo("test-trend-caption")));
		return img;
	}

	/**
	 * Verifies floating graph image's caption.
	 * And Gets image instance.
	 * @param times builds running times.
	 * @return HtmlImage instance.
	 */
	public HtmlImage verifyFloatingGraphForDiffAdded(final int times) {
		// 右フローティンググラフ
		HtmlImage img = this.findImageOfFloatingTrendGraphForDiffAdded();
		if (times < 1) {
			System.out.println("[TEST] img is " + img);
			assertThat(img, is(nullValue()));
			return null;
		}
		assertThat(img, is(not(nullValue())));
		System.out.println("[TEST] img src:" + img.getAttribute("src"));
		System.out.println("[TEST] img alt:" + img.getAttribute("alt"));
		System.out.println("[TEST] img usemap:" + img.getAttribute("usemap"));
		assertThat(img.getAttribute("alt"), is(equalTo("Trend graph of DiffCount added steps")));
		// グラフキャプション
		String graphCaptionXpath = getXPathOfCaptionDivisionFromImageOfDiffCountAdded(img);
		HtmlDivision captionDiv = (HtmlDivision) this.getPage().getFirstByXPath(graphCaptionXpath);
		String captionTitle = captionDiv.getTextContent();
		System.out.println("[TEST] graph caption:" + captionTitle);
		assertThat(captionTitle, is(equalTo("Trend of Diff Steps (Code only)")));
		String classValue = captionDiv.getAttribute("class");
		assertThat(classValue, is(equalTo("test-trend-caption")));
		return img;
	}

	/**
	 * Verifies floating graph image's caption.
	 * And Gets image instance.
	 * @param times builds running times.
	 * @return HtmlImage instance.
	 */
	public HtmlImage verifyFloatingGraphForDiffDeleted(final int times) {
		// 右フローティンググラフ
		HtmlImage img = this.findImageOfFloatingTrendGraphForDiffDeleted();
		if (times < 1) {
			System.out.println("[TEST] img is " + img);
			assertThat(img, nullValue());
			return null;
		}
		assertThat(img, is(not(nullValue())));
		System.out.println("[TEST] img src:" + img.getAttribute("src"));
		System.out.println("[TEST] img alt:" + img.getAttribute("alt"));
		System.out.println("[TEST] img usemap:" + img.getAttribute("usemap"));
		assertThat(img.getAttribute("alt"), is(equalTo("Trend graph of DiffCount deleted steps")));
		// グラフキャプション
		String graphCaptionXpath = getXPathOfCaptionDivisionFromImageOfDiffCountDeleted(img);
		HtmlDivision captionDiv = (HtmlDivision) this.getPage().getFirstByXPath(graphCaptionXpath);
		String captionTitle = captionDiv.getTextContent();
		System.out.println("[TEST] graph caption:" + captionTitle);
		assertThat(captionTitle, is(equalTo("Trend of Diff Steps (Code only)")));
		String classValue = captionDiv.getAttribute("class");
		assertThat(classValue, is(equalTo("test-trend-caption")));
		return img;
	}

	/**
	 * Asserts that DiffCount graphs do not exist.
	 */
	public void assertThatDiffCountGraphsDoNotExist() {
		HtmlImage img = this.findImageOfFloatingTrendGraphForDiffAdded();
		assertThat(img, is(nullValue()));
		img = this.findImageOfFloatingTrendGraphForDiffDeleted();
		assertThat(img, is(nullValue()));
	}

	private String getUsemap(final HtmlImage image) {
		if (image == null) {
			return null;
		}
		String usemap = image.getAttribute("usemap");
		if (usemap == null) {
			return null;
		} else if (usemap.length() < 2) {
			return "";
		}
		return  usemap.substring(1);
	}

	private HtmlMap getMapOfFloatingGraph(final HtmlImage image, final boolean isNull) {
		HtmlMap foundMap = null;
		String target = this.getUsemap(image);
		if (target == null || target.isEmpty()) {
			return null;
		}
		List<DomElement> list = this.getPage().getElementsByTagName("map");
		for (DomElement node : list) {
			String name = node.getAttribute("name");
			//System.out.println("[TEST DEBUG] map name:" + name);
			if (target.equals(name)) {
				System.out.println("[TEST] map name:" + name);
				foundMap = (HtmlMap) node;
			}
		}
		if (foundMap == null) {
			if (isNull) {
				return null;
			} else {
				fail("map name=" + target + " not found.");
			}
		}
		return foundMap;
	}

	private int[] verifyMapArea(final HtmlMap map, final int times,
			final String[] checkCategories, final String checkHref, final boolean checkCoords) {
		//System.out.println("[TEST] map XPath:" + map.getCanonicalXPath());
		System.out.println("[TEST] map is " + map.getAttribute("name"));
		System.out.println("[TEST] map children size:" + map.getChildElementCount());
		Iterable<DomElement> it = map.getChildElements();
		List<Integer> categoryPositionY = null;
		if (checkCoords) {
			categoryPositionY = new ArrayList<Integer>();
		}
		int i = 1; // area index
		int j = 0; // category index
		int k = times; // build index
		for (DomElement element : it) {
			HtmlArea area = (HtmlArea) element;
			String title = area.getAttribute("title");
			String href = area.getAttribute("href");
			String coords = area.getAttribute("coords");
			System.out.println("[TEST] area[" + i + "]:title=" + title + ", href=" + href
					+ ", coords=" + coords);
			if (checkCategories != null) {
				assertThat(title, is(allOf(
						containsString(checkCategories[j]), containsString("Build #"))));
			}
			if (checkHref != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toString(k)).append(checkHref);
				assertThat(href, is(allOf(startsWith(sb.toString()), endsWith(checkCategories[j]))));
			}
			if (checkCoords) {
				if (k == times) { // when last build, add Y-axis：step value into list
					categoryPositionY.add(this.getPositionYOf(coords));
				}
			}
			// setting for next iteration
			if (i % 2 == 0) { // category change by 2 areas
				j++; // next category
				if (j < checkCategories.length) { // next category is ok
					k++; // build keeping to current target.
				} else { // category reset and build target going 1 old
					j = 0;
				}
			} else { // category keeping and build going 1 old of target
				k--; // build decr
			}
			i++; // area incr
		}
		if (checkCoords) {
			return this.convertListOfIntegerToArrayOfInt(categoryPositionY);
		}
		return null;
	}

	private int[] verifyMapAreaOfStackedBar(final HtmlMap map, final int times,
			final String[] checkCategories, final String checkHref, final boolean checkCoords) {
		//System.out.println("[TEST] map XPath:" + map.getCanonicalXPath());
		System.out.println("[TEST] map is " + map.getAttribute("name"));
		System.out.println("[TEST] map children size:" + map.getChildElementCount());
		Iterable<DomElement> it = map.getChildElements();
		List<Integer> categoryPositionY = null;
		if (checkCoords) {
			categoryPositionY = new ArrayList<Integer>();
		}
		int i = 1; // bar area index
		int j = 0; // category index
		int k = times; // build index
		for (DomElement element : it) {
			HtmlArea area = (HtmlArea) element;
			String title = area.getAttribute("title");
			String href = area.getAttribute("href");
			String coords = area.getAttribute("coords");
			System.out.println("[TEST] bar[" + i + "]:title=" + title + ", href=" + href
					+ ", coords=" + coords);
			if (checkCategories != null) {
				assertThat(title, is(allOf(
						containsString(checkCategories[j]), containsString("Build #"))));
			}
			if (checkHref != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toString(k)).append(checkHref);
				assertThat(href, is(allOf(startsWith(sb.toString()), endsWith(checkCategories[j]))));
			}
			if (checkCoords) {
				if (k == times) { // when last build, add Y-axis：step value into list
					categoryPositionY.add(this.getPositionYOf(coords));
				}
			}
			// setting for next iteration
			j++; // next category
			if (j == checkCategories.length) {
				j = 0; // category reset
				k--;   // build decr
			}
			i++; // bar incr
		}
		if (checkCoords) {
			return this.convertListOfIntegerToArrayOfInt(categoryPositionY);
		}
		return null;
	}

	private Integer getPositionYOf(final String coords) {
		String[] array = coords.split(",");
		String posYstr = array[array.length - 1];
		return Integer.parseInt(posYstr);
	}

	private int[] convertListOfIntegerToArrayOfInt(final List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i).intValue();
		}
		return array;
	}

	/**
	 * Verifies HtmlMap and HtmlMapArea.
	 * And Gets array of positionY that is at last build by category.
	 * @param image HtmlImage instance using this HtmlMap.
	 * @param times build running times.
	 * @param categories array of categories.
	 * @param checkY boolean for getting return.
	 * @return array of int.
	 */
	public int[] verifyMapAndGetPositionY(final HtmlImage image, final int times,
			final String[] categories, final boolean checkY) {
		HtmlMap map = this.getMapOfFloatingGraph(image, (times < 1));
		if (times < 1) {
			System.out.println("[TEST] map is " + map);
			assertThat(map, nullValue());
			return null;
		}
		assertThat(map, is(not(nullValue())));
		if (checkY && (categories == null || categories.length == 0)) {
			System.out.println("[TEST] categories is required when checkY is true.");
			throw new RuntimeException("wrong use ProjectMainPageUI"
					+ "#validateMapArea(times, categories=empty, checkY=true)");
		}
		return this.verifyMapArea(map, times, categories, "/keisuke/result", checkY);
	}

	/**
	 * Verifies HtmlMap and HtmlMapArea.
	 * And returns HtmlMap instance.
	 * @param image HtmlImage instance using this HtmlMap.
	 * @param times build running times.
	 * @param categories array of categories.
	 * @return HtmlMap instance.
	 */
	public HtmlMap verifyMap(final HtmlImage image, final int times, final String[] categories) {
		HtmlMap map = this.getMapOfFloatingGraph(image, (times < 1));
		if (times < 1) {
			System.out.println("[TEST] map is " + map);
			assertThat(map, nullValue());
			return null;
		}
		assertThat(map, is(not(nullValue())));
		this.verifyMapArea(map, times, categories, "/keisuke/result", false);
		return map;
	}

	/**
	 * Verifies HtmlMap and HtmlMapArea.
	 * And returns HtmlMap instance.
	 * @param image HtmlImage instance using this HtmlMap.
	 * @param times build running times.
	 * @param categories array of categories.
	 * @return HtmlMap instance.
	 */
	public HtmlMap verifyMapForDiffCount(final HtmlImage image, final int times, final String[] categories) {
		HtmlMap map = this.getMapOfFloatingGraph(image, (times < 1));
		if (times < 1) {
			System.out.println("[TEST] map is " + map);
			assertThat(map, nullValue());
			return null;
		}
		assertThat(map, is(not(nullValue())));
		this.verifyMapAreaOfStackedBar(map, times, categories, "/keisuke/result", false);
		return map;
	}

	private static final String BEGIN_BRACKET = "(";
	private static final String END_BRACKET = ")";

	public static String getDisplayStepKindCaption(final String title) {
		if (title == null || title.isEmpty()) {
			return "";
		}

		int pos = title.indexOf(BEGIN_BRACKET);
		if (pos < 0) {
			return "";
		}
		int pos2 = title.indexOf(END_BRACKET, pos + BEGIN_BRACKET.length());
		if (pos2 < 0) {
			return "";
		}
		return title.substring(pos + BEGIN_BRACKET.length(), pos2);
	}

	public static String getExpectedDisplayStepKindCaption(final DisplayStepKindEnum kind) {
		if (kind == null) {
			return null;
		}
		String kindValue = kind.getValue();
		Class<Messages> obj = Messages.class;
		try {
			Method method = obj.getMethod(kindValue);
			return (String) method.invoke(obj);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return "Fail to get expected caption";
	}
}
