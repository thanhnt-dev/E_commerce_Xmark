package com.thanhnt.productservice.api.request;

public enum RequestType {
  CREATE,
  UPDATE;

  public boolean isCreate() {
    return this == CREATE;
  }

  public boolean isUpdate() {
    return this == UPDATE;
  }
}
