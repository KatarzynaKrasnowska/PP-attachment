import gui.MainWindow;
import annotation.SuperPatternTableModel;

public class SuperMain {
	
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow mainWindow = new MainWindow();
				mainWindow.setPatternsModel(new SuperPatternTableModel());
				mainWindow.setVisible(true);
			}
		});

	}

}
