package annotation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("serial")
public class PatternTableModel extends AbstractPatternTableModel {

	@Override
	protected void readData() {

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(DATAFILE), "UTF8"));
			String line;
			Integer index = 0;
			while ((line = in.readLine()) != null && (index < 100000)) {
				KrzakiPattern pattern = new KrzakiPattern(line);
				this.patterns.put(index, pattern);
				if (pattern.getAttachment() != null) {
					++this.decided;
				}
				++index;
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setDatafile() {
		this.DATAFILE = "full-manual.csv";
	}

}
