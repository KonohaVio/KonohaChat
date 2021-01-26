import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetSpider {
    public static void main(String[] args) throws Exception {
        String urlStr="https://safebooru.donmai.us/posts/random?tags=kousaka_reina+yuri";             //要获取图片的网址http://www.mmonly.cc    http://www.photophoto.cn
        String regexStr="<img\\b[^<>]*?\\bsrc[\\s\\t\\r\\n]*=[\\s\\t\\r\\n]*[\"\"']?[\\s\\t\\r\\n]*(?<imgUrl>[^\\s\\t\\r\\n\"\"'<>]*)[^<>]*?/?[\\s\\t\\r\\n]*>";  //获取的图片正则表达式
        String destPath="D:\\image\\";                      //保存的路径
        String encode="utf-8";                       //网页编码方式（右键点击网页查看源码编码方式，也可自己用正则表达式提取）


        List<String> picUrl=srcWeb(getNetContent(urlStr,encode),regexStr,1);           //获取的图片Url
        getPicture(picUrl,destPath);                                //获取图片并保存到本地
    }

    /**
     * 获取url的图片并保存到本地
     *
     * @param destPath 保存的路径
     */
    public static void getPicture(List<String> picUrl,String destPath) {
        BufferedInputStream bis=null;                       //读取网页
        BufferedOutputStream bos=null;                   //输出到本地
        URL url=null;
        for(String temp:picUrl) {
            System.out.println(temp);
            String[] regex=temp.split("/");
            String name=regex[regex.length-1];          //获取网页图片名字
            try {
                url=new URL(temp);
                bis=new BufferedInputStream(url.openStream());
                byte[] b=new byte[1024];
                int len=0;
                bos=new BufferedOutputStream(new FileOutputStream(new File(destPath+name)));
                while((len=bis.read(b))!=-1) {
                    bos.write(b,0,len);
                    bos.flush();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取指定的url并保存到List中
     * @param urlStr    url网址
     * @param regexStr    正则解析式
     * @param group        正则组
     * @return
     */
    public static List<String> srcWeb(String urlStr,String regexStr,int group){
        List<String> list=new ArrayList<>();
        Pattern p = Pattern.compile(regexStr);
        Matcher m = p.matcher(urlStr);
        while(m.find()) {
            list.add(m.group(group));
        }
        return list;
    }


    /**
     * 获取网页内容
     * @param urlStr    url网址
     * encode    编码方式
     * @return
     */
    public static String getNetContent(String urlStr,String encode) throws IOException {
        URL url=null;
        BufferedReader br=null;
        StringBuilder sb = new StringBuilder();
        try {
            url=new URL(urlStr);
            br=new BufferedReader(new InputStreamReader(url.openStream(),encode));
            String temp =null;
            while(null!=(temp=br.readLine())) {
                sb.append(temp+"\r\n");
            }
            System.out.println(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        br.close();
        return sb.toString();
    }

}
