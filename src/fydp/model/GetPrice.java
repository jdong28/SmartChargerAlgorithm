import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.thoughtworks.xstream.XStream;


public class getPrice {

	public static void main(String[] args){
		String s = new String(); 
		//private double[] prices = new double[48];
		XStream xstream = new XStream();

		s = getPrice("http://reports.ieso.ca/public/DispUnconsHOEP/PUB_DispUnconsHOEP.xml");

		String deSerializedPrice =  (String)xstream.fromXML(s);


		System.out.println(deSerializedPrice);



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
