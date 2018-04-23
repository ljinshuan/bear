package com.taobao.brand.bear.gitlab;

import com.taobao.brand.bear.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author jinshuan.li 23/04/2018 10:47
 */
@Slf4j
public class GitLabApiTest {

    GitLabApi gitLabApi = new GitLabApi(GitLabApi.ApiVersion.V3, "http://127.0.0.1/", "t_bj6gJywKH2fCkbWY7k");

    private String groupName = "tac-admin";

    private String username = "tac-admin";

    private String basePath = "/Users/jinshuan.li/Documents/git_test";

    private final static String REMOTE_ORIGIN = "refs/remotes/origin/";

    private final static String REMOTE = "refs/remotes/";

    private String remoteGitLabURL = "http://127.0.0.1";

    @Test
    public void test1() throws GitLabApiException {

        List<Project> projects = gitLabApi.getProjectApi().getProjects();

        projects.stream().forEach(s -> {

            log.info("{}", StringHelper.toJsonString(s));
        });

    }

    @Test
    public void testCreateProject() throws GitLabApiException {

        String projectName = "tac-test07-import";

        try {
            Project project1 = gitLabApi.getProjectApi().getProject(groupName, projectName);
            log.info("{}", StringHelper.toJsonString(project1));
        } catch (Exception e) {

            System.out.println("e");
        }

        Project project = new Project();
        project.setPublic(Boolean.TRUE);
        project.setName(projectName);
        project = gitLabApi.getProjectApi().createProject(project, "http://127.0.0.1/tac-admin/tac-test01.git");

        log.info("{}", StringHelper.toJsonString(project));
    }

    @Test
    public void testGitPull() {

        String groupName = "tac-admin";

        String projectName = "tac-test01";

        String branch = "daily_01";

        if (!localRepoExists(groupName, projectName, branch)) {
            cloneRepo(groupName, projectName, branch);
        }

        // 拉分支数据

        pullRepo(groupName, projectName, branch);

    }

    /**
     * 拉取代码数据
     *
     * @param groupName
     * @param projectName
     * @param branch
     */
    private void pullRepo(String groupName, String projectName, String branch) {

        Git localGit = null;
        String localRepoDir = this.getLocalPath(groupName, projectName, branch);
        PullResult pullResult = null;
        try {
            localGit = Git.open(new File(localRepoDir));
            PullCommand pullCommand = localGit.pull();

            pullCommand.setRemote("origin").setCredentialsProvider(
                new UsernamePasswordCredentialsProvider(username, username)).setTimeout(30);

            pullResult = pullCommand.call();
            String header = localGit.getRepository().getBranch();
            //如果当前分支header引用和branch分支不一致，切换分支
            if (!branch.equals(header)) {
                //检测当前分支是否存在，不存在，返回异常
                Ref remoteRef = localGit.getRepository().exactRef(REMOTE_ORIGIN + branch);
                if (null == remoteRef) {
                    log.error("[Git Refresh Source] specified branch {} remote origin not exist.", branch);
                    throw new IllegalStateException("remote origin not exist " + branch);
                }
                localGit.checkout().setForce(true).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .setStartPoint(
                        REMOTE_ORIGIN + branch).setName(branch).call();
            }

        } catch (Exception e) {

            log.error("[Git Refresh Source] pull result error  path:{} {}", localRepoDir, e.getMessage(), e);

            throw new IllegalStateException("pull source failed " + e.getMessage());
        }
        if (null == pullResult || !pullResult.isSuccessful()) {
            log.error("[Git Refresh Source] pull result failed");
            throw new IllegalStateException("pull source failed " + localRepoDir);
        }

    }

    /**
     * clone 代码仓库
     *
     * @param groupName
     * @param projectName
     * @param branch
     */
    private void cloneRepo(String groupName, String projectName, String branch) {

        String remoteURL = String.format("%s/%s/%s.git", remoteGitLabURL, groupName, projectName);

        String localPath = getLocalPath(groupName, projectName, branch);

        CloneCommand cloneCommand = Git.cloneRepository().setURI(remoteURL).setBranch(branch).setDirectory(
            new File(localPath)).setTimeout(30);
        Git git = null;
        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, username));

        try {
            git = cloneCommand.call();

            // 取分支
            String existBranch = git.getRepository().getBranch();
            if (!StringUtils.equals(branch, existBranch)) {
                throw new IllegalStateException(String.format("branch %s not exist", branch));
            }
        } catch (Exception e) {
            log.error("[Git Refresh Source] clone repository error . remote:{} {}", remoteURL, e.getMessage(), e);

            throw new IllegalStateException("clone repository error ." + e.getMessage());
        }
    }

    /**
     * 本地数据是否存在
     *
     * @param groupName
     * @param projectName
     * @param branch
     * @return
     */
    public Boolean localRepoExists(String groupName, String projectName, String branch) {

        File file = new File(getLocalPath(groupName, projectName, branch) + ".git");

        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 本地代码路径
     *
     * @param groupName
     * @param projectName
     * @param branch
     * @return
     */
    private String getLocalPath(String groupName, String projectName, String branch) {

        StringBuilder localRepoDir = new StringBuilder(basePath);
        localRepoDir.append(File.separator).append(groupName).append(File.separator).append(projectName).append(
            File.separator).append(branch).append(File.separator);

        return localRepoDir.toString();
    }
}
