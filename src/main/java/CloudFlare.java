import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by a.g.seliverstov on 01.04.2016.
 */
public class CloudFlare {
    private static final boolean BYPASS = false;
    public static final String HOST = "cherry-pudding-87894.herokuapp.com";

    public static String bypass(String originUrl) {
        if (!BYPASS) return originUrl;
        URL url = null;
        try {
            url = new URL(originUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        return originUrl.replace(url.getHost(), HOST);
    }

    public static void main(String[] args){
        System.out.println(CloudFlare.bypass("http://cdn.animenewsnetwork.com/encyclopedia/api.php"));
    }
}
