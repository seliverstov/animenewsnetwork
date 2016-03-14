import okio.BufferedSink;
import okio.Okio;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a.g.seliverstov on 03.03.2016.
 */
public class DumpToFiles {
    private AnimeNewsNetworkClient client;

    private int skipParameter;
    private int listParameter;
    private AnimeNewsNetworkClient.AnimeType typeParameter;
    private String nameParameter;

    private boolean proxy;

    public DumpToFiles(){
        client = new AnimeNewsNetworkClientImpl(proxy);
    }

    public void createNewDump(String rootDirPath) throws IOException{
        dump(rootDirPath,null,null,false);
    }

    public void dump(String rootDirPath, String loadFilePath, String skipFilePath, boolean resume) throws IOException {
        File rootDir = new File(rootDirPath);
        if (!rootDir.exists()) {
            rootDir.mkdir();
            System.out.println("Root dir created at: "+rootDir.getAbsolutePath());
        }else{
            System.out.println("Root dir set to: "+rootDir.getAbsolutePath());
        }

        File baseDir;
        if(!resume) {
            baseDir = new File(rootDir.getAbsolutePath(), getBaseDirName());
            if (!baseDir.exists()) {
                baseDir.mkdir();
                System.out.println("Base dir created at: "+baseDir.getAbsolutePath());
            }
        }else{
            baseDir = rootDir;
            System.out.println("Base dir set to: "+baseDir.getAbsolutePath());
        }

        File itemsDir = new File(baseDir.getAbsolutePath(),"items");
        if (!itemsDir.exists()) {
            itemsDir.mkdir();
            System.out.println("Items dir created at: "+itemsDir.getAbsolutePath());
        }else{
            System.out.println("Items dir set to: "+itemsDir.getAbsolutePath());
        }

        File list = new File(baseDir.getAbsolutePath(),"list.xml");
        File success = new File(baseDir.getAbsolutePath(),"success.txt");
        File failure = new File(baseDir.getAbsolutePath(),"failure.txt");
        File woImages = new File(baseDir.getAbsolutePath(),"woImages.txt");
        File imageLoadErrors = new File(baseDir.getAbsolutePath(),"imageLoadErrors.txt");

        List<Integer> skip = new ArrayList<Integer>();

        if (skipFilePath!=null && new File(skipFilePath).exists()) {
            System.out.println("Read items to skip from file: "+skipFilePath);
            BufferedReader in = new BufferedReader(new FileReader(skipFilePath));
            String line;
            while((line=in.readLine())!=null){
                skip.add(Integer.parseInt(line));
            }
            in.close();
            System.out.println("Should skip "+skip.size()+" items");
        }

        List<Integer> load = new ArrayList<Integer>();

        if (loadFilePath!=null && new File(loadFilePath).exists()){
            System.out.println("Read items to load from file: "+loadFilePath);
            BufferedReader in = new BufferedReader(new FileReader(loadFilePath));
            String line;
            while((line=in.readLine())!=null){
                load.add(Integer.parseInt(line));
            }
            in.close();
            System.out.println("Should load "+load.size()+" items");
        }else {
            String listXml = client.queryTitlesXML(getSkipParameter(), getListParameter(), getTypeParameter(), getNameParameter());
            saveToFile(list, listXml);

            Pattern p = Pattern.compile("<id>(\\d+)</id>");
            Matcher m = p.matcher(listXml);

            while (m.find()) {
                String id = m.group(1);
                load.add(Integer.parseInt(id));
            }
        }
        for(int i=0; i< load.size(); i++){
            Integer id = load.get(i);
            try {
                System.out.println("Process id "+id+"(item "+i+" of "+load.size()+")");
                if (skip.contains(id)){
                    System.out.println("Id "+id+" was skipped");
                }else {
                    String detailsXML = client.queryDetailsXml(id, null);
                    File itemDir = new File(itemsDir.getAbsolutePath(),id.toString());
                    if (!itemDir.exists()) itemDir.mkdir();
                    File item = new File(itemDir.getAbsolutePath(), id + ".xml");
                    saveToFile(item, detailsXML);
                    appendToFile(success, id.toString());
                    Pattern imgPattern = Pattern.compile("<img\\s+src=\"(.+?)\"\\s+width=\"(\\d+)\"\\s+height=\"(\\d+)\"/>");
                    Matcher imgMather = imgPattern.matcher(detailsXML);
                    int imageCount = 0;
                    while(imgMather.find()){
                        imageCount++;
                        String url = imgMather.group(1);
                        String width = imgMather.group(2);
                        String height = imgMather.group(3);
                        try {
                            File downloadedFile = new File(itemDir, id + "." + width + "." + height + url.substring(url.lastIndexOf(".")));
                            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                            sink.writeAll(client.queryImage(url));
                            sink.close();
                        }catch(Exception e){
                            appendToFile(imageLoadErrors,id+","+url);
                        }
                    }
                    if (imageCount==0){
                        appendToFile(woImages,id.toString());
                    }

                }
            }catch(Throwable e){
                e.printStackTrace();
                appendToFile(failure,id.toString());
            }
        }

    }

    protected String stripNonValidXMLCharacters(String xml){
        if (xml!=null)
            return xml.replaceAll("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F\\u007F-\\u0084\\u0086-\\u009F\\uD800-\\uDFFF\\uFDD0-\\uFDEF\\uFFFE\\uFFFF]","");
        else
            return null;
    }

    protected void saveToFile(File file, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(stripNonValidXMLCharacters(content));
        out.close();
        System.out.println("File created: "+file.getName());
    }

    protected void appendToFile(File file,String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
        out.append(content+"\n");
        out.close();
        System.out.println("Id ["+content+"] added to "+file.getName());
    }

    protected String getBaseDirName(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        return sdf.format(new Date());
    }

    public static void main(String[] args) throws IOException {
        DumpToFiles dumpUtil = new DumpToFiles();

        //dumpUtil.setListParameter(10);
        String baseDir = System.getenv("ANN_HOME");
        if (!baseDir.endsWith("\\")||!baseDir.endsWith("/"))
            baseDir+="/";
        dumpUtil.setProxy(true);
        dumpUtil.dump(
                baseDir,
                null,
                baseDir+"success.txt",
                true);

    }

    public int getSkipParameter() {
        return skipParameter;
    }

    public void setSkipParameter(int skipParameter) {
        this.skipParameter = skipParameter;
    }

    public int getListParameter() {
        return listParameter;
    }

    public void setListParameter(int listParameter) {
        this.listParameter = listParameter;
    }

    public String getNameParameter() {
        return nameParameter;
    }

    public void setNameParameter(String nameParameter) {
        this.nameParameter = nameParameter;
    }

    public AnimeNewsNetworkClient.AnimeType getTypeParameter() {
        return typeParameter;
    }

    public void setTypeParameter(AnimeNewsNetworkClient.AnimeType typeParameter) {
        this.typeParameter = typeParameter;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
        client = new AnimeNewsNetworkClientImpl(proxy);
    }
}
