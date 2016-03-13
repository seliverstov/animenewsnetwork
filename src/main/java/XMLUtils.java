import okhttp3.OkHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import xml.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by a.g.seliverstov on 09.03.2016.
 */
public class XMLUtils {

    public static Titles parseTitles(String baseUrl) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("127.0.0.1", 3128))).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();
        XMLService xmlService = retrofit.create(XMLService.class);
        Call<Titles> request = xmlService.listTitles();
        Response<Titles> response = request.execute();
        System.out.println(response.raw().request().url());
        if (!response.isSuccess()){
            System.out.println(response.errorBody().string());
        }
        return response.body();
    }

    public static Titles parseTitles(File file) throws Exception {
        Serializer serializer = new Persister();
        return serializer.read(Titles.class, new InputStreamReader(new FileInputStream(file),"UTF-8"));
    }

    public static Images parseImages(File file) throws Exception {
        Serializer serializer = new Persister();
        return  serializer.read(Images.class, new InputStreamReader(new FileInputStream(file),"UTF-8"));
    }

    public static ANN parseANN(File file) throws  Exception {
        Serializer serializer = new Persister();
        return  serializer.read(ANN.class, new InputStreamReader(new FileInputStream(file),"UTF-8"));
    }

    public static void main(String[] args) throws Exception {

    }

    public interface XMLService{
        @GET("/encyclopedia/reports.xml?id=155")
        Call<Titles> listTitles();
    }
}
