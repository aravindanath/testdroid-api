package com.testdroid.api.model;

import com.testdroid.api.*;
import com.testdroid.api.model.APIFiles.APIFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Łukasz Kajda <lukasz.kajda@bitbar.com>
 * @author Slawomir Pawluk <slawomir.pawluk@bitbar.com>
 */
@XmlRootElement
public class APIProject extends APIEntity {

    @XmlType(namespace = "APIProject")
    public static enum APIArchivingStrategy {
        NEVER,
        DAYS,
        RUNS
    }

    @XmlType(namespace = "APIProject")
    public static enum Type {
        ANDROID,
        CTS,
        IOS,
        UIAUTOMATOR,
        REMOTECONTROL,
        RECORDERONLINE,
        CALABASH,
        CALABASH_IOS,
        APPIUM_ANDROID,
        APPIUM_IOS,
        TELERIK_ANDROID,
        TELERIK_IOS;

        public Class<? extends APIFiles> getFilesClass() {
            switch (this) {
                case ANDROID:
                    return AndroidFiles.class;
                case CTS:
                    return null;
                case IOS:
                    return IOSFiles.class;
                case UIAUTOMATOR:
                    return UIAutomatorFiles.class;
                case REMOTECONTROL:
                    return RemoteControlFiles.class;
                case RECORDERONLINE:
                    return RecorderOnlineFiles.class;
                case CALABASH:
                    return CalabashFiles.class;
                case CALABASH_IOS:
                    return CalabashIOSFiles.class;
                case APPIUM_ANDROID:
                    return AppiumAndroidFiles.class;
                case APPIUM_IOS:
                    return AppiumIOSFiles.class;
                case TELERIK_ANDROID:
                    return TelerikAndroidFiles.class;
                case TELERIK_IOS:
                    return TelerikIOSFiles.class;
                default:
                    return null;
            }
        }
    }

    private Integer archivingItemCount;

    private APIArchivingStrategy archivingStrategy;

    private boolean common;

    private String description;

    private APIFiles files;

    private byte[] icon;

    private Map<APIProjectJobConfig.Type, APIProjectJobConfig> jobConfig;

    private String name;

    private String sharedByEmail;

    private Long sharedById;

    private APITestRunConfig testRunConfig;

    private Type type;

    private Date createTime;

    public APIProject() {
    }

    public APIProject(
            Long id, Date createTime, String name, String description, Type type, Long sharedById, String sharedByEmail,
            boolean common, APIArchivingStrategy archivingStrategy, Integer archivingItemCount) {
        super(id);
        this.createTime = createTime;
        this.name = name;
        this.description = description;
        this.type = type;
        this.sharedById = sharedById;
        this.sharedByEmail = sharedByEmail;
        this.common = common;
        this.archivingStrategy = archivingStrategy;
        this.archivingItemCount = archivingItemCount;
        this.jobConfig = new HashMap<APIProjectJobConfig.Type, APIProjectJobConfig>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Returns user ID sharing this project or null if project is owned or common.
     *
     * @return user ID sharing this project
     */
    public Long getSharedById() {
        return sharedById;
    }

    public void setSharedById(Long sharedById) {
        this.sharedById = sharedById;
    }

    public String getSharedByEmail() {
        return sharedByEmail;
    }

    public void setSharedByEmail(String sharedByEmail) {
        this.sharedByEmail = sharedByEmail;
    }

    public APIArchivingStrategy getArchivingStrategy() {
        return archivingStrategy;
    }

    public void setArchivingStrategy(APIArchivingStrategy archivingStrategy) {
        this.archivingStrategy = archivingStrategy;
    }

    public Integer getArchivingItemCount() {
        return archivingItemCount;
    }

    public void setArchivingItemCount(Integer archivingItemCount) {
        this.archivingItemCount = archivingItemCount;
    }

    public String getArchivingStrategyDisplayValue() {
        switch (archivingStrategy) {
            case NEVER:
                return "never";
            case RUNS:
                return String.format("%s run%s", archivingItemCount, archivingItemCount != 1 ? "s" : "");
            case DAYS:
                return String.format("%s day%s", archivingItemCount, archivingItemCount != 1 ? "s" : "");
            default:
                return "";
        }
    }

    private String getConfigURI() {
        return createUri(selfURI, "/config");
    }

    private String getJobConfigURI(APIProjectJobConfig.Type type) {
        return createUri(selfURI, "/job-configs/" + type.toString());
    }

    private String getFilesURI() {
        return createUri(selfURI, "/files");
    }

    private String getIconURI() {
        return createUri(selfURI, "/icon");
    }

    private String getSharingsURI() {
        return createUri(selfURI, "/sharings");
    }

    private String getRunsURI() {
        return createUri(selfURI, "/runs");
    }

    private String getRunURI(Long id) {
        return createUri(selfURI, "/runs/" + id);
    }

    private String getPublicDeviceGroupsURI() {
        return createUri(selfURI, "/public-device-groups");
    }

    private String getDeviceGroupsURI() {
        return createUri(selfURI, "/device-groups");
    }

    private String getUploadApplicationURI() {
        return createUri(selfURI, "/files/application");
    }

    private String getUploadTestURI() {
        return createUri(selfURI, "/files/test");
    }

    private String getUploadDataURI() {
        return createUri(selfURI, "/files/data");
    }

    private String getNotificationEmailsURI() {
        return createUri(selfURI, "/notifications");
    }

    private String getParametersURI() {
        return createUri(selfURI, "/config/parameters");
    }

    private Map<String, Object> getCreateRunParameters(String testRunName) {
        return Collections.singletonMap("name", (Object) testRunName);
    }

    private Map<String, Object> getCreateRunParameters(List<Long> usedDeviceIds) {
        return Collections.singletonMap("usedDeviceIds[]", (Object) StringUtils.join(usedDeviceIds, ","));
    }

    private Map<String, Object> getCreateRunParameters(String testRunName, List<Long> usedDeviceIds) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.putAll(getCreateRunParameters(testRunName));
        result.putAll(getCreateRunParameters(usedDeviceIds));
        return result;
    }

