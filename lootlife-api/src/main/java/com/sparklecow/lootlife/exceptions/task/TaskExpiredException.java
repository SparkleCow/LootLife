package com.sparklecow.lootlife.exceptions.task;

public class TaskExpiredException extends RuntimeException {
  public TaskExpiredException(String message) {
    super(message);
  }
}
