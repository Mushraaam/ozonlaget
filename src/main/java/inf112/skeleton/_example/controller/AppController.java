package inf112.skeleton._example.controller;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import inf112.skeleton._example.model.AppWorld;
import inf112.skeleton._example.model.Gator;
import inf112.skeleton._example.model.ModelObject;
import inf112.skeleton._example.view.AppView;

public class AppController implements ApplicationListener {
	private AppView appView = new AppView();
	private AppWorld world;
	private boolean paused = false;

	@Override
	public void create() { // Called at startup

		// MODEL: our virtual world
		world = new AppWorld(1280, 768);
		world.add(new Gator(123, 456));

		// VIEW: graphics
		appView.create(world.width(), world.height());
		Gdx.graphics.setForegroundFPS(60);
	}

	@Override
	public void dispose() {
		// Called at shutdown

		// Graphics and sound resources aren't managed by Java's garbage collector, so
		// they must generally be disposed of manually when no longer needed. But,
		// any remaining resources are typically cleaned up automatically when the
		// application exits, so these aren't strictly necessary here.
		// (We might need to do something like this when loading a new game level in
		// a large game, for instance, or if the user switches to another application
		// temporarily (e.g., incoming phone call on a phone, or something).
		appView.dispose();
	}

	@Override
	public void render() {
		// Called when the application should draw a new frame (many times per second).

		// MODEL: simulate one step
		if (!paused)
			world.step(Gdx.graphics.getDeltaTime());

		// VIEW: draw stuff
		appView.render(painter -> {
			for (ModelObject obj : world) {
				// commands to draw stuff
				painter.draw(obj.x(), obj.y(), obj.width(), obj.height(), obj.appearance());
			}
		});

		// Don't handle input this way – use event handlers!
		if (Gdx.input.justTouched()) { // check for mouse click
			appView.playSound();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // check for key press
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		// Called whenever the window is resized (including with its original size at
		// startup)
		appView.resize(width, height);

		// if appView can change the world size, we'd have to update *world* here
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}
}
