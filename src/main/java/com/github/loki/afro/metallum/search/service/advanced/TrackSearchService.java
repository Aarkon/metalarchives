package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.TrackSearchParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;
import com.github.loki.afro.metallum.search.query.entity.TrackQuery;
import com.google.common.collect.Iterables;

import java.util.function.Function;

public class TrackSearchService extends AbstractSearchService<Track, TrackQuery, SearchTrackResult> {

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
     * @param loadLyrics If true, the lyrics will get downloaded.
     */
    public TrackSearchService(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
    }


    public final void setLoadLyrics(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
    }

    @Override
    protected Function<SearchTrackResult, Track> parseFully() {
        return searchResult -> {
            Track.PartialDisc partialDisc = new Track.PartialDisc(searchResult.getDiscId(), searchResult.getDiscName(), searchResult.getDiscType().orElse(null));
            PartialBand bandPartial = new PartialBand(searchResult.getBandId(), searchResult.getBandName());
            Track track = new Track(partialDisc, bandPartial, searchResult.getId(), searchResult.getName());
            track.setLyrics(searchResult.getLyrics().orElse(null));
            return track;
        };
    }

    @Override
    protected Function<Long, Track> getById() {
        throw new UnsupportedOperationException("currently tracks cannot be searched by id");
    }

    @Override
    protected final TrackSearchParser getSearchParser(TrackQuery trackQuery) {
        TrackSearchParser trackSearchParser = new TrackSearchParser();
        trackSearchParser.setLoadLyrics(this.loadLyrics);
        trackSearchParser.setIsAbleToParseGenre(MetallumUtil.isNotBlank(trackQuery.getGenre()));
        trackSearchParser.setIsAbleToParseDiscType(trackQuery.getDiscTypes().size() != 1);
        return trackSearchParser;
    }

    @Override
    protected void enrichParsedEntity(TrackQuery query, SearchTrackResult result) {
        if (query.getDiscTypes().size() == 1) {
            final DiscType discType = Iterables.getOnlyElement(query.getDiscTypes());
            result.setDiscType(discType);
        }
    }

}