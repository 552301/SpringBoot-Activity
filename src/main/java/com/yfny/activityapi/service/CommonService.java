package com.yfny.activityapi.service;

import com.yfny.activityapi.utils.ActivitiUtils;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Activity通用Service
 * <p>
 * Created  by  jinboYu  on  2019/3/11
 */
@Service
public class CommonService {

    @Autowired
    private ActivitiUtils activitiUtils;

    @Autowired
    private TaskService taskService;


    public String createTask(String userId, String key, Map<String,Object> variables){
        try {
            //获取当前流程实例ID
            String processInstanceId =  activitiUtils.getProcessInstance(userId,key).getId();
            //查询第一个任务
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            //设置流程任务变量
            taskService.setVariables(task.getId(),variables);
            //完成任务
            taskService.complete(task.getId());
            //返回下一个任务的ID
            String taskId = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId();
            return "任务ID:"+taskId+",流程实例ID:"+processInstanceId;
        } catch (Exception e) {
            return null;
        }
    }

    public String fulfilTask(String taskId, Map<String,Object> variables){
        try {
            //根据任务ID获取当前任务实例
            Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
            //设置流程任务变量
            taskService.setVariables(taskId,variables);
            //完成任务
            taskService.complete(taskId);
            //返回下一个任务的ID
            taskId = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult().getId();
            return "任务ID:"+taskId+",流程实例ID:"+task.getProcessInstanceId();
        } catch (Exception e) {
            return null;
        }
    }
}
