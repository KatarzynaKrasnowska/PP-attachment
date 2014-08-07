package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import annotation.KrzakiPattern.Attachment;
import annotation.PatternTableModel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ListSelectionListener,
		ItemListener {

	private final static int WIDTH = 1000;
	private final static int HEIGHT = 700;
	protected final static Color SELECTED_COLOR = new Color(184, 207, 229);
	protected final static Color UNSELECTED_COLOR = new Color(240, 240, 240);

	private Map<JCheckBox, Attachment> choices = new HashMap<JCheckBox, Attachment>();

	private JCheckBox verbChoice = new JCheckBox("...");
	private JCheckBox substChoice = new JCheckBox("...");
	private JCheckBox undecidableChoice = new JCheckBox("oba prawdopodobne");
	private JCheckBox incorrectChoice = new JCheckBox("oba niepoprawne");
	private JTextArea sentenceArea = new JTextArea();
	private JTable sentenceTable = new JTable(0, 3);

	private PatternTableModel model;

	private int currentIndex;
	private Attachment selectedAttachment;
	
	private JFrame me = this;

	public MainWindow() {
		super("PP Attachment");

		this.setLayout(new BorderLayout());

		JPanel choicePanel = new JPanel();
		choicePanel.setLayout(new GridLayout(4, 1));
		this.add(choicePanel, BorderLayout.CENTER);

		this.prepareChoiceBox(this.verbChoice, Attachment.VERB);
		this.prepareChoiceBox(this.substChoice, Attachment.SUBST);
		this.prepareChoiceBox(this.undecidableChoice, Attachment.UNDECIDABLE);
		this.prepareChoiceBox(this.incorrectChoice, Attachment.INCORRECT);
		choicePanel.add(this.verbChoice);
		choicePanel.add(this.substChoice);
		choicePanel.add(this.undecidableChoice);
		choicePanel.add(this.incorrectChoice);
		//choicePanel.setPreferredSize(new Dimension(WIDTH, 200));
		this.add(choicePanel, BorderLayout.CENTER);

		this.prepareSentenceArea();
		//this.sentenceArea.setPreferredSize(new Dimension(WIDTH, 200));
		this.add(this.sentenceArea, BorderLayout.NORTH);

		this.sentenceTable.setRowSelectionAllowed(true);
		this.sentenceTable.setColumnSelectionAllowed(false);
		this.sentenceTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.sentenceTable.getSelectionModel().addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(this.sentenceTable);
		//scroll.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
		this.add(scroll, BorderLayout.SOUTH);
		this.pack();
		this.setBounds(10, 10, WIDTH, HEIGHT);

		JPanel leftButtons = new JPanel();
		leftButtons.setLayout(new GridLayout(2, 1));
		JButton prev = new JButton("poprzednie");
		prev.addActionListener(new NextSentenceSwitch(-1));
		JButton prevUndecided = new JButton("poprzednie niegotowe");
		prevUndecided.addActionListener(new NextUndecidedSwitch(-1));
		leftButtons.add(prev);
		leftButtons.add(prevUndecided);
		leftButtons.setPreferredSize(new Dimension(200, 200));
		this.add(leftButtons, BorderLayout.WEST);

		JPanel rightButtons = new JPanel();
		rightButtons.setLayout(new GridLayout(2, 1));
		JButton next = new JButton("następne");
		next.addActionListener(new NextSentenceSwitch(1));
		JButton nextUndecided = new JButton("następne niegotowe");
		nextUndecided.addActionListener(new NextUndecidedSwitch(1));
		rightButtons.add(next);
		rightButtons.add(nextUndecided);
		rightButtons.setPreferredSize(new Dimension(200, 200));
		this.add(rightButtons, BorderLayout.EAST);

		this.setResizable(false);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	model.saveData();
		        JOptionPane.showMessageDialog(me, "Dane zapisano. Naciśnij OK, aby zakończyć");
		        System.exit(0);
		    }
		});

	}

	public void setPatternsModel(PatternTableModel model) {
		this.model = model;
		this.sentenceTable.setModel(model);
		this.sentenceTable.getColumnModel().getColumn(2)
				.setPreferredWidth(WIDTH - 40);
		this.sentenceTable.getColumnModel().getColumn(0).setHeaderValue("Nr");
		this.sentenceTable.getColumnModel().getColumn(1).setHeaderValue("Gotowe?");
		this.sentenceTable.getColumnModel().getColumn(2).setHeaderValue("Zdanie");
		this.setCurrentIndex(0);
	}

	private void prepareChoiceBox(JCheckBox box, Attachment attachment) {
		this.choices.put(box, attachment);
		Font f = box.getFont();
		box.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 7));
		// padding
		box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		box.setOpaque(true);
		box.setBackground(UNSELECTED_COLOR);
		box.addItemListener(this);
	}

	private void prepareSentenceArea() {
		Font f = this.sentenceArea.getFont();
		this.sentenceArea.setFont(new Font(f.getName(), f.getStyle(), f
				.getSize() + 5));
		this.sentenceArea.setRows(5);
		this.sentenceArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
				10));
		this.sentenceArea.setLineWrap(true);
		this.sentenceArea.setWrapStyleWord(true);
	}

	private void setCurrentIndex(int index) {
		this.currentIndex = index;
		this.sentenceTable.getSelectionModel().setSelectionInterval(
				this.currentIndex, this.currentIndex);
		this.sentenceTable.scrollRectToVisible(new Rectangle(this.sentenceTable
				.getCellRect(this.currentIndex, 0, true)));
		this.selectSentence(this.currentIndex);
		this.setTitle("Frazy przyimkowe (pozostało " + this.model.howManyUndecided() + " do zrobienia)");
	}

	private void selectSentence(int index) {
		this.setPhraseText(this.verbChoice, this.model.getVerbText(index));
		this.setPhraseText(this.substChoice, this.model.getSubstText(index));
		this.selectedAttachment = this.model.getAttachment(this.currentIndex);
		this.setChoice(this.selectedAttachment);
		this.sentenceArea.setText(this.model.getSentenceText(index));
	}

	private void setChoice(Attachment choice) {
		for (Entry<JCheckBox, Attachment> e : this.choices.entrySet()) {
			this.setCheckBoxSelected(e.getKey(),
					e.getValue() == this.selectedAttachment);
		}
	}

	private void setPhraseText(JCheckBox box, String text) {
		box.setText(text);
	}

	private void setCheckBoxSelected(JCheckBox box, boolean selected) {
		Color color = (selected ? SELECTED_COLOR : UNSELECTED_COLOR);
		box.setBackground(color);
		if (selected != box.isSelected()) {
			// dont't fire an event!
			box.removeItemListener(this);
			box.setSelected(selected);
			// now can set the listener back
			box.addItemListener(this);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel m = (ListSelectionModel) e.getSource();
		if (!m.isSelectionEmpty()) {
			int index = 0;
			for (int i = m.getMinSelectionIndex(); i <= m
					.getMaxSelectionIndex(); i++) {
				if (m.isSelectedIndex(i)) {
					index = i;
				}
			}
			setCurrentIndex(index);
			;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		JCheckBox selected = (JCheckBox) event.getItemSelectable();
		Attachment attachment = this.choices.get(selected);
		if (attachment == this.selectedAttachment) {
			// choice was unclicked
			this.selectedAttachment = null;
		} else {
			this.selectedAttachment = attachment;
		}
		this.setChoice(this.selectedAttachment);
		model.setAttachment(currentIndex, this.selectedAttachment);
	}

	private class NextSentenceSwitch implements ActionListener {

		private int direction;

		public NextSentenceSwitch(int direction) {
			this.direction = direction;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = currentIndex + this.direction;
			if (index >= 0 && index < sentenceTable.getRowCount()) {
				setCurrentIndex(index);
			}
		}

	}

	private class NextUndecidedSwitch implements ActionListener {

		private int direction;

		public NextUndecidedSwitch(int direction) {
			this.direction = direction;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = model.nextNotDecided(currentIndex, this.direction);
			if (index != -1) {
				setCurrentIndex(index);
			}
		}

	}

}
