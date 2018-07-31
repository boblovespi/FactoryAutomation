package boblovespi.factoryautomation.common.guidebook;

import boblovespi.factoryautomation.common.guidebook.entry.GuidebookEntry;
import boblovespi.factoryautomation.common.guidebook.pages.PageText;

/**
 * Created by Willi on 7/30/2018.
 * <p>
 * code is a modified variant of botania's lexicon, made by vaskii
 * botania github: https://github.com/Vazkii/Botania
 */
public class GuidebookEntries
{
	public static GuidebookEntry mainPage = null;
	public static GuidebookPage testPage = new PageText("test", "testPage",
			"testing, hi!<br>new lines!<br><br>this is a very long paragraph. it contains lots of text for you to read. the purpose of this paragraph is to test the ability of the guidebook to automatically make new lines for paragraphs which are too long. you should be able to read this text here if it worked.");
	public static GuidebookPage testPage2 = new PageText(
			"test2", "testPage2", "this is the second page of the test entry");

	public static GuidebookEntry testEntry = new GuidebookEntry("testEntry", null).setGuidebookPages(testPage, testPage2);
}
