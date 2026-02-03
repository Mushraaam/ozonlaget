package inf112.skeleton._example.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppWorld implements Iterable<ModelObject> {
	double width;
	double height;
	double currentTime = 0;
	List<ModelObject> objects = new ArrayList<>();

	public AppWorld(double width, double height) {
		super();
		this.width = width;
		this.height = height;
	}

	@Override
	public Iterator<ModelObject> iterator() {
		return objects.iterator();
	}

	public void step(double deltaTime) {
		currentTime += deltaTime;
		for (var obj : objects) {
			obj.step(this, deltaTime);
		}
	}

	public double width() {
		return width;
	}

	public double height() {
		return height;
	}

	public double currentTime() {
		return currentTime;
	}

	public void add(ModelObject obj) {
		objects.add(obj);
	}
}
