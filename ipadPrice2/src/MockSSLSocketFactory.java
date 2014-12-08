import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class MockSSLSocketFactory extends SSLSocketFactory {

	public MockSSLSocketFactory() throws NoSuchAlgorithmException,
			KeyManagementException, KeyStoreException,
			UnrecoverableKeyException {
		super(trustStrategy, hostnameVerifier);
	}

	private static final X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
		@Override
		public void verify(String host, SSLSocket ssl) throws IOException {
			// Do nothing
		}

		@Override
		public void verify(String host, String[] cns, String[] subjectAlts)
				throws SSLException {
			// Do nothing
		}

		@Override
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}

		@Override
		public void verify(String arg0, java.security.cert.X509Certificate arg1)
				throws SSLException {
			// TODO Auto-generated method stub

		}
	};

	private static final TrustStrategy trustStrategy = new TrustStrategy() {

		@Override
		public boolean isTrusted(java.security.cert.X509Certificate[] arg0,
				String arg1) throws CertificateException {
			return true;
		}
	};
}
