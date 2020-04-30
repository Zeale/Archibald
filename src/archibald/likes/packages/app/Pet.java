package archibald.likes.packages.app;

import java.awt.Color;

public class Pet {
	private final String type;
	private final double size;
	private final int value;
	private final Color color;

	Pet(String type, double size, int value, Color color) {
		this.type = type;
		this.size = size;
		this.value = value;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Type: `" + type + "`\nSize: `" + size + "`\nValue: `" + value + "`\nColor: `" + color + '`';
	}

}
