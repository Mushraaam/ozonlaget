package inf112.skeleton._example.model;

public class Gator implements ModelObject {
	private double x, y, dx = 100, dy = 100;

	public Gator(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public double width() {
		return 100;
	}

	@Override
	public double height() {
		return 100;
	}

	@Override
	public String appearance() {
		return "obligator.png";
	}

	@Override
	public String sound() {
		return "blipp.ogg";
	}

	@Override
	public void step(AppWorld world, double deltaTime) {
		double tmpX = x + dx * deltaTime, tmpY = y + dy * deltaTime;

		if (tmpX + width() > world.width() || tmpX < 0)
			dx = -dx;
		if (tmpY + height() > world.height() || tmpY < 0)
			dy = -dy;
		x = x + dx * deltaTime;
		y = y + dy * deltaTime;
	}

}
