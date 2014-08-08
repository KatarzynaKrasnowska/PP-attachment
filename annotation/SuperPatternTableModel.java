package annotation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import annotation.KrzakiPattern.Attachment;

@SuppressWarnings("serial")
public class SuperPatternTableModel extends AbstractPatternTableModel {

	private final static String ANN_PREFIX = "/home/kasia/Dokumenty/Robota/PP-attachment/manual/probka-200/full-manual-";
	private final static String ANN_1 = ANN_PREFIX + "ec.csv";
	private final static String ANN_2 = ANN_PREFIX + "pw.csv";

	@Override
	protected void setDatafile() {
		this.DATAFILE = "super.csv";
	}

	@Override
	protected void readData() {
		Integer index;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(DATAFILE), "UTF8"));
			String line;
			index = 0;
			while ((line = in.readLine()) != null && (index < 100000)) {
				SuperKrzakiPattern pattern = new SuperKrzakiPattern(line);
				this.patterns.put(index, pattern);
				if (pattern.getAttachment() != null) {
					++this.decided;
				}
				++index;
			}
			in.close();
		} catch (IOException e) {
			System.out.println(ANN_1);
			System.out.println(ANN_2);
			List<KrzakiPattern> patterns1 = this.readPatternList(ANN_1);
			List<KrzakiPattern> patterns2 = this.readPatternList(ANN_2);
			index = 0;
			for (int i = 0; i < patterns1.size(); ++i) {
				KrzakiPattern pat1 = patterns1.get(i);
				KrzakiPattern pat2 = patterns2.get(i);
				if (!pat1.getAttachment().equals(pat2.getAttachment())) {
					SuperKrzakiPattern pattern = new SuperKrzakiPattern(pat1,
							pat2);
					this.patterns.put(index, pattern);
					if (pattern.getAttachment() != null) {
						++this.decided;
					}
					++index;
				}
			}
		}

	}

	/*
	 * @Override public String getSentenceText(int index) { SuperKrzakiPattern
	 * pattern = (SuperKrzakiPattern) this.patterns .get(index); return
	 * pattern.getText() + "\n------------\nAnotator 1: " +
	 * this.annToString(pattern.getAnn1(), pattern) + "\nAnotator 2: " +
	 * this.annToString(pattern.getAnn2(), pattern); }
	 */

	/*
	 * private String annToString(Attachment a, KrzakiPattern p) { switch (a) {
	 * case VERB: { return p.getVerbPP(); } case SUBST: { return p.getSubstPP();
	 * } case UNDECIDABLE: { return MainWindow.UNDECIDABLE_TEXT; } case
	 * INCORRECT: { return MainWindow.INCORRECT_TEXT; } default: { return null;
	 * } } }
	 */

	private List<KrzakiPattern> readPatternList(String f) {
		List<KrzakiPattern> patterns = new LinkedList<KrzakiPattern>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), "UTF8"));
			String line;
			while ((line = in.readLine()) != null) {
				KrzakiPattern pattern = new KrzakiPattern(line);
				patterns.add(pattern);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return patterns;
	}

	@Override
	public Set<Attachment> highlightedAttachments(int index) {
		Set<Attachment> ret = new HashSet<Attachment>();
		SuperKrzakiPattern pattern = (SuperKrzakiPattern) this.patterns.get(index);
		ret.add(pattern.getAnn1());
		ret.add(pattern.getAnn2());
		return ret;
	}
	
	

}
