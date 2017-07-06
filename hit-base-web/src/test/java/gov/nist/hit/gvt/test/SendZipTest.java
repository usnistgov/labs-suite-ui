package gov.nist.hit.gvt.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
@Ignore
public class SendZipTest {
		
	@Test
	  public void testSendZip() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("igamtZip.zip").getFile());
	
	    ResponseEntity<?> response = send(file, "http://localhost:8080/gvt/api/upload/uploadZip", "bmljbzpUb3RvNjY2");
	    Map<String, Object> map = (Map<String, Object>) response.getBody();
	    assertNotNull(map);
	    assertNotNull(map.get("token"));
	    System.out.println(map.get("token"));
		System.out.println("http://localhost:8080/gvt/#/uploadTokens?x="+map.get("token")+"&y=bmljbzpUb3RvNjY2");
		
	  }
	
	@Test
	  public void testSendZipToHitDev() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("igamtZip.zip").getFile());
	
	    ResponseEntity<?> response = send(file, "https://hit-dev.nist.gov:8099/gvt/api/upload/uploadZip", "bmljbzpUb3RvNjY2");
	    Map<String, Object> map = (Map<String, Object>) response.getBody();
	    assertNotNull(map);
	    assertNotNull(map.get("token"));
	    System.out.println(map.get("token"));
		System.out.println("http://hit-dev.nist.gov:8099/gvt/#/uploadTokens?x="+map.get("token")+"&y=bmljbzpUb3RvNjY2");
	  }
	
	
	
	public ResponseEntity<?> send(File oFile, String endpoint, String authorization) {
		    LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		    parts.add("file", new FileSystemResource(oFile));
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		    headers.add("Authorization", "Basic " + authorization);
		    RestTemplate restTemplate = new RestTemplate();
		    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
		        new HttpEntity<LinkedMultiValueMap<String, Object>>(parts, headers);
		    ResponseEntity<?> response =
		        restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, Map.class);
		    return response;
		  }
	
	 public File toFile(InputStream io) {
		    OutputStream outputStream = null;
		    File f = null;
		    try {
		      // write the inputStream to a FileOutputStream
		      f = File.createTempFile("IGAMT", ".zip");
		      outputStream = new FileOutputStream(f);
		      int read = 0;
		      byte[] bytes = new byte[1024];
		      while ((read = io.read(bytes)) != -1) {
		        outputStream.write(bytes, 0, read);
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    } finally {
		      if (io != null) {
		        try {
		          io.close();
		        } catch (IOException e) {
		          e.printStackTrace();
		        }
		      }
		      if (outputStream != null) {
		        try {
		          // outputStream.flush();
		          outputStream.close();
		        } catch (IOException e) {
		          e.printStackTrace();
		        }

		      }
		    }
		    return f;
		  }
}
