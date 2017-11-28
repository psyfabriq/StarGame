package ru.pfq.stargame;


import com.badlogic.gdx.Game;

import ru.pfq.stargame.objects.BackgroundForAllScreens;
import ru.pfq.stargame.screens.MenuScreen;

public class StarGame extends Game{

	BackgroundForAllScreens backgroundObj = BackgroundForAllScreens.getInstance();
	@Override
	public void create() {
		backgroundObj.setTextureBackground("bg.png");
		backgroundObj.setTextureAtlas("packButtons.pack");
		backgroundObj.setAtlasRegions("star");
		backgroundObj.initStars(BackgroundForAllScreens.STAR_COUNT);
		setScreen(new MenuScreen(this));
	}
}
