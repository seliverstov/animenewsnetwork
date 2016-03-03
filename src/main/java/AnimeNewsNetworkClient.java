import okio.Source;

import java.io.InputStream;

/**
 * Created by a.g.seliverstov on 03.03.2016.
 */
public interface AnimeNewsNetworkClient {
    String QUERY_TITLES = "http://www.animenewsnetwork.com/encyclopedia/reports.xml?id=155";
    String QUERY_TITLES_NLIST_PARAMETER = "nlist";
    String QUERY_TITLES_NSKIP_PARAMETER = "nskip";
    String QUERY_TITLES_NAME_PARAMETER = "name";
    String QUERY_TITLES_TYPE_PARAMETER = "type";
    String QUERY_TITLES_NLIST_PARAMETER_ALL_VALUE = "all";

    String QUERY_DETAILS = "http://cdn.animenewsnetwork.com/encyclopedia/api.xml";
    String QUERY_DETAILS_TITLE_PARAMETER = "title";

    enum AnimeType {
        ANIME("anime"),
        MANGA("manga");

        private final String text;

        private AnimeType(final String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    String queryTitlesXML(Integer skip, Integer list, AnimeType type, String name);

    String queryAllTitlesXML();

    Source queryImage(String url);

    String queryDetailsXml(Integer id, AnimeType type);
}
