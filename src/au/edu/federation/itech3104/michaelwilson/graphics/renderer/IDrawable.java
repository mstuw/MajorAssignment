package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;

public interface IDrawable {

	public void draw(IDrawableRenderer renderer);

	public Material getMaterial();

}
