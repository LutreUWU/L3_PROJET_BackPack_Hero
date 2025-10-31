package view.weaponView;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

public record SwordView(ApplicationContext context,  ScreenInfo screenInfo, int grid_size, int x, int y) {
	public void draw(){
		context.renderFrame(graphics -> {
			graphics.setColor(Color.RED);
	        graphics.draw(new Rectangle2D.Double((grid_size/4 + screenInfo.width()/2) - 3.5*grid_size + (grid_size * y), 
	        									( grid_size/4 + screenInfo.height()/2) - 2.5*grid_size + (grid_size * x), 
	        									  grid_size/2, grid_size/2));
		});
	}
}
