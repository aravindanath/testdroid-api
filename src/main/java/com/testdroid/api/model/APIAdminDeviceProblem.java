package com.testdroid.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.testdroid.api.APIEntity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Michał Szpruta <michal.szpruta@bitbar.com>
 */
@XmlRootElement
public class APIAdminDeviceProblem extends APIEntity {

    private Long clusterId;

    private String clusterName;

    private Long deviceId;

    private String deviceName;

    private APIAdminDeviceProblemPair[] problems;

    public APIAdminDeviceProblem() {
    }

    public APIAdminDeviceProblem(
            Long clusterId, String clusterName, Long deviceId, String deviceName, APIAdminDeviceProblemPair[] problems) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.problems = problems;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public APIAdminDeviceProblemPair[] getProblems() {
        return problems;
    }

    public void setProblems(APIAdminDeviceProblemPair[] problems) {
        this.problems = problems;
    }

    @Override
    @JsonIgnore
    protected <T extends APIEntity> void clone(T from) {
        APIAdminDeviceProblem apiAdminDeviceProblem = (APIAdminDeviceProblem) from;
        cloneBase(from);
        this.clusterId = apiAdminDeviceProblem.clusterId;
        this.clusterName = apiAdminDeviceProblem.clusterName;
        this.deviceId = apiAdminDeviceProblem.deviceId;
        this.deviceName = apiAdminDeviceProblem.deviceName;
        this.problems = apiAdminDeviceProblem.problems;
    }
}
