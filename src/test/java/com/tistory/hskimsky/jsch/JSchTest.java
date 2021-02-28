package com.tistory.hskimsky.jsch;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hskimsky
 * @version 1.0
 * @since 2021-02-28
 */
public class JSchTest {

  private static final Logger logger = LoggerFactory.getLogger(JSchTest.class);

  private Session session;

  @Before
  public void setup() throws JSchException {
    JSch jsch = new JSch();
    String username = "YOUR_USERNAME";
    String host = "YOUR_HOSTNAME";
    // String username = "YOUR_USERNAME";
    // String host = "YOUR_HOSTNAME";
    int port = 22;
    session = jsch.getSession(username, host, port);
    // session.setUserInfo(new NfsUserInfo("USER_PASSWORD"));
    // Properties props = new Properties();
    // props.setProperty("StrictHostKeyChecking", "no");
    // session.setConfig(props);
    session.setPassword("USER_PASSWORD");
    session.setConfig("StrictHostKeyChecking", "no");
    // session.setConfig("PreferredAuthentications", "password");
    // jsch.addIdentity("C:\\Users\\hskim\\.ssh\\id_rsa_root_nfs");
    // jsch.addIdentity("C:\\Users\\hskim\\.ssh\\id_rsa_cloudine_imac");
    // jsch.setKnownHosts("C:\\Users\\hskim\\.ssh\\known_hosts");
    session.connect();
  }

  @After
  public void cleanup() {
    if (session != null) {
      session.disconnect();
    }
  }

  @Ignore
  @Test
  public void jschTest() throws JSchException, IOException {
    ChannelExec channel = (ChannelExec) session.openChannel("exec");
    channel.setCommand("ls -alF /");

    channel.setInputStream(null);
    channel.setErrStream(System.err);

    InputStream is = channel.getInputStream();
    channel.connect();

    int bufferSize = 4096;
    byte[] buffer = new byte[bufferSize];
    while (true) {
      while (is.available() > 0) {
        int read = is.read(buffer, 0, bufferSize);
        if (read < 0) {
          break;
        }
        System.out.print(new String(buffer, 0, read));
      }
      if (channel.isClosed()) {
        if (is.available() > 0) {
          continue;
        }
        System.out.print("exit-status: " + channel.getExitStatus());
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    channel.disconnect();
  }

  @Test
  public void sendTest() throws IOException, JSchException {
    this.sendCommand("ls -alF");
    this.sendCommand("ls -alF /");
  }

  private void sendCommand(String command) throws JSchException, IOException {
    ChannelExec channel = (ChannelExec) session.openChannel("exec");
    channel.setCommand(command);
    // channel.setInputStream(null);
    // channel.setErrStream(System.err);
    InputStream is = channel.getInputStream();
    channel.connect();

    int bufferSize = 4096;
    byte[] buffer = new byte[bufferSize];
    int read;
    while ((read = is.read(buffer)) != -1) {
      System.out.println(new String(buffer, 0, read));
    }
    logger.info("ssh user@host \"{}\"", command);
    channel.disconnect();
  }
}
