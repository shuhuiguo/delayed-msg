package com.shuhg.queue;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务节点，每个节点都包含一个任务队列集合，和当前节点ID
 * Created by 大舒 on 2017/8/10.
 */
public class TaskNode {
    /**
     * 环型队列长度3600
     */
    private int nodeIndex;
    /**
     * 队列任务
     */
    private Set<Task> tasks = new HashSet<>();

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
