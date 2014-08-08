package annotation;

public class KrzakiPattern {

	public enum Attachment {
		UNDECIDABLE("?"),
		INCORRECT("0"),
		VERB("1"),
		SUBST("2");

		private String a;

		private Attachment(String a) {
			this.a = a;
		}

		public String getAttachment() {
			return this.a;
		}
		
		public static Attachment makeAttachment(String s) {
			for (Attachment a : Attachment.values()) {
				if (a.a.contentEquals(s)) {
					return a;
				}
			}
			return null;
		}
		
	}
	
	protected String verbPP;
	protected String substPP;
	protected String text;
	protected String id;
	protected Attachment attachment = null;
	
	public KrzakiPattern() {
		super();
	}

	public KrzakiPattern(String dataline) {
		super();
		String[] lineElements = dataline.split("\\t");
		this.id = lineElements[0];
		this.text = lineElements[1];
		this.verbPP = lineElements[2];
		this.substPP = lineElements[3];
		// attachment defined
		if (lineElements.length > 4) {
			this.attachment = Attachment.makeAttachment(lineElements[4]);
		}
	}
	
	public String toDataLine() {
		String s = this.id + "\t" + this.text + "\t" + this.verbPP + "\t" + this.substPP;
		if (this.attachment != null) {
			s += "\t" + this.attachment.getAttachment();
		}
		return s + "\n";
	}
	
	public String getText() {
		return this.text;
	}

	public String getVerbPP() {
		return verbPP;
	}

	public String getSubstPP() {
		return substPP;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
}
