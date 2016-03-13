package de.loki.metallum.search.service.advanced;

import de.loki.metallum.MetallumException;
import de.loki.metallum.core.parser.search.TrackSearchParser;
import de.loki.metallum.entity.Track;
import de.loki.metallum.search.AbstractSearchQuery;
import de.loki.metallum.search.AbstractSearchService;

import java.util.List;

/**
 * Represents the advanced search for tracks Here we'll search and try to parse the Result of our
 * Search.
 * <p/>
 * How to use this: Setup a TrackSearchQuery and just call performSearch(TrackSearchQuery query)
 * <p/>
 * This will also call the Parser to finally get a clear result. <br>
 * <br>
 * <b>What this class does not do:</b> It does not fill the resultList with the whole data what you
 * can get from the metal-archives<br>
 * <br>
 * <p/>
 * <b>Actually it does just search!</b>
 *
 * @author Zarathustra
 * @see http://www.metal-archives.com/search/advanced/?search -> Click Tap "Search songs"
 */
public class TrackSearchService extends AbstractSearchService<Track> {

	private boolean loadLyrics;

	/**
	 * Constructs a default TrackSearchService.
	 */
	public TrackSearchService() {
		this(false);
	}

	/**
	 * Constructs a default TrackSearchService.
	 *
	 * @param loadLyrics If true, the lyrics will get downlaoded.
	 */
	public TrackSearchService(final boolean loadLyrics) {
		this.loadLyrics = loadLyrics;
		// because we can enable the cache this way
		this.objectToLoad = Integer.MAX_VALUE;
	}

	@Override
	protected final TrackSearchParser getSearchParser() {
		return new TrackSearchParser();
	}

	@Override
	public final List<Track> performSearch(final AbstractSearchQuery<Track> query) throws MetallumException {
		((TrackSearchParser) this.parser).setLoadLyrics(this.loadLyrics);
		return super.performSearch(query);
	}

	public final void setLoadLyrics(final boolean loadLyrics) {
		this.loadLyrics = loadLyrics;
	}

}