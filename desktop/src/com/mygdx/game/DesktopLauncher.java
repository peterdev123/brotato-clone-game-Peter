package com.mygdx.game;

import Menu.Menu;
//import com.mygdx.game.main.Menu;

import javax.swing.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//		config.setForegroundFPS(60);
//		config.setTitle("Title Fight");
//		config.setWindowedMode(1920, 1080);
//		config.setResizable(true);
//		new Lwjgl3Application(new TitleFight(), config);
		// Start of the Main Menu
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Menu menu = new Menu();
			menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			menu.setVisible(true);
		});
	}
}
