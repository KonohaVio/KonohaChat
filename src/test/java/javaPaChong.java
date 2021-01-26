//import java.io.*;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class javaPaChong {
//    // ��ַ
//    private static final String URL = "https://safebooru.donmai.us/posts?tags=";
//    // ��ȡimg��ǩ����
//    //private static final String IMGURL_REG = "(?is)<img\\s*((?<key>[^=]+)=\"*(?<value>[^\"]+)\")+?\\s*/?>";
//    private static final String IMGURL_REG =  "(?is)<img\\s*((?<key>[^=]+)=\"*(?<value>[^\"]+)\")+?\\s*/?>";
//    // ��ȡsrc·��������
//    private static final String IMGSRC_REG = "<img\\b[^<>]*?\\bsrc[\\s\\t\\r\\n]*=[\\s\\t\\r\\n]*[\"\"']?[\\s\\t\\r\\n]*(?<imgUrl>[^\\s\\t\\r\\n\"\"'<>]*)[^<>]*?/?[\\s\\t\\r\\n]*>";
//    // ��ȡa��ǩ������
//    private static final  String A_REG = "article id";//"<a.+?href=\\\"(.+?)\\\".*>"
//    private static final  String dataFileUrl = "data-file-url=\".*.jpg\"";
//    //data-file-url="https://safebooru.donmai.us/data/e3c5b259b7c10d46d72a5c53a9e8ef0b.jpg" data-large-file-url="https://safebooru.donmai.us/data/e3c5b259b7c10d46d72a5c53a9e8ef0b.jpg"
//
//    // ��ȡhtml����
//    public static String getHTML(String srcUrl) throws Exception {
//        URL url = new URL(srcUrl);
//        URLConnection conn = url.openConnection();
//        InputStream is = conn.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//
//        String line = null;
//        StringBuffer buffer = new StringBuffer();
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//            buffer.append("\n");
//        }
//        br.close();
//        isr.close();
//        is.close();
//        return buffer.toString();
//    }
//
//    // ��ȡimage url��ַ
//    public static List<String> getImageURL(String html) {
//        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
//        List<String> list = new ArrayList<>();
//        while (matcher.find()) {
//            list.add(matcher.group());
//        }
//        return list;
//    }
//
//    //��ȡ����ҳ��ַ
//    public static List<String> getChildUrl(String html){
//        Matcher childUrl = Pattern.compile(A_REG).matcher(html);
//        List<String> list = new ArrayList<>();
//        while (childUrl.find()) {
//            System.out.println(childUrl.group());
//            list.add(childUrl.group());
//        }
//        return list;
//    }
//    //��ȡ��ͼ���ص�ַ
//    public static List<String> getdataUrl(String html){
//        Matcher dataFileUrl__ = Pattern.compile(dataFileUrl).matcher(html);
//        List<String> list = new ArrayList<>();
//        while (dataFileUrl__.find()) {
//            System.out.println(dataFileUrl__.group());
//            list.add(dataFileUrl__.group().split("\"")[1]);
//        }
//        return list;
//    }
//
//    // ��ȡimage src��ַ
//    public static List<String> getImageSrc(List<String> listUrl) {
//        List<String> listSrc = new ArrayList<String>();
//        for (String img : listUrl) {
//            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(img);
//            while (matcher.find()) {
//                listSrc.add(matcher.group().substring(0,
//                        matcher.group().length() - 1));
//            }
//        }
//        return listSrc;
//    }
//
//
//
//
//
//    // ����ͼƬ
//    private static void Download(List<String> listImgSrc) {
//        try {
//            // ��ʼʱ��
//            Date begindate = new Date();
//            for (String url : listImgSrc) {
//                // ��ʼʱ��
//                Date begindate2 = new Date();
//                String imageName = url.substring(url.lastIndexOf("/") + 1,
//                        url.length());
//                imageName = "D://image//"+imageName;
//                URL uri = new URL(url);
//                uri.openConnection().setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 5.0;Windows NT;DigExt)");
//
//                InputStream in = uri.openStream();
//                FileOutputStream fo = new FileOutputStream(new File(imageName));// �ļ������
//                byte[] buf = new byte[1024];
//                int length = 0;
//                System.out.println("��ʼ����:" + url);
//                while ((length = in.read(buf, 0, buf.length)) != -1) {
//                    fo.write(buf, 0, length);
//                }
//                // �ر���
//                in.close();
//                fo.close();
//                System.out.println(imageName + "�������");
//                // ����ʱ��
//                Date overdate2 = new Date();
//                double time = overdate2.getTime() - begindate2.getTime();
//                System.out.println("��ʱ��" + time / 1000 + "s");
//            }
//            Date overdate = new Date();
//            double time = overdate.getTime() - begindate.getTime();
//            System.out.println("�ܺ�ʱ��" + time / 1000 + "s");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void getPicture(List<String> picUrl,String destPath) {
//        BufferedInputStream bis=null;
//        BufferedOutputStream bos=null;
//        URL url=null;
//        for(String temp:picUrl) {
//            //System.out.println(temp);
//            String[] regex=temp.split("/");
//            String name=regex[regex.length-1];
//            try {
//                /*
//                /images/github-logo.png
//        /images/twitter-logo.png
//        /images/discord-logo.png
//                 */
//                url=new URL(temp);
//                bis=new BufferedInputStream(url.openStream());
//                byte[] b=new byte[1024];
//                int len=0;
//                bos=new BufferedOutputStream(new FileOutputStream(new File(destPath+name)));
//                while((len=bis.read(b))!=-1) {
//                    bos.write(b,0,len);
//                    bos.flush();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        String tags = "kousaka_reina";
//        String html = getHTML(URL+tags);
//
//        List<String> dataUrl = getdataUrl(html);
//        getPicture(dataUrl,"D:\\image\\");
//
//    }
//
//}
