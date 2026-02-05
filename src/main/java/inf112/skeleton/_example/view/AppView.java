package inf112.skeleton._example.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

public class AppView implements Painter {
	private Viewport viewport; // defines screen / world size, aspect ratio and camera
	private SpriteBatch batch; // used for drawing images / textures
	private BitmapFont font; // used for writing
	private ShapeRenderer shape; // used for drawing shapes
	private Map<String, Texture> textures = new HashMap<>();
	private Sound blippSound; // unused

	public void create(double worldWidth, double worldHeight) {

		// To preserve aspect ratio, use FitViewport, ExtendViewport or FillViewport.
		// FitViewport keeps aspect by leaving empty space.
		// ExtendViewport keeps aspect by extending the world size in one direction.
		// FillViewport keeps aspect by hiding part of the world in the other direction.
		this.viewport = new FillViewport((float) worldWidth, (float) worldHeight);
		this.batch = new SpriteBatch();
		this.shape = new ShapeRenderer();
		this.blippSound = Gdx.audio.newSound(Gdx.files.internal("blipp.ogg"));

		this.font = new BitmapFont();
		font.setColor(Color.RED);

		preloadTextures();

		Gdx.graphics.setForegroundFPS(60);
	}

	/*
	 * Loading everything at startup prevents glitches later on.
	 */
	private void preloadTextures() {
		for (String fileName : List.of("obligator.png")) {
			getTexture(fileName);
		}
	}

	/*
	 * Example: load textures only once, and save them in a hash map.
	 * 
	 * You should do the same with sounds and other resources. LibGDX has a built-in
	 * AssetManager that can help with this.
	 * 
	 * If you do new Texture() every time you draw, you'll slow the game down and
	 * fill up memory.
	 * 
	 * @param name
	 * 
	 * @return
	 */
	private Texture getTexture(String name) {
		if (!textures.containsKey(name)) {
			FileHandle file = Gdx.files.internal(name);
			if (file != null)
				textures.put(name, new Texture(file));
			else
				textures.put(name, null);
		}
		return textures.get(name);
	}

	public void dispose() {
		// Called at shutdown

		// Graphics and sound resources aren't managed by Java's garbage collector, so
		// they must generally be disposed of manually when no longer needed. But,
		// any remaining resources are typically cleaned up automatically when the
		// application exits, so these aren't strictly necessary here.
		// (We might need to do something like this when loading a new game level in
		// a large game, for instance, or if the user switches to another application
		// temporarily (e.g., incoming phone call on a phone, or something).
		batch.dispose();
		shape.dispose();
		blippSound.dispose();
		font.dispose();
		textures.values().forEach(tex -> {
			if (tex != null)
				tex.dispose();
		});
	}

	@Override
	public void draw(double x, double y, double w, double h, String textureName) {
		Texture texture = getTexture(textureName);
		if (texture != null) {
			batch.draw(texture, //
					(float) x, //
					(float) y, //
					(float) w, //
					(float) h);
		}
	}

	public void playSound() {
		// TODO: play different sounds
		blippSound.play();
	}

	/**
	 * Example:
	 * 
	 * {@snippet :
	 * appView.render(painter -> {
	 * 	painter.draw(x, y, w, h, tex1);
	 * 	painter.draw(x, y, w, h, tex2);
	 * });
	 * }
	 * 
	 * TODO: probably easier to just accept a list of objects to draw.
	 * 
	 * See lectures for better examples.
	 * 
	 * Doing it this way has the advantage of making sure that SpriteBatch's begin()
	 * and end() methods are called properly.
	 * 
	 * @param drawCommands
	 */
	public void render(Consumer<Painter> drawCommands) {
		// Called when the application should draw a new frame (many times per second).

		// Start with a blank screen

		ScreenUtils.clear(Color.WHITE);

		// Draw calls should be wrapped in batch.begin() ... batch.end()
		batch.begin();
		// The projection matrix translates from world to view coordinates.
		batch.setProjectionMatrix(viewport.getCamera().combined);
		font.draw(batch, "Hello, World!", 200, 200);
		try {
			drawCommands.accept(this);
		} finally {
			batch.end(); // if you forget this, resources will leak!
		}

		// It's also possible to draw simple shapes
		shape.begin(ShapeType.Filled);
		shape.setProjectionMatrix(viewport.getCamera().combined);
		shape.setColor(Color.BLUE);
		shape.box(100, 100, 0, 100, 100, 0);
		shape.end();
	}

	public void resize(int width, int height) {
		// Called whenever the window is resized (including with its original size at
		// startup)

		viewport.update(width, height, true);
		// System.out.printf("%dx%d, %fx%f%n", viewport.getScreenWidth(),
		// viewport.getScreenHeight(),
		// viewport.getWorldWidth(), viewport.getWorldHeight());
	}

	// If we use ExtendViewport, the world size might change when the window is
	// resize, so the controller needs access to this information
	public double worldWidth() {
		return viewport.getWorldWidth();
	}

	public double worldHeight() {
		return viewport.getWorldHeight();
	}
}
