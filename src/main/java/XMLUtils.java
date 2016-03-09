import okhttp3.OkHttpClient;
import org.xml.sax.SAXException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import xml.Titles;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        Titles titles = parseTitles("http://www.animenewsnetwork.com/");
        System.out.println(titles);
        for(Titles.Item item: titles.items){
            System.out.println(item);
        }
    }

    public interface XMLService{
        @GET("/encyclopedia/reports.xml?id=155")
        Call<Titles> listTitles();
    }
}
