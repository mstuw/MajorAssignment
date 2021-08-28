package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.List;

/**
 * Represents a implementation that has a collection of
 * {@link ILight} objects.
 */
public interface ILightingTracker {
	
	List<ILight> getLights();
	
}