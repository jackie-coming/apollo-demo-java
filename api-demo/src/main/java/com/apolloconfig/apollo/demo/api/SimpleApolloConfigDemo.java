/*
 * Copyright 2022 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.apolloconfig.apollo.demo.api;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.google.common.base.Charsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApolloConfigDemo {

  private static final Logger logger = LoggerFactory.getLogger(SimpleApolloConfigDemo.class);
  private String DEFAULT_VALUE = "undefined";
  private Config config;

  public SimpleApolloConfigDemo() {
    ConfigChangeListener changeListener = changeEvent -> {
      System.out.println("Changes for namespace {}"+ changeEvent.getNamespace());
      for (String key : changeEvent.changedKeys()) {
        ConfigChange change = changeEvent.getChange(key);
        System.out.println("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}"+
            change.getPropertyName()+change.getOldValue()+change.getNewValue()+
            change.getChangeType());
      }
    };
    config = ConfigService.getConfig("gjx10117");
    config.addChangeListener(changeListener);
  }

  private String getConfig(String key) {
    String result = config.getProperty(key, DEFAULT_VALUE);
    System.out.println(String.format("Loading key : %s with value: %s", key, result));
    return result;
  }

  public static void main(String[] args) throws IOException {
    SimpleApolloConfigDemo apolloConfigDemo = new SimpleApolloConfigDemo();
    System.out.println(
        "Apollo Config Demo. Please input key to get the value. Input quit to exit.");
    while (true) {
      System.out.print("> ");
      String input = new BufferedReader(
          new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if (input == null || input.length() == 0) {
        continue;
      }
      input = input.trim();
      if (input.equalsIgnoreCase("quit")) {
        System.exit(0);
      }
      System.out.print("> ");
      System.out.println(apolloConfigDemo.getConfig(input));
    }
  }
}
