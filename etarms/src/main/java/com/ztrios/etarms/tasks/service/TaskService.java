package com.ztrios.etarms.tasks.service;

import com.ztrios.etarms.tasks.dto.*;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request);

    TaskReassignResponse reassignTask(TaskReassignRequest request);

    TaskResponse statusUpdate(TaskStatusUpdateReq request);

    List<TaskResponse> getTasksByEmployee(String employeeId);

    List<TaskResponse> getTasksByManager(String manager);

}
