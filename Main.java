import gui.MainWindow;
import annotation.PatternTableModel;

public class Main {

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow mainWindow = new MainWindow();
				mainWindow.setPatternsModel(new PatternTableModel());
				mainWindow.setVisible(true);
			}
		});

	}

}
