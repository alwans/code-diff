package com.test.diff.services;

import com.test.diff.common.domain.ClassInfo;
import com.test.diff.common.util.JacksonUtil;
import com.test.diff.services.enums.DiffTypeEnum;
import com.test.diff.services.internal.DiffWorkFlow;
import com.test.diff.services.internal.jacoco.JacocoHandle;

import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.utils.FileUtil;
import org.jacoco.cli.internal.core.data.ChainNode;
import org.jacoco.cli.internal.core.data.ExecutionData;
import org.jacoco.cli.internal.core.data.ExecutionDataStore;
import org.jacoco.cli.internal.core.tools.ExecFileLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.Collection;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class JacocoHandleTest {

    @Resource
    private DiffWorkFlow diffWorkFlow;

    @Value("${server.port}")
    private String port;

    @Test
    public void testDump() throws Exception {
        File execfile = new File(
                "C:\\Users\\wl\\Desktop\\dump2.exec");

        JacocoHandle.dump("192.168.100.83", "6300", execfile.getAbsolutePath());
    }

    @Test
    public void testMerge() throws Exception {
        String newEexcFilePath = "C:\\Users\\wl\\Desktop\\merge.exec";
        File[] files = new File("C:\\Users\\wl\\Desktop").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".exec");
            }
        });
        JacocoHandle.merge(0, port, newEexcFilePath, files);
    }

    @Test
    public void testChainNode() throws IOException, ClassNotFoundException {
        File file = new File("C:\\Users\\wl\\Desktop\\obj.txt");
        InputStream is = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(is);
        ChainNode node =(ChainNode) ois.readObject();
        System.out.println(node);
    }

    @Test
    public void testLoadExecFile(){
        String execFilePath = "C:\\Users\\wl\\Desktop\\dump.exec";
        ExecFileLoader loader = JacocoHandle.getLoader(execFilePath);
        ExecutionDataStore store = loader.getExecutionDataStore();
        Collection<ExecutionData> data = store.getContents();
        data.stream()
                .forEach(exec -> {
//                    if(exec.getName().equals(" com/scm/financial/service/api/receipt/entity/FundCollectionRegisterImport")
//                            || exec.getName().equals("com/beitai/inventory/managment/service/impl/ImmSingleProductRecordServiceImpl")){
//                        System.out.println(exec);
//                    }
                    System.out.println(exec.getName());
                });
    }

    @Test
    public void testReport() throws Exception {
        String baseVersion = "master";
        String newVersion = "feature/v1.1.1";
        ProjectDiffParams params = new ProjectDiffParams();
        params.setId(1);
        params.setOldVersion(baseVersion);
        params.setNewVersion(newVersion);
        params.setOldCommitId("122155db3626a4b39b8be5254bfe65e8139e39a2");
        params.setNewCommitId("13509e9ddc4ea6ec240a7bd4628aeb82cfec9365");
//        params.setDiffTypeCode(DiffTypeEnum.BRANCH_DIFF.getCode());
        params.setDiffTypeCode(DiffTypeEnum.BRANCH_DIFF.getCode());
        params.setUpdateCode(true);

        List<ClassInfo> classInfos = diffWorkFlow.diff(params);

        String execFilePath = "C:\\Users\\wl\\Desktop\\dump.exec";
        String projectDir = "C:\\Users\\wl\\Desktop\\feature_v1.1.1_17602254b3c491c2f4c3dcc693db6f41ec5af8e7";
        String reportPath = "C:\\Users\\wl\\Desktop\\report";
        FileUtil fileUtil = new FileUtil();
        JacocoHandle.report(execFilePath, fileUtil.getAllClassFilePathsByProject(projectDir),
                fileUtil.getAllSourcePathsByProject(projectDir),reportPath, JacksonUtil.serialize(classInfos), null);
    }

}
