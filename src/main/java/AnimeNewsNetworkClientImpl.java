import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by a.g.seliverstov on 03.03.2016.
 */
public class AnimeNewsNetworkClientImpl implements AnimeNewsNetworkClient {
    private OkHttpClient client;
    private String baseUrl;

    public AnimeNewsNetworkClientImpl(boolean proxy, boolean CFBypass){
        if (proxy) {
            client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("127.0.0.1", 3128))).build();
        }else{
            client = new OkHttpClient();
        }

        if (CFBypass){
            baseUrl = AnimeNewsNetworkClient.CF_BYPASS_BASE_URL;
        }else{
            baseUrl = AnimeNewsNetworkClient.BASE_URL;
        }
    }

    public AnimeNewsNetworkClientImpl(){
        this(true, true);
    }

    @Override
    public String queryTitlesXML(Integer skip, Integer list, AnimeType type, String name) {
        if (skip < 0 || list < 0) return null;
        try{
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl+QUERY_TITLES).newBuilder();
            if (skip>0) {
                urlBuilder.addQueryParameter(QUERY_TITLES_NSKIP_PARAMETER, skip.toString());
            }
            if (list == 0) {
                urlBuilder.addQueryParameter(QUERY_TITLES_NLIST_PARAMETER, QUERY_TITLES_NLIST_PARAMETER_ALL_VALUE);
            }else {
                urlBuilder.addQueryParameter(QUERY_TITLES_NLIST_PARAMETER, list.toString());
            }
            if (type!=null){
                urlBuilder.addQueryParameter(QUERY_TITLES_TYPE_PARAMETER, type.toString());
            }
            if (name!=null && !name.trim().equals("")){
                urlBuilder.addQueryParameter(QUERY_TITLES_NAME_PARAMETER,name);
            }
            String url = urlBuilder.build().toString();
            System.out.println("Query titles: "+url);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else{
                throw new IOException("Unexpected code " + response);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String queryAllTitlesXML() {
        return queryTitlesXML(0,0,null,null);
    }

    @Override
    public Source queryImage(String url) {
        try {
            System.out.println("Query image: "+url);
            if (url.startsWith(AnimeNewsNetworkClient.BASE_URL) && !baseUrl.equals(AnimeNewsNetworkClient.BASE_URL)){
                url = url.replace(AnimeNewsNetworkClient.BASE_URL, baseUrl);
                System.out.println("Update url to: "+url);
            }
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().source();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String queryDetailsXml(Integer id, AnimeType type) {
        if (id <= 0) return null;
        try{
            HttpUrl url = HttpUrl.parse(baseUrl+QUERY_DETAILS).newBuilder().addQueryParameter((type==null?QUERY_DETAILS_TITLE_PARAMETER:type.toString()),id.toString()).build();
            System.out.println("Query details: "+url.toString());
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else{
                throw new IOException("Unexpected code " + response);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
