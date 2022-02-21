package net.kogics.kojo.codex

import org.apache.hc.client5.http.classic.methods.{HttpGet, HttpPost}
import org.apache.hc.client5.http.cookie.BasicCookieStore
import org.apache.hc.client5.http.entity.mime.{FileBody, HttpMultipartMode, MultipartEntityBuilder, StringBody}
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

import java.io.File
import java.net.URI

class CodexSession(server: String) {
  val cookieStore = new BasicCookieStore()
  val client = HttpClients.custom()
    .setDefaultCookieStore(cookieStore)
    .disableRedirectHandling()
    .build()

  def login(email: String, password: String): Unit = {
    val login = ClassicRequestBuilder.post()
      .setUri(new URI(s"$server/login"))
      .addParameter("email", email)
      .addParameter("password", password)
      .build();

    val resp = client.execute(login)
    try {
      EntityUtils.consume(resp.getEntity)
    }
    finally {
      resp.close()
    }
    if (resp.getCode != 302) {
      client.close()
      throw new RuntimeException("Login Failed")
    }
  }

  def upload(title: String, category: String, code: String, image: File): Unit = {
    val upload = new HttpPost(s"$server/codeupload")
    val titleBody = new StringBody(title, ContentType.MULTIPART_FORM_DATA);
    val catBody = new StringBody(category, ContentType.MULTIPART_FORM_DATA);
    val codeBody = new StringBody(code, ContentType.MULTIPART_FORM_DATA);
    val fileBody = new FileBody(image, ContentType.IMAGE_PNG)

    val mpEntity = MultipartEntityBuilder.create()
      .setMode(HttpMultipartMode.LEGACY)
      .addPart("title", titleBody)
      .addPart("category", catBody)
      .addPart("code", codeBody)
      .addPart("image", fileBody)
      .build();

    upload.setEntity(mpEntity)

    val resp = client.execute(upload)
    try {
      EntityUtils.consume(resp.getEntity)
    }
    finally {
      resp.close()
    }

    if (resp.getCode != 200) {
      client.close()
      throw new RuntimeException("Upload Failed")
    }
  }

  def logout(): Unit = {
    val logout = new HttpGet(s"$server/logout")
    val resp = client.execute(logout)
    try {
      EntityUtils.consume(resp.getEntity)
      if (resp.getCode != 302) {
        throw new RuntimeException("Logout Failed")
      }
    }
    finally {
      resp.close()
      client.close()
    }
  }
}
