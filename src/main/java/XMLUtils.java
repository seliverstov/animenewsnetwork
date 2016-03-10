import okhttp3.OkHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import xml.Anime;
import xml.Images;
import xml.Manga;
import xml.Titles;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by a.g.seliverstov on 09.03.2016.
 */
public class XMLUtils {
    public static void validate(String xmlFilePath, String xsdFilePath) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdFilePath));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new File(xsdFilePath)));

    }

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
        return serializer.read(Titles.class, file);

    }

    public static Manga parseManga(File file) throws Exception {
        Serializer serializer = new Persister();
        return  serializer.read(Manga.class, file);
    }

    public static Anime parseAnime(File file) throws Exception {
        Serializer serializer = new Persister();
        return  serializer.read(Anime.class, file);
    }


    public static Images parseImages(File file) throws Exception {
        Serializer serializer = new Persister();
        return  serializer.read(Images.class, file);
    }

    public static void main(String[] args) throws Exception {
        /*Titles titles = parseTitles("http://www.animenewsnetwork.com/"); */
        Titles titles = parseTitles(new File("D:\\ANN\\list.xml"));

       for(Titles.Item t: titles.items){
            if (("manga".equals(t.type) || "anthology".equals(t.type)) && !"5445".equals(t.id)) {
                try {
                    String path = "D:\\ANN\\items\\" + t.id + "\\" + t.id + ".xml";
                    Manga manga = parseManga(new File(path));
                    System.out.println(manga);
                    Images images = parseImages(new File(path));
                    System.out.println(images);
                }catch (Exception e){
                    System.out.println(t.id+","+t.type+" - error: ");
                    e.printStackTrace();
                    return;
                }
            }
        }

        for(Titles.Item t: titles.items){
            if (!"manga".equals(t.type) && !"anthology".equals(t.type) && !"16857".equals(t.id)) {
                try {
                    String path = "D:\\ANN\\items\\" + t.id + "\\" + t.id + ".xml";
                    Anime anime = parseAnime(new File(path));
                    System.out.println(anime);
                    Images images = parseImages(new File(path));
                    System.out.println(images);
                }catch (Exception e){
                    System.out.println(t.id+","+t.type+" - error: ");
                    e.printStackTrace();
                    return;
                }
            }
        }


    }

    public interface XMLService{
        @GET("/encyclopedia/reports.xml?id=155")
        Call<Titles> listTitles();
    }
}
