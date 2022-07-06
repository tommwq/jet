package com.tommwq.jet.routine;

import java.util.Collections;
import java.util.List;

public class TaskResult {
  public Object value;
  public List<TaskResult> children = Collections.emptyList();
}
