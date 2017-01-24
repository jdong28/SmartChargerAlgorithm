import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class getPrice {

	public static void main(String[] args){
		String s = new String();
		s = getPrice("http://reports.ieso.ca/public/DispUnconsHOEP/PUB_DispUnconsHOEP.xml");
		System.out.println(s);
	}

	public static String getPrice(String getURL) {
		System.out.println("Result: " + getURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConnection = null;
		InputStreamReader in = null;

		try {
			URL url = new URL(getURL);
			urlConnection = url.openConnection();
			if (urlConnection != null){
				// 60 seconds
				urlConnection.setReadTimeout(60000);
			}
			if (urlConnection != null && urlConnection.getInputStream() != null){
				in = new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null){
					int cp;
					while ((cp = bufferedReader.read()) != -1){
						sb.append((char) cp);
					}
				bufferedReader.close();
				}
			}
			in.close();
		} catch(Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ getURL, e);
		}

		return sb.toString();
	}
}