package inf112.skeleton.app;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import inf112.skeleton._example.controller.AppController;

public class MvcMain {
	public static void main(String[] args) {
		if (SharedLibraryLoader.os == Os.MacOsX) {
			Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
		}

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("MVC Example");
		cfg.setWindowedMode(1280, 768);

		new Lwjgl3Application(new AppController(), cfg); // replace with your own
	}
}