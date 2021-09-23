package com.test.diff.services;

import com.test.diff.services.internal.source.ISourceFileHandle;
import com.test.diff.services.internal.source.SourceFileHandleFactory;
import com.test.diff.services.utils.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GitFileHandleTest {

    @Autowired
     protected ApplicationContext ctx;

    @Test
    public void testCopyProject(){
        SpringUtil.setContext(ctx);
        ISourceFileHandle handle = SourceFileHandleFactory.build();
        String commitId = handle.copyProjectFile(null, "master", "C:\\Users\\wl\\code-diff\\jvmTest", 1);
        System.out.println(commitId);
    }
}