    private Map<String, Object> getCreateNotificationParameters(
            final String email,
            final APINotificationEmail.Type type) {
        return new HashMap<String, Object>() {{
            put("email", email);
            put("type", type);
        }};
    }

    private Map<String, Object> getCreateParameterParameters(final String key, final String value) {
        return new HashMap<String, Object>() {{
            put("key", key);
            put("value", value);
        }};
    }

    private Map<String, Object> getShareParameters(final String email) {
        return new HashMap<String, Object>() {{
            put("email", email);
        }};
    }

    @JsonIgnore
    public APITestRunConfig getTestRunConfig() throws APIException {
        if (testRunConfig == null) {
            testRunConfig = getResource(getConfigURI(), APITestRunConfig.class).getEntity();
        }
        return testRunConfig;
    }

    @JsonIgnore
    public APIProjectJobConfig getJobConfig(APIProjectJobConfig.Type type) throws APIException {
        if (jobConfig == null) {
            jobConfig = new HashMap<APIProjectJobConfig.Type, APIProjectJobConfig>();
        }
        if (jobConfig.get(type) == null) {
            jobConfig.put(type, getResource(getJobConfigURI(type), APIProjectJobConfig.class).getEntity());
        }
        return jobConfig.get(type);
    }

    /**
     * Returns APIFiles entity about files uploaded to this project.
     * Depending on <code>type</code> it may be any subclass of <code>APIFiles</code> returned.
     */
    @JsonIgnore
    public <T extends APIFiles> T getFiles(Class<T> clazz) throws APIException {
        if (clazz == null || !clazz.isAssignableFrom(type.getFilesClass())) {
            throw new APIException("This project type does not have requested file types");
        }
        if (files == null) {
            files = getResource(getFilesURI(), clazz).getEntity();
        }
        return (T) files;
    }

    @JsonIgnore
    public byte[] getIcon() throws APIException, IOException {
        if (icon == null) {
            icon = IOUtils.toByteArray(getResource(getIconURI(), null).getStream());
        }
        return icon;
    }

    @JsonIgnore
    public APITestRun run() throws APIException {
        return postResource(getRunsURI(), null, APITestRun.class);
    }

    @JsonIgnore
    public APITestRun run(String testRunName) throws APIException {
        return postResource(getRunsURI(), getCreateRunParameters(testRunName), APITestRun.class);
    }

    @JsonIgnore
    public APITestRun run(List<Long> usedDevicesId) throws APIException {
        return postResource(getRunsURI(), getCreateRunParameters(usedDevicesId), APITestRun.class);
    }

    @JsonIgnore
    public APITestRun run(String testRunName, List<Long> usedDevicesId) throws APIException {
        return postResource(getRunsURI(), getCreateRunParameters(testRunName, usedDevicesId), APITestRun.class);
    }

    public void delete() throws APIException {
        deleteResource(selfURI);
    }

    @JsonIgnore
    public APIListResource<APITestRun> getTestRunsResource() throws APIException {
        return getListResource(getRunsURI());
    }

    /**
     * @param queryBuilder
     * @return
     * @throws APIException
     * @since 1.3.34
     */
    @JsonIgnore
    public APIListResource<APITestRun> getTestRunsResource(APIQueryBuilder queryBuilder) throws APIException {
        return getListResource(getRunsURI(), queryBuilder);
    }

    public APITestRun getTestRun(Long id) throws APIException {
        return getResource(getRunURI(id), APITestRun.class).getEntity();
    }

