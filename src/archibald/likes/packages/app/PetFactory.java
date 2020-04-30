package archibald.likes.packages.app;

import java.awt.Color;

import org.alixia.javalibrary.JavaTools;

public class PetFactory {

	private final static String[] PET_TYPES = { "Dog", "Cat", "Mouse", "Vegetoid", "Dragon", "Box" };

	public static Pet getRandomPet() {
		return new Pet(JavaTools.pickRandomElement(PET_TYPES), Math.random() * 45 + 15,
				(int) (Math.random() * 100 + 20),
				Color.getHSBColor((float) (Math.random() * 360), (float) Math.sqrt(Math.sqrt(Math.random())), 1f));
	}
}
