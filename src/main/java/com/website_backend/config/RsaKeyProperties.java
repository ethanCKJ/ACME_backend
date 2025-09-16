package com.website_backend.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "rsa")
public class RsaKeyProperties {

  /**
   * These are bound from properties (e.g. rsa.public-key=classpath:certs/public.pem)
   */
  private Resource publicKey;
  private Resource privateKey;

  public Resource getPublicKeyResource() { return publicKey; }
  public void setPublicKey(Resource publicKey) { this.publicKey = publicKey; }

  public Resource getPrivateKeyResource() { return privateKey; }
  public void setPrivateKey(Resource privateKey) { this.privateKey = privateKey; }

  public RSAPublicKey getPublicKey() {
    try {
      byte[] der = pemToDer(publicKey);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) kf.generatePublic(spec);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load RSA public key from " + publicKey, e);
    }
  }

  public RSAPrivateKey getPrivateKey() {
    try {
      byte[] der = pemToDer(privateKey);
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPrivateKey) kf.generatePrivate(spec);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load RSA private key from " + privateKey +
          ". Ensure your private key is in PKCS#8 format (BEGIN PRIVATE KEY).", e);
    }
  }

  private static byte[] pemToDer(Resource res) throws IOException {
    String pem = new String(res.getInputStream().readAllBytes(), StandardCharsets.US_ASCII);
    // remove PEM headers/footers and all whitespace, then Base64-decode
    String base64 = pem.replaceAll("-----BEGIN [^-]+-----", "")
        .replaceAll("-----END [^-]+-----", "")
        .replaceAll("\\s", "");
    return Base64.getDecoder().decode(base64);
  }
}
