package com.test.diff.services.internal.source;

import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.FileUtil;
import com.test.diff.services.utils.SpringUtil;
import org.springframework.context.ApplicationContext;



public class SourceFileHandleFactory {

    public static ISourceFileHandle build(){
        ApplicationContext context = SpringUtil.getContext();
        ProjectInfoService projectInfoService = context.getBean(ProjectInfoService.class);
        RepoInfoService repoInfoService = context.getBean(RepoInfoService.class);
        FileUtil fileUtil = context.getBean(FileUtil.class);
        return new LocalSourceFileHandle(fileUtil, projectInfoService, repoInfoService);
    }
}