    /**
     * @param offset
     * @param limit
     * @param search
     * @param sort
     * @return
     * @throws APIException
     * @deprecated
     */
    @JsonIgnore
    public APIListResource<APITestRun> getTestRunsResource(long offset, long limit, String search, APISort sort)
            throws APIException {
        return getListResource(getRunsURI(), offset, limit, search, sort, APITestRun.class);
    }

    @JsonIgnore
    public APIProjectSharing share(String email) throws APIException {
        return postResource(getSharingsURI(), getShareParameters(email), APIProjectSharing.class);
    }

    @JsonIgnore
    public APIListResource<APIProjectSharing> getProjectSharings() throws APIException {
        return getListResource(getSharingsURI());
    }

    /**
     * @param queryBuilder
     * @return
     * @throws APIException
     * @since 1.3.34
     */
    @JsonIgnore
    public APIListResource<APIProjectSharing> getProjectSharings(APIQueryBuilder queryBuilder) throws APIException {
        return getListResource(getSharingsURI(), queryBuilder);
    }

    /**
     * @param offset
     * @param limit
     * @param search
     * @param sort
     * @return
     * @throws APIException
     * @deprecated
     */
    @JsonIgnore
    public APIListResource<APIProjectSharing> getProjectSharings(long offset, long limit, String search, APISort sort)
            throws APIException {
        return getListResource(getSharingsURI(), offset, limit, search, sort, APIProjectSharing.class);
    }

    @JsonIgnore
    public APIListResource<APIDeviceGroup> getPublicDeviceGroups() throws APIException {
        return getListResource(getPublicDeviceGroupsURI());
    }

    @JsonIgnore
    public APIListResource<APIDeviceGroup> getDeviceGroups() throws APIException {
        return getListResource(getDeviceGroupsURI());
    }

    @JsonIgnore
    public APINotificationEmail createNotificationEmail(String email, APINotificationEmail.Type type)
            throws APIException {
        return postResource(getNotificationEmailsURI(), getCreateNotificationParameters(email, type),
                APINotificationEmail.class);
    }

    @JsonIgnore
    public APIListResource<APINotificationEmail> getNotificationEmails() throws APIException {
        return getListResource(getNotificationEmailsURI());
    }

    /**
     * Returns list of notification emails for project.
     *
     * @param queryBuilder
     * @return
     * @throws APIException
     * @since 1.3.34
     */
    @JsonIgnore
    public APIListResource<APINotificationEmail> getNotificationEmails(APIQueryBuilder queryBuilder)
            throws APIException {
        return getListResource(getNotificationEmailsURI(), queryBuilder);
    }

    @JsonIgnore
    public APIFiles.APIFile uploadApplication(File file, String contentType) throws APIException {
        return postFile(getUploadApplicationURI(), file, contentType, APIFile.class);
    }

    @JsonIgnore
    public APIFiles.APIFile uploadTest(File file, String contentType) throws APIException {
        return postFile(getUploadTestURI(), file, contentType, APIFile.class);
    }

    @JsonIgnore
    public APIFiles.APIFile uploadData(File file, String contentType) throws APIException {
        return postFile(getUploadDataURI(), file, contentType, APIFile.class);
    }

    @JsonIgnore
    public APITestRunParameter createParameter(String key, String value) throws APIException {
        return postResource(getParametersURI(), getCreateParameterParameters(key, value), APITestRunParameter.class);
    }

    @JsonIgnore
    public APIListResource<APITestRunParameter> getParameters() throws APIException {
        return getListResource(getParametersURI());
    }

    @JsonIgnore
    public APIListResource<APITestRunParameter> getParameters(APIQueryBuilder queryBuilder) throws APIException {
        return getListResource(getParametersURI(), queryBuilder);
    }

    @JsonIgnore
    public void deleteParameter(long parameterId) throws APIException {
        deleteResource(String.format("%s/%s", getParametersURI(), parameterId));
    }

    public void update() throws APIException {
        Map<String, Object> body = new HashMap<String, Object>() {{
            put("name", name);
            put("description", description);
            put("common", common);
            put("archivingStrategy", archivingStrategy.name());
            put("archivingItemCount", archivingItemCount);
        }};
        APIProject project = postResource(selfURI, body, APIProject.class);
        clone(project);
    }

    @Override
    @JsonIgnore
    protected <T extends APIEntity> void clone(T from) {
        APIProject apiProject = (APIProject) from;
        cloneBase(from);
        this.common = apiProject.common;
        this.createTime = apiProject.createTime;
        this.description = apiProject.description;
        this.files = apiProject.files;
        this.icon = apiProject.icon;
        this.jobConfig = apiProject.jobConfig;
        this.name = apiProject.name;
        this.sharedById = apiProject.sharedById;
        this.testRunConfig = apiProject.testRunConfig;
        this.type = apiProject.type;
        this.archivingStrategy = apiProject.archivingStrategy;
        this.archivingItemCount = apiProject.archivingItemCount;
    }
}
