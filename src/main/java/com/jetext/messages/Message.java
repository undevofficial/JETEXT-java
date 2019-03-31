package com.jetext.messages;

import com.jetext.ApiClient;

class Message {

  private ApiClient apiClient;

  public Message(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return this.apiClient;
  }

}
