
package app;

import ui.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow MainWindow = new MainWindow();
            MainWindow.Initialize();
        });
    }
}