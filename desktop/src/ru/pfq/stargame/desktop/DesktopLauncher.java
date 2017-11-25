package ru.pfq.stargame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.pfq.stargame.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect =3f/4f;
		config.height = 800;
		config.width =(int)(config.height * aspect);
		new LwjglApplication(new StarGame(), config);
	}
}
