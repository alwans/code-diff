package com.test.diff.services.internal;

import lombok.Data;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wl
 * 通过这个类获取远程分支和tags太麻烦了
 * 改为下方链接方法获取本地分支和tag列表
 * {@link BaseRepository#lsLocalBranchList(String, String)}
 */
@Component
@Data
@Deprecated
public class RefHandle {
    
    private static final String BRANCH_FLAG = "refs/heads/";
    private static final String TAGS_FLAG = "refs/tags/";

    public List<Ref> getBranchList(Collection<Ref> refsList){
        return getTargetRefList(refsList, BRANCH_FLAG);
    }

    public List<Ref> getTagsList(Collection<Ref> refsList){
        return getTargetRefList(refsList, TAGS_FLAG);
    }

    public List<String> getBranchNameList(Collection<Ref> refsList){
        return getTargetRefNameList(refsList, BRANCH_FLAG);
    }

    public List<String> getTagNameList(Collection<Ref>  refsList){
        return getTargetRefNameList(refsList, TAGS_FLAG);
    }


    private List<Ref> getTargetRefList(Collection<Ref> refsList, String refFlag){
        List<Ref> targetRefList = new ArrayList<>();
        if(CollectionUtils.isEmpty(refsList)){
            return targetRefList;
        }
        for(Ref ref: refsList){
            if(ref.getName().startsWith(refFlag)){
               targetRefList.add(ref);
            }
        }
        return targetRefList;
    }
    private List<String> getTargetRefNameList(Collection<Ref> refsList, String refFlag){
        List<String> targetRefNames = new ArrayList<>();
        if(CollectionUtils.isEmpty(refsList)){
            return targetRefNames;
        }
        for(Ref ref: refsList){
            if(ref.getName().startsWith(refFlag)){
                targetRefNames.add(ref.getName().replace(refFlag, ""));
            }
        }
        return targetRefNames;
    }
    
    

}
