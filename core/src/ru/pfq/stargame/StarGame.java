package ru.pfq.stargame;


import com.badlogic.gdx.Game;

import ru.pfq.stargame.objects.CoreBackground;
import ru.pfq.stargame.screens.MenuScreen;

public class StarGame extends Game{

	CoreBackground backgroundObj = CoreBackground.getInstance();
	@Override
	public void create() {
		backgroundObj.setTextureBackground("bg.png");
		backgroundObj.setTextureAtlas("packButtons.pack");
		backgroundObj.setAtlasRegions("star");
		backgroundObj.initStars(CoreBackground.STAR_COUNT);
		setScreen(new MenuScreen(this));
	}
}
