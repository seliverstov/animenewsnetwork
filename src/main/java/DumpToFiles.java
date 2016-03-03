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

    public DumpToFiles(){
        client = new AnimeNewsNetworkClientImpl();
    }

    public void dump(String rootDirPath, String skipFilePath) throws IOException {
        File rootDir = new File(rootDirPath);
        if (!rootDir.exists()){
            rootDir.mkdir();
        }

        File baseDir = new File(rootDir.getAbsolutePath(),getBaseDirName());
        baseDir.mkdir();
        System.out.println("Base dir created at: "+baseDir.getAbsolutePath());

        File itemsDir = new File(baseDir.getAbsolutePath(),"items");
        itemsDir.mkdir();
        System.out.println("Items dir created at: "+itemsDir.getAbsolutePath());

        File list = new File(baseDir.getAbsolutePath(),"list.xml");
        File success = new File(baseDir.getAbsolutePath(),"success.txt");
        File failure = new File(baseDir.getAbsolutePath(),"failure.txt");
        File woImages = new File(baseDir.getAbsolutePath(),"woImages.txt");
        File imageLoadErrors = new File(baseDir.getAbsolutePath(),"imageLoadErrors.txt");

        List<Integer> skip = new ArrayList<>();
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

        String listXml = client.queryTitlesXML(0,10,null,null);
        saveToFile(list,listXml);

        Pattern p = Pattern.compile("<id>(\\d+)</id>");
        Matcher m = p.matcher(listXml);

        List<Integer> items = new ArrayList<>();
        while(m.find()){
            String id = m.group(1);
            items.add(Integer.parseInt(id));
        }

        for(int i=0; i< items.size(); i++){
            Integer id = items.get(i);
            try {
                System.out.println("Process id "+id+"(item "+i+" of "+items.size()+")");
                if (skip.contains(id)){
                    System.out.println("Id "+id+" was skipped");
                }else {
                    String detailsXML = client.queryDetailsXml(id, null);
                    File itemDir = new File(itemsDir.getAbsolutePath(),id.toString());
                    itemDir.mkdir();
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

    protected void saveToFile(File file, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(content);
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
        dumpUtil.dump("D:\\AnimeNewsNetwork",null);
    }
}
