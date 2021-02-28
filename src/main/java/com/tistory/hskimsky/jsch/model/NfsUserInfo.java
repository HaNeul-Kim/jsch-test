package com.tistory.hskimsky.jsch.model;

import com.jcraft.jsch.UserInfo;

/**
 * @author hskimsky
 * @version 1.0
 * @since 2021-02-28
 */
public class NfsUserInfo implements UserInfo {

  private final String password;

  public NfsUserInfo(String password) {
    this.password = password;
  }

  @Override
  public String getPassphrase() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean promptPassword(String message) {
    return true;
  }

  @Override
  public boolean promptPassphrase(String message) {
    return true;
  }

  @Override
  public boolean promptYesNo(String message) {
    return true;
  }

  @Override
  public void showMessage(String message) {
  }
}
