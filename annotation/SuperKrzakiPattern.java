package annotation;


public class SuperKrzakiPattern extends KrzakiPattern {

	private Attachment ann1;
	private Attachment ann2;

	public SuperKrzakiPattern(String dataline) {
		super();
		String[] lineElements = dataline.split("\\t");
		this.id = lineElements[0];
		this.text = lineElements[1];
		this.verbPP = lineElements[2];
		this.substPP = lineElements[3];
		this.ann1 = Attachment.makeAttachment(lineElements[4]);
		this.ann2 = Attachment.makeAttachment(lineElements[5]);
		// attachment defined
		if (lineElements.length > 6) {
			this.attachment = Attachment.makeAttachment(lineElements[6]);
		}
	}

	public SuperKrzakiPattern(KrzakiPattern pat1, KrzakiPattern pat2) {
		super();
		this.id = pat1.id;
		this.text = pat1.text;
		this.verbPP = pat1.verbPP;
		this.substPP = pat1.substPP;
		this.ann1 = pat1.attachment;
		this.ann2 = pat2.attachment;
		this.attachment = null;
	}

	@Override
	public String toDataLine() {
		String s = this.id + "\t" + this.text + "\t" + this.verbPP + "\t"
				+ this.substPP + "\t" + this.ann1.getAttachment() + "\t"
				+ this.ann2.getAttachment();
		if (this.attachment != null) {
			s += "\t" + this.attachment.getAttachment();
		}
		return s + "\n";
	}

	public Attachment getAnn1() {
		return ann1;
	}

	public Attachment getAnn2() {
		return ann2;
	}

	
}
