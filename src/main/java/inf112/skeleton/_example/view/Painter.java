package inf112.skeleton._example.view;

/**
 * TODO: You might want to drop this interface (used to draw things from the
 * controller) and just give the view a list of objects to draw instead.
 */
public interface Painter {
	/**
	 * Slight simplification of SpriteBatch's draw() methods.
	 * 
	 * TODO: It's probably better to receive some sort of ViewObject or ModelObject.
	 * Definitely try to avoid lots of parameters!
	 * 
	 * @param x           lower left corner
	 * @param y           lower left corner
	 * @param w           width
	 * @param h           height
	 * @param textureName
	 */
	void draw(double x, double y, double w, double h, String textureName);
}
