package annotation;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import annotation.KrzakiPattern.Attachment;

@SuppressWarnings("serial")
public abstract class AbstractPatternTableModel extends AbstractTableModel {

	protected Map<Integer, KrzakiPattern> patterns = new HashMap<Integer, KrzakiPattern>();
	protected int decided;
	
	protected String DATAFILE;
	
	public AbstractPatternTableModel() {
		super();
		this.setDatafile();
		this.readData();
	}
	
	protected abstract void setDatafile();
	
	protected abstract void readData();
	
	public void saveData() {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(DATAFILE), "UTF8"));
			for (int i = 0; i < this.patterns.size(); ++i) {
				out.write(this.patterns.get(i).toDataLine());
			}
			out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return this.patterns.size();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0 : {
			return Integer.class;
		}
		case 1 : {
			return Boolean.class;
		}
		case 2 : {
			return String.class;
		}
		default : {
			return null;
		}
		}
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		KrzakiPattern pattern = this.patterns.get(arg0);
		switch (arg1) {
		case 0 : {
			return arg0 + 1;
		}
		case 1 : {
			return (pattern.getAttachment() != null);
		}
		case 2 : {
			return pattern.getText();
		}
		default : {
			return null;
		}
		}
	}

	public String getVerbText(int index) {
		return this.patterns.get(index).getVerbPP();
	}
	
	public String getSubstText(int index) {
		return this.patterns.get(index).getSubstPP();
	}
	
	public String getSentenceText(int index) {
		return this.patterns.get(index).getText();
	}
	
	public void setAttachment(int index, Attachment attachment) {
		boolean wasDecided = this.patterns.get(index).getAttachment() != null;
		this.patterns.get(index).setAttachment(attachment);
		boolean isDecided = this.patterns.get(index).getAttachment() != null;
		if (!wasDecided && isDecided) {
			++this.decided;
		} else if (wasDecided && !isDecided) {
			--this.decided;
		}
	}
	
	public Attachment getAttachment(int index) {
		return this.patterns.get(index).getAttachment();
	}
	
	public Set<Attachment> highlightedAttachments(int index) {
		return new HashSet<Attachment>();
	}
	
	public int nextNotDecided(int index, int direction) {
		int i = index + direction;
		while (i >= 0 && i < this.patterns.size()) {
			if (this.patterns.get(i).getAttachment() == null) {
				return i;
			}
			i += direction;
		}
		return -1;
	}
	
	public int howManyUndecided() {
		return this.patterns.size() - this.decided;
	}

}
