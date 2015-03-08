import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author ponkotuy
 * Date: 14/04/06.
 */
public class Main {
    public static void main(String[] args) {
        try {
            List<String> urls = getProperties("update.properties");
            for(String uStr: urls) {
                URL url = new URL(uStr);
                Path dst = Paths.get(url.getPath()).getFileName();
                if( ! Files.exists(dst) ) {
                    System.out.println(dst.getFileName() + "は存在しません。ダウンロードします。");
                }
                boolean updated = request(url, dst);
                if ( updated ) {
                  System.out.println(dst.getFileName() + "のダウンロードが完了しました");
                } else {
                  System.out.println(dst.getFileName() + "に変更はありません");
                }
            }
        } catch (MalformedURLException e) {
            System.err.println("おやっ、URLの書式に異常です！ぽんこつさんが悪いです！");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("おやっ、IOExceptionです！");
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    public static List<String> getProperties(String fName) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath(fName);
        Properties p = new Properties();
        try(InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
            p.load(is);
        }
        List<String> result = new ArrayList<>(p.size());
        for(Object prop: p.keySet()) {
            String key = (String) prop;
            result.add(p.getProperty(key));
        }
        return result;
    }

    public static boolean request(URL url, Path dst) throws IOException {
        FileTime fileModified = Files.getLastModifiedTime(dst,LinkOption.NOFOLLOW_LINKS);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setIfModifiedSince(fileModified.toMillis());
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept-Encoding","gzip");
            connection.setRequestProperty("User-Agent","MyFleetGirls Updater");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if ( responseCode == HttpURLConnection.HTTP_OK ) {
              try (InputStream is = connection.getInputStream()) {
                if ( isGziped(connection) ) {
                  Files.copy(new GZIPInputStream(is), dst, StandardCopyOption.REPLACE_EXISTING);
                } else {
                  Files.copy(is, dst, StandardCopyOption.REPLACE_EXISTING);
                }
              }
              long requestModified = connection.getLastModified();
              if ( requestModified != 0 ) {
                Files.setLastModifiedTime(dst,FileTime.fromMillis(requestModified));
              }
              return true;
            } else if ( responseCode == HttpURLConnection.HTTP_NOT_MODIFIED ) {
              return false;
            } else {
              throw new RuntimeException("Unknown status:"+responseCode);
            }
        } finally {
          connection.disconnect();
        }
    }

    public static boolean isGziped(HttpURLConnection connection) {
      List<String> contentEncodings = connection.getHeaderFields().get( "Content-Encoding" );
      if ( contentEncodings != null ) {
        for ( int i = 0; i < contentEncodings.size(); ++i ) {
          String contentEncoding = contentEncodings.get( i );
          if ( "gzip".equals(contentEncoding) ) {
            return true;
          }
        }
      }
      return false;
    }
}
